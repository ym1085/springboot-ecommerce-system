package com.shoppingmall.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {
    // products
    PHONE("휴대폰"),
    WATCH("스마트시계"),
    TABLET("태블릿"),
    LAPTOP("노트북"),
    ACCESSORY("액세서리"),

    // posts
    GBOARD("전체게시판"),
    MBOARD("회원게시판")
    ;

    public String getLowerCaseName() {
        return this.name().toLowerCase();
    }

    public String getUpperCaseName() {
        return this.name().toUpperCase();
    }

    /**
     * 상품 카테고리명을 기반으로 ENUM 객체 반환
     * 파일 업로드/업데이트 시 디렉토리 경로명으로 사용하기 위해 사용
     * @param categoryName
     * @return
     */
    public static Category fromCategoryName(String categoryName) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getCategoryName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown category name: " + categoryName));
    }

    private final String categoryName;
}