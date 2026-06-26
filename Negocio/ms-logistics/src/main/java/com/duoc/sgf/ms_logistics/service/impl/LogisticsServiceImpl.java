package com.duoc.sgf.ms_logistics.service.impl;

import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import com.duoc.sgf.ms_logistics.repository.LogisticsRepository;
import com.duoc.sgf.ms_logistics.model.mapper.PuestoMapper;
import com.duoc.sgf.ms_logistics.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository repository;
    private final PuestoMapper mapper;

    @Override
    public PuestoResponseDto crearPuesto(PuestoRequestDto requestDto) {
        log.info("Creando nuevo puesto fronterizo: {}", requestDto.getName());
        PuestoFronterizo nuevoPuesto = mapper.toEntity(requestDto);
        PuestoFronterizo puestoGuardado = repository.save(nuevoPuesto);
        log.info("Puesto fronterizo creado correctamente con id: {}", puestoGuardado.getId());
        return mapper.toDto(puestoGuardado);
    }

    @Override
    public List<PuestoResponseDto> listarTodo() {
        log.info("Listando todos los puestos fronterizos");

        // Reemplazamos el for-loop manual por un Stream moderno
        List<PuestoResponseDto> listaDtos = repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        log.info("Total de puestos encontrados: {}", listaDtos.size());
        return listaDtos;
    }

    @Override
    public PuestoResponseDto buscarporId(Long id) {
        log.info("Buscando puesto fronterizo por id: {}", id);
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    log.warn("No se encontró puesto fronterizo con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Puesto fronterizo no encontrado");
                });
    }

    @Override
    public PuestoResponseDto actualizarPuesto(Long id, PuestoRequestDto requestDto) {
        log.info("Actualizando puesto fronterizo con id: {}", id);

        PuestoFronterizo puestoFronterizo = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Puesto fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Puesto fronterizo no encontrado");
                });

        puestoFronterizo.setName(requestDto.getName());
        puestoFronterizo.setDireccion(requestDto.getDireccion());
        puestoFronterizo.setCantPersonMax(requestDto.getCantPersonMax());
        puestoFronterizo.setGuardPerson(requestDto.getGuardPerson());
        puestoFronterizo.setEstadoOperativo(requestDto.getEstadoOperativo());

        PuestoFronterizo actualizado = repository.save(puestoFronterizo);
        log.info("Puesto fronterizo actualizado correctamente con id: {}", id);

        return mapper.toDto(actualizado);
    }

    @Override
    public void eliminarPuesto(Long id) {
        log.info("Eliminando puesto fronterizo con id: {}", id);

        PuestoFronterizo puestoFronterizo = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo eliminar. Puesto fronterizo no encontrado con id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Puesto fronterizo no encontrado");
                });

        repository.delete(puestoFronterizo);
        log.info("Puesto fronterizo eliminado correctamente con id: {}", id);
    }
}