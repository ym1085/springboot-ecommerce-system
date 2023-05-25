package com.post.web;

import com.post.service.FileService;
import com.post.utils.FileUtils;
import com.post.web.dto.resposne.FileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

    @Value("${upload.directory}")
    private String uploadPath;

    private final ResourceLoader resourceLoader;
    private final FileService fileService;
    private final FileUtils fileUtils;

    /**
     * 게시글에 첨부되어 있는 '파일' <a href='http://host/download/{fileId}'> 클릭 시 해당 fileId(번호:PK)에 맞는 파일 다운로드
     * @param id 파일 id 번호 (fileId), 나중에 {id} -> 바꾸기 -> fileId로
     *
     * Todo: Enum으로 파일 Type 구분해서 tar.gz || zip 인 경우 추가적인 함수 호출 후 압축해서 내려주는걸로
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
        try {
            FileResponseDto files = fileService.getFileById(id);
            String parentSubDir = fileUtils.getFileDirPathByDateTime(); // 230524

            if (StringUtils.isBlank(files.getFilePath()) || files.getSaveName().length() == 0) {
                throw new IllegalArgumentException("file path or save name is blank");
            }

            File file = new File(uploadPath + File.separator + parentSubDir + File.separator + files.getSaveName());
            Resource resource = resourceLoader.getResource("file:" + file.getPath());
            InputStream inputStream = resource.getInputStream();

            ResponseEntity<Resource> response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getOriginalName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(new InputStreamResource(inputStream));

            fileService.increaseDownloadCnt(id);
            return response;
        } catch (IllegalArgumentException e) {
            log.error("file path or save name is blank, id = {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (FileNotFoundException e) {
            log.error("file not found, id = {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            log.error("error occurred while downloading file, id = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
