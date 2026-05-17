package com.duoc.sgf.ms_visa.repository;

import com.duoc.sgf.ms_visa.model.VisaRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisaRequestRepository extends JpaRepository<VisaRequest, Long> {

    List<VisaRequest> findByUserId(Long userId);

    List<VisaRequest> findByIdentityDocumentId(Long identityDocumentId);

    List<VisaRequest> findByStatus(String status);
}