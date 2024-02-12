package com.shoppingmall.service;

import com.shoppingmall.ShopApplication;
import com.shoppingmall.dto.request.PostFileSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Transactional
@SpringBootTest(classes = ShopApplication.class)
class FileServiceTest {

    @Autowired
    private FileService fileService;

    @BeforeEach
    public void setup() {
        String username = "admin";
        String password = "Funin0302!@#$%$";
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UserDetails principal = new User(username, password, AuthorityUtils.createAuthorityList("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    private static List<PostFileSaveRequestDto> getFileRequestDtoBuilder() {
        return Arrays.asList(
                PostFileSaveRequestDto.builder()
                        .originFileName("file1.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".jpg")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".jpg")
                        .fileSize(1024L)
                        .fileType("jpg")
                        .fileAttached("Y")
                        .build(),
                PostFileSaveRequestDto.builder()
                        .originFileName("file2.jpg")
                        .storedFileName(UUID.randomUUID().toString() + ".png")
                        .filePath("/Users/ymkim/" + UUID.randomUUID().toString() + ".png")
                        .fileSize(1024L)
                        .fileType("png")
                        .fileAttached("Y")
                        .build(),
                PostFileSaveRequestDto.builder()
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
        List<PostFileSaveRequestDto> postFileSaveRequestDtos = getFileRequestDtoBuilder();
    }
}
