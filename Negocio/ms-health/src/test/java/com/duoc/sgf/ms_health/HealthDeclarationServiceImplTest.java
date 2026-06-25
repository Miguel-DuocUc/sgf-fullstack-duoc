package com.duoc.sgf.ms_health;

import com.duoc.sgf.ms_health.client.IdentityClient;
import com.duoc.sgf.ms_health.client.UserClient;
import com.duoc.sgf.ms_health.model.HealthDeclaration;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationRequestDto;
import com.duoc.sgf.ms_health.model.dto.HealthDeclarationResponseDto;
import com.duoc.sgf.ms_health.model.dto.IdentityDocumentBasicDto;
import com.duoc.sgf.ms_health.model.dto.UserBasicDto;
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
    private HealthDeclarationServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(HealthDeclarationRepository.class);
        userClient = mock(UserClient.class);
        identityClient = mock(IdentityClient.class);
        service = new HealthDeclarationServiceImpl(repository, userClient, identityClient);
    }

    @Test
    void debeCrearDeclaracionCorrectamente() {
        HealthDeclarationRequestDto request = crearRequestValido();
        prepararValidacionesCorrectas();

        HealthDeclaration saved = crearDeclaracion();
        when(repository.save(any(HealthDeclaration.class))).thenReturn(saved);

        HealthDeclarationResponseDto response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("BAJO", response.getRiskLevel());
        assertEquals("PENDIENTE", response.getStatus());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
    }

    @Test
    void debeBuscarDeclaracionPorIdCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(crearDeclaracion()));

        HealthDeclarationResponseDto response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PENDIENTE", response.getStatus());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoDeclaracionNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById(99L));

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeEvaluarDeclaracionAptaCorrectamente() {
        HealthDeclaration declaration = crearDeclaracion();
        declaration.setHasSymptoms(false);
        declaration.setHasRecentContact(false);

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(repository.save(any(HealthDeclaration.class))).thenReturn(declaration);

        HealthDeclarationResponseDto response = service.evaluate(1L);

        assertNotNull(response);
        assertEquals("APTO", response.getStatus());
        assertEquals("BAJO", response.getRiskLevel());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
    }

    @Test
    void debeEvaluarDeclaracionNoAptaPorSintomas() {
        HealthDeclaration declaration = crearDeclaracion();
        declaration.setHasSymptoms(true);
        declaration.setHasRecentContact(false);

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(repository.save(any(HealthDeclaration.class))).thenReturn(declaration);

        HealthDeclarationResponseDto response = service.evaluate(1L);

        assertNotNull(response);
        assertEquals("NO_APTO", response.getStatus());
        assertEquals("ALTO", response.getRiskLevel());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
    }

    @Test
    void debeEvaluarDeclaracionEnRevisionPorContactoReciente() {
        HealthDeclaration declaration = crearDeclaracion();
        declaration.setHasSymptoms(false);
        declaration.setHasRecentContact(true);

        when(repository.findById(1L)).thenReturn(Optional.of(declaration));
        when(repository.save(any(HealthDeclaration.class))).thenReturn(declaration);

        HealthDeclarationResponseDto response = service.evaluate(1L);

        assertNotNull(response);
        assertEquals("EN_REVISION", response.getStatus());
        assertEquals("MEDIO", response.getRiskLevel());

        verify(repository, times(1)).save(any(HealthDeclaration.class));
    }

    @Test
    void noDebeCrearDeclaracionConUsuarioInactivo() {
        HealthDeclarationRequestDto request = crearRequestValido();

        UserBasicDto user = crearUsuarioActivo();
        user.setStatus("INACTIVO");

        when(userClient.findById(1L)).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(HealthDeclaration.class));
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