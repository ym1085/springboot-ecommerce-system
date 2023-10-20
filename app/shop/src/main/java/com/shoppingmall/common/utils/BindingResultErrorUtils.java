package com.shoppingmall.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingResultErrorUtils {

    public static Map<String, String> extractBindingResultErrorMessages(BindingResult bindingResult) {
        Map<String, String> result = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError error : fieldErrors) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            result.put(fieldName, errorMessage);
        }

        return result;
    }
}
