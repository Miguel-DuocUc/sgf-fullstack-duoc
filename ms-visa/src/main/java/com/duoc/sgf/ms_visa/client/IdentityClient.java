package com.duoc.sgf.ms_visa.client;

import com.duoc.sgf.ms_visa.dto.IdentityDocumentBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-identity", url = "${clients.ms-identity.url}")
public interface IdentityClient {

    @GetMapping("/api/v1/identity-documents/{id}")
    IdentityDocumentBasicDto findById(@PathVariable("id") Long id);
}