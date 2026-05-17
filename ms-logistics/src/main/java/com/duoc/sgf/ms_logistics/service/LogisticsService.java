package com.duoc.sgf.ms_logistics.service;

import com.duoc.sgf.ms_logistics.dto.PuestoRequestDto;
import com.duoc.sgf.ms_logistics.dto.PuestoResponseDto;

import java.util.List;

public interface LogisticsService {
    //
    PuestoResponseDto crearPuesto(PuestoRequestDto requestDto);
    List<PuestoResponseDto> listarTodo();
    PuestoResponseDto buscarporId(Long id);
    PuestoResponseDto actualizarPuesto(Long id , PuestoRequestDto requestDto);
    boolean eliminarPuesto(Long id);
}
