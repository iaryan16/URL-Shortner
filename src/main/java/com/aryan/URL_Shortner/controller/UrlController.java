package com.aryan.URL_Shortner.controller;

import com.aryan.URL_Shortner.dto.request.CreateShortCode;
import com.aryan.URL_Shortner.dto.response.ShortCodeResponse;
import com.aryan.URL_Shortner.dto.response.UrlStatusResponse;
import com.aryan.URL_Shortner.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shortner/create-code")
    public ShortCodeResponse createShortUrl(@RequestBody @Valid CreateShortCode request){
        return urlService.createShortCode(request);
    }

    @GetMapping("/{code:[a-zA-Z0-9_-]+}")
    public ResponseEntity<Void>  getOriginalUrl(@PathVariable String code){
        String originalUrl = urlService.getOriginalUrl(code);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION,originalUrl)
                .build();
    }

    @GetMapping("/{code}/status")
    public UrlStatusResponse getStatus(@PathVariable String code){
        return  urlService.getUrlStatus(code);
    }
}