package com.duoc.sgf.ms_bordercontrol.service.impl;

import com.duoc.sgf.ms_bordercontrol.client.HealthClient;
import com.duoc.sgf.ms_bordercontrol.client.IdentityClient;
import com.duoc.sgf.ms_bordercontrol.client.LogisticsClient;
import com.duoc.sgf.ms_bordercontrol.client.UserClient;
import com.duoc.sgf.ms_bordercontrol.client.VisaClient;
import com.duoc.sgf.ms_bordercontrol.event.BorderControlEventProducer;
import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
import com.duoc.sgf.ms_bordercontrol.model.dto.*;
import com.duoc.sgf.ms_bordercontrol.model.mapper.BorderControlMapper;
import com.duoc.sgf.ms_bordercontrol.repository.BorderControlRepository;
import com.duoc.sgf.ms_bordercontrol.service.BorderControlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorderControlServiceImpl implements BorderControlService {

    private final BorderControlRepository borderControlRepository;
    private final BorderControlMapper mapper;
    private final UserClient userClient;
    private final IdentityClient identityClient;
    private final VisaClient visaClient;
    private final HealthClient healthClient;
    private final LogisticsClient logisticsClient;
    private final BorderControlEventProducer borderControlEventProducer;

    @Override
    public List<BorderControlResponseDto> findAll() {
        log.info("Listando todos los controles fronterizos registrados");
        List<BorderControlResponseDto> controls = borderControlRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        log.info("Total de controles fronterizos encontrados: {}", controls.size());
        return controls;
    }

    @Override
    public BorderControlResponseDto findById(Long id) {
        log.info("Buscando control fronterizo por id: {}", id);
        BorderControl borderControl = borderControlRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Control fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Control fronterizo no encontrado");
                });
        log.info("Control fronterizo encontrado correctamente con id: {}", id);
        return mapper.toDto(borderControl);
    }

    @Override
    public List<BorderControlResponseDto> findByUserId(Long userId) {
        log.info("Buscando controles fronterizos por usuario id: {}", userId);
        List<BorderControlResponseDto> controls = borderControlRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
        log.info("Consulta completada para usuario id: {}. Controles encontrados: {}", userId, controls.size());
        return controls;
    }

    @Override
    public List<BorderControlResponseDto> findByStatus(String status) {
        log.info("Buscando controles fronterizos por estado: {}", status);
        List<BorderControlResponseDto> controls = borderControlRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(mapper::toDto)
                .toList();
        log.info("Consulta completada para estado: {}. Controles encontrados: {}", status, controls.size());
        return controls;
    }

    @Override
    public BorderControlResponseDto create(BorderControlRequestDto request) {
        log.info("Iniciando creación de control fronterizo para usuario id: {}", request.getUserId());
        validateAllRequirements(request);

        BorderControl borderControl = mapper.toEntity(request);

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            borderControl.setStatus("AUTORIZADO");
        } else {
            borderControl.setStatus(request.getStatus().toUpperCase());
        }

        if (request.getObservations() == null || request.getObservations().isBlank()) {
            borderControl.setObservations("Control fronterizo autorizado correctamente");
        }

        BorderControl saved = borderControlRepository.save(borderControl);
        log.info("Enviando evento Kafka para control fronterizo id: {}", saved.getId());
        borderControlEventProducer.publishBorderControlEvent(saved);
        log.info("Control fronterizo creado correctamente con id: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    public BorderControlResponseDto update(Long id, BorderControlRequestDto request) {
        log.info("Iniciando actualización de control fronterizo con id: {}", id);
        BorderControl borderControl = borderControlRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Control fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Control fronterizo no encontrado");
                });

        validateAllRequirements(request);

        borderControl.setUserId(request.getUserId());
        borderControl.setIdentityDocumentId(request.getIdentityDocumentId());
        borderControl.setVisaRequestId(request.getVisaRequestId());
        borderControl.setHealthDeclarationId(request.getHealthDeclarationId());
        borderControl.setLogisticsCheckpointId(request.getLogisticsCheckpointId());
        borderControl.setOfficerName(request.getOfficerName());
        borderControl.setMovementType(request.getMovementType().toUpperCase());
        borderControl.setStatus(request.getStatus() == null || request.getStatus().isBlank()
                ? "AUTORIZADO" : request.getStatus().toUpperCase());
        borderControl.setObservations(request.getObservations());

        BorderControl updated = borderControlRepository.save(borderControl);
        log.info("Enviando evento Kafka por actualización de control fronterizo id: {}", updated.getId());
        borderControlEventProducer.publishBorderControlEvent(updated);
        log.info("Control fronterizo actualizado correctamente con id: {}", id);
        return mapper.toDto(updated);
    }

    @Override
    public BorderControlResponseDto evaluate(Long id) {
        log.info("Evaluando control fronterizo con id: {}", id);
        BorderControl borderControl = borderControlRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo evaluar. Control fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Control fronterizo no encontrado");
                });

        BorderControlRequestDto request = new BorderControlRequestDto();
        request.setUserId(borderControl.getUserId());
        request.setIdentityDocumentId(borderControl.getIdentityDocumentId());
        request.setVisaRequestId(borderControl.getVisaRequestId());
        request.setHealthDeclarationId(borderControl.getHealthDeclarationId());
        request.setLogisticsCheckpointId(borderControl.getLogisticsCheckpointId());
        request.setOfficerName(borderControl.getOfficerName());
        request.setMovementType(borderControl.getMovementType());

        validateAllRequirements(request);

        borderControl.setStatus("AUTORIZADO");
        borderControl.setObservations("Evaluación aprobada. El viajero cumple con todos los requisitos.");

        BorderControl evaluated = borderControlRepository.save(borderControl);
        log.info("Enviando evento Kafka por evaluación de control fronterizo id: {}", evaluated.getId());
        borderControlEventProducer.publishBorderControlEvent(evaluated);
        log.info("Control fronterizo evaluado correctamente con id: {}", id);
        return mapper.toDto(evaluated);
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando control fronterizo con id: {}", id);
        if (!borderControlRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Control fronterizo no encontrado con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Control fronterizo no encontrado");
        }
        borderControlRepository.deleteById(id);
        log.info("Control fronterizo eliminado correctamente con id: {}", id);
    }

    private void validateAllRequirements(BorderControlRequestDto request) {
        log.info("Validando requisitos completos para usuario id: {}", request.getUserId());
        UserBasicDto user = validateUser(request.getUserId());
        IdentityDocumentBasicDto document = validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        VisaRequestBasicDto visa = validateVisa(request.getVisaRequestId(), request.getUserId(), request.getIdentityDocumentId());
        HealthDeclarationBasicDto health = validateHealth(request.getHealthDeclarationId(), request.getUserId(), request.getIdentityDocumentId());
        validateCheckpoint(request.getLogisticsCheckpointId());
        log.info("Validaciones completadas. Usuario: {}, Documento: {}, Visa: {}, Salud: {}, Checkpoint ID: {}",
                user.getId(), document.getId(), visa.getId(), health.getId(), request.getLogisticsCheckpointId());
    }

    private void validateCheckpoint(Long checkpointId) {
        try {
            log.info("Consultando paso fronterizo en MS-Logistics con id: {}", checkpointId);
            Object checkpoint = logisticsClient.obtenerPasoFronterizoPorId(checkpointId);
            if (checkpoint == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paso fronterizo indicado no existe");
            log.info("Paso fronterizo validado correctamente con id: {}", checkpointId);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Logistics para checkpoint id: {}. Detalle: {}", checkpointId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Logistics no disponible o checkpoint (ID: " + checkpointId + ") no existe.");
        }
    }

    private UserBasicDto validateUser(Long userId) {
        try {
            log.info("Consultando usuario en MS-Users con id: {}", userId);
            UserBasicDto user = userClient.findById(userId);
            if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está activo");
            log.info("Usuario validado correctamente con id: {}", userId);
            return user;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Users para usuario id: {}. Detalle: {}", userId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Users no disponible o el usuario no existe");
        }
    }

    private IdentityDocumentBasicDto validateIdentityDocument(Long identityDocumentId, Long userId) {
        try {
            log.info("Consultando documento de identidad en MS-Identity con id: {}", identityDocumentId);
            IdentityDocumentBasicDto document = identityClient.findById(identityDocumentId);
            if (document == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no existe");
            if (!document.getUserId().equals(userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento no pertenece al usuario indicado");
            boolean valid = "VALIDO".equalsIgnoreCase(document.getStatus()) || "VALIDADO".equalsIgnoreCase(document.getStatus());
            if (!valid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no está validado");
            log.info("Documento validado correctamente con id: {}", identityDocumentId);
            return document;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Identity para documento id: {}. Detalle: {}", identityDocumentId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Identity no disponible o el documento no existe");
        }
    }

    private VisaRequestBasicDto validateVisa(Long visaRequestId, Long userId, Long identityDocumentId) {
        try {
            log.info("Consultando visa en MS-Visa con id: {}", visaRequestId);
            VisaRequestBasicDto visa = visaClient.findById(visaRequestId);
            if (visa == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud de visa no existe");
            if (!visa.getUserId().equals(userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no pertenece al usuario indicado");
            if (!visa.getIdentityDocumentId().equals(identityDocumentId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no corresponde al documento indicado");
            if (!"APROBADA".equalsIgnoreCase(visa.getStatus())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no está aprobada");
            log.info("Visa validada correctamente con id: {}", visaRequestId);
            return visa;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Visa para visa id: {}. Detalle: {}", visaRequestId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Visa no disponible o la visa no existe");
        }
    }

    private HealthDeclarationBasicDto validateHealth(Long healthDeclarationId, Long userId, Long identityDocumentId) {
        try {
            log.info("Consultando declaración sanitaria en MS-Health con id: {}", healthDeclarationId);
            HealthDeclarationBasicDto health = healthClient.findById(healthDeclarationId);
            if (health == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no existe");
            if (!health.getUserId().equals(userId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no pertenece al usuario indicado");
            if (!health.getIdentityDocumentId().equals(identityDocumentId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no corresponde al documento indicado");
            if (!"APTO".equalsIgnoreCase(health.getStatus())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no está apta");
            log.info("Declaración sanitaria validada correctamente con id: {}", healthDeclarationId);
            return health;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Health para declaración id: {}. Detalle: {}", healthDeclarationId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Health no disponible o la declaración no existe");
        }
    }
}