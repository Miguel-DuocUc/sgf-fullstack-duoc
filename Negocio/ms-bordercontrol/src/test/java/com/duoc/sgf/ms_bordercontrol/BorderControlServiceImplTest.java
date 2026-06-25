package com.duoc.sgf.ms_bordercontrol;

import com.duoc.sgf.ms_bordercontrol.client.HealthClient;
import com.duoc.sgf.ms_bordercontrol.client.IdentityClient;
import com.duoc.sgf.ms_bordercontrol.client.LogisticsClient;
import com.duoc.sgf.ms_bordercontrol.client.UserClient;
import com.duoc.sgf.ms_bordercontrol.client.VisaClient;
import com.duoc.sgf.ms_bordercontrol.event.BorderControlEventProducer;
import com.duoc.sgf.ms_bordercontrol.model.BorderControl;
import com.duoc.sgf.ms_bordercontrol.model.dto.*;
import com.duoc.sgf.ms_bordercontrol.repository.BorderControlRepository;
import com.duoc.sgf.ms_bordercontrol.service.impl.BorderControlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorderControlServiceImplTest {

    private BorderControlRepository repository;
    private UserClient userClient;
    private IdentityClient identityClient;
    private VisaClient visaClient;
    private HealthClient healthClient;
    private LogisticsClient logisticsClient;
    private BorderControlEventProducer eventProducer;
    private BorderControlServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(BorderControlRepository.class);
        userClient = mock(UserClient.class);
        identityClient = mock(IdentityClient.class);
        visaClient = mock(VisaClient.class);
        healthClient = mock(HealthClient.class);
        logisticsClient = mock(LogisticsClient.class);
        eventProducer = mock(BorderControlEventProducer.class);

        service = new BorderControlServiceImpl(
                repository,
                userClient,
                identityClient,
                visaClient,
                healthClient,
                logisticsClient,
                eventProducer
        );
    }

    @Test
    void debeCrearControlFronterizoCorrectamente() {
        BorderControlRequestDto request = crearRequestValido();
        prepararValidacionesRemotasCorrectas();

        BorderControl saved = crearBorderControl();
        saved.setStatus("AUTORIZADO");

        when(repository.save(any(BorderControl.class))).thenReturn(saved);

        BorderControlResponseDto response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("AUTORIZADO", response.getStatus());

        verify(repository, times(1)).save(any(BorderControl.class));
        verify(eventProducer, times(1)).publishBorderControlEvent(any(BorderControl.class));
    }

    @Test
    void debeBuscarControlPorIdCorrectamente() {
        BorderControl borderControl = crearBorderControl();

        when(repository.findById(1L)).thenReturn(Optional.of(borderControl));

        BorderControlResponseDto response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("AUTORIZADO", response.getStatus());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoControlNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById(99L));

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeEliminarControlCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void noDebeEliminarControlInexistente() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.delete(99L));

        verify(repository, never()).deleteById(99L);
    }

    @Test
    void noDebeCrearControlSiUsuarioNoEstaActivo() {
        BorderControlRequestDto request = crearRequestValido();

        UserBasicDto user = new UserBasicDto();
        user.setId(1L);
        user.setStatus("INACTIVO");

        when(userClient.findById(1L)).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(BorderControl.class));
        verify(eventProducer, never()).publishBorderControlEvent(any(BorderControl.class));
    }

    @Test
    void noDebeCrearControlSiVisaNoEstaAprobada() {
        BorderControlRequestDto request = crearRequestValido();

        UserBasicDto user = crearUsuarioActivo();
        IdentityDocumentBasicDto document = crearDocumentoValidado();

        VisaRequestBasicDto visa = crearVisaAprobada();
        visa.setStatus("PENDIENTE");

        when(userClient.findById(1L)).thenReturn(user);
        when(identityClient.findById(2L)).thenReturn(document);
        when(visaClient.findById(3L)).thenReturn(visa);

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(BorderControl.class));
        verify(eventProducer, never()).publishBorderControlEvent(any(BorderControl.class));
    }

    private BorderControlRequestDto crearRequestValido() {
        BorderControlRequestDto request = new BorderControlRequestDto();
        request.setUserId(1L);
        request.setIdentityDocumentId(2L);
        request.setVisaRequestId(3L);
        request.setHealthDeclarationId(4L);
        request.setLogisticsCheckpointId(5L);
        request.setOfficerName("Funcionario Test");
        request.setMovementType("ENTRADA");
        request.setStatus("AUTORIZADO");
        request.setObservations("Control autorizado");
        return request;
    }

    private BorderControl crearBorderControl() {
        BorderControl borderControl = new BorderControl();
        borderControl.setId(1L);
        borderControl.setUserId(1L);
        borderControl.setIdentityDocumentId(2L);
        borderControl.setVisaRequestId(3L);
        borderControl.setHealthDeclarationId(4L);
        borderControl.setLogisticsCheckpointId(5L);
        borderControl.setOfficerName("Funcionario Test");
        borderControl.setMovementType("ENTRADA");
        borderControl.setStatus("AUTORIZADO");
        borderControl.setObservations("Control autorizado");
        borderControl.setCreatedAt(LocalDateTime.now());
        return borderControl;
    }

    private void prepararValidacionesRemotasCorrectas() {
        when(userClient.findById(1L)).thenReturn(crearUsuarioActivo());
        when(identityClient.findById(2L)).thenReturn(crearDocumentoValidado());
        when(visaClient.findById(3L)).thenReturn(crearVisaAprobada());
        when(healthClient.findById(4L)).thenReturn(crearDeclaracionApta());
        when(logisticsClient.obtenerPasoFronterizoPorId(5L)).thenReturn(crearCheckpointValido());
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

    private VisaRequestBasicDto crearVisaAprobada() {
        VisaRequestBasicDto visa = new VisaRequestBasicDto();
        visa.setId(3L);
        visa.setUserId(1L);
        visa.setIdentityDocumentId(2L);
        visa.setVisaType("TURISMO");
        visa.setDestinationCountry("ARGENTINA");
        visa.setTravelPurpose("VACACIONES");
        visa.setStartDate(LocalDate.now().plusDays(1));
        visa.setEndDate(LocalDate.now().plusDays(15));
        visa.setStatus("APROBADA");
        visa.setObservations("Visa aprobada");
        visa.setCreatedAt(LocalDateTime.now());
        return visa;
    }

    private HealthDeclarationBasicDto crearDeclaracionApta() {
        HealthDeclarationBasicDto health = new HealthDeclarationBasicDto();
        health.setId(4L);
        health.setUserId(1L);
        health.setIdentityDocumentId(2L);
        health.setHasSymptoms(false);
        health.setSymptomsDescription("Sin síntomas");
        health.setHasRecentContact(false);
        health.setVaccinationStatus("COMPLETA");
        health.setRiskLevel("BAJO");
        health.setStatus("APTO");
        health.setObservations("Declaración apta");
        health.setCreatedAt(LocalDateTime.now());
        return health;
    }

    private LogisticsCheckpointBasicDto crearCheckpointValido() {
        LogisticsCheckpointBasicDto checkpoint = new LogisticsCheckpointBasicDto();
        checkpoint.setId(5L);
        checkpoint.setName("Paso Los Libertadores");
        checkpoint.setDireccion("Ruta Internacional 60");
        checkpoint.setEstadoOperativo("OPERATIVO");
        checkpoint.setCantPersonMax(100);
        checkpoint.setGuardPerson(10);
        return checkpoint;
    }
}