package com.aryan.URL_Shortner.service;

import com.aryan.URL_Shortner.dto.request.CreateShortCode;
import com.aryan.URL_Shortner.dto.response.ShortCodeResponse;
import com.aryan.URL_Shortner.dto.response.UrlStatusResponse;

public interface UrlService {
    ShortCodeResponse createShortCode(CreateShortCode request);

    String getOriginalUrl(String shortCode);

    UrlStatusResponse getUrlStatus(String shortCode);
}
