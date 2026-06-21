package com.duoc.sgf.ms_bordercontrol.client;

import com.duoc.sgf.ms_bordercontrol.model.dto.UserBasicDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-users")
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    UserBasicDto findById(@PathVariable("id") Long id);
}