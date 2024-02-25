package com.shoppingmall.service;

import com.shoppingmall.common.response.ErrorCode;
import com.shoppingmall.dto.request.FileSaveRequestDto;
import com.shoppingmall.dto.request.ProductSaveRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.ProductDetailResponseDto;
import com.shoppingmall.dto.response.ProductFileResponseDto;
import com.shoppingmall.exception.FailSaveFileException;
import com.shoppingmall.exception.FailSaveProductException;
import com.shoppingmall.mapper.ProductFileMapper;
import com.shoppingmall.mapper.ProductMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.utils.PaginationUtils;
import com.shoppingmall.vo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductFileMapper productFileMapper;
    private final FileHandlerHelper fileHandlerHelper;

    public PagingResponseDto<ProductDetailResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        int totalRecordCount = productMapper.count(searchRequestDto); // 50
        if (totalRecordCount < 1) {
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        PaginationUtils pagination = new PaginationUtils(totalRecordCount, searchRequestDto); // 50, xxx
        searchRequestDto.setPagination(pagination);

        List<Product> products1 = productMapper.getProducts(searchRequestDto);

        List<ProductDetailResponseDto> products = productMapper.getProducts(searchRequestDto)
                .stream()
                .map(ProductDetailResponseDto::toDto)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(products, pagination);
    }

    public ProductDetailResponseDto getProductByProductId(Long productId) {
        return productMapper.getProductByProductId(productId)
                .map(ProductDetailResponseDto::toDto)
                .orElse(new ProductDetailResponseDto());
    }

    @Transactional
    public Long saveProducts(ProductSaveRequestDto productRequestDto) {
        Product product = productRequestDto.toEntity();
        int responseCode = productMapper.saveProducts(product);
        if (responseCode == 0) {
            log.error("[Occurred Exception] Error Message = {}", ErrorCode.FAIL_SAVE_PRODUCT.getMessage());
            throw new FailSaveProductException(ErrorCode.FAIL_SAVE_PRODUCT);
        }

        try {
        } catch (RuntimeException e) {
            // 예외 발생 시 서버의 특정 경로에 업로드 된 파일을 삭제해야 하기에, 추가
            // 파일 업로드 성공 ----> 파일 정보 DB 저장(ERROR!! 발생) ----> Transaction Rollback ----> 이미 업로드한 파일은 지워줘야 함
            List<FileResponseDto> fileResponseDtos = getFileResponseDtos(productRequestDto.getProductId());
            fileHandlerHelper.deleteFiles(fileResponseDtos);
            throw e; // 현재 트랜잭션 롤백
        }

        if (!productRequestDto.getFiles().isEmpty()) {
            List<FileSaveRequestDto> baseFileSaveRequestDtos = fileHandlerHelper.uploadFiles(productRequestDto.getFiles(), productRequestDto.getDirPathType());
            responseCode = saveFiles(product.getProductId(), baseFileSaveRequestDtos);
            if (responseCode == 0) {
                log.error("[Occurred Exception] Error Message = {}", ErrorCode.FAIL_SAVE_FILES);
                throw new FailSaveFileException(ErrorCode.FAIL_SAVE_FILES);
            }
        }
        return product.getProductId();
    }

    public int saveFiles(Long productId, List<FileSaveRequestDto> files) {
        if (CollectionUtils.isEmpty(files) || files.get(0) == null || productId == null) {
            return 0;
        }

        for (FileSaveRequestDto file : files) {
            if (file == null) {
                continue;
            }
            file.setId(productId);
        }

        return productFileMapper.saveFiles(files);
    }

    private List<FileResponseDto> getFileResponseDtos(long productId) {
        return productFileMapper.getFilesByProductId(productId)
                .stream()
                .map(ProductFileResponseDto::toDto)
                .collect(Collectors.toList());
    }
}