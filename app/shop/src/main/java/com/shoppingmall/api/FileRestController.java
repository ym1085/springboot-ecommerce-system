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
    public ResponseEntity<Resource> downloadSingleFile(@PathVariable("fileId") Long fileId) {
        try {
            FileResponseDto files = fileService.getFileByFileId(fileId);
            String parentSubDir = fileHandlerHelper.getSubFileDirPathByDate();

            if (StringUtils.isBlank(files.getFilePath()) || files.getStoredFileName().length() == 0) {
                throw new IllegalArgumentException("file path or save name is blank");
            }

            String uploadPath = fileHandlerHelper.getUploadPath();
            File file = new File(uploadPath + File.separator + parentSubDir + File.separator + files.getStoredFileName());
            Resource resource = resourceLoader.getResource("file:" + file.getPath());
            InputStream inputStream = resource.getInputStream();

            ResponseEntity<Resource> response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getOriginFileName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(new InputStreamResource(inputStream));

            fileService.increaseDownloadCntByFileId(fileId);
            return response;
        } catch (IllegalArgumentException e) {
            log.error("file path or save name is blank, fileId = {}", fileId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (FileNotFoundException e) {
            log.error("file not found, fileId = {}", fileId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            log.error("error occurred while downloading file, fileId = {}", fileId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/compress/{postId}")
    public void downloadMultiZipFile(@PathVariable("postId") Long postId, HttpServletResponse response) throws IOException {
        List<File> files = fileService.getFilesByPostId(postId)
                .stream()
                .map(fileResponseDto -> new File(fileResponseDto.getFilePath()))
                .collect(Collectors.toList());

        if (files.isEmpty()) {
            throw new FileNotFoundException("file not found");
        }

        fileHandlerHelper.transferToZipFile(response.getOutputStream(), files);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + "zipFile" + ".zip\"");
    }
}
