package com.shoppingmall.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class HealthCheckRestController {

    @GetMapping("/shop/health-check")
    public ResponseEntity<HttpStatus> healthCheck(@RequestHeader HttpHeaders header) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
