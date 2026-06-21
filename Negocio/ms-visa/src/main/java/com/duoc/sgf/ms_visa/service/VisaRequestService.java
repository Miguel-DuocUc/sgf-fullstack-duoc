package com.duoc.sgf.ms_visa.service;

import com.duoc.sgf.ms_visa.model.dto.VisaRequestDto;
import com.duoc.sgf.ms_visa.model.dto.VisaResponseDto;

import java.util.List;

public interface VisaRequestService {

    List<VisaResponseDto> findAll();

    VisaResponseDto findById(Long id);

    List<VisaResponseDto> findByUserId(Long userId);

    List<VisaResponseDto> findByStatus(String status);

    VisaResponseDto create(VisaRequestDto request);

    VisaResponseDto update(Long id, VisaRequestDto request);

    VisaResponseDto approve(Long id);

    VisaResponseDto reject(Long id);

    void delete(Long id);
}