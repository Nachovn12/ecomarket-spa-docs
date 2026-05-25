package com.ecomarket.logistica.repository;

import com.ecomarket.logistica.model.RutaEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaEntregaRepository extends JpaRepository<RutaEntrega, Long> {
}
