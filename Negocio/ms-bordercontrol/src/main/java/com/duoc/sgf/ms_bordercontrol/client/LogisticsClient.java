package com.duoc.sgf.ms_bordercontrol.client;


import com.duoc.sgf.ms_bordercontrol.model.dto.LogisticsCheckpointBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-logistics")
public interface LogisticsClient {

    @GetMapping("/api/v1/logistics/{id}")
    LogisticsCheckpointBasicDto findById(@PathVariable Long id);

}