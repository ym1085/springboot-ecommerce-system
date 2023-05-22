package com.post.service.impl;

import com.post.web.dto.request.FileSaveRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Transactional
@SpringBootTest
class FileServiceImplTest {

    @Autowired
    private FileServiceImpl fileService;

    private static List<FileSaveRequestDto> getFileSaveRequestDtoBuilder() {
        return Arrays.asList(
                FileSaveRequestDto.builder()
                        .originalName("file1.jpg")
                        .saveName(UUID.randomUUID().toString() + ".jpg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpg")
                        .fileSize(1024L)
                        .fileType("jpg")
                        .build(),
                FileSaveRequestDto.builder()
                        .originalName("file2.jpg")
                        .saveName(UUID.randomUUID().toString() + ".png")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".png")
                        .fileSize(1024L)
                        .fileType("png")
                        .build(),
                FileSaveRequestDto.builder()
                        .originalName("file2.jpg")
                        .saveName(UUID.randomUUID().toString() + ".jpeg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpeg")
                        .fileSize(1024L)
                        .fileType("jpeg")
                        .build()
        );
    }

    @Test
    @DisplayName("게시글 파일 저장 - 성공")
    @Order(1)
    void testSaveFilesSuccess() {
        //given
        Long postId = 1L;
        List<FileSaveRequestDto> fileSaveRequestDto = getFileSaveRequestDtoBuilder();

        //when
        fileService.saveFiles(postId, fileSaveRequestDto);

        //then
    }
}
