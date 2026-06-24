package com.duoc.sgf.ms_health.service.impl;

import com.duoc.sgf.ms_health.client.IdentityClient;
import com.duoc.sgf.ms_health.client.UserClient;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;
import com.duoc.sgf.ms_health.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_health.model.dto.UserBasicDto;
import com.duoc.sgf.ms_health.model.HealthDeclaration;
import com.duoc.sgf.ms_health.repository.HealthDeclarationRepository;
import com.duoc.sgf.ms_health.service.HealthDeclarationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthDeclarationServiceImpl implements HealthDeclarationService {

    private final HealthDeclarationRepository healthDeclarationRepository;
    private final UserClient userClient;
    private final IdentityClient identityClient;

    @Override
    public List<HealthDeclarationResponseDto> findAll() {
        log.info("Listando todas las declaraciones sanitarias registradas");

        List<HealthDeclarationResponseDto> declarations = healthDeclarationRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Total de declaraciones sanitarias encontradas: {}", declarations.size());

        return declarations;
    }

    @Override
    public HealthDeclarationResponseDto findById(Long id) {
        log.info("Buscando declaración sanitaria por id: {}", id);

        HealthDeclaration declaration = healthDeclarationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Declaración sanitaria no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Declaración sanitaria no encontrada");
                });

        log.info("Declaración sanitaria encontrada correctamente con id: {}", id);

        return toResponseDto(declaration);
    }

    @Override
    public List<HealthDeclarationResponseDto> findByUserId(Long userId) {
        log.info("Buscando declaraciones sanitarias asociadas al usuario id: {}", userId);

        List<HealthDeclarationResponseDto> declarations = healthDeclarationRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Consulta completada para usuario id: {}. Declaraciones encontradas: {}", userId, declarations.size());

        return declarations;
    }

    @Override
    public List<HealthDeclarationResponseDto> findByStatus(String status) {
        log.info("Buscando declaraciones sanitarias por estado: {}", status);

        List<HealthDeclarationResponseDto> declarations = healthDeclarationRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Consulta completada para estado: {}. Declaraciones encontradas: {}", status, declarations.size());

        return declarations;
    }

    @Override
    public HealthDeclarationResponseDto create(HealthDeclarationRequestDto request) {
        log.info("Iniciando creación de declaración sanitaria para usuario id: {}", request.getUserId());

        validateUser(request.getUserId());
        log.info("Usuario validado correctamente para creación de declaración sanitaria. Usuario id: {}", request.getUserId());

        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        log.info("Documento de identidad validado correctamente para declaración sanitaria. Documento id: {}", request.getIdentityDocumentId());

        HealthDeclaration declaration = new HealthDeclaration();
        declaration.setUserId(request.getUserId());
        declaration.setIdentityDocumentId(request.getIdentityDocumentId());
        declaration.setHasSymptoms(request.getHasSymptoms());
        declaration.setSymptomsDescription(request.getSymptomsDescription());
        declaration.setHasRecentContact(request.getHasRecentContact());
        declaration.setVaccinationStatus(request.getVaccinationStatus().toUpperCase());
        declaration.setObservations(request.getObservations());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            declaration.setStatus("PENDIENTE");
        } else {
            declaration.setStatus(request.getStatus().toUpperCase());
        }

        declaration.setRiskLevel(calculateRiskLevel(request));

        log.info("Nivel de riesgo calculado para declaración sanitaria: {}", declaration.getRiskLevel());
        log.info("Guardando declaración sanitaria para usuario id: {}", request.getUserId());

        HealthDeclaration savedDeclaration = healthDeclarationRepository.save(declaration);

        log.info("Declaración sanitaria creada correctamente con id: {}", savedDeclaration.getId());

        return toResponseDto(savedDeclaration);
    }

    @Override
    public HealthDeclarationResponseDto update(Long id, HealthDeclarationRequestDto request) {
        log.info("Iniciando actualización de declaración sanitaria con id: {}", id);

        HealthDeclaration declaration = healthDeclarationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Declaración sanitaria no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Declaración sanitaria no encontrada");
                });

        validateUser(request.getUserId());
        log.info("Usuario validado correctamente para actualización de declaración sanitaria. Usuario id: {}", request.getUserId());

        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        log.info("Documento de identidad validado correctamente para actualización de declaración sanitaria. Documento id: {}", request.getIdentityDocumentId());

        declaration.setUserId(request.getUserId());
        declaration.setIdentityDocumentId(request.getIdentityDocumentId());
        declaration.setHasSymptoms(request.getHasSymptoms());
        declaration.setSymptomsDescription(request.getSymptomsDescription());
        declaration.setHasRecentContact(request.getHasRecentContact());
        declaration.setVaccinationStatus(request.getVaccinationStatus().toUpperCase());
        declaration.setObservations(request.getObservations());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            declaration.setStatus("PENDIENTE");
        } else {
            declaration.setStatus(request.getStatus().toUpperCase());
        }

        declaration.setRiskLevel(calculateRiskLevel(request));

        log.info("Nivel de riesgo recalculado para declaración sanitaria id {}: {}", id, declaration.getRiskLevel());
        log.info("Guardando actualización de declaración sanitaria con id: {}", id);

        HealthDeclaration updatedDeclaration = healthDeclarationRepository.save(declaration);

        log.info("Declaración sanitaria actualizada correctamente con id: {}", updatedDeclaration.getId());

        return toResponseDto(updatedDeclaration);
    }

    @Override
    public HealthDeclarationResponseDto evaluate(Long id) {
        log.info("Iniciando evaluación de declaración sanitaria con id: {}", id);

        HealthDeclaration declaration = healthDeclarationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo evaluar. Declaración sanitaria no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Declaración sanitaria no encontrada");
                });

        log.info("Aplicando reglas sanitarias para declaración id: {}", id);

        if (Boolean.TRUE.equals(declaration.getHasSymptoms())) {
            declaration.setStatus("NO_APTO");
            declaration.setRiskLevel("ALTO");
            declaration.setObservations("Declaración rechazada por presencia de síntomas.");
            log.warn("Declaración sanitaria id {} marcada como NO_APTO por presencia de síntomas", id);
        } else if (Boolean.TRUE.equals(declaration.getHasRecentContact())) {
            declaration.setStatus("EN_REVISION");
            declaration.setRiskLevel("MEDIO");
            declaration.setObservations("Declaración requiere revisión por contacto reciente.");
            log.warn("Declaración sanitaria id {} enviada a revisión por contacto reciente", id);
        } else {
            declaration.setStatus("APTO");
            declaration.setRiskLevel("BAJO");
            declaration.setObservations("Declaración sanitaria aprobada.");
            log.info("Declaración sanitaria id {} marcada como APTO", id);
        }

        HealthDeclaration evaluatedDeclaration = healthDeclarationRepository.save(declaration);

        log.info(
                "Declaración sanitaria evaluada correctamente con id: {}, estado: {} y riesgo: {}",
                id,
                evaluatedDeclaration.getStatus(),
                evaluatedDeclaration.getRiskLevel()
        );

        return toResponseDto(evaluatedDeclaration);
    }

    @Override
    public void delete(Long id) {
        log.info("Iniciando eliminación de declaración sanitaria con id: {}", id);

        if (!healthDeclarationRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Declaración sanitaria no encontrada con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Declaración sanitaria no encontrada");
        }

        healthDeclarationRepository.deleteById(id);

        log.info("Declaración sanitaria eliminada correctamente con id: {}", id);
    }

    private void validateUser(Long userId) {
        try {
            log.info("Consultando usuario en MS-Users con id: {}", userId);

            UserBasicDto user = userClient.findById(userId);

            log.info("Respuesta recibida desde MS-Users para usuario id: {}", userId);

            if (user == null) {
                log.warn("Usuario remoto no encontrado con id: {}", userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            }

            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) {
                log.warn("Usuario no activo. Id: {}, estado: {}", userId, user.getStatus());
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

    private void validateIdentityDocument(Long identityDocumentId, Long userId) {
        try {
            log.info("Consultando documento de identidad en MS-Identity con id: {}", identityDocumentId);

            IdentityDocumentBasicDto document = identityClient.findById(identityDocumentId);

            log.info("Respuesta recibida desde MS-Identity para documento id: {}", identityDocumentId);

            if (document == null) {
                log.warn("Documento remoto no encontrado con id: {}", identityDocumentId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no existe");
            }

            if (!document.getUserId().equals(userId)) {
                log.warn("Documento id {} no pertenece al usuario id {}", identityDocumentId, userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento no pertenece al usuario indicado");
            }

            boolean documentValid = "VALIDO".equalsIgnoreCase(document.getStatus())
                    || "VALIDADO".equalsIgnoreCase(document.getStatus());

            if (!documentValid) {
                log.warn("Documento no validado. Id: {}, estado: {}", identityDocumentId, document.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no está validado");
            }

            log.info("Documento de identidad validado correctamente desde MS-Identity. Documento id: {}", identityDocumentId);

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Identity para documento id: {}. Detalle: {}", identityDocumentId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Identity no está disponible o el documento no existe");
        }
    }

    private String calculateRiskLevel(HealthDeclarationRequestDto request) {
        log.info("Calculando nivel de riesgo sanitario");

        if (Boolean.TRUE.equals(request.getHasSymptoms())) {
            log.info("Nivel de riesgo sanitario calculado: ALTO");
            return "ALTO";
        }

        if (Boolean.TRUE.equals(request.getHasRecentContact())) {
            log.info("Nivel de riesgo sanitario calculado: MEDIO");
            return "MEDIO";
        }

        log.info("Nivel de riesgo sanitario calculado: BAJO");
        return "BAJO";
    }

    private HealthDeclarationResponseDto toResponseDto(HealthDeclaration declaration) {
        return new HealthDeclarationResponseDto(
                declaration.getId(),
                declaration.getUserId(),
                declaration.getIdentityDocumentId(),
                declaration.getHasSymptoms(),
                declaration.getSymptomsDescription(),
                declaration.getHasRecentContact(),
                declaration.getVaccinationStatus(),
                declaration.getRiskLevel(),
                declaration.getStatus(),
                declaration.getObservations(),
                declaration.getCreatedAt()
        );
    }
}