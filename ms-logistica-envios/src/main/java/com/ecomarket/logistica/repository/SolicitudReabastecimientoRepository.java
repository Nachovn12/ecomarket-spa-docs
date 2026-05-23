package com.ecomarket.logistica.repository;

import com.ecomarket.logistica.model.SolicitudReabastecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudReabastecimientoRepository extends JpaRepository<SolicitudReabastecimiento, Long> {
}