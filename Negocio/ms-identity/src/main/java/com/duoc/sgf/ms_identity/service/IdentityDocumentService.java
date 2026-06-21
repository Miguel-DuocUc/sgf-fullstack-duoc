package com.duoc.sgf.ms_identity.service;

import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentRequestDto;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentResponseDto;

import java.util.List;

public interface IdentityDocumentService {

    List<IdentityDocumentResponseDto> findAll();

    IdentityDocumentResponseDto findById(Long id);

    List<IdentityDocumentResponseDto> findByUserId(Long userId);

    List<IdentityDocumentResponseDto> findByStatus(String status);

    IdentityDocumentResponseDto create(IdentityDocumentRequestDto request);

    IdentityDocumentResponseDto update(Long id, IdentityDocumentRequestDto request);

    IdentityDocumentResponseDto validateDocument(Long id);

    void delete(Long id);
}