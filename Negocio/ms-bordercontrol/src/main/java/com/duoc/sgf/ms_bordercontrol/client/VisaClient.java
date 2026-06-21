package com.duoc.sgf.ms_bordercontrol.client;

import com.duoc.sgf.ms_bordercontrol.model.dto.VisaRequestBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-visa")
public interface VisaClient {

    @GetMapping("/api/v1/visas/{id}")
    VisaRequestBasicDto findById(@PathVariable("id") Long id);
}