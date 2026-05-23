package com.ecomarket.logistica.repository;

import com.ecomarket.logistica.model.SeguimientoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeguimientoEnvioRepository extends JpaRepository<SeguimientoEnvio, Long> {
    List<SeguimientoEnvio> findByEnvioIdOrderByFechaRegistroDesc(Long envioId);
}