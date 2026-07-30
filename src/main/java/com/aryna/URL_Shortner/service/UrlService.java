package com.aryna.URL_Shortner.service;


import com.aryna.URL_Shortner.dto.request.CreateShortCode;
import com.aryna.URL_Shortner.dto.response.ShortCodeResponse;
import com.aryna.URL_Shortner.dto.response.UrlStatusResponse;

public interface UrlService {
    ShortCodeResponse createShortCode(CreateShortCode request);

    String getOriginalUrl(String shortCode);

    UrlStatusResponse getUrlStatus(String shortCode);
}
