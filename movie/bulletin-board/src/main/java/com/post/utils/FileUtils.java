package com.post.utils;

import com.post.constant.FileExtension;
import com.post.web.dto.request.FileSaveRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author      :   ymkim
 * @since       :   2023. 05. 20
 * @description :   파일 업로드 전 가공 후 반환 하는 클래스
 */
@Slf4j
@Component // -> Bean Scan
public class FileUtils {

    private static final List<String> fileExtensions = Arrays.asList(
            FileExtension.JPG.getExtension(),
            FileExtension.JPEG.getExtension(),
            FileExtension.PNG.getExtension()
    );

    private static final String uploadPath = Paths.get("C:", "movie", "upload-files").toString();

    /**
     * 다중 파일 업로드
     * @return DB에 저장할 파일 정보 List 반환
     */
    public List<FileSaveRequestDto> uploadFiles(List<MultipartFile> multipartFiles) {
        List<FileSaveRequestDto> files = new ArrayList<>();
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
    private FileSaveRequestDto uploadFile(MultipartFile multipartFile) {
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

        return FileSaveRequestDto.builder()
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
     * 파일 확장자 체크
     */
    private boolean validateFileExtension(String ext) {
        return fileExtensions.contains(ext);
    }
}
