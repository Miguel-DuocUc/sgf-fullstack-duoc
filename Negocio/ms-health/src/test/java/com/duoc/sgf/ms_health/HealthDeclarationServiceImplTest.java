package com.duoc.sgf.ms_health;

import com.duoc.sgf.ms_health.client.IdentityClient;
import com.duoc.sgf.ms_health.client.UserClient;
import com.duoc.sgf.ms_health.model.HealthDeclaration;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;
import com.duoc.sgf.ms_health.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_health.model.dto.UserBasicDto;
import com.duoc.sgf.ms_health.model.mapper.HealthDeclarationMapper;
import com.duoc.sgf.ms_health.repository.HealthDeclarationRepository;
import com.duoc.sgf.ms_health.service.impl.HealthDeclarationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthDeclarationServiceImplTest {

    private HealthDeclarationRepository repository;
    private UserClient userClient;
    private IdentityClient identityClient;
    private HealthDeclarationMapper mapper;
    private HealthDeclarationServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(HealthDeclarationRepository.class);
        userClient = mock(UserClient.class);
        identityClient = mock(IdentityClient.class);
        mapper = mock(HealthDeclarationMapper.class);

        service = new HealthDeclarationServiceImpl(
                repository,
                mapper,
                userClient,
                identityClient
        );
    }

    @Test
    void debeCrearDeclaracionCorrectamente() {
        HealthDeclarationRequestDto request = crearRequestValido();
        prepararValidacionesCorrectas();

        HealthDeclaration declarationToSave = crearDeclaracionSinId();
        HealthDeclaration savedDeclaration = crearDeclaracion();
        HealthDeclarationResponseDto responseDto = crearResponseDto();

        when(mapper.toEntity(request)).thenReturn(declarationToSave);
        when(repository.save(any(HealthDeclaration.class))).thenReturn(savedDeclaration);
        when(mapper.toDto(savedDeclaration)).thenReturn(responseDto);

        HealthDeclarationResponseDto response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("BAJO", response.getRiskLevel());
        assertEquals("PENDIENTE", response.getStatus());

        verify(mapper, times(1)).toEntity(request);
        verify(repository, times(1)).save(any(HealthDeclaration.class));
        verify(mapper, times(1)).toDto(savedDeclaration);
    }

    @Test
    void debeBuscarDeclaracionPorIdCorrectamente() {
        HealthDeclaration declaration = crearDeclaracion();
        HealthDeclarationResponseDto responseDto = crearResponseDto();

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(mapper.toDto(declaration)).thenReturn(responseDto);

        HealthDeclarationResponseDto response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PENDIENTE", response.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toDto(declaration);
    }

    @Test
    void debeLanzarErrorCuandoDeclaracionNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById(99L));

        verify(repository, times(1)).findById(99L);
        verify(mapper, never()).toDto(any(HealthDeclaration.class));
    }

    @Test
    void debeEvaluarDeclaracionAptaCorrectamente() {
        HealthDeclaration declaration = crearDeclaracion();
        declaration.setHasSymptoms(false);
        declaration.setHasRecentContact(false);

        HealthDeclarationResponseDto responseDto = crearResponseDto();
        responseDto.setStatus("APTO");
        responseDto.setRiskLevel("BAJO");
        responseDto.setObservations("Declaración sanitaria aprobada.");

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(repository.save(any(HealthDeclaration.class))).thenReturn(declaration);
        when(mapper.toDto(declaration)).thenReturn(responseDto);

        HealthDeclarationResponseDto response = service.evaluate(1L);

        assertNotNull(response);
        assertEquals("APTO", response.getStatus());
        assertEquals("BAJO", response.getRiskLevel());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
        verify(mapper, times(1)).toDto(declaration);
    }

    @Test
    void debeEvaluarDeclaracionNoAptaPorSintomas() {
        HealthDeclaration declaration = crearDeclaracion();
        declaration.setHasSymptoms(true);
        declaration.setHasRecentContact(false);

        HealthDeclarationResponseDto responseDto = crearResponseDto();
        responseDto.setStatus("NO_APTO");
        responseDto.setRiskLevel("ALTO");
        responseDto.setObservations("Declaración rechazada por presencia de síntomas.");

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(repository.save(any(HealthDeclaration.class))).thenReturn(declaration);
        when(mapper.toDto(declaration)).thenReturn(responseDto);

        HealthDeclarationResponseDto response = service.evaluate(1L);

        assertNotNull(response);
        assertEquals("NO_APTO", response.getStatus());
        assertEquals("ALTO", response.getRiskLevel());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
        verify(mapper, times(1)).toDto(declaration);
    }

    @Test
    void debeEvaluarDeclaracionEnRevisionPorContactoReciente() {
        HealthDeclaration declaration = crearDeclaracion();
        declaration.setHasSymptoms(false);
        declaration.setHasRecentContact(true);

        HealthDeclarationResponseDto responseDto = crearResponseDto();
        responseDto.setStatus("EN_REVISION");
        responseDto.setRiskLevel("MEDIO");
        responseDto.setObservations("Declaración requiere revisión por contacto reciente.");

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(repository.save(any(HealthDeclaration.class))).thenReturn(declaration);
        when(mapper.toDto(declaration)).thenReturn(responseDto);

        HealthDeclarationResponseDto response = service.evaluate(1L);

        assertNotNull(response);
        assertEquals("EN_REVISION", response.getStatus());
        assertEquals("MEDIO", response.getRiskLevel());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
        verify(mapper, times(1)).toDto(declaration);
    }

    @Test
    void noDebeCrearDeclaracionConUsuarioInactivo() {
        HealthDeclarationRequestDto request = crearRequestValido();

        UserBasicDto user = crearUsuarioActivo();
        user.setStatus("INACTIVO");

        when(userClient.findById(1L)).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(HealthDeclaration.class));
        verify(mapper, never()).toEntity(any(HealthDeclarationRequestDto.class));
    }

    @Test
    void debeEliminarDeclaracionCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    private HealthDeclarationRequestDto crearRequestValido() {
        HealthDeclarationRequestDto request = new HealthDeclarationRequestDto();
        request.setUserId(1L);
        request.setIdentityDocumentId(2L);
        request.setHasSymptoms(false);
        request.setSymptomsDescription("Sin síntomas");
        request.setHasRecentContact(false);
        request.setVaccinationStatus("COMPLETA");
        request.setStatus("PENDIENTE");
        request.setObservations("Declaración de prueba");
        return request;
    }

    private HealthDeclaration crearDeclaracionSinId() {
        HealthDeclaration declaration = new HealthDeclaration();
        declaration.setUserId(1L);
        declaration.setIdentityDocumentId(2L);
        declaration.setHasSymptoms(false);
        declaration.setSymptomsDescription("Sin síntomas");
        declaration.setHasRecentContact(false);
        declaration.setVaccinationStatus("COMPLETA");
        declaration.setRiskLevel("BAJO");
        declaration.setStatus("PENDIENTE");
        declaration.setObservations("Declaración de prueba");
        return declaration;
    }

    private HealthDeclaration crearDeclaracion() {
        HealthDeclaration declaration = new HealthDeclaration();
        declaration.setId(1L);
        declaration.setUserId(1L);
        declaration.setIdentityDocumentId(2L);
        declaration.setHasSymptoms(false);
        declaration.setSymptomsDescription("Sin síntomas");
        declaration.setHasRecentContact(false);
        declaration.setVaccinationStatus("COMPLETA");
        declaration.setRiskLevel("BAJO");
        declaration.setStatus("PENDIENTE");
        declaration.setObservations("Declaración de prueba");
        declaration.setCreatedAt(LocalDateTime.now());
        return declaration;
    }

    private HealthDeclarationResponseDto crearResponseDto() {
        HealthDeclarationResponseDto response = new HealthDeclarationResponseDto();
        response.setId(1L);
        response.setUserId(1L);
        response.setIdentityDocumentId(2L);
        response.setHasSymptoms(false);
        response.setSymptomsDescription("Sin síntomas");
        response.setHasRecentContact(false);
        response.setVaccinationStatus("COMPLETA");
        response.setRiskLevel("BAJO");
        response.setStatus("PENDIENTE");
        response.setObservations("Declaración de prueba");
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