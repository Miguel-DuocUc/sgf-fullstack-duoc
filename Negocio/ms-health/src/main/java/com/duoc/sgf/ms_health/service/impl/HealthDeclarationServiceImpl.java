package com.duoc.sgf.ms_health.service.impl;

import com.duoc.sgf.ms_health.client.IdentityClient;
import com.duoc.sgf.ms_health.client.UserClient;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;
import com.duoc.sgf.ms_health.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_health.model.dto.UserBasicDto;
import com.duoc.sgf.ms_health.model.HealthDeclaration;
import com.duoc.sgf.ms_health.model.mapper.HealthDeclarationMapper;
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
    private final HealthDeclarationMapper mapper;
    private final UserClient userClient;
    private final IdentityClient identityClient;

    @Override
    public List<HealthDeclarationResponseDto> findAll() {
        log.info("Listando todas las declaraciones sanitarias registradas");
        List<HealthDeclarationResponseDto> declarations = healthDeclarationRepository.findAll()
                .stream()
                .map(mapper::toDto)  
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
        return mapper.toDto(declaration);  
    }

    @Override
    public List<HealthDeclarationResponseDto> findByUserId(Long userId) {
        log.info("Buscando declaraciones sanitarias asociadas al usuario id: {}", userId);
        List<HealthDeclarationResponseDto> declarations = healthDeclarationRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)  
                .toList();
        log.info("Consulta completada para usuario id: {}. Declaraciones encontradas: {}", userId, declarations.size());
        return declarations;
    }

    @Override
    public List<HealthDeclarationResponseDto> findByStatus(String status) {
        log.info("Buscando declaraciones sanitarias por estado: {}", status);
        List<HealthDeclarationResponseDto> declarations = healthDeclarationRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(mapper::toDto)  
                .toList();
        log.info("Consulta completada para estado: {}. Declaraciones encontradas: {}", status, declarations.size());
        return declarations;
    }

    @Override
    public HealthDeclarationResponseDto create(HealthDeclarationRequestDto request) {
        log.info("Iniciando creación de declaración sanitaria para usuario id: {}", request.getUserId());
        validateUser(request.getUserId());
        log.info("Usuario validado correctamente. Usuario id: {}", request.getUserId());
        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        log.info("Documento validado correctamente. Documento id: {}", request.getIdentityDocumentId());

        HealthDeclaration declaration = mapper.toEntity(request);  

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            declaration.setStatus("PENDIENTE");
        } else {
            declaration.setStatus(request.getStatus().toUpperCase());
        }

        declaration.setVaccinationStatus(request.getVaccinationStatus().toUpperCase());
        declaration.setRiskLevel(calculateRiskLevel(request));
        log.info("Nivel de riesgo calculado: {}", declaration.getRiskLevel());

        HealthDeclaration saved = healthDeclarationRepository.save(declaration);
        log.info("Declaración sanitaria creada correctamente con id: {}", saved.getId());
        return mapper.toDto(saved);  
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
        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());

        declaration.setUserId(request.getUserId());
        declaration.setIdentityDocumentId(request.getIdentityDocumentId());
        declaration.setHasSymptoms(request.getHasSymptoms());
        declaration.setSymptomsDescription(request.getSymptomsDescription());
        declaration.setHasRecentContact(request.getHasRecentContact());
        declaration.setVaccinationStatus(request.getVaccinationStatus().toUpperCase());
        declaration.setObservations(request.getObservations());
        declaration.setStatus(request.getStatus() == null || request.getStatus().isBlank()
                ? "PENDIENTE" : request.getStatus().toUpperCase());
        declaration.setRiskLevel(calculateRiskLevel(request));
        log.info("Nivel de riesgo recalculado para id {}: {}", id, declaration.getRiskLevel());

        HealthDeclaration updated = healthDeclarationRepository.save(declaration);
        log.info("Declaración sanitaria actualizada correctamente con id: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    public HealthDeclarationResponseDto evaluate(Long id) {
        log.info("Iniciando evaluación de declaración sanitaria con id: {}", id);
        HealthDeclaration declaration = healthDeclarationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo evaluar. Declaración sanitaria no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Declaración sanitaria no encontrada");
                });

        if (Boolean.TRUE.equals(declaration.getHasSymptoms())) {
            declaration.setStatus("NO_APTO");
            declaration.setRiskLevel("ALTO");
            declaration.setObservations("Declaración rechazada por presencia de síntomas.");
            log.warn("Declaración id {} marcada como NO_APTO por presencia de síntomas", id);
        } else if (Boolean.TRUE.equals(declaration.getHasRecentContact())) {
            declaration.setStatus("EN_REVISION");
            declaration.setRiskLevel("MEDIO");
            declaration.setObservations("Declaración requiere revisión por contacto reciente.");
            log.warn("Declaración id {} enviada a revisión por contacto reciente", id);
        } else {
            declaration.setStatus("APTO");
            declaration.setRiskLevel("BAJO");
            declaration.setObservations("Declaración sanitaria aprobada.");
            log.info("Declaración id {} marcada como APTO", id);
        }

        HealthDeclaration evaluated = healthDeclarationRepository.save(declaration);
        log.info("Declaración evaluada. Id: {}, estado: {}, riesgo: {}",
                id, evaluated.getStatus(), evaluated.getRiskLevel());
        return mapper.toDto(evaluated);  
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
            if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está activo");
            log.info("Usuario validado correctamente con id: {}", userId);
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
            if (document == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no existe");
            if (!document.getUserId().equals(userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento no pertenece al usuario indicado");
            boolean valid = "VALIDO".equalsIgnoreCase(document.getStatus()) || "VALIDADO".equalsIgnoreCase(document.getStatus());
            if (!valid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no está validado");
            log.info("Documento validado correctamente con id: {}", identityDocumentId);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Identity para documento id: {}. Detalle: {}", identityDocumentId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Identity no está disponible o el documento no existe");
        }
    }

    private String calculateRiskLevel(HealthDeclarationRequestDto request) {
        if (Boolean.TRUE.equals(request.getHasSymptoms())) return "ALTO";
        if (Boolean.TRUE.equals(request.getHasRecentContact())) return "MEDIO";
        return "BAJO";
    }
}