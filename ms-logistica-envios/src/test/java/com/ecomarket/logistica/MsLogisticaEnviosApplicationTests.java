package com.ecomarket.logistica;

import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MsLogisticaEnviosApplicationTests {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Test
    void contextLoadsAndDatabaseWorks() {
        // Prueba básica para asegurar que H2 levanta y guarda correctamente
        Proveedor p = new Proveedor();
        p.setRazonSocial("Logística Rápida Test");
        p.setRut("12345678-9");
        p.setTipoProveedor("TRANSPORTE");
        p.setCobertura("Santiago");
        p.setActivo(true);
        
        Proveedor guardado = proveedorRepository.save(p);
        
        assertNotNull(guardado.getId(), "El ID no debería ser nulo tras guardar en H2");
        assertTrue(guardado.getActivo(), "El proveedor debe estar activo");
    }

}