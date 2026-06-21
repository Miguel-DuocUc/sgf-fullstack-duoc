package com.duoc.sgf.ms_identity.repository;

import com.duoc.sgf.ms_identity.model.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long> {

    List<IdentityDocument> findByUserId(Long userId);

    Optional<IdentityDocument> findByDocumentNumber(String documentNumber);

    List<IdentityDocument> findByStatus(String status);

    boolean existsByDocumentNumber(String documentNumber);
}