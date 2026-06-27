package com.duoc.sgf.ms_bordercontrol.client;

import com.duoc.sgf.ms_bordercontrol.model.dto.AlertBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-alerts")
public interface AlertClient {
    @GetMapping("/api/v1/alerts/passport/{pasaporte}")
    List<AlertBasicDto> findByPassport(@PathVariable("pasaporte") String pasaporte);
}