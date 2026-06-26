package com.duoc.sgf.ms_logistics;

import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.mapper.PuestoMapper;
import com.duoc.sgf.ms_logistics.repository.LogisticsRepository;
import com.duoc.sgf.ms_logistics.service.impl.LogisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogisticsServiceImplTest {

    private LogisticsRepository repository;
    private PuestoMapper mapper;
    private LogisticsServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(LogisticsRepository.class);
        mapper = mock(PuestoMapper.class);
        service = new LogisticsServiceImpl(repository, mapper);
    }

    @Test
    void debeCrearPuestoCorrectamente() {
        PuestoRequestDto request = crearRequest();
        PuestoFronterizo entity = crearEntidad();
        PuestoFronterizo saved = crearEntidad();
        PuestoResponseDto responseDto = crearResponse();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        PuestoResponseDto response = service.crearPuesto(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Paso Los Libertadores", response.getName());
        assertEquals("OPERATIVO", response.getEstadoOperativo());

        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDto(saved);
    }

    @Test
    void debeListarTodosLosPuestos() {
        PuestoFronterizo puesto = crearEntidad();
        PuestoResponseDto responseDto = crearResponse();

        when(repository.findAll()).thenReturn(List.of(puesto));
        when(mapper.toDto(puesto)).thenReturn(responseDto);

        List<PuestoResponseDto> response = service.listarTodo();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Paso Los Libertadores", response.get(0).getName());

        verify(repository, times(1)).findAll();
    }

    @Test
    void debeBuscarPuestoPorIdCorrectamente() {
        PuestoFronterizo puesto = crearEntidad();
        PuestoResponseDto responseDto = crearResponse();

        when(repository.findById(1L)).thenReturn(Optional.of(puesto));
        when(mapper.toDto(puesto)).thenReturn(responseDto);

        PuestoResponseDto response = service.buscarporId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Paso Los Libertadores", response.getName());

        verify(repository, times(1)).findById(1L);
    }

    // AQUI ESTÁ EL CAMBIO: Verificamos que lance la excepción
    @Test
    void debeLanzarExcepcionCuandoPuestoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Usamos assertThrows para verificar que "estalle" correctamente
        assertThrows(ResponseStatusException.class, () -> service.buscarporId(99L));

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeActualizarPuestoCorrectamente() {
        PuestoRequestDto request = crearRequest();
        request.setName("Paso Actualizado");

        PuestoFronterizo puesto = crearEntidad();

        PuestoResponseDto responseDto = crearResponse();
        responseDto.setName("Paso Actualizado");

        when(repository.findById(1L)).thenReturn(Optional.of(puesto));
        when(repository.save(puesto)).thenReturn(puesto);
        when(mapper.toDto(puesto)).thenReturn(responseDto);

        PuestoResponseDto response = service.actualizarPuesto(1L, request);

        assertNotNull(response);
        assertEquals("Paso Actualizado", response.getName());

        verify(repository, times(1)).save(puesto);
    }

    // AQUI ESTÁ EL CAMBIO: Verificamos que lance la excepción
    @Test
    void debeLanzarExcepcionAlActualizarPuestoInexistente() {
        PuestoRequestDto request = crearRequest();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.actualizarPuesto(99L, request));

        verify(repository, never()).save(any(PuestoFronterizo.class));
    }

    // AQUI ESTÁ EL CAMBIO: Ya no esperamos un boolean, solo ejecutamos
    @Test
    void debeEliminarPuestoCorrectamente() {
        PuestoFronterizo puesto = crearEntidad();

        when(repository.findById(1L)).thenReturn(Optional.of(puesto));

        // Como el método es void, solo lo llamamos
        service.eliminarPuesto(1L);

        verify(repository, times(1)).delete(puesto);
    }

    // AQUI ESTÁ EL CAMBIO: Verificamos que lance la excepción al no encontrarlo
    @Test
    void debeLanzarExcepcionAlEliminarPuestoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.eliminarPuesto(99L));

        verify(repository, never()).delete(any(PuestoFronterizo.class));
    }

    private PuestoRequestDto crearRequest() {
        PuestoRequestDto request = new PuestoRequestDto();
        request.setName("Paso Los Libertadores");
        request.setDireccion("Ruta Internacional 60");
        request.setCantPersonMax(100);
        request.setGuardPerson(10);
        request.setEstadoOperativo("OPERATIVO");
        return request;
    }

    private PuestoFronterizo crearEntidad() {
        PuestoFronterizo puesto = new PuestoFronterizo();
        puesto.setId(1L);
        puesto.setName("Paso Los Libertadores");
        puesto.setDireccion("Ruta Internacional 60");
        puesto.setCantPersonMax(100);
        puesto.setGuardPerson(10);
        puesto.setEstadoOperativo("OPERATIVO");
        return puesto;
    }

    private PuestoResponseDto crearResponse() {
        PuestoResponseDto response = new PuestoResponseDto();
        response.setId(1L);
        response.setName("Paso Los Libertadores");
        response.setDireccion("Ruta Internacional 60");
        response.setCantPersonMax(100);
        response.setGuardPerson(10);
        response.setEstadoOperativo("OPERATIVO");
        return response;
    }
}