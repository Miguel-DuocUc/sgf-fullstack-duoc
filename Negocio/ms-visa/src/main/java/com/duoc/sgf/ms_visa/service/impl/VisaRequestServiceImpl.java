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
import com.duoc.sgf.ms_visa.model.mapper.VisaRequestMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisaRequestServiceImpl implements VisaRequestService {

    private final VisaRequestRepository visaRequestRepository;
    private final VisaRequestMapper mapper;  
    private final UserClient userClient;
    private final IdentityClient identityClient;

    @Override
    public List<VisaResponseDto> findAll() {
        log.info("Listando todas las solicitudes de visa registradas");
        List<VisaResponseDto> visas = visaRequestRepository.findAll()
                .stream()
                .map(mapper::toDto)  // ← cambiado
                .toList();
        log.info("Total de solicitudes de visa encontradas: {}", visas.size());
        return visas;
    }

    @Override
    public VisaResponseDto findById(Long id) {
        log.info("Buscando solicitud de visa por id: {}", id);
        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });
        log.info("Solicitud de visa encontrada correctamente con id: {}", id);
        return mapper.toDto(visaRequest);  // ← cambiado
    }

    @Override
    public List<VisaResponseDto> findByUserId(Long userId) {
        log.info("Buscando solicitudes de visa asociadas al usuario id: {}", userId);
        List<VisaResponseDto> visas = visaRequestRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)  // ← cambiado
                .toList();
        log.info("Consulta completada para usuario id: {}. Solicitudes encontradas: {}", userId, visas.size());
        return visas;
    }

    @Override
    public List<VisaResponseDto> findByStatus(String status) {
        log.info("Buscando solicitudes de visa por estado: {}", status);
        List<VisaResponseDto> visas = visaRequestRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(mapper::toDto)  // ← cambiado
                .toList();
        log.info("Consulta completada para estado: {}. Solicitudes encontradas: {}", status, visas.size());
        return visas;
    }

    @Override
    public VisaResponseDto create(VisaRequestDto request) {
        log.info("Iniciando creación de solicitud de visa para usuario id: {}", request.getUserId());
        validateUser(request.getUserId());
        log.info("Usuario validado correctamente. Usuario id: {}", request.getUserId());
        validateIdentityDocument(request.getIdentityDocumentId(), request.getUserId());
        log.info("Documento validado correctamente. Documento id: {}", request.getIdentityDocumentId());
        validateDates(request);
        log.info("Fechas validadas. Inicio: {}, término: {}", request.getStartDate(), request.getEndDate());

        VisaRequest visaRequest = mapper.toEntity(request);  // ← cambiado
        visaRequest.setVisaType(request.getVisaType().toUpperCase());
        visaRequest.setDestinationCountry(request.getDestinationCountry().toUpperCase());
        visaRequest.setStatus(request.getStatus() == null || request.getStatus().isBlank()
                ? "PENDIENTE" : request.getStatus().toUpperCase());

        VisaRequest saved = visaRequestRepository.save(visaRequest);
        log.info("Solicitud de visa creada correctamente con id: {}", saved.getId());
        return mapper.toDto(saved);  // ← cambiado
    }

    @Override
    public VisaResponseDto update(Long id, VisaRequestDto request) {
        log.info("Iniciando actualización de solicitud de visa con id: {}", id);
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
        visaRequest.setStatus(request.getStatus() == null || request.getStatus().isBlank()
                ? "PENDIENTE" : request.getStatus().toUpperCase());

        VisaRequest updated = visaRequestRepository.save(visaRequest);
        log.info("Solicitud de visa actualizada correctamente con id: {}", updated.getId());
        return mapper.toDto(updated);  // ← cambiado
    }

    @Override
    public VisaResponseDto approve(Long id) {
        log.info("Iniciando aprobación de solicitud de visa con id: {}", id);
        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo aprobar. Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });

        validateUser(visaRequest.getUserId());
        validateIdentityDocument(visaRequest.getIdentityDocumentId(), visaRequest.getUserId());

        visaRequest.setStatus("APROBADA");
        visaRequest.setObservations("Solicitud aprobada correctamente");

        VisaRequest approved = visaRequestRepository.save(visaRequest);
        log.info("Solicitud de visa aprobada correctamente con id: {}", id);
        return mapper.toDto(approved);  // ← cambiado
    }

    @Override
    public VisaResponseDto reject(Long id) {
        log.info("Iniciando rechazo de solicitud de visa con id: {}", id);
        VisaRequest visaRequest = visaRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo rechazar. Solicitud de visa no encontrada con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
                });

        visaRequest.setStatus("RECHAZADA");
        visaRequest.setObservations("Solicitud rechazada por revisión administrativa");

        VisaRequest rejected = visaRequestRepository.save(visaRequest);
        log.warn("Solicitud de visa rechazada correctamente con id: {}", id);
        return mapper.toDto(rejected);  // ← cambiado
    }

    @Override
    public void delete(Long id) {
        log.info("Iniciando eliminación de solicitud de visa con id: {}", id);
        if (!visaRequestRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Solicitud de visa no encontrada con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud de visa no encontrada");
        }
        visaRequestRepository.deleteById(id);
        log.info("Solicitud de visa eliminada correctamente con id: {}", id);
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

    private void validateDates(VisaRequestDto request) {
        log.info("Validando fechas. Inicio: {}, término: {}", request.getStartDate(), request.getEndDate());
        if (request.getEndDate().isBefore(request.getStartDate())
                || request.getEndDate().isEqual(request.getStartDate())) {
            log.warn("Fechas inválidas. Inicio: {}, término: {}", request.getStartDate(), request.getEndDate());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de término debe ser posterior a la fecha de inicio");
        }
        log.info("Fechas validadas correctamente");
    }
}