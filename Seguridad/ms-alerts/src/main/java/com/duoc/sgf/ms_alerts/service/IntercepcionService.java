package com.duoc.sgf.ms_alerts.service;

import com.duoc.sgf.ms_alerts.model.dto.IntercepcionEventDto;
import com.duoc.sgf.ms_alerts.model.dto.IntercepcionResponseDto;

public interface IntercepcionService {
    IntercepcionResponseDto registrarIntercepcion(IntercepcionEventDto evento);
}