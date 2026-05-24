package com.ecomarket.logistica.repository;

import com.ecomarket.logistica.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByIdPedido(Long idPedido);
}