package com.duoc.sgf.ms_logistics.service.impl;
import com.duoc.sgf.ms_logistics.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.dto.PuestoResponseDto;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;
import com.duoc.sgf.ms_logistics.repository.LogisticsRepository;
import com.duoc.sgf.ms_logistics.model.mapper.PuestoMapper;
import com.duoc.sgf.ms_logistics.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository repository;
    private final PuestoMapper mapper;

    @Override
    public PuestoResponseDto crearPuesto(PuestoRequestDto requestDto) {
        PuestoFronterizo nuevopuesto = mapper.toEntity(requestDto);
        PuestoFronterizo puestoGuardado = repository.save(nuevopuesto);
        return mapper.toDto(nuevopuesto);

    }

    @Override
    public List<PuestoResponseDto> listarTodo() {
        return List.of();
    }

    @Override
    public PuestoResponseDto buscarporId(Long id) {
        return null;
    }

    @Override
    public PuestoResponseDto actualizarPuesto(Long id, PuestoRequestDto requestDto) {
        return null;
    }

    @Override
    public boolean eliminarPuesto(Long id) {
        return false;
    }
}
