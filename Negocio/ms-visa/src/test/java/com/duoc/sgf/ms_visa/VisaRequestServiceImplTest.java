package com.duoc.sgf.ms_visa;

import com.duoc.sgf.ms_visa.client.IdentityClient;
import com.duoc.sgf.ms_visa.client.UserClient;
import com.duoc.sgf.ms_visa.model.VisaRequest;
import com.duoc.sgf.ms_visa.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_visa.model.dto.UserBasicDto;
import com.duoc.sgf.ms_visa.model.dto.VisaRequestDto;
import com.duoc.sgf.ms_visa.model.dto.VisaResponseDto;
import com.duoc.sgf.ms_visa.model.mapper.VisaRequestMapper;
import com.duoc.sgf.ms_visa.repository.VisaRequestRepository;
import com.duoc.sgf.ms_visa.service.impl.VisaRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisaRequestServiceImplTest {

    private VisaRequestRepository repository;
    private UserClient userClient;
    private IdentityClient identityClient;
    private VisaRequestMapper mapper;
    private VisaRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(VisaRequestRepository.class);
        userClient = mock(UserClient.class);
        identityClient = mock(IdentityClient.class);
        mapper = mock(VisaRequestMapper.class);

        service = new VisaRequestServiceImpl(
                repository,
                mapper,
                userClient,
                identityClient
        );
    }

    @Test
    void debeCrearVisaCorrectamente() {
        VisaRequestDto request = crearRequestValido();
        prepararValidacionesCorrectas();

        VisaRequest visaToSave = crearVisaSinId();
        VisaRequest savedVisa = crearVisa();
        VisaResponseDto responseDto = crearResponseDto();

        when(mapper.toEntity(request)).thenReturn(visaToSave);
        when(repository.save(any(VisaRequest.class))).thenReturn(savedVisa);
        when(mapper.toDto(savedVisa)).thenReturn(responseDto);

        VisaResponseDto response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(2L, response.getIdentityDocumentId());
        assertEquals("TURISMO", response.getVisaType());
        assertEquals("PENDIENTE", response.getStatus());

        verify(mapper, times(1)).toEntity(request);
        verify(repository, times(1)).save(any(VisaRequest.class));
        verify(mapper, times(1)).toDto(savedVisa);
    }

    @Test
    void debeBuscarVisaPorIdCorrectamente() {
        VisaRequest visa = crearVisa();
        VisaResponseDto responseDto = crearResponseDto();

        when(repository.findById(1L)).thenReturn(Optional.of(visa));
        when(mapper.toDto(visa)).thenReturn(responseDto);

        VisaResponseDto response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("TURISMO", response.getVisaType());

        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toDto(visa);
    }

    @Test
    void debeLanzarErrorCuandoVisaNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById(99L));

        verify(repository, times(1)).findById(99L);
        verify(mapper, never()).toDto(any(VisaRequest.class));
    }

    @Test
    void debeAprobarVisaCorrectamente() {
        VisaRequest visa = crearVisa();
        visa.setStatus("PENDIENTE");

        VisaResponseDto responseDto = crearResponseDto();
        responseDto.setStatus("APROBADA");
        responseDto.setObservations("Solicitud aprobada correctamente");

        when(repository.findById(1L)).thenReturn(Optional.of(visa));
        prepararValidacionesCorrectas();
        when(repository.save(any(VisaRequest.class))).thenReturn(visa);
        when(mapper.toDto(visa)).thenReturn(responseDto);

        VisaResponseDto response = service.approve(1L);

        assertNotNull(response);
        assertEquals("APROBADA", response.getStatus());

        verify(repository, times(1)).save(any(VisaRequest.class));
        verify(mapper, times(1)).toDto(visa);
    }

    @Test
    void debeRechazarVisaCorrectamente() {
        VisaRequest visa = crearVisa();

        VisaResponseDto responseDto = crearResponseDto();
        responseDto.setStatus("RECHAZADA");
        responseDto.setObservations("Solicitud rechazada por revisión administrativa");

        when(repository.findById(1L)).thenReturn(Optional.of(visa));
        when(repository.save(any(VisaRequest.class))).thenReturn(visa);
        when(mapper.toDto(visa)).thenReturn(responseDto);

        VisaResponseDto response = service.reject(1L);

        assertNotNull(response);
        assertEquals("RECHAZADA", response.getStatus());

        verify(repository, times(1)).save(any(VisaRequest.class));
        verify(mapper, times(1)).toDto(visa);
    }

    @Test
    void noDebeCrearVisaConUsuarioInactivo() {
        VisaRequestDto request = crearRequestValido();

        UserBasicDto user = crearUsuarioActivo();
        user.setStatus("INACTIVO");

        when(userClient.findById(1L)).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(VisaRequest.class));
        verify(mapper, never()).toEntity(any(VisaRequestDto.class));
    }

    @Test
    void noDebeCrearVisaConFechasInvalidas() {
        VisaRequestDto request = crearRequestValido();
        request.setStartDate(LocalDate.now().plusDays(10));
        request.setEndDate(LocalDate.now().plusDays(5));

        prepararValidacionesCorrectas();

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(VisaRequest.class));
        verify(mapper, never()).toEntity(any(VisaRequestDto.class));
    }

    @Test
    void debeEliminarVisaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    private VisaRequestDto crearRequestValido() {
        VisaRequestDto request = new VisaRequestDto();
        request.setUserId(1L);
        request.setIdentityDocumentId(2L);
        request.setVisaType("TURISMO");
        request.setDestinationCountry("ARGENTINA");
        request.setTravelPurpose("VACACIONES");
        request.setStartDate(LocalDate.now().plusDays(1));
        request.setEndDate(LocalDate.now().plusDays(10));
        request.setStatus("PENDIENTE");
        request.setObservations("Solicitud de prueba");
        return request;
    }

    private VisaRequest crearVisaSinId() {
        VisaRequest visa = new VisaRequest();
        visa.setUserId(1L);
        visa.setIdentityDocumentId(2L);
        visa.setVisaType("TURISMO");
        visa.setDestinationCountry("ARGENTINA");
        visa.setTravelPurpose("VACACIONES");
        visa.setStartDate(LocalDate.now().plusDays(1));
        visa.setEndDate(LocalDate.now().plusDays(10));
        visa.setStatus("PENDIENTE");
        visa.setObservations("Solicitud de prueba");
        return visa;
    }

    private VisaRequest crearVisa() {
        VisaRequest visa = new VisaRequest();
        visa.setId(1L);
        visa.setUserId(1L);
        visa.setIdentityDocumentId(2L);
        visa.setVisaType("TURISMO");
        visa.setDestinationCountry("ARGENTINA");
        visa.setTravelPurpose("VACACIONES");
        visa.setStartDate(LocalDate.now().plusDays(1));
        visa.setEndDate(LocalDate.now().plusDays(10));
        visa.setStatus("PENDIENTE");
        visa.setObservations("Solicitud de prueba");
        visa.setCreatedAt(LocalDateTime.now());
        return visa;
    }

    private VisaResponseDto crearResponseDto() {
        VisaResponseDto response = new VisaResponseDto();
        response.setId(1L);
        response.setUserId(1L);
        response.setIdentityDocumentId(2L);
        response.setVisaType("TURISMO");
        response.setDestinationCountry("ARGENTINA");
        response.setTravelPurpose("VACACIONES");
        response.setStartDate(LocalDate.now().plusDays(1));
        response.setEndDate(LocalDate.now().plusDays(10));
        response.setStatus("PENDIENTE");
        response.setObservations("Solicitud de prueba");
        response.setCreatedAt(LocalDateTime.now());
        return response;
    }

    private void prepararValidacionesCorrectas() {
        when(userClient.findById(1L)).thenReturn(crearUsuarioActivo());
        when(identityClient.findById(2L)).thenReturn(crearDocumentoValidado());
    }

    private UserBasicDto crearUsuarioActivo() {
        UserBasicDto user = new UserBasicDto();
        user.setId(1L);
        user.setRut("12345678-9");
        user.setName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan.perez@test.com");
        user.setRole("USUARIO");
        user.setStatus("ACTIVO");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private IdentityDocumentBasicDto crearDocumentoValidado() {
        IdentityDocumentBasicDto document = new IdentityDocumentBasicDto();
        document.setId(2L);
        document.setUserId(1L);
        document.setDocumentType("PASAPORTE");
        document.setDocumentNumber("ABC123456");
        document.setIssuingCountry("CHILE");
        document.setHolderName("Juan");
        document.setHolderLastName("Perez");
        document.setExpirationDate(LocalDate.now().plusYears(2));
        document.setMinor(false);
        document.setNotarizedAuthorization(false);
        document.setStatus("VALIDADO");
        document.setCreatedAt(LocalDateTime.now());
        return document;
    }
}