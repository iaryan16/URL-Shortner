package com.aryna.URL_Shortner.entity;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class UrlMapping {

    @Id
    private String id;

    @NonNull
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt;

}
