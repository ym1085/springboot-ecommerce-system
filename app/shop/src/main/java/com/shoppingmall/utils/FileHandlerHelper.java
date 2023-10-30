package com.shoppingmall.utils;

import com.shoppingmall.constant.FileExtension;
import com.shoppingmall.dto.request.FileRequestDto;
import com.shoppingmall.dto.response.FileResponseDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            uploadPath = Paths.get(uploadPathByWindow).toString();
        } else if (os.contains("mac")) {
            uploadPath = Paths.get(uploadPathByMac).toString();
        } else if (os.contains("linux")) {
            uploadPath = Paths.get(uploadPathByLinux).toString();
        }
        log.debug("uploadPath = {}", uploadPath); // /Users/youngminkim/projects/shoppingmall/upload-files
    }

    public List<FileRequestDto> uploadFiles(List<MultipartFile> multipartFiles) {
        List<FileRequestDto> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file.isEmpty()) {
                continue;
            }
            files.add(uploadFile(file));
        }
        return files;
    }

    private FileRequestDto uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = getFileExtensionByOriginalFileName(originalFilename);
        if (!validateFileExtension(ext)) {
            return null;
        }

        String storedFileName = generateFileName(originalFilename);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();

        String fileUploadPath = getFileUploadLocation(today) + File.separator + storedFileName;
        File uploadFile = new File(fileUploadPath);

        try {
            log.info("start file upload to physical path..");
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            log.error("error when uploading file to physical path, e = {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return FileRequestDto.builder()
                .originFileName(multipartFile.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath(fileUploadPath)
                .fileSize(multipartFile.getSize())
                .fileType(ext)
                .build();
    }

    public String generateFileName(String originalFilename) {
        return generateUUID() + "." + getFileExtensionByOriginalFileName(originalFilename);
    }

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String getFileExtensionByOriginalFileName(String originalFilename) {
        return StringUtils.getFilenameExtension(originalFilename);
    }

    private String getFileUploadLocation() {
        return createDirectory(uploadPath);
    }

    private String getFileUploadLocation(String addPath) {
        return createDirectory(uploadPath + File.separator + addPath);
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

    public void responseZipFromAttachments(HttpServletResponse response, List<File> files) {
        try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    FileSystemResource fsr = new FileSystemResource(file.getPath());
                    ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(fsr.getFilename()));
                    zipEntry.setSize(fsr.contentLength());
                    zipEntry.setTime(System.currentTimeMillis());

                    zos.putNextEntry(zipEntry);

                    StreamUtils.copy(fsr.getInputStream(), zos);
                    zos.closeEntry();
                }
            }
            zos.finish();
        } catch (IOException e) {
            log.error("파일 다운로드 중 오류 발생, e = {}", e.getMessage());
        }
    }
}
