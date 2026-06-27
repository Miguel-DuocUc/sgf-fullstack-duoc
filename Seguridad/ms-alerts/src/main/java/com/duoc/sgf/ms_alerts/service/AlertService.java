package com.duoc.sgf.ms_alerts.service;

import com.duoc.sgf.ms_alerts.model.dto.AlertRequestDto;
import com.duoc.sgf.ms_alerts.model.dto.AlertResponseDto;
import java.util.List;

public interface AlertService {
    AlertResponseDto crearAlerta(AlertRequestDto request);
    List<AlertResponseDto> obtenerAlertasActivas();
    List<AlertResponseDto> verificarPasaporte(String pasaporte);
    AlertResponseDto desactivarAlerta(Long id);
}