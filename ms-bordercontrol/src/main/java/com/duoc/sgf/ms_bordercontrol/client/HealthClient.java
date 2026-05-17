package com.duoc.sgf.ms_bordercontrol.client;

import com.duoc.sgf.ms_bordercontrol.dto.HealthDeclarationBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-health-border-client", url = "${clients.ms-health.url}")
public interface HealthClient {

    @GetMapping("/api/v1/health-declarations/{id}")
    HealthDeclarationBasicDto findById(@PathVariable("id") Long id);
}