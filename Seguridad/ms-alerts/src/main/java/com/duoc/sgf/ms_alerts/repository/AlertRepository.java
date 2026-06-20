package com.duoc.sgf.ms_alerts.repository;

import com.duoc.sgf.ms_alerts.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByPasaporteCiudadanoAndActivaTrue(String pasaporteCiudadano);
    List<Alert> findByActivaTrue();
}