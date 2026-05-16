package com.duoc.sgf.ms_logistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duoc.sgf.ms_logistics.model.PuestoFronterizo;

@Repository
public interface LogisticsRepository extends JpaRepository <PuestoFronterizo,Long> {

}
