package com.duoc.sgf.ms_bordercontrol.service.impl;

import com.duoc.sgf.ms_bordercontrol.client.HealthClient;
import com.duoc.sgf.ms_bordercontrol.client.IdentityClient;
import com.duoc.sgf.ms_bordercontrol.client.LogisticsClient;
import com.duoc.sgf.ms_bordercontrol.client.UserClient;
import com.duoc.sgf.ms_bordercontrol.client.VisaClient;
import com.duoc.sgf.ms_bordercontrol.model.dto.BorderControlRequestDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.BorderControlResponseDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.HealthDeclarationBasicDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.UserBasicDto;
import com.duoc.sgf.ms_bordercontrol.model.dto.VisaRequestBasicDto;
import com.duoc.sgf.ms_bordercontrol.event.BorderControlEventProducer;
import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
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
                .map(this::toResponseDto)
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

        return toResponseDto(borderControl);
    }

    @Override
    public List<BorderControlResponseDto> findByUserId(Long userId) {
        log.info("Buscando controles fronterizos por usuario id: {}", userId);

        List<BorderControlResponseDto> controls = borderControlRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Consulta completada para usuario id: {}. Controles encontrados: {}", userId, controls.size());

        return controls;
    }

    @Override
    public List<BorderControlResponseDto> findByStatus(String status) {
        log.info("Buscando controles fronterizos por estado: {}", status);

        List<BorderControlResponseDto> controls = borderControlRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();

        log.info("Consulta completada para estado: {}. Controles encontrados: {}", status, controls.size());

        return controls;
    }

    @Override
    public BorderControlResponseDto create(BorderControlRequestDto request) {
        log.info("Iniciando creación de control fronterizo para usuario id: {}", request.getUserId());
        log.info("Iniciando validación de requisitos para crear control fronterizo");

        validateAllRequirements(request);

        BorderControl borderControl = new BorderControl();
        borderControl.setUserId(request.getUserId());
        borderControl.setIdentityDocumentId(request.getIdentityDocumentId());
        borderControl.setVisaRequestId(request.getVisaRequestId());
        borderControl.setHealthDeclarationId(request.getHealthDeclarationId());
        borderControl.setLogisticsCheckpointId(request.getLogisticsCheckpointId());
        borderControl.setOfficerName(request.getOfficerName());
        borderControl.setMovementType(request.getMovementType().toUpperCase());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            borderControl.setStatus("AUTORIZADO");
        } else {
            borderControl.setStatus(request.getStatus().toUpperCase());
        }

        if (request.getObservations() == null || request.getObservations().isBlank()) {
            borderControl.setObservations("Control fronterizo autorizado correctamente");
        } else {
            borderControl.setObservations(request.getObservations());
        }

        log.info("Guardando control fronterizo para usuario id: {}", request.getUserId());

        BorderControl savedBorderControl = borderControlRepository.save(borderControl);

        log.info("Enviando evento Kafka para control fronterizo id: {}", savedBorderControl.getId());

        borderControlEventProducer.publishBorderControlEvent(savedBorderControl);

        log.info("Control fronterizo creado correctamente con id: {}", savedBorderControl.getId());

        return toResponseDto(savedBorderControl);
    }

    @Override
    public BorderControlResponseDto update(Long id, BorderControlRequestDto request) {
        log.info("Iniciando actualización de control fronterizo con id: {}", id);

        BorderControl borderControl = borderControlRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Control fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Control fronterizo no encontrado");
                });

        log.info("Validando requisitos para actualización de control fronterizo id: {}", id);

        validateAllRequirements(request);

        borderControl.setUserId(request.getUserId());
        borderControl.setIdentityDocumentId(request.getIdentityDocumentId());
        borderControl.setVisaRequestId(request.getVisaRequestId());
        borderControl.setHealthDeclarationId(request.getHealthDeclarationId());
        borderControl.setLogisticsCheckpointId(request.getLogisticsCheckpointId());
        borderControl.setOfficerName(request.getOfficerName());
        borderControl.setMovementType(request.getMovementType().toUpperCase());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            borderControl.setStatus("AUTORIZADO");
        } else {
            borderControl.setStatus(request.getStatus().toUpperCase());
        }

        borderControl.setObservations(request.getObservations());

        log.info("Guardando actualización de control fronterizo con id: {}", id);

        BorderControl updatedBorderControl = borderControlRepository.save(borderControl);

        log.info("Enviando evento Kafka por actualización de control fronterizo id: {}", updatedBorderControl.getId());

        borderControlEventProducer.publishBorderControlEvent(updatedBorderControl);

        log.info("Control fronterizo actualizado correctamente con id: {}", updatedBorderControl.getId());

        return toResponseDto(updatedBorderControl);
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

        log.info("Ejecutando validaciones para evaluación de control fronterizo id: {}", id);

        validateAllRequirements(request);

        borderControl.setStatus("AUTORIZADO");
        borderControl.setObservations("Evaluación aprobada. El viajero cumple con todos los requisitos.");

        BorderControl evaluatedBorderControl = borderControlRepository.save(borderControl);

        log.info("Enviando evento Kafka por evaluación de control fronterizo id: {}", evaluatedBorderControl.getId());

        borderControlEventProducer.publishBorderControlEvent(evaluatedBorderControl);

        log.info("Control fronterizo evaluado correctamente con id: {}", id);

        return toResponseDto(evaluatedBorderControl);
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

        log.info(
                "Validaciones completadas correctamente. Usuario: {}, Documento: {}, Visa: {}, Salud: {}, Checkpoint ID: {}",
                user.getId(),
                document.getId(),
                visa.getId(),
                health.getId(),
                request.getLogisticsCheckpointId()
        );
    }

    private void validateCheckpoint(Long checkpointId) {
        try {
            log.info("Consultando paso fronterizo en MS-Logistics con id: {}", checkpointId);

            Object checkpointInfo = logisticsClient.obtenerPasoFronterizoPorId(checkpointId);

            if (checkpointInfo == null) {
                log.warn("Paso fronterizo no encontrado en MS-Logistics con id: {}", checkpointId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paso fronterizo indicado no existe");
            }

            log.info("Paso fronterizo validado correctamente en MS-Logistics con id: {}", checkpointId);

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Logistics para checkpoint id: {}. Detalle: {}", checkpointId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Logistics no está disponible o el paso fronterizo (ID: " + checkpointId + ") no existe.");
        }
    }

    private UserBasicDto validateUser(Long userId) {
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

            return user;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Users para usuario id: {}. Detalle: {}", userId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Users no está disponible o el usuario no existe");
        }
    }

    private IdentityDocumentBasicDto validateIdentityDocument(Long identityDocumentId, Long userId) {
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

            boolean validDocument = "VALIDO".equalsIgnoreCase(document.getStatus())
                    || "VALIDADO".equalsIgnoreCase(document.getStatus());

            if (!validDocument) {
                log.warn("Documento no validado. Id: {}, estado: {}", identityDocumentId, document.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no está validado");
            }

            log.info("Documento validado correctamente desde MS-Identity con id: {}", identityDocumentId);

            return document;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Identity para documento id: {}. Detalle: {}", identityDocumentId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Identity no está disponible o el documento no existe");
        }
    }

    private VisaRequestBasicDto validateVisa(Long visaRequestId, Long userId, Long identityDocumentId) {
        try {
            log.info("Consultando visa en MS-Visa con id: {}", visaRequestId);

            VisaRequestBasicDto visa = visaClient.findById(visaRequestId);

            log.info("Respuesta recibida desde MS-Visa para visa id: {}", visaRequestId);

            if (visa == null) {
                log.warn("Solicitud de visa remota no encontrada con id: {}", visaRequestId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud de visa no existe");
            }

            if (!visa.getUserId().equals(userId)) {
                log.warn("Visa id {} no pertenece al usuario id {}", visaRequestId, userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no pertenece al usuario indicado");
            }

            if (!visa.getIdentityDocumentId().equals(identityDocumentId)) {
                log.warn("Visa id {} no corresponde al documento id {}", visaRequestId, identityDocumentId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no corresponde al documento indicado");
            }

            if (!"APROBADA".equalsIgnoreCase(visa.getStatus())) {
                log.warn("Visa no aprobada. Id: {}, estado: {}", visaRequestId, visa.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no está aprobada");
            }

            log.info("Visa validada correctamente desde MS-Visa con id: {}", visaRequestId);

            return visa;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Visa para visa id: {}. Detalle: {}", visaRequestId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Visa no está disponible o la visa no existe");
        }
    }

    private HealthDeclarationBasicDto validateHealth(Long healthDeclarationId, Long userId, Long identityDocumentId) {
        try {
            log.info("Consultando declaración sanitaria en MS-Health con id: {}", healthDeclarationId);

            HealthDeclarationBasicDto health = healthClient.findById(healthDeclarationId);

            log.info("Respuesta recibida desde MS-Health para declaración id: {}", healthDeclarationId);

            if (health == null) {
                log.warn("Declaración sanitaria remota no encontrada con id: {}", healthDeclarationId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no existe");
            }

            if (!health.getUserId().equals(userId)) {
                log.warn("Declaración sanitaria id {} no pertenece al usuario id {}", healthDeclarationId, userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no pertenece al usuario indicado");
            }

            if (!health.getIdentityDocumentId().equals(identityDocumentId)) {
                log.warn("Declaración sanitaria id {} no corresponde al documento id {}", healthDeclarationId, identityDocumentId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no corresponde al documento indicado");
            }

            if (!"APTO".equalsIgnoreCase(health.getStatus())) {
                log.warn("Declaración sanitaria no apta. Id: {}, estado: {}", healthDeclarationId, health.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no está apta");
            }

            log.info("Declaración sanitaria validada correctamente desde MS-Health con id: {}", healthDeclarationId);

            return health;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Health para declaración id: {}. Detalle: {}", healthDeclarationId, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Health no está disponible o la declaración no existe");
        }
    }

    private BorderControlResponseDto toResponseDto(BorderControl borderControl) {
        return new BorderControlResponseDto(
                borderControl.getId(),
                borderControl.getUserId(),
                borderControl.getIdentityDocumentId(),
                borderControl.getVisaRequestId(),
                borderControl.getHealthDeclarationId(),
                borderControl.getLogisticsCheckpointId(),
                borderControl.getOfficerName(),
                borderControl.getMovementType(),
                borderControl.getStatus(),
                borderControl.getObservations(),
                borderControl.getCreatedAt()
        );
    }
}

