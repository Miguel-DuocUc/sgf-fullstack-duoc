package com.duoc.sgf.ms_bordercontrol.service.impl;

import com.duoc.sgf.ms_bordercontrol.client.HealthClient;
import com.duoc.sgf.ms_bordercontrol.client.IdentityClient;
import com.duoc.sgf.ms_bordercontrol.client.UserClient;
import com.duoc.sgf.ms_bordercontrol.client.VisaClient;
import com.duoc.sgf.ms_bordercontrol.dto.BorderControlRequestDto;
import com.duoc.sgf.ms_bordercontrol.dto.BorderControlResponseDto;
import com.duoc.sgf.ms_bordercontrol.dto.HealthDeclarationBasicDto;
import com.duoc.sgf.ms_bordercontrol.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_bordercontrol.dto.UserBasicDto;
import com.duoc.sgf.ms_bordercontrol.dto.VisaRequestBasicDto;
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
    private final BorderControlEventProducer borderControlEventProducer;

    @Override
    public List<BorderControlResponseDto> findAll() {
        log.info("Listando controles fronterizos");

        return borderControlRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public BorderControlResponseDto findById(Long id) {
        log.info("Buscando control fronterizo por id: {}", id);

        BorderControl borderControl = borderControlRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Control fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Control fronterizo no encontrado");
                });

        return toResponseDto(borderControl);
    }

    @Override
    public List<BorderControlResponseDto> findByUserId(Long userId) {
        log.info("Buscando controles fronterizos por usuario id: {}", userId);

        return borderControlRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<BorderControlResponseDto> findByStatus(String status) {
        log.info("Buscando controles fronterizos por estado: {}", status);

        return borderControlRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public BorderControlResponseDto create(BorderControlRequestDto request) {
        log.info("Creando control fronterizo para usuario id: {}", request.getUserId());

        validateAllRequirements(request);

        BorderControl borderControl = new BorderControl();
        borderControl.setUserId(request.getUserId());
        borderControl.setIdentityDocumentId(request.getIdentityDocumentId());
        borderControl.setVisaRequestId(request.getVisaRequestId());
        borderControl.setHealthDeclarationId(request.getHealthDeclarationId());
        borderControl.setCheckpoint(request.getCheckpoint().toUpperCase());
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

        BorderControl savedBorderControl = borderControlRepository.save(borderControl);

        borderControlEventProducer.publishBorderControlEvent(savedBorderControl);

        log.info("Control fronterizo creado correctamente con id: {}", savedBorderControl.getId());

        return toResponseDto(savedBorderControl);
    }

    @Override
    public BorderControlResponseDto update(Long id, BorderControlRequestDto request) {
        log.info("Actualizando control fronterizo con id: {}", id);

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
        borderControl.setCheckpoint(request.getCheckpoint().toUpperCase());
        borderControl.setOfficerName(request.getOfficerName());
        borderControl.setMovementType(request.getMovementType().toUpperCase());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            borderControl.setStatus("AUTORIZADO");
        } else {
            borderControl.setStatus(request.getStatus().toUpperCase());
        }

        borderControl.setObservations(request.getObservations());

        BorderControl updatedBorderControl = borderControlRepository.save(borderControl);

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
        request.setCheckpoint(borderControl.getCheckpoint());
        request.setOfficerName(borderControl.getOfficerName());
        request.setMovementType(borderControl.getMovementType());

        validateAllRequirements(request);

        borderControl.setStatus("AUTORIZADO");
        borderControl.setObservations("Evaluación aprobada. El viajero cumple con todos los requisitos.");

        BorderControl evaluatedBorderControl = borderControlRepository.save(borderControl);

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
        UserBasicDto user = validateUser(request.getUserId());
        IdentityDocumentBasicDto document = validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        VisaRequestBasicDto visa = validateVisa(request.getVisaRequestId(), request.getUserId(), request.getIdentityDocumentId());
        HealthDeclarationBasicDto health = validateHealth(request.getHealthDeclarationId(), request.getUserId(), request.getIdentityDocumentId());

        log.info("Validaciones completadas correctamente. Usuario: {}, Documento: {}, Visa: {}, Salud: {}",
                user.getId(), document.getId(), visa.getId(), health.getId());
    }

    private UserBasicDto validateUser(Long userId) {
        try {
            UserBasicDto user = userClient.findById(userId);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            }

            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está activo");
            }

            return user;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Users para usuario id: {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Users no está disponible o el usuario no existe");
        }
    }

    private IdentityDocumentBasicDto validateIdentityDocument(Long identityDocumentId, Long userId) {
        try {
            IdentityDocumentBasicDto document = identityClient.findById(identityDocumentId);

            if (document == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no existe");
            }

            if (!document.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento no pertenece al usuario indicado");
            }

            boolean validDocument = "VALIDO".equalsIgnoreCase(document.getStatus())
                    || "VALIDADO".equalsIgnoreCase(document.getStatus());

            if (!validDocument) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento de identidad no está validado");
            }

            return document;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Identity para documento id: {}", identityDocumentId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Identity no está disponible o el documento no existe");
        }
    }

    private VisaRequestBasicDto validateVisa(Long visaRequestId, Long userId, Long identityDocumentId) {
        try {
            VisaRequestBasicDto visa = visaClient.findById(visaRequestId);

            if (visa == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La solicitud de visa no existe");
            }

            if (!visa.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no pertenece al usuario indicado");
            }

            if (!visa.getIdentityDocumentId().equals(identityDocumentId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no corresponde al documento indicado");
            }

            if (!"APROBADA".equalsIgnoreCase(visa.getStatus())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La visa no está aprobada");
            }

            return visa;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Visa para visa id: {}", visaRequestId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Visa no está disponible o la visa no existe");
        }
    }

    private HealthDeclarationBasicDto validateHealth(Long healthDeclarationId, Long userId, Long identityDocumentId) {
        try {
            HealthDeclarationBasicDto health = healthClient.findById(healthDeclarationId);

            if (health == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no existe");
            }

            if (!health.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no pertenece al usuario indicado");
            }

            if (!health.getIdentityDocumentId().equals(identityDocumentId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no corresponde al documento indicado");
            }

            if (!"APTO".equalsIgnoreCase(health.getStatus())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La declaración sanitaria no está apta");
            }

            return health;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Health para declaración id: {}", healthDeclarationId);
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
                borderControl.getCheckpoint(),
                borderControl.getOfficerName(),
                borderControl.getMovementType(),
                borderControl.getStatus(),
                borderControl.getObservations(),
                borderControl.getCreatedAt()
        );
    }
}