package com.duoc.sgf.ms_alerts.repository;

import com.duoc.sgf.ms_alerts.model.Intercepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntercepcionRepository extends JpaRepository<Intercepcion, Long> {
}