package com.aryna.URL_Shortner.service;

import com.aryna.URL_Shortner.dto.request.CreateShortCode;
import com.aryna.URL_Shortner.dto.response.ShortCodeResponse;
import com.aryna.URL_Shortner.dto.response.UrlStatusResponse;
import com.aryna.URL_Shortner.entity.UrlMapping;
import com.aryna.URL_Shortner.exception.ExpiredUrlException;
import com.aryna.URL_Shortner.exception.InvalidUrlException;
import com.aryna.URL_Shortner.exception.ShortCodeAlreadyExistsException;
import com.aryna.URL_Shortner.exception.UrlNotFoundException;
import com.aryna.URL_Shortner.repository.UrlRepository;
import com.aryna.URL_Shortner.utils.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.UnknownNullability;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    @Value("${app.base-url}")
    private String baseUrl;

    private final UrlRepository urlRepository;

    @Override
    public ShortCodeResponse createShortCode(@UnknownNullability CreateShortCode request) {

        String originalUrl = request.getOriginalUrl();

        // Validate URL
        try {
            URI.create(originalUrl).toURL();
        } catch (Exception e) {
            throw new InvalidUrlException("Invalid URL is given");
        }

        String code;

        // Custom short code
        if (request.getCustomCode() != null &&
                !request.getCustomCode().trim().isEmpty()) {

            String customCode = request.getCustomCode().trim();

            boolean isValid =
                    customCode.matches("^[a-zA-Z0-9_-]{3,10}$");

            if (!isValid) {
                throw new InvalidUrlException(
                        "Short code must be 3-10 characters and contain only letters, numbers, _ or -"
                );
            }

            if (urlRepository.existsByShortCode(customCode)) {
                throw new ShortCodeAlreadyExistsException(
                        customCode + " is already taken"
                );
            }

            code = customCode;

        } else {

            // Generate random unique code
            do {
                code = ShortCodeGenerator.generate();
            } while (urlRepository.existsByShortCode(code));
        }

        // Validate expiry
        if (request.getExpiryAt() != null &&
                request.getExpiryAt().isBefore(OffsetDateTime.now())) {

            throw new InvalidUrlException(
                    "Expiry time must be in the future"
            );
        }

        // MongoDB document
        UrlMapping newEntity = UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortCode(code)
                .createdAt(Instant.now())
                .expiryAt(request.getExpiryAt())
                .build();

        urlRepository.save(newEntity);

        return ShortCodeResponse.builder()
                .originalUrl(originalUrl)
                .shortUrl(baseUrl + "/" + code)
                .shortCode(code)
                .build();
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short code not found")
                );

        // Check expiration
        if (mapping.getExpiryAt() != null &&
                mapping.getExpiryAt().isBefore(OffsetDateTime.now())) {

            throw new ExpiredUrlException(
                    "The given ShortCode has expired"
            );
        }

        // Increase click count
        mapping.setClickCount(mapping.getClickCount() + 1);

        // Updates existing MongoDB document because _id already exists
        urlRepository.save(mapping);

        return mapping.getOriginalUrl();
    }

    @Override
    public UrlStatusResponse getUrlStatus(String shortCode) {

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short code not found")
                );

        return UrlStatusResponse.builder()
                .originalUrl(mapping.getOriginalUrl())
                .shortCode(mapping.getShortCode())
                .createdAt(mapping.getCreatedAt())
                .clickCount(mapping.getClickCount())
                .build();
    }
}