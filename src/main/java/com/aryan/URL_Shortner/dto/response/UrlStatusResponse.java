package com.aryan.URL_Shortner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.OffsetDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlStatusResponse {

    private String originalUrl;

    private String shortCode;

    private Instant createdAt;

    private OffsetDateTime expiryAt;

    private Long clickCount ;
}