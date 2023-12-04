package com.shoppingmall.api;

import com.shoppingmall.common.ApiUtils;
import com.shoppingmall.common.ErrorCode;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.service.FileService;
import com.shoppingmall.utils.FileHandlerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final FileService fileService;
    private final FileHandlerHelper fileHandlerHelper;

    @GetMapping("/download/{domain}/{postFileId}")
    public ResponseEntity downloadPostFile(
            @PathVariable("postFileId") Long postFileId,
            @PathVariable("domain") String domain) {
        FileResponseDto files = fileService.getFileByPostFileId(postFileId);
        if (files.getStoredFileName().isBlank()) {
            return ApiUtils.fail(ErrorCode.NOT_FOUND_POST_FILES.getCode(), ErrorCode.NOT_FOUND_POST_FILES.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (StringUtils.isBlank(files.getFilePath())) {
            return ApiUtils.fail(ErrorCode.NOT_FOUND_POST_FILES_PATH.getCode(), ErrorCode.NOT_FOUND_POST_FILES_PATH.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String serverUploadPath = fileHandlerHelper.getUploadPath();
        if (StringUtils.isBlank(serverUploadPath)) {
            return ApiUtils.fail(ErrorCode.NOT_FOUND_POST_FILES.getCode(), ErrorCode.NOT_FOUND_POST_FILES.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String fileUploadPath = fileHandlerHelper.extractFileDateTimeByFilePath(files.getFilePath());
        File file = fileHandlerHelper.getDownloadFile(serverUploadPath, domain, fileUploadPath, files.getStoredFileName());

        Resource resource = fileHandlerHelper.getDownloadFileResource(file.getPath());
        InputStream inputStream = fileHandlerHelper.getDownloadFileInputStream(resource);
        HttpHeaders httpHeaders = fileHandlerHelper.getHttpHeadersByDownloadFile(files, resource, inputStream);

        fileService.increaseDownloadCntByFileId(postFileId);
        return new ResponseEntity<>(
                new InputStreamResource(inputStream),
                httpHeaders,
                HttpStatus.OK
        );
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
