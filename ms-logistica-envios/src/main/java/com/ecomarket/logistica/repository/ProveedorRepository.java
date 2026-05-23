package com.ecomarket.logistica.repository;

import com.ecomarket.logistica.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByActivoTrue();
    List<Proveedor> findByTipoProveedorAndCobertura(String tipoProveedor, String cobertura);
}