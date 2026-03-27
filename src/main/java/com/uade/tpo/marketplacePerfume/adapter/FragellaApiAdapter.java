package com.uade.tpo.marketplacePerfume.adapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse;

@Component
public class FragellaApiAdapter {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FragellaApiAdapter(RestTemplate fragellaRestTemplate,
                             @Value("${fragella.api.base-url}") String baseUrl) {
        this.restTemplate = fragellaRestTemplate;
        this.baseUrl = baseUrl;
    }

    public List<FragellaFragranceResponse> searchFragrances(String query, int limit) {
        String url = baseUrl + "/fragrances?search=" + query + "&limit=" + limit;
        ResponseEntity<FragellaFragranceResponse[]> response =
                restTemplate.getForEntity(url, FragellaFragranceResponse[].class);
        return response.getBody() != null
                ? Arrays.asList(response.getBody())
                : Collections.emptyList();
    }
}
