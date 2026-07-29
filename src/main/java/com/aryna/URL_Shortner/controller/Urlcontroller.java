package com.aryna.URL_Shortner.controller;

import com.aryna.URL_Shortner.dto.request.CreateShortCode;
import com.aryna.URL_Shortner.dto.response.ShortCodeResponse;
import com.aryna.URL_Shortner.dto.response.UrlStatusResponse;
import com.aryna.URL_Shortner.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Urlcontroller {
    private final UrlService urlService;

    @PostMapping("/api/urls")
    public ShortCodeResponse createShortUrl(@RequestBody @Valid CreateShortCode request){
        return urlService.createShortCode(request);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void>  getOriginalUrl(@PathVariable String code){
        String originalUrl = urlService.getOriginalUrl(code);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION,originalUrl)
                .build();
    }

    @GetMapping("/{code}/stats")
    public UrlStatusResponse getStats(@PathVariable String code){
        return  urlService.getUrlStatus(code);
    }
}