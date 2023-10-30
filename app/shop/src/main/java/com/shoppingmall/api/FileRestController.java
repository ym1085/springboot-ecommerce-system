package com.shoppingmall.api;

import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.service.FileService;
import com.shoppingmall.utils.FileHandlerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class FileRestController {
    private final ResourceLoader resourceLoader;
    private final FileService fileService;
    private final FileHandlerHelper fileHandlerHelper;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadSingleFile(@PathVariable("fileId") Long fileId) {
        try {
            FileResponseDto files = fileService.getFileByFileId(fileId);
            String parentSubDir = fileHandlerHelper.getSubFileDirPathByDate();

            if (StringUtils.isBlank(files.getFilePath())) {
                throw new FileNotFoundException("파일 경로가 존재하지 않습니다.");
            } else if (files.getStoredFileName().isEmpty()) {
                throw new FileNotFoundException("파일이 존재하지 않습니다.");
            }

            String uploadPath = fileHandlerHelper.getUploadPath();
            File file = new File(uploadPath + File.separator + parentSubDir + File.separator + files.getStoredFileName());
            Resource resource = resourceLoader.getResource("file:" + file.getPath());
            InputStream inputStream = resource.getInputStream();

            fileService.increaseDownloadCntByFileId(fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getOriginFileName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e) {
            log.error("e = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일이 존재하지 않습니다. 다시 시도해주세요.");
        } catch (IOException e) {
            log.error("e = {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 다운로드 중 서버 오류가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    @GetMapping(value = "/download/compress/{postId}", produces = "application/zip")
    public void downloadMultiZipFile(@PathVariable("postId") Long postId, HttpServletResponse response) throws IOException {
        // post(1) : files(N) + postId(required : true)
        List<File> files = fileService.getFilesByPostId(postId)
                .stream()
                .map(fileResponseDto -> new File(fileResponseDto.getFilePath()))
                .collect(Collectors.toList());

        if (files.isEmpty()) {
            throw new FileNotFoundException("저장된 파일이 존재하지 않습니다");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileHandlerHelper.generateUUID() + ".zip\";");
        response.setContentType("application/zip");

        fileHandlerHelper.responseZipFromAttachments(response, files);
    }
}
