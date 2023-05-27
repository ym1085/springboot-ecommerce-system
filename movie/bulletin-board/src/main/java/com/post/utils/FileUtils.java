package com.post.utils;

import com.post.constant.FileExtension;
import com.post.web.dto.request.FileRequestDto;
import com.post.web.dto.resposne.FileResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   파일 업로드 전 가공 후 반환 하는 클래스
 */
@Slf4j
@Component // -> Bean Scan
public class FileUtils {

    @Value("${upload.directory}")
    private String uploadPath;

    // -> Initialize IOC Container -> Create Beans -> Relationships beans -> Call initialized call back method
    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadPath).toString();
        log.trace("FileUtils init, file upload path -> {}", this.uploadPath);
    }

    /**
     * 다중 파일 업로드
     * @return DB에 저장할 파일 정보 List 반환
     */
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

    /**
     * 단일 파일 업로드
     */
    private FileRequestDto uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = getFileExtensionByOriginalFileName(originalFilename);
        if (!validateFileExtension(ext)) {
            return null; // 앞 단에서 검증 했다는 가정하에, 다른 확장자가 들어온다는 것은 유저가 악의적으로 뚫었다고 가정
        }

        String saveFileName = createSaveFileName(originalFilename);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();

        String uploadPath = getUploadPath(today) + File.separator + saveFileName;
        File uploadFile = new File(uploadPath);

        try {
            log.info("start file upload to physical path..");
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            log.error("error when uploading file to physical path, e = {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return FileRequestDto.builder()
                .originalName(multipartFile.getOriginalFilename())
                .saveName(saveFileName)
                .filePath(uploadPath)
                .fileSize(multipartFile.getSize())
                .fileType(ext)
                .build();
    }

    /**
     * UUID + ext 기반 파일명 생성 함수
     */
    private String createSaveFileName(String originalFilename) {
        return generateUUID() + "." + getFileExtensionByOriginalFileName(originalFilename);
    }

    /**
     * UUID 반환 함수
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 파일 확장자(jpg, jpeg, png..) 반환 함수
     */
    private String getFileExtensionByOriginalFileName(String originalFilename) {
        return StringUtils.getFilenameExtension(originalFilename);
    }

    /**
     * 업로드 경로 반환 - 추가 경로 없음(C:movie/upload-files/)
     */
    private String getUploadPath() {
        return createDirectory(uploadPath);
    }

    /**
     * 업로드 경로 반환 - 추가 경로 없음(C:movie/upload-files/20230520)
     */
    private String getUploadPath(String addPath) {
        return createDirectory(uploadPath + File.separator + addPath); // C:movie/upload-files/20230520/
    }

    /**
     * 업로드 폴더(디렉티러) 생성
     * @param path 파일 업로드 경로
     * @return 파일 업로드 경로
     */
    private String createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 파일 확장자 체크 - enum 순회하면서 체크
     */
    private boolean validateFileExtension(String ext) {
        return FileExtension.isAcceptFileExtension(ext);
    }

    /**
     * 파일 삭제 (게시글 수정, 삭제에서 사용)
     * @param fileResponseDtos
     */
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

    /**
     * 특정 경로에 파일이 존재하는지 확인
     * @param file
     * @return
     */
    private boolean isExistsFile(File file) {
        return file.exists();
    }

    /**
     * 특정 경로에 존재하는 파일 삭제
     * @param file
     * @return
     */
    private boolean isDeletedFile(File file) {
        return file.delete();
    }

    /**
     * 파일 서브 디렉토리 경로 반환 -> 230502 -> yyMMdd
     * @return
     */
    public String getFileDirPathByDateTime() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    }

    /**
     * 파일 압축 다운로드
     * @param os
     * @param files
     */
    public void convertFileToZipFile(OutputStream os, List<File> files) {
        try (ZipOutputStream zos = new ZipOutputStream(os)) {
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    try (FileInputStream fis = new FileInputStream(file)) {
                        StreamUtils.copy(fis, zos);
                    }
                    zos.closeEntry();
                }
            }
        } catch (IOException e) {
            // Fixme: 기능 동작은 되나 현재 연결은 호스트의... IOException이 나는데 확인이 필요함 -> 기능 동작이 문제가 아님 -> 잠재적 오류 잡아야함
            log.warn("cannot download file, e = {}", e.getMessage());
        }
    }
}
