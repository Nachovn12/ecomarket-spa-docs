package com.ecomarket.admin.repository;

import com.ecomarket.admin.model.AsignacionPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionPersonalRepository extends JpaRepository<AsignacionPersonal, Long> {

    List<AsignacionPersonal> findByTiendaIdTiendaAndActivaTrue(Long idTienda);

    List<AsignacionPersonal> findByIdUsuarioInternoAndActivaTrue(Long idUsuarioInterno);
}
