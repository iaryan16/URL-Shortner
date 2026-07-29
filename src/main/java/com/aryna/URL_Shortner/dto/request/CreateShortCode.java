package com.aryna.URL_Shortner.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
public class CreateShortCode {
    @NotBlank(message = "URL cannot be Empty")
    private String originalUrl;
    private String customCode;

    private OffsetDateTime expiryAt;
}