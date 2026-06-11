package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.exception.ReporteException;
import com.ecomarket.reportes.exception.ReporteNotFoundException;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.Reporte;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.model.TipoReporte;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import com.ecomarket.reportes.repository.ReporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio principal de Reportes y KPIs.
 * Genera reportes basados en los indicadores KPI almacenados en la BD local.
 */
@Service
@Transactional
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;
    private final IndicadorKPIRepository indicadorKPIRepository;

    public ReporteService(ReporteRepository reporteRepository,
                          IndicadorKPIRepository indicadorKPIRepository) {
        this.reporteRepository = reporteRepository;
        this.indicadorKPIRepository = indicadorKPIRepository;
    }

    // =========================================================================
    // REPORTES — CRUD
    // =========================================================================

    @Transactional(readOnly = true)
    public List<Reporte> listarReportes() {
        log.info("Listando todos los reportes");
        return reporteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reporte obtenerReportePorId(Long id) {
        log.info("Consultando reporte por ID: {}", id);
        return reporteRepository.findById(id)
                .orElseThrow(() -> new ReporteNotFoundException("Reporte no encontrado con id: " + id));
    }

    public Reporte crearReporte(Reporte reporte) {
        log.info("Creando reporte de tipo: {}", reporte.getTipo());
        Reporte guardado = reporteRepository.save(reporte);
        log.info("Reporte creado correctamente. id={}", guardado.getId());
        return guardado;
    }

    public void eliminarReporte(Long id) {
        log.info("Eliminando reporte. id={}", id);
        if (!reporteRepository.existsById(id)) {
            throw new ReporteNotFoundException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
        log.info("Reporte eliminado correctamente. id={}", id);
    }

    @Transactional(readOnly = true)
    public List<Reporte> listarPorTipo(TipoReporte tipo) {
        log.info("Listando reportes por tipo: {}", tipo);
        return reporteRepository.findByTipo(tipo);
    }

    @Transactional(readOnly = true)
    public List<Reporte> listarPorTienda(Long idTienda) {
        log.info("Listando reportes por tienda. idTienda={}", idTienda);
        return reporteRepository.findByIdTienda(idTienda);
    }

    // =========================================================================
    // GENERACIÓN DE REPORTES (datos desde KPIs almacenados en BD local)
    // =========================================================================

    public ReporteVentasDTO generarReporteVentas(ReporteFiltroRequestDTO filtro) {
        validarRangoFechas(filtro);
        log.info("Generando reporte de ventas. idTienda={}, desde={}, hasta={}",
                filtro.getIdTienda(), filtro.getFechaInicio(), filtro.getFechaFin());

        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.VENTAS);
        reporte.setIdTienda(filtro.getIdTienda());
        reporte.setFechaInicio(filtro.getFechaInicio());
        reporte.setFechaFin(filtro.getFechaFin());
        reporteRepository.save(reporte);

        List<IndicadorKPI> kpisVentas = indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES);
        double totalVentas = kpisVentas.stream().mapToDouble(IndicadorKPI::getValor).sum();

        ReporteVentasDTO dto = new ReporteVentasDTO();
        dto.setIdTienda(filtro.getIdTienda());
        dto.setFechaInicio(filtro.getFechaInicio());
        dto.setFechaFin(filtro.getFechaFin());
        dto.setVentasTotales(totalVentas);
        dto.setTotalTransacciones(kpisVentas.size());
        dto.setProductosVendidos(kpisVentas.size() * 5);

        log.info("Reporte de ventas generado. ventasTotales={}, transacciones={}",
                totalVentas, kpisVentas.size());
        return dto;
    }

    public ReporteInventarioDTO generarReporteInventario(Long idTienda) {
        log.info("Generando reporte de inventario. idTienda={}", idTienda);

        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.INVENTARIO);
        reporte.setIdTienda(idTienda);
        reporteRepository.save(reporte);

        List<IndicadorKPI> kpisBajoStock = indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO);
        List<IndicadorKPI> kpisRotacion = indicadorKPIRepository.findByTipo(TipoKPI.ROTACION_INVENTARIO);

        ReporteInventarioDTO dto = new ReporteInventarioDTO();
        dto.setIdTienda(idTienda);
        dto.setProductosDisponibles(kpisRotacion.size() * 10);
        dto.setProductosBajoStock(kpisBajoStock.size());
        dto.setProductosSinStock(0);

        log.info("Reporte de inventario generado. productosBajoStock={}", kpisBajoStock.size());
        return dto;
    }

    public ReporteRendimientoDTO generarReporteRendimiento(ReporteFiltroRequestDTO filtro) {
        validarRangoFechas(filtro);
        log.info("Generando reporte de rendimiento. idTienda={}", filtro.getIdTienda());

        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.RENDIMIENTO_TIENDA);
        reporte.setIdTienda(filtro.getIdTienda());
        reporte.setFechaInicio(filtro.getFechaInicio());
        reporte.setFechaFin(filtro.getFechaFin());
        reporteRepository.save(reporte);

        List<IndicadorKPI> kpisVentas = indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES);
        List<IndicadorKPI> kpisPedidos = indicadorKPIRepository.findByTipo(TipoKPI.PEDIDOS_ENTREGADOS);
        List<IndicadorKPI> kpisBajoStock = indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO);
        List<IndicadorKPI> kpisRendimiento = indicadorKPIRepository.findByTipo(TipoKPI.RENDIMIENTO_TIENDA);

        double ventas = kpisVentas.stream().mapToDouble(IndicadorKPI::getValor).sum();
        double rendimiento = kpisRendimiento.stream().mapToDouble(IndicadorKPI::getValor).average().orElse(0.0);

        ReporteRendimientoDTO dto = new ReporteRendimientoDTO();
        dto.setIdTienda(filtro.getIdTienda());
        dto.setFechaInicio(filtro.getFechaInicio());
        dto.setFechaFin(filtro.getFechaFin());
        dto.setVentasPorTienda(ventas);
        dto.setPedidosEntregados(kpisPedidos.size());
        dto.setStockBajo(kpisBajoStock.size());
        dto.setRendimientoOperativo(rendimiento);

        log.info("Reporte de rendimiento generado. idTienda={}, rendimiento={}",
                filtro.getIdTienda(), rendimiento);
        return dto;
    }

    // =========================================================================
    // KPIs — CRUD
    // =========================================================================

    @Transactional(readOnly = true)
    public List<IndicadorKPI> listarKPIs() {
        log.info("Listando todos los KPIs");
        return indicadorKPIRepository.findAll();
    }

    @Transactional(readOnly = true)
    public IndicadorKPI obtenerKPIPorId(Long id) {
        log.info("Consultando KPI por ID: {}", id);
        return indicadorKPIRepository.findById(id)
                .orElseThrow(() -> new ReporteNotFoundException("IndicadorKPI no encontrado con id: " + id));
    }

    public IndicadorKPI crearKPI(IndicadorKPI kpi) {
        log.info("Creando KPI de tipo: {}", kpi.getTipo());
        IndicadorKPI guardado = indicadorKPIRepository.save(kpi);
        log.info("KPI creado correctamente. id={}", guardado.getId());
        return guardado;
    }

    public void eliminarKPI(Long id) {
        log.info("Eliminando KPI. id={}", id);
        if (!indicadorKPIRepository.existsById(id)) {
            throw new ReporteNotFoundException("IndicadorKPI no encontrado con id: " + id);
        }
        indicadorKPIRepository.deleteById(id);
        log.info("KPI eliminado correctamente. id={}", id);
    }

    @Transactional(readOnly = true)
    public List<IndicadorKPI> listarKPIsPorTipo(TipoKPI tipo) {
        log.info("Listando KPIs por tipo: {}", tipo);
        return indicadorKPIRepository.findByTipo(tipo);
    }

    public IndicadorKPIResponseDTO toDTO(IndicadorKPI kpi) {
        IndicadorKPIResponseDTO dto = new IndicadorKPIResponseDTO();
        dto.setId(kpi.getId());
        dto.setTipo(kpi.getTipo().name());
        dto.setValor(kpi.getValor());
        dto.setDescripcion(kpi.getDescripcion());
        dto.setFechaCalculo(kpi.getFechaCalculo());
        return dto;
    }

    // =========================================================================
    // Validaciones privadas
    // =========================================================================

    private void validarRangoFechas(ReporteFiltroRequestDTO filtro) {
        if (filtro.getFechaInicio() == null || filtro.getFechaFin() == null) {
            throw new ReporteException("Las fechas de inicio y fin son obligatorias");
        }
        if (filtro.getFechaInicio().isAfter(filtro.getFechaFin())) {
            throw new ReporteException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
}
