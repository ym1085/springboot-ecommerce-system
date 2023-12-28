package com.shoppingmall.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {

    private String email;
    private String authCode;

}
