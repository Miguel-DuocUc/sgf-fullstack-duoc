package com.duoc.sgf.ms_identity;

import com.duoc.sgf.ms_identity.client.UserClient;
import com.duoc.sgf.ms_identity.model.IdentityDocument;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentRequestDto;
import com.duoc.sgf.ms_identity.model.dto.IdentityDocumentResponseDto;
import com.duoc.sgf.ms_identity.model.dto.UserBasicDto;
import com.duoc.sgf.ms_identity.repository.IdentityDocumentRepository;
import com.duoc.sgf.ms_identity.service.impl.IdentityDocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdentityDocumentServiceImplTest {

    private IdentityDocumentRepository repository;
    private UserClient userClient;
    private IdentityDocumentServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(IdentityDocumentRepository.class);
        userClient = mock(UserClient.class);
        service = new IdentityDocumentServiceImpl(repository, userClient);
    }

    @Test
    void debeCrearDocumentoCorrectamente() {
        IdentityDocumentRequestDto request = new IdentityDocumentRequestDto();
        request.setUserId(1L);
        request.setDocumentType("PASAPORTE");
        request.setDocumentNumber("ABC123456");
        request.setIssuingCountry("CHILE");
        request.setHolderName("Juan");
        request.setHolderLastName("Perez");
        request.setExpirationDate(LocalDate.now().plusYears(2));
        request.setMinor(false);
        request.setNotarizedAuthorization(false);
        request.setStatus("PENDIENTE");

        UserBasicDto user = new UserBasicDto();
        user.setId(1L);
        user.setStatus("ACTIVO");

        IdentityDocument savedDocument = new IdentityDocument();
        savedDocument.setId(1L);
        savedDocument.setUserId(1L);
        savedDocument.setDocumentType("PASAPORTE");
        savedDocument.setDocumentNumber("ABC123456");
        savedDocument.setIssuingCountry("CHILE");
        savedDocument.setHolderName("Juan");
        savedDocument.setHolderLastName("Perez");
        savedDocument.setExpirationDate(LocalDate.now().plusYears(2));
        savedDocument.setMinor(false);
        savedDocument.setNotarizedAuthorization(false);
        savedDocument.setStatus("PENDIENTE");
        savedDocument.setCreatedAt(LocalDateTime.now());

        when(userClient.findById(1L)).thenReturn(user);
        when(repository.existsByDocumentNumber("ABC123456")).thenReturn(false);
        when(repository.save(any(IdentityDocument.class))).thenReturn(savedDocument);

        IdentityDocumentResponseDto response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("PASAPORTE", response.getDocumentType());
        assertEquals("ABC123456", response.getDocumentNumber());
        assertEquals("PENDIENTE", response.getStatus());

        verify(repository, times(1)).save(any(IdentityDocument.class));
    }

    @Test
    void noDebeCrearDocumentoConNumeroDuplicado() {
        IdentityDocumentRequestDto request = new IdentityDocumentRequestDto();
        request.setUserId(1L);
        request.setDocumentType("PASAPORTE");
        request.setDocumentNumber("ABC123456");
        request.setIssuingCountry("CHILE");
        request.setHolderName("Juan");
        request.setHolderLastName("Perez");
        request.setExpirationDate(LocalDate.now().plusYears(2));

        UserBasicDto user = new UserBasicDto();
        user.setId(1L);
        user.setStatus("ACTIVO");

        when(userClient.findById(1L)).thenReturn(user);
        when(repository.existsByDocumentNumber("ABC123456")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> service.create(request));

        verify(repository, never()).save(any(IdentityDocument.class));
    }

    @Test
    void debeBuscarDocumentoPorIdCorrectamente() {
        IdentityDocument document = new IdentityDocument();
        document.setId(1L);
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

        when(repository.findById(1L)).thenReturn(Optional.of(document));

        IdentityDocumentResponseDto response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("ABC123456", response.getDocumentNumber());
        assertEquals("VALIDADO", response.getStatus());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoDocumentoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.findById(99L));

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeValidarDocumentoCorrectamente() {
        IdentityDocument document = new IdentityDocument();
        document.setId(1L);
        document.setUserId(1L);
        document.setDocumentType("PASAPORTE");
        document.setDocumentNumber("ABC123456");
        document.setIssuingCountry("CHILE");
        document.setHolderName("Juan");
        document.setHolderLastName("Perez");
        document.setExpirationDate(LocalDate.now().plusYears(2));
        document.setMinor(false);
        document.setNotarizedAuthorization(false);
        document.setStatus("PENDIENTE");
        document.setCreatedAt(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(document));
        when(repository.save(any(IdentityDocument.class))).thenReturn(document);

        IdentityDocumentResponseDto response = service.validateDocument(1L);

        assertNotNull(response);
        assertEquals("VALIDADO", response.getStatus());

        verify(repository, times(1)).save(any(IdentityDocument.class));
    }

    @Test
    void debeRechazarDocumentoVencido() {
        IdentityDocument document = new IdentityDocument();
        document.setId(1L);
        document.setUserId(1L);
        document.setDocumentType("PASAPORTE");
        document.setDocumentNumber("ABC123456");
        document.setIssuingCountry("CHILE");
        document.setHolderName("Juan");
        document.setHolderLastName("Perez");
        document.setExpirationDate(LocalDate.now().minusDays(1));
        document.setMinor(false);
        document.setNotarizedAuthorization(false);
        document.setStatus("PENDIENTE");
        document.setCreatedAt(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(document));
        when(repository.save(any(IdentityDocument.class))).thenReturn(document);

        IdentityDocumentResponseDto response = service.validateDocument(1L);

        assertNotNull(response);
        assertEquals("RECHAZADO", response.getStatus());

        verify(repository, times(1)).save(any(IdentityDocument.class));
    }

    @Test
    void debeEliminarDocumentoCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}