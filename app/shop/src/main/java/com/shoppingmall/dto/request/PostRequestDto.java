package com.shoppingmall.dto.request;

import com.shoppingmall.constant.FileType;
import com.shoppingmall.vo.PostVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

    private Long postId;
    private Long memberId;

    @NotEmpty(message = "제목은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 20, message = "제목은 20자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String title;

    @NotEmpty(message = "내용은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String content;

    private String fixedYn;

    private List<MultipartFile> files = new ArrayList<>();
    private FileType fileType = FileType.POSTS;

    private int categoryId;

    public PostVO toEntity() {
        return PostVO.builder()
                .postId(postId)
                .memberId(memberId)
                .title(title)
                .content(content)
                .fixedYn(fixedYn)
                .categoryId(categoryId)
                .build();
    }

}
