package com.duoc.sgf.ms_visa.service.impl;

import com.duoc.sgf.ms_visa.client.IdentityClient;
import com.duoc.sgf.ms_visa.client.UserClient;
import com.duoc.sgf.ms_visa.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_visa.model.dto.UserBasicDto;
import com.duoc.sgf.ms_visa.model.dto.VisaRequestDto;
import com.duoc.sgf.ms_visa.model.dto.VisaResponseDto;
import com.duoc.sgf.ms_visa.model.VisaRequest;
import com.duoc.sgf.ms_visa.repository.VisaRequestRepository;
import com.duoc.sgf.ms_visa.service.VisaRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisaRequestServiceImpl implements VisaRequestService {

    private final VisaRequestRepository visaRequestRepository;
    private final UserClient userClient;
    private final IdentityClient identityClient;

    @Override
    public List<VisaResponseDto> findAll() {
        log.info("Listando solicitudes de visa");

        return visaRequestRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public VisaResponseDto findById(Long id) {
        log.info("Buscando solicitud de visa por id: {}", id);

        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });

        return toResponseDto(visaRequest);
    }

    @Override
    public List<VisaResponseDto> findByUserId(Long userId) {
        log.info("Buscando solicitudes de visa por usuario id: {}", userId);

        return visaRequestRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<VisaResponseDto> findByStatus(String status) {
        log.info("Buscando solicitudes de visa por estado: {}", status);

        return visaRequestRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public VisaResponseDto create(VisaRequestDto request) {
        log.info("Creando solicitud de visa para usuario id: {}", request.getUserId());

        validateUser(request.getUserId());
        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        validateDates(request);

        VisaRequest visaRequest = new VisaRequest();
        visaRequest.setUserId(request.getUserId());
        visaRequest.setIdentityDocumentId(request.getIdentityDocumentId());
        visaRequest.setVisaType(request.getVisaType().toUpperCase());
        visaRequest.setDestinationCountry(request.getDestinationCountry().toUpperCase());
        visaRequest.setTravelPurpose(request.getTravelPurpose());
        visaRequest.setStartDate(request.getStartDate());
        visaRequest.setEndDate(request.getEndDate());
        visaRequest.setObservations(request.getObservations());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            visaRequest.setStatus("PENDIENTE");
        } else {
            visaRequest.setStatus(request.getStatus().toUpperCase());
        }

        VisaRequest savedVisaRequest = visaRequestRepository.save(visaRequest);

        log.info("Solicitud de visa creada correctamente con id: {}", savedVisaRequest.getId());

        return toResponseDto(savedVisaRequest);
    }

    @Override
    public VisaResponseDto update(Long id, VisaRequestDto request) {
        log.info("Actualizando solicitud de visa con id: {}", id);

        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });

        validateUser(request.getUserId());
        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        validateDates(request);

        visaRequest.setUserId(request.getUserId());
        visaRequest.setIdentityDocumentId(request.getIdentityDocumentId());
        visaRequest.setVisaType(request.getVisaType().toUpperCase());
        visaRequest.setDestinationCountry(request.getDestinationCountry().toUpperCase());
        visaRequest.setTravelPurpose(request.getTravelPurpose());
        visaRequest.setStartDate(request.getStartDate());
        visaRequest.setEndDate(request.getEndDate());
        visaRequest.setObservations(request.getObservations());

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            visaRequest.setStatus("PENDIENTE");
        } else {
            visaRequest.setStatus(request.getStatus().toUpperCase());
        }

        VisaRequest updatedVisaRequest = visaRequestRepository.save(visaRequest);

        log.info("Solicitud de visa actualizada correctamente con id: {}", updatedVisaRequest.getId());

        return toResponseDto(updatedVisaRequest);
    }

    @Override
    public VisaResponseDto approve(Long id) {
        log.info("Aprobando solicitud de visa con id: {}", id);

        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo aprobar. Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });

        validateUser(visaRequest.getUserId());
        validateIdentityDocument(visaRequest.getIdentityDocumentId(), visaRequest.getUserId());

        visaRequest.setStatus("APROBADA");
        visaRequest.setObservations("Solicitud aprobada correctamente");

        VisaRequest approvedVisaRequest = visaRequestRepository.save(visaRequest);

        log.info("Solicitud de visa aprobada correctamente con id: {}", id);

        return toResponseDto(approvedVisaRequest);
    }

    @Override
    public VisaResponseDto reject(Long id) {
        log.info("Rechazando solicitud de visa con id: {}", id);

        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo rechazar. Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });

        visaRequest.setStatus("RECHAZADA");
        visaRequest.setObservations("Solicitud rechazada por revisión administrativa");

        VisaRequest rejectedVisaRequest = visaRequestRepository.save(visaRequest);

        log.info("Solicitud de visa rechazada correctamente con id: {}", id);

        return toResponseDto(rejectedVisaRequest);
    }

    @Override
    public void delete(Long id) {
        log.info("Eliminando solicitud de visa con id: {}", id);

        if (!visaRequestRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Solicitud de visa no encontrada con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
        }

        visaRequestRepository.deleteById(id);

        log.info("Solicitud de visa eliminada correctamente con id: {}", id);
    }

    private void validateUser(Long userId) {
        try {
            UserBasicDto user = userClient.findById(userId);

            if (user == null) {
                log.warn("Usuario remoto no encontrado con id: {}", userId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            }

            if (!"ACTIVO".equalsIgnoreCase(user.getStatus())) {
                log.warn("Usuario no activo. Id: {}, estado: {}", userId, user.getStatus());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está activo");
            }

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Users para usuario id: {}", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Users no está disponible o el usuario no existe");
        }
    }

    private void validateIdentityDocument(Long identityDocumentId, Long userId) {
        try {
            IdentityDocumentBasicDto document = identityClient.findById(identityDocumentId);

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

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al comunicarse con MS-Identity para documento id: {}", identityDocumentId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MS-Identity no está disponible o el documento no existe");
        }
    }

    private void validateDates(VisaRequestDto request) {
        if (request.getEndDate().isBefore(request.getStartDate())
                || request.getEndDate().isEqual(request.getStartDate())) {

            log.warn("Fechas inválidas. Inicio: {}, término: {}", request.getStartDate(), request.getEndDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de término debe ser posterior a la fecha de inicio");
        }
    }

    private VisaResponseDto toResponseDto(VisaRequest visaRequest) {
        return new VisaResponseDto(
                visaRequest.getId(),
                visaRequest.getUserId(),
                visaRequest.getIdentityDocumentId(),
                visaRequest.getVisaType(),
                visaRequest.getDestinationCountry(),
                visaRequest.getTravelPurpose(),
                visaRequest.getStartDate(),
                visaRequest.getEndDate(),
                visaRequest.getStatus(),
                visaRequest.getObservations(),
                visaRequest.getCreatedAt()
        );
    }
}