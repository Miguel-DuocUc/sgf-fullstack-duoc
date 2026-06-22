package com.duoc.sgf.ms_logistics.service.impl;

import com.duoc.sgf.ms_logistics.model.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.model.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import com.duoc.sgf.ms_logistics.repository.LogisticsRepository;
import com.duoc.sgf.ms_logistics.model.mapper.PuestoMapper;
import com.duoc.sgf.ms_logistics.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<PuestoFronterizo> puestoFronterizos = repository.findAll();
        ArrayList<PuestoResponseDto> listaDtos = new ArrayList<>();
        for (PuestoFronterizo puesto : puestoFronterizos) {
            PuestoResponseDto dto = mapper.toDto(puesto);
            listaDtos.add(dto);
        }
        log.info("Total de puestos encontrados: {}", listaDtos.size());
        return listaDtos;
    }

    @Override
    public PuestoResponseDto buscarporId(Long id) {
        log.info("Buscando puesto fronterizo por id: {}", id);
        PuestoFronterizo puestoFronterizo = repository.findById(id).orElse(null);
        if (puestoFronterizo != null) {
            log.info("Puesto fronterizo encontrado con id: {}", id);
            return mapper.toDto(puestoFronterizo);
        }
        log.warn("No se encontró puesto fronterizo con id: {}", id);
        return null;
    }

    @Override
    public PuestoResponseDto actualizarPuesto(Long id, PuestoRequestDto requestDto) {
        log.info("Actualizando puesto fronterizo con id: {}", id);
        PuestoFronterizo puestoFronterizo = repository.findById(id).orElse(null);
        if (puestoFronterizo != null) {
            puestoFronterizo.setName(requestDto.getName());
            puestoFronterizo.setDireccion(requestDto.getDireccion());
            puestoFronterizo.setCantPersonMax(requestDto.getCantPersonMax());
            puestoFronterizo.setGuardPerson(requestDto.getGuardPerson());
            puestoFronterizo.setEstadoOperativo(requestDto.getEstadoOperativo());
            repository.save(puestoFronterizo);
            log.info("Puesto fronterizo actualizado correctamente con id: {}", id);
            return mapper.toDto(puestoFronterizo);
        } else {
            log.warn("No se pudo actualizar. Puesto fronterizo no encontrado con id: {}", id);
            return null;
        }
    }

    @Override
    public boolean eliminarPuesto(Long id) {
        log.info("Eliminando puesto fronterizo con id: {}", id);
        PuestoFronterizo puestoFronterizo = repository.findById(id).orElse(null);
        if (puestoFronterizo != null) {
            repository.delete(puestoFronterizo);
            log.info("Puesto fronterizo eliminado correctamente con id: {}", id);
            return true;
        } else {
            log.warn("No se pudo eliminar. Puesto fronterizo no encontrado con id: {}", id);
            return false;
        }
    }
}