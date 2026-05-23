package com.ecomarket.logistica.repository;

import com.ecomarket.logistica.model.RecepcionMercaderia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionMercaderiaRepository extends JpaRepository<RecepcionMercaderia, Long> {
}