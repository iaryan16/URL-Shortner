package com.aryan.URL_Shortner.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.time.OffsetDateTime;

@Document(collection = "url_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlMapping {

    @Id
    private String id;

    private String originalUrl;

    @Indexed(unique = true)
    private String shortCode;

    @CreatedDate
    private Instant createdAt;

    private OffsetDateTime expiryAt;

    @Builder.Default
    private Long clickCount = 0L;
}