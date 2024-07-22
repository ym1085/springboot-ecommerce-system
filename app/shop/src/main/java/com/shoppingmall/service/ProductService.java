package com.shoppingmall.service;

import com.shoppingmall.constant.DirPathType;
import com.shoppingmall.dto.request.FileSaveRequestDto;
import com.shoppingmall.dto.request.ProductSaveRequestDto;
import com.shoppingmall.dto.request.ProductUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.dto.response.FileResponseDto;
import com.shoppingmall.dto.response.PagingResponseDto;
import com.shoppingmall.dto.response.ProductDetailResponseDto;
import com.shoppingmall.dto.response.ProductFileResponseDto;
import com.shoppingmall.exception.FileException;
import com.shoppingmall.exception.ProductException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.common.code.failure.file.FileFailureCode.*;
import static com.shoppingmall.common.code.failure.product.ProductFailureCode.*;

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

        List<ProductDetailResponseDto> products = productMapper.getProducts(searchRequestDto)
                .stream()
                .map(ProductDetailResponseDto::toDto)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(products, pagination);
    }

    public ProductDetailResponseDto getProductByProductId(Integer productId) {
        return productMapper.getProductByProductId(productId)
                .map(ProductDetailResponseDto::toDto)
                .orElse(new ProductDetailResponseDto());
    }

    @Transactional
    public int saveProducts(ProductSaveRequestDto productRequestDto) {
        Product product = productRequestDto.toEntity();

        if (productMapper.countByProductName(product) > 0) {
            log.error("[Occurred Exception] Error Message = {}", DUPLICATE_PRODUCT_NAME.getMessage());
            throw new ProductException(DUPLICATE_PRODUCT_NAME);
        }

        if (productMapper.saveProducts(product) < 1) {
            log.error("[Occurred Exception] Error Message = {}", SAVE_PRODUCT.getMessage());
            throw new ProductException(SAVE_PRODUCT);
        }

        try {
            if (!productRequestDto.getFiles().isEmpty()) {
                List<FileSaveRequestDto> baseFileSaveRequestDtos = fileHandlerHelper.uploadFiles(productRequestDto.getFiles(), productRequestDto.getDirPathType());
                if (saveFiles(product.getProductId(), baseFileSaveRequestDtos) < 1) {
                    log.error("[Occurred Exception] Error Message = {}", SAVE_FILES);
                    throw new FileException(SAVE_FILES);
                }
            }
        } catch (RuntimeException e) {
            // 예외 발생 시 서버의 특정 경로에 업로드 된 파일을 삭제해야 하기에, 추가
            // 파일 업로드 성공 ----> 파일 정보 DB 저장(ERROR!! 발생) ----> Transaction Rollback ----> 이미 업로드한 파일은 지워줘야 함
            List<FileResponseDto> fileResponseDtos = getFileResponseDtos(productRequestDto.getProductId());
            fileHandlerHelper.deleteFiles(fileResponseDtos);
            throw e; // 현재 트랜잭션 롤백
        }
        return product.getProductId();
    }

    public int saveFiles(Integer productId, List<FileSaveRequestDto> files) {
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

    @Transactional
    public void updateProduct(ProductUpdateRequestDto productUpdateRequestDto) {
        if (productMapper.updateProduct(productUpdateRequestDto.toEntity()) < 0) {
            throw new ProductException(SAVE_PRODUCT);
        }

        if (!productUpdateRequestDto.getFiles().isEmpty()) {
            if (updateFilesByProductId(productUpdateRequestDto.getProductId(), productUpdateRequestDto.getFiles()) < 1) {
                log.error("[Occurred Exception] Error Message = {}", UPLOAD_FILES.getMessage());
                throw new FileException(UPLOAD_FILES);
            }
        }
    }

    private int updateFilesByProductId(Integer productId, List<MultipartFile> files) {
        if (productFileMapper.countProductFileByProductId(productId) > 0) {
            if (productFileMapper.deleteFilesByProductId(productId) > 0) { // [HINT] DB에 존재하는 파일 정보 삭제 (SOFT DELETE)
                log.info("success delete product files from database");
                fileHandlerHelper.deleteFiles(getFileResponseDtos(productId)); // [HINT] 서버의 특정 경로에 존재하는 파일 삭제
            } else {
                log.info("fail delete product files from database");
            }
        }

        List<FileSaveRequestDto> fileSaveRequestDtos = fileHandlerHelper.uploadFiles(files, DirPathType.products); // [HINT] 서버의 특정 경로에 파일 업로드
        fileSaveRequestDtos.forEach(fileRequestDto -> fileRequestDto.setId(productId));
        return productFileMapper.saveFiles(fileSaveRequestDtos); // [HINT] DB에 파일 정보 저장
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        // 상품 삭제 성공하지 않았을 경우, 추가 작업 중단 후 메서드 종료
        if (productMapper.deleteProduct(productId) < 1) {
            return;
        }

        // 파일 목록이 비어있을 경우, 파일 삭제 로직 수행할 필요가 없음
        List<FileResponseDto> fileResponseDtos = getFileResponseDtos(productId);
        if (CollectionUtils.isEmpty(fileResponseDtos)) {
            return;
        }

        if (productFileMapper.deleteFilesByProductId(productId) > 0) {
            fileHandlerHelper.deleteFiles(fileResponseDtos);
        }
    }

    private List<FileResponseDto> getFileResponseDtos(Integer productId) {
        return productFileMapper.getFilesByProductId(productId)
                .stream()
                .map(ProductFileResponseDto::toDto)
                .collect(Collectors.toList());
    }
}