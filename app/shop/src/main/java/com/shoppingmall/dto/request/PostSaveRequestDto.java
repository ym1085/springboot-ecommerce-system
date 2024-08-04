package com.shoppingmall.dto.request;

import com.shoppingmall.constant.DirPathType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostSaveRequestDto {
    private Integer postId;
    private Integer memberId;
    private int categoryId;

    @NotEmpty(message = "제목은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 20, message = "제목은 20자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String title;

    @NotEmpty(message = "내용은 반드시 입력되어야 합니다. 다시 한번 시도해주세요.")
    @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다. 다시 한번 시도해주세요.")
    private String content;
    private String fixedYn;

    private List<MultipartFile> files = new ArrayList<>();
    private DirPathType dirPathType;
}
