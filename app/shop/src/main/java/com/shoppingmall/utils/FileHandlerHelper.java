package com.shoppingmall.utils;

import com.shoppingmall.common.ErrorCode;
import com.shoppingmall.constant.FileExtension;
import com.shoppingmall.constant.FileType;
import com.shoppingmall.constant.OSType;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.exception.FailDownloadFilesException;
import com.shoppingmall.exception.UploadFileException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Getter
@Component
public class FileHandlerHelper {

    @Value("${os.window.upload-path}")
    private String uploadPathByWindow;

    @Value("${os.mac.upload-path}")
    private String uploadPathByMac;

    @Value("${os.linux.upload-path}")
    private String uploadPathByLinux;

    private String uploadPath;

    @PostConstruct
    public void init() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains(OSType.WINDOW.getOsName())) {
            this.uploadPath = Paths.get(uploadPathByWindow).toString();
        } else if (osName.contains(OSType.MAC.getOsName())) {
            this.uploadPath = Paths.get(uploadPathByMac).toString();
        } else if (osName.contains(OSType.LINUX.getOsName())) {
            this.uploadPath = Paths.get(uploadPathByLinux).toString();
        }
    }

    private final ResourceLoader resourceLoader;

    public FileHandlerHelper(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<FileRequestDto> uploadFiles(List<MultipartFile> multipartFiles, FileType fileType) {
        List<FileRequestDto> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file.isEmpty()) {
                continue;
            }
            FileRequestDto fileRequestDto = uploadFileToServer(file, fileType); // client 측 파일을 서버 특정 경로에 Upload
            files.add(fileRequestDto);
        }
        return files;
    }

    private FileRequestDto uploadFileToServer(MultipartFile multipartFile, FileType fileType) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = getFileExtensionByOriginalFileName(originalFilename);
        if (!validateFileExtension(ext)) {
            return null;
        }

        String storedFileName = generateStoredFileName(originalFilename);
        String today = getCurrentLocalDateTime();

        String fileUploadPath = getFileUploadPathTotransfer(fileType, today, storedFileName); // file upload path on physical server
        File uploadFile = new File(fileUploadPath);

        transferTo(multipartFile, fileUploadPath, uploadFile); // upload file to server

        return FileRequestDto.builder()
                .originFileName(multipartFile.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath(fileUploadPath)
                .fileSize(multipartFile.getSize())
                .fileType(ext)
                .fileAttached("Y")
                .build();
    }

    private void transferTo(MultipartFile multipartFile, String fileUploadPath, File uploadFile) {
        try {
            log.info("start transfer file to server, fileUploadPath = {}", fileUploadPath);
            multipartFile.transferTo(uploadFile);
            log.info("finish transfer file to server, fileUploadPath = {}", fileUploadPath);
        } catch (IOException e) {
            log.error("[IOException] error occurred, e = {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.error("[IllegalStateException] error occurred, e = {}", e.getMessage());
        } catch (Exception e) {
            log.error("[Exception] error occurred, e = {}", e.getMessage());
            throw new UploadFileException();
        }
    }

    public String generateStoredFileName(String originalFilename) {
        return generateUUID() + "." + getFileExtensionByOriginalFileName(originalFilename);
    }

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 디렉토리 경로 yyyy-MM-dd 형태로 생성하기 위해 사용
     */
    private String getCurrentLocalDateTime() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 특정 경로에 dir 없으면 해당 경로 dir 생성 후 파일 업로드 경로 생성하여 반환
     */
    private String getFileUploadPathTotransfer(FileType fileType, String today, String storedFileName) {
        return (fileType == FileType.POSTS)
                ? getPhysicalExternalFileUploadPath(FileType.POSTS.getFileTypeName(), today) + File.separator + storedFileName
                : getPhysicalExternalFileUploadPath(FileType.SHOP.getFileTypeName(), today) + File.separator + storedFileName;
    }

    private String getFileExtensionByOriginalFileName(String originalFilename) {
        return StringUtils.getFilenameExtension(originalFilename);
    }

    private String getPhysicalExternalFileUploadPath(String fileType, String today) {
        return createDirectory(uploadPath + File.separator + fileType + File.separator + today);
    }

    private String getPhysicalExternalFileUploadPath(String today) {
        return createDirectory(uploadPath + File.separator + today);
    }

    private String createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    private boolean validateFileExtension(String ext) {
        return FileExtension.isAcceptFileExtension(ext);
    }

    public void deleteFiles(List<FileResponseDto> fileResponseDtos) {
        for (int i = 0; i < fileResponseDtos.size(); i++) {
            File deletedFile = new File(fileResponseDtos.get(i).getFilePath());
            if (isExistsFile(deletedFile) && isDeletedFile(deletedFile)) { // 파일 존재하고 파일 삭제된 경우
                log.info("success to delete file[{}] = {}", i, deletedFile.getPath());
            } else {
                log.info("fail to delete file = {}", deletedFile.getPath());
            }
        }
    }

    private boolean isExistsFile(File file) {
        return file.exists();
    }

    private boolean isDeletedFile(File file) {
        return file.delete();
    }

    public String getSubFileDirPathByDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    }

    /*public String extractFileDateTime(String filePath) {
        Path path = Paths.get(filePath);
        if (!StringUtils.isEmpty(path.getName(3).toString())) {
            return path.getName(3).toString();
        }
        return "";
    }*/

    public String extractFileDateTimeByFilePath(String filePath) {
        if (filePath.isEmpty()) {
            return "";
        }

        final Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(filePath);
        return matcher.find() ? matcher.group(0) : "";
    }

    public File getDownloadFile(String uploadPath, String domain, String fileCreatedDate, String storedFileName) {
        return new File(uploadPath + File.separator + domain + File.separator + fileCreatedDate + File.separator + storedFileName);
    }

    public Resource getDownloadFileResource(String uploadPath) {
        return resourceLoader.getResource("file:" + uploadPath);
    }

    public InputStream getDownloadFileInputStream(Resource resource) {
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FailDownloadFilesException();
        }
    }

    public HttpHeaders getHttpHeadersByDownloadFile(FileResponseDto files, Resource resource, InputStream inputStream) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getOriginFileName() + "\"");
            httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FailDownloadFilesException();
        }
        return httpHeaders;
    }

    public void responseZipFromAttachments(HttpServletResponse response, List<File> files) {
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (File file : files) {
                if (!file.exists() || file.isFile()) {
                    continue;
                }

                FileSystemResource fsr = new FileSystemResource(file.getPath());
                ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(fsr.getFilename()));
                zipEntry.setSize(fsr.contentLength());
                zipEntry.setTime(System.currentTimeMillis());

                zos.putNextEntry(zipEntry);

                StreamUtils.copy(fsr.getInputStream(), zos);
                zos.closeEntry();
            }
            zos.finish();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
