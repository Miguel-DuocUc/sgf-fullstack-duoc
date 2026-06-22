package com.duoc.sgf.ms_identity.service.impl;

import com.duoc.sgf.ms_identity.client.UserClient;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentRequestDto;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentResponseDto;
import com.duoc.sgf.ms_identity.model.dto.UserBasicDto;
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
        log.info("Listando todos los documentos de identidad registrados");

        List<IdentityDocumentResponseDto> documents = identityDocumentRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Total de documentos de identidad encontrados: {}", documents.size());

        return documents;
    }

    @Override
    public IdentityDocumentResponseDto findById(Long id) {
        log.info("Buscando documento de identidad por id: {}", id);

        IdentityDocument document = identityDocumentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Documento de identidad no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
                });

        log.info("Documento de identidad encontrado correctamente con id: {}", id);

        return toResponseDto(document);
    }

    @Override
    public List<IdentityDocumentResponseDto> findByUserId(Long userId) {
        log.info("Buscando documentos de identidad asociados al usuario id: {}", userId);

        List<IdentityDocumentResponseDto> documents = identityDocumentRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Consulta completada para usuario id: {}. Documentos encontrados: {}", userId, documents.size());

        return documents;
    }

    @Override
    public List<IdentityDocumentResponseDto> findByStatus(String status) {
        log.info("Buscando documentos de identidad por estado: {}", status);

        List<IdentityDocumentResponseDto> documents = identityDocumentRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Consulta completada para estado: {}. Documentos encontrados: {}", status, documents.size());

        return documents;
    }

    @Override
    public IdentityDocumentResponseDto create(IdentityDocumentRequestDto request) {
        log.info("Iniciando creación de documento de identidad para usuario id: {}", request.getUserId());

        validateUserExists(request.getUserId());

        log.info("Usuario validado correctamente para creación de documento. Usuario id: {}", request.getUserId());

        if (identityDocumentRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            log.warn("No se pudo crear documento. Número de documento duplicado: {}", request.getDocumentNumber());
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

        log.info("Guardando documento de identidad para usuario id: {}", request.getUserId());

        IdentityDocument savedDocument = identityDocumentRepository.save(document);

        log.info("Documento de identidad creado correctamente con id: {}", savedDocument.getId());

        return toResponseDto(savedDocument);
    }

    @Override
    public IdentityDocumentResponseDto update(Long id, IdentityDocumentRequestDto request) {
        log.info("Iniciando actualización de documento de identidad con id: {}", id);

        IdentityDocument document = identityDocumentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Documento no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
                });

        validateUserExists(request.getUserId());

        log.info("Usuario validado correctamente para actualización de documento. Usuario id: {}", request.getUserId());

        identityDocumentRepository.findByDocumentNumber(request.getDocumentNumber()).ifPresent(existingDocument -> {
            if (!existingDocument.getId().equals(id)) {
                log.warn("No se pudo actualizar documento id: {}. Número de documento duplicado: {}", id, request.getDocumentNumber());
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

        log.info("Guardando actualización de documento de identidad con id: {}", id);

        IdentityDocument updatedDocument = identityDocumentRepository.save(document);

        log.info("Documento de identidad actualizado correctamente con id: {}", updatedDocument.getId());

        return toResponseDto(updatedDocument);
    }

    @Override
    public IdentityDocumentResponseDto validateDocument(Long id) {
        log.info("Iniciando validación de documento de identidad con id: {}", id);

        IdentityDocument document = identityDocumentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo validar. Documento no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
                });

        log.info("Evaluando reglas de validación para documento id: {}", id);

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

        log.info("Proceso de validación finalizado para documento id: {} con estado: {}", id, validatedDocument.getStatus());

        return toResponseDto(validatedDocument);
    }

    @Override
    public void delete(Long id) {
        log.info("Iniciando eliminación de documento de identidad con id: {}", id);

        if (!identityDocumentRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Documento no encontrado con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento de identidad no encontrado");
        }

        identityDocumentRepository.deleteById(id);

        log.info("Documento de identidad eliminado correctamente con id: {}", id);
    }

    private void validateUserExists(Long userId) {
        try {
            log.info("Consultando usuario en MS-Users con id: {}", userId);

            UserBasicDto user = userClient.findById(userId);

            log.info("Respuesta recibida desde MS-Users para usuario id: {}", userId);

            if (user == null) {
                log.warn("Usuario remoto no encontrado con id: {}", userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            }

            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) {
                log.warn("Usuario remoto no activo. Id: {}, estado: {}", userId, user.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está activo");
            }

            log.info("Usuario validado correctamente desde MS-Users con id: {}", userId);

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Users para usuario id: {}. Detalle: {}", userId, ex.getMessage());
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