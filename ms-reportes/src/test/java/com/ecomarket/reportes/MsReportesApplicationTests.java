package com.ecomarket.reportes;

import com.ecomarket.reportes.entity.IndicadorKPI;
import com.ecomarket.reportes.entity.TipoKPI;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import com.ecomarket.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MsReportesApplicationTests {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private IndicadorKPIRepository indicadorKPIRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testCrearIndicadorKPI() {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setTipo(TipoKPI.VENTAS_TOTALES);
        kpi.setValor(15000.0);
        kpi.setDescripcion("Ventas totales del mes");
        indicadorKPIRepository.save(kpi);

        var encontrado = indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES);
        assertFalse(encontrado.isEmpty());
        assertEquals(15000.0, encontrado.get(0).getValor());
    }

    @Test
    void testListarReportes() {
        long count = reporteRepository.count();
        assertTrue(count >= 0);
    }

    @Test
    void testKpiStockBajo() {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setTipo(TipoKPI.STOCK_BAJO);
        kpi.setValor(5.0);
        kpi.setDescripcion("Productos con stock bajo");
        indicadorKPIRepository.save(kpi);

        var resultado = indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO);
        assertFalse(resultado.isEmpty());
    }
}