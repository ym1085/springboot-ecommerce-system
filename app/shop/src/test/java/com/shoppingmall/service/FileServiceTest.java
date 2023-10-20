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
                        .originalName("file1.jpg")
                        .saveName(UUID.randomUUID().toString() + ".jpg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpg")
                        .fileSize(1024L)
                        .fileType("jpg")
                        .build(),
                FileRequestDto.builder()
                        .originalName("file2.jpg")
                        .saveName(UUID.randomUUID().toString() + ".png")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".png")
                        .fileSize(1024L)
                        .fileType("png")
                        .build(),
                FileRequestDto.builder()
                        .originalName("file2.jpg")
                        .saveName(UUID.randomUUID().toString() + ".jpeg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpeg")
                        .fileSize(1024L)
                        .fileType("jpeg")
                        .build()
        );
    }

    @Test
    // @WithMockUser(roles = "USER")
    @DisplayName("게시글 파일 저장 - 성공")
    void testSaveFilesSuccess() {
        //given
        Long postId = 1L;
        List<FileRequestDto> fileRequestDtos = getFileRequestDtoBuilder();

        //when
        fileService.saveFiles(postId, fileRequestDtos);

        //then
    }
}
