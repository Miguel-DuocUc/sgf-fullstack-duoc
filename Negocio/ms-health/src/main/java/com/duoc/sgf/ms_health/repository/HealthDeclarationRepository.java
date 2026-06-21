package com.duoc.sgf.ms_health.repository;

import com.duoc.sgf.ms_health.model.HealthDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthDeclarationRepository extends JpaRepository<HealthDeclaration, Long> {

    List<HealthDeclaration> findByUserId(Long userId);

    List<HealthDeclaration> findByIdentityDocumentId(Long identityDocumentId);

    List<HealthDeclaration> findByStatus(String status);

    List<HealthDeclaration> findByRiskLevel(String riskLevel);
}