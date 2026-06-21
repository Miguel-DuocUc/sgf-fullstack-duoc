package com.duoc.sgf.ms_bordercontrol.client;

import com.duoc.sgf.ms_bordercontrol.model.dto.HealthDeclarationBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-health")
public interface HealthClient {

    @GetMapping("/api/v1/health/{id}")
    HealthDeclarationBasicDto findById(@PathVariable("id") Long id);
}