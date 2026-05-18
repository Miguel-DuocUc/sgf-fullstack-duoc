package com.duoc.sgf.ms_alerts.service;


import com.duoc.sgf.ms_alerts.model.Alert;
import java.util.List;

public interface AlertService {
    Alert crearAlerta(Alert alerta);
    List<Alert> obtenerAlertasActivas();
    List<Alert> verificarPasaporte(String pasaporte);
}