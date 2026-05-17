package com.duoc.sgf.ms_bordercontrol.repository;

import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorderControlRepository extends JpaRepository<BorderControl, Long> {

    List<BorderControl> findByUserId(Long userId);

    List<BorderControl> findByIdentityDocumentId(Long identityDocumentId);

    List<BorderControl> findByVisaRequestId(Long visaRequestId);

    List<BorderControl> findByHealthDeclarationId(Long healthDeclarationId);

    List<BorderControl> findByStatus(String status);
}