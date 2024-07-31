package com.shoppingmall.service;

import com.shoppingmall.dto.request.PostFileSaveRequestDto;
import com.shoppingmall.mapper.PostFileMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private PostFileMapper postFileMapper;

    private static List<PostFileSaveRequestDto> getFileRequestDtoBuilder() {
        return Arrays.asList(
                PostFileSaveRequestDto.builder()
                        .originFileName("file1.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".jpg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpg")
                        .fileSize(1024L)
                        .fileExp("jpg")
                        .fileAttached("Y")
                        .build(),

                PostFileSaveRequestDto.builder()
                        .originFileName("file2.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".png")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".png")
                        .fileSize(1024L)
                        .fileExp("png")
                        .fileAttached("Y")
                        .build(),

                PostFileSaveRequestDto.builder()
                        .originFileName("file2.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".jpeg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpeg")
                        .fileSize(1024L)
                        .fileExp("jpeg")
                        .fileAttached("Y")
                        .build()
        );
    }

    @Test
    @DisplayName("게시글 파일 저장 - 성공")
    void testSaveFilesSuccess() {
        Integer postId = 1;
        List<PostFileSaveRequestDto> postFileSaveRequestDtos = getFileRequestDtoBuilder();
    }

    private static Stream<Arguments> is_save_post_files() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        PostFileSaveRequestDto.builder()
                                .originFileName("file1.jpg")
                                .storedFileName(UUID.randomUUID().toString() + ".jpg")
                                .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpg")
                                .fileSize(1024L)
                                .fileExp("jpg")
                                .fileAttached("Y")
                                .build(),

                        PostFileSaveRequestDto.builder()
                                .originFileName("file2.jpg")
                                .storedFileName(UUID.randomUUID().toString() + ".png")
                                .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".png")
                                .fileSize(1024L)
                                .fileExp("png")
                                .fileAttached("Y")
                                .build(),

                        PostFileSaveRequestDto.builder()
                                .originFileName("file2.jpg")
                                .storedFileName(UUID.randomUUID().toString() + ".jpeg")
                                .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpeg")
                                .fileSize(1024L)
                                .fileExp("jpeg")
                                .fileAttached("Y")
                                .build()
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("is_save_post_files")
    @DisplayName("게시글 파일 저장 - 성공")
    void testSavePostFiles(List<PostFileSaveRequestDto> mockPostFileSaveRequestDtos) throws Exception {
        //given
        //when
        //then
    }
}
