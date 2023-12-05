package com.shoppingmall.service;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.FileRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Transactional
@SpringBootTest(classes = ShopApplication.class)
class FileServiceTest {

    @Autowired
    private FileService fileService;

    private static List<FileRequestDto> getFileRequestDtoBuilder() {
        return Arrays.asList(
                FileRequestDto.builder()
                        .originFileName("file1.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".jpg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpg")
                        .fileSize(1024L)
                        .fileType("jpg")
                        .fileAttached("Y")
                        .build(),
                FileRequestDto.builder()
                        .originFileName("file2.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".png")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".png")
                        .fileSize(1024L)
                        .fileType("png")
                        .fileAttached("Y")
                        .build(),
                FileRequestDto.builder()
                        .originFileName("file2.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".jpeg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpeg")
                        .fileSize(1024L)
                        .fileType("jpeg")
                        .fileAttached("Y")
                        .build()
        );
    }

    @Test
    @DisplayName("게시글 파일 저장 - 성공")
    void testSaveFilesSuccess() {
        Long postId = 1L;
        List<FileRequestDto> fileRequestDtos = getFileRequestDtoBuilder();
    }
}
