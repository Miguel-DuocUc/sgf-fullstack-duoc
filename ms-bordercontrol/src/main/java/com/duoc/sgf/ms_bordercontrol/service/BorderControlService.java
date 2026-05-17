package com.duoc.sgf.ms_bordercontrol.service;

import com.duoc.sgf.ms_bordercontrol.dto.BorderControlRequestDto;
import com.duoc.sgf.ms_bordercontrol.dto.BorderControlResponseDto;

import java.util.List;

public interface BorderControlService {

    List<BorderControlResponseDto> findAll();
    BorderControlResponseDto findById(Long id);
    List<BorderControlResponseDto> findByUserId(Long userId);
    List<BorderControlResponseDto> findByStatus(String status);
    BorderControlResponseDto create(BorderControlRequestDto request);
    BorderControlResponseDto update(Long id, BorderControlRequestDto request);
    BorderControlResponseDto evaluate(Long id);

    void delete(Long id);
}