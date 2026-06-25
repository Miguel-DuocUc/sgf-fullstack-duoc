package com.duoc.sgf.ms_logistics;

import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.mapper.PuestoMapper;
import com.duoc.sgf.ms_logistics.repository.LogisticsRepository;
import com.duoc.sgf.ms_logistics.service.impl.LogisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void debeRetornarNullCuandoPuestoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        PuestoResponseDto response = service.buscarporId(99L);

        assertNull(response);

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

    @Test
    void debeRetornarNullAlActualizarPuestoInexistente() {
        PuestoRequestDto request = crearRequest();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        PuestoResponseDto response = service.actualizarPuesto(99L, request);

        assertNull(response);

        verify(repository, never()).save(any(PuestoFronterizo.class));
    }

    @Test
    void debeEliminarPuestoCorrectamente() {
        PuestoFronterizo puesto = crearEntidad();

        when(repository.findById(1L)).thenReturn(Optional.of(puesto));

        boolean eliminado = service.eliminarPuesto(1L);

        assertTrue(eliminado);

        verify(repository, times(1)).delete(puesto);
    }

    @Test
    void debeRetornarFalseAlEliminarPuestoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        boolean eliminado = service.eliminarPuesto(99L);

        assertFalse(eliminado);

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