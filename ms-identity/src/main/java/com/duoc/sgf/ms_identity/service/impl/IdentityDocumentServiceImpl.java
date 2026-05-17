package com.duoc.sgf.ms_identity.service.impl;

import com.duoc.sgf.ms_identity.client.UserClient;
import com.duoc.sgf.ms_identity.dto.IdentityDocumentRequestDto;
import com.duoc.sgf.ms_identity.dto.IdentityDocumentResponseDto;
import com.duoc.sgf.ms_identity.dto.UserBasicDto;
import com.duoc.sgf.ms_identity.model.IdentityDocument;
import com.duoc.sgf.ms_identity.repository.IdentityDocumentRepository;
import com.duoc.sgf.ms_identity.service.IdentityDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private final IdentityDocumentRepository identityDocumentRepository;
    private final UserClient userClient;

    @Override
    public List<IdentityDocumentResponseDto> findAll() {
        log.info("Listando documentos de identidad");
        return identityDocumentRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public IdentityDocumentResponseDto findById(Long id) {
        log.info("Buscando documento de identidad por id: {}", id);

        IdentityDocument document = identityDocumentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Documento de identidad no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
                });

        return toResponseDto(document);
    }

    @Override
    public List<IdentityDocumentResponseDto> findByUserId(Long userId) {
        log.info("Buscando documentos de identidad por usuario: {}", userId);

        return identityDocumentRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<IdentityDocumentResponseDto> findByStatus(String status) {
        log.info("Buscando documentos de identidad por estado: {}", status);

        return identityDocumentRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public IdentityDocumentResponseDto create(IdentityDocumentRequestDto request) {
        log.info("Creando documento de identidad para usuario id: {}", request.getUserId());

        validateUserExists(request.getUserId());

        if (identityDocumentRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            log.warn("Número de documento duplicado: {}", request.getDocumentNumber());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un documento con ese número");
        }

        IdentityDocument document = new IdentityDocument();
        document.setUserId(request.getUserId());
        document.setDocumentType(request.getDocumentType().toUpperCase());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setIssuingCountry(request.getIssuingCountry().toUpperCase());
        document.setHolderName(request.getHolderName());
        document.setHolderLastName(request.getHolderLastName());
        document.setExpirationDate(request.getExpirationDate());
        document.setMinor(request.getMinor() != null ? request.getMinor() : false);
        document.setNotarizedAuthorization(request.getNotarizedAuthorization() != null ? request.getNotarizedAuthorization() : false);

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            document.setStatus("PENDIENTE");
        } else {
            document.setStatus(request.getStatus().toUpperCase());
        }

        IdentityDocument savedDocument = identityDocumentRepository.save(document);

        log.info("Documento de identidad creado correctamente con id: {}", savedDocument.getId());

        return toResponseDto(savedDocument);
    }

    @Override
    public IdentityDocumentResponseDto update(Long id, IdentityDocumentRequestDto request) {
        log.info("Actualizando documento de identidad con id: {}", id);

        IdentityDocument document = identityDocumentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Documento no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
                });

        validateUserExists(request.getUserId());

        identityDocumentRepository.findByDocumentNumber(request.getDocumentNumber()).ifPresent(existingDocument -> {
            if (!existingDocument.getId().equals(id)) {
                log.warn("Intento de actualizar con número de documento duplicado: {}", request.getDocumentNumber());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otro documento con ese número");
            }
        });

        document.setUserId(request.getUserId());
        document.setDocumentType(request.getDocumentType().toUpperCase());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setIssuingCountry(request.getIssuingCountry().toUpperCase());
        document.setHolderName(request.getHolderName());
        document.setHolderLastName(request.getHolderLastName());
        document.setExpirationDate(request.getExpirationDate());
        document.setMinor(request.getMinor() != null ? request.getMinor() : false);
        document.setNotarizedAuthorization(request.getNotarizedAuthorization() != null ? request.getNotarizedAuthorization() : false);

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            document.setStatus("PENDIENTE");
        } else {
            document.setStatus(request.getStatus().toUpperCase());
        }

        IdentityDocument updatedDocument = identityDocumentRepository.save(document);

        log.info("Documento de identidad actualizado correctamente con id: {}", updatedDocument.getId());

        return toResponseDto(updatedDocument);
    }

    @Override
    public IdentityDocumentResponseDto validateDocument(Long id) {
        log.info("Validando documento de identidad con id: {}", id);

        IdentityDocument document = identityDocumentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo validar. Documento no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
                });

        if (document.getExpirationDate().isBefore(LocalDate.now())) {
            document.setStatus("RECHAZADO");
            log.warn("Documento rechazado por estar vencido. Id: {}", id);
        } else if (Boolean.TRUE.equals(document.getMinor()) && !Boolean.TRUE.equals(document.getNotarizedAuthorization())) {
            document.setStatus("RECHAZADO");
            log.warn("Documento rechazado por menor sin autorización notarial. Id: {}", id);
        } else {
            document.setStatus("VALIDADO");
            log.info("Documento validado correctamente. Id: {}", id);
        }

        IdentityDocument validatedDocument = identityDocumentRepository.save(document);

        return toResponseDto(validatedDocument);
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando documento de identidad con id: {}", id);

        if (!identityDocumentRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Documento no encontrado con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
        }

        identityDocumentRepository.deleteById(id);

        log.info("Documento de identidad eliminado correctamente con id: {}", id);
    }

    private void validateUserExists(Long userId) {
        try {
            UserBasicDto user = userClient.findById(userId);

            if (user == null) {
                log.warn("Usuario remoto no encontrado con id: {}", userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            }

            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) {
                log.warn("Usuario remoto no activo. Id: {}, estado: {}", userId, user.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está activo");
            }

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Users para usuario id: {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Users no está disponible o el usuario no existe");
        }
    }

    private IdentityDocumentResponseDto toResponseDto(IdentityDocument document) {
        return new IdentityDocumentResponseDto(
                document.getId(),
                document.getUserId(),
                document.getDocumentType(),
                document.getDocumentNumber(),
                document.getIssuingCountry(),
                document.getHolderName(),
                document.getHolderLastName(),
                document.getExpirationDate(),
                document.getMinor(),
                document.getNotarizedAuthorization(),
                document.getStatus(),
                document.getCreatedAt()
        );
    }
}