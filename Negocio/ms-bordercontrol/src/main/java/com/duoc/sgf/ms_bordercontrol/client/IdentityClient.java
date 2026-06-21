package com.duoc.sgf.ms_bordercontrol.client;

import com.duoc.sgf.ms_bordercontrol.model.dto.IdentityDocumentBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-identity")
public interface IdentityClient {

    @GetMapping("/api/v1/identity/{id}")
    IdentityDocumentBasicDto findById(@PathVariable("id") Long id);
}