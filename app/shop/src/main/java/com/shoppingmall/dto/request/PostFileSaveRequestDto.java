package com.shoppingmall.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFileSaveRequestDto implements BaseFileSaveRequestDto {

    private Long postFileId;
    private Long postId;
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private long fileSize;
    private String fileType;
    private String fileAttached;

    /**
     * 파일 저장의 경우 게시글 등록이 완료 된 이후에 진행 한다.
     * 해당 함수는 생성된 게시글 ID를 파일 요청 객체의 postId에 저장하는 용도로 사용
     * -> FileServiceImpl 클래스 참고
     */
    @Override
    public void setId(Long id) {
        this.postId = id;
    }
}
