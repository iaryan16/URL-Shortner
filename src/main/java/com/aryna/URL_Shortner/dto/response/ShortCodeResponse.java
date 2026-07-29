package com.aryna.URL_Shortner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ShortCodeResponse {
    private String originalUrl;
    private String shortUrl;
    private String shortCode;
}