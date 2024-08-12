package com.shoppingmall.service;

import com.shoppingmall.constant.FileType;
import com.shoppingmall.dto.request.FileSaveRequestDto;
import com.shoppingmall.dto.request.ProductSaveRequestDto;
import com.shoppingmall.dto.request.ProductUpdateRequestDto;
import com.shoppingmall.dto.request.SearchRequestDto;
import com.shoppingmall.exception.FileException;
import com.shoppingmall.exception.ProductException;
import com.shoppingmall.mapper.ProductFileMapper;
import com.shoppingmall.mapper.ProductMapper;
import com.shoppingmall.utils.FileHandlerHelper;
import com.shoppingmall.utils.PaginationUtils;
import com.shoppingmall.vo.PagingResponse;
import com.shoppingmall.vo.Product;
import com.shoppingmall.vo.ProductFiles;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static com.shoppingmall.common.code.failure.file.FileFailureCode.FAIL_UPDATE_FILES;
import static com.shoppingmall.common.code.failure.file.FileFailureCode.FAIL_UPLOAD_FILES;
import static com.shoppingmall.common.code.failure.product.ProductFailureCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductFileMapper productFileMapper;
    private final FileHandlerHelper fileHandlerHelper;

    public PagingResponse<Product> getProducts(SearchRequestDto searchRequestDto) {
        int totalRecordCount = productMapper.count(searchRequestDto);
        if (totalRecordCount < 1) {
            return new PagingResponse<>(Collections.emptyList(), null);
        }
        PaginationUtils pagination = new PaginationUtils(totalRecordCount, searchRequestDto);
        searchRequestDto.setPagination(pagination);

        List<Product> products = productMapper.getProducts(searchRequestDto);
        return new PagingResponse<>(products, pagination);
    }

    public Product getProductByProductId(Integer productId) {
        return productMapper.getProductByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_PRODUCT.getMessage()));
    }

    @Transactional
    public int saveProducts(ProductSaveRequestDto productRequestDto) {
        if (productMapper.countByProductName(productRequestDto) > 0) {
            log.error(FAIL_DUPLICATE_PRODUCT_NAME.getMessage());
            throw new ProductException(FAIL_DUPLICATE_PRODUCT_NAME);
        }

        if (productMapper.saveProducts(productRequestDto) < 1) {
            log.error(FAIL_SAVE_PRODUCT.getMessage());
            throw new ProductException(FAIL_SAVE_PRODUCT);
        }

        handleProductFiles(productRequestDto);
        return productRequestDto.getProductId();
    }

    private void handleProductFiles(ProductSaveRequestDto productRequestDto) {
        if (CollectionUtils.isEmpty(productRequestDto.getFiles())) {
            log.warn("No files to upload for product.");
            return;
        }

        try {
            List<FileSaveRequestDto> fileSaveRequestDtos = fileHandlerHelper.uploadFilesToServer(productRequestDto.getFiles(), FileType.products);
            int result = saveFiles(productRequestDto.getProductId(), fileSaveRequestDtos);
            log.info("Successfully saved {} files to database for product ID {}.", result, productRequestDto.getProductId());
        } catch (IOException | FileException e) {
            log.error("File processing error: {}", e.getMessage());
            rollbackUploadedFiles(productRequestDto.getProductId());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            rollbackUploadedFiles(productRequestDto.getProductId());
            throw e;
        }
    }

    private void rollbackUploadedFiles(Integer productId) {
        List<ProductFiles> productFiles = getFileResponseDtos(productId);
        fileHandlerHelper.deleteFiles(productFiles);
    }

    public int saveFiles(Integer productId, List<FileSaveRequestDto> files) {
        if (CollectionUtils.isEmpty(files)) {
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
        if (productMapper.updateProduct(productUpdateRequestDto) < 0) {
            throw new ProductException(FAIL_SAVE_PRODUCT);
        }

        if (isEmptyFiles(productUpdateRequestDto.getFiles())) {
            throw new FileException(FAIL_UPDATE_FILES);
        }

        int uploadFilesCount = updateFilesByProductId(productUpdateRequestDto.getProductId(), productUpdateRequestDto.getFiles());
        if (uploadFilesCount < 1) {
            log.error(FAIL_UPLOAD_FILES.getMessage());
            throw new FileException(FAIL_UPLOAD_FILES);
        }
    }

    private int updateFilesByProductId(Integer productId, List<MultipartFile> files) {
        if (productId == null || CollectionUtils.isEmpty(files)) {
            return 0;
        }
        int deleteCount = deleteExistingFiles(productId);
        log.info("[updateFilesByProductId] 삭제된 파일 개수 = {}", deleteCount);

        return saveUpdatedNewFiles(productId, files);
    }

    private int saveUpdatedNewFiles(Integer productId, List<MultipartFile> files) {
        List<FileSaveRequestDto> fileSaveRequestDtos = fileHandlerHelper.uploadFilesToServer(files, FileType.products); // 서버의 특정 경로에 파일 업로드
        fileSaveRequestDtos.forEach(fileRequestDto -> fileRequestDto.setId(productId));
        return productFileMapper.saveFiles(fileSaveRequestDtos);
    }

    private int deleteExistingFiles(Integer productId) {
        int deleteCount = productFileMapper.deleteFilesByProductId(productId);
        if (deleteCount > 0) {
            log.info("[updateFilesByProductId] 파일 삭제에 성공하였습니다. productId = {}", productId);
            fileHandlerHelper.deleteFiles(getFileResponseDtos(productId)); // 서버 특정 경로에 존재하는 파일 삭제
        } else {
            log.error("[updateFilesByProductId] 파일 삭제에 실패하였습니다. productId = {}", productId);
        }
        return deleteCount;
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        // 상품 삭제 성공하지 않았을 경우, 추가 작업 중단 후 메서드 종료
        if (productMapper.deleteProduct(productId) < 1) {
            return;
        }

        // 파일 목록이 비어있을 경우, 파일 삭제 로직 수행할 필요가 없음
        List<ProductFiles> fileResponseDtos = getFileResponseDtos(productId);
        if (CollectionUtils.isEmpty(fileResponseDtos)) {
            return;
        }

        if (productFileMapper.deleteFilesByProductId(productId) > 0) {
            fileHandlerHelper.deleteFiles(fileResponseDtos);
        }
    }

    private boolean isEmptyFiles(List<MultipartFile> files) {
        return CollectionUtils.isEmpty(files);
    }

    private List<ProductFiles> getFileResponseDtos(Integer productId) {
        return productFileMapper.getFilesByProductId(productId);
    }
}