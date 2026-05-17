package com.duoc.sgf.ms_health.client;

import com.duoc.sgf.ms_health.dto.IdentityDocumentBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-identity-health-client", url = "${clients.ms-identity.url}")
public interface IdentityClient {

    @GetMapping("/api/v1/identity-documents/{id}")
    IdentityDocumentBasicDto findById(@PathVariable("id") Long id);
}