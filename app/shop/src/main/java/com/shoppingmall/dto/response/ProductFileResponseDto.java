package com.shoppingmall.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoppingmall.vo.ProductFiles;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductFileResponseDto extends FileResponseDto {
    private Long productFileId;
    private Long productId;

    public static ProductFileResponseDto toDto(ProductFiles productFiles) {
        return ProductFileResponseDto.builder()
                .productFileId(productFiles.getProductFileId())
                .productId(productFiles.getProductId())
                .originFileName(productFiles.getOriginFileName())
                .storedFileName(productFiles.getStoredFileName())
                .filePath(productFiles.getFilePath())
                .fileSize(productFiles.getFileSize())
                .fileExp(productFiles.getFileExp())
                .downloadCnt(productFiles.getDownloadCnt())
                .delYn(productFiles.getDelYn())
                .createDate(productFiles.getCreateDate())
                .deleteDate(productFiles.getDeleteDate())
                .fileAttached(productFiles.getFileAttached())
                .build();
    }

    @Override
    public Long getFileId() {
        return this.productFileId;
    }

    @Override
    public Long getId() {
        return this.productId;
    }
}
