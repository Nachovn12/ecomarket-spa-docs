package com.ecomarket.admin.service;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.exception.RecursoNoEncontradoException;
import com.ecomarket.admin.model.*;
import com.ecomarket.admin.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministracionSoporteService {

    private static final Logger log = LoggerFactory.getLogger(AdministracionSoporteService.class);

    private final TiendaRepository tiendaRepository;
    private final AsignacionPersonalRepository asignacionPersonalRepository;
    private final TicketSoporteRepository ticketSoporteRepository;
    private final RespuestaSoporteRepository respuestaSoporteRepository;
    private final AlertaSistemaRepository alertaSistemaRepository;
    private final MetricaSistemaRepository metricaSistemaRepository;
    private final RespaldoDatosRepository respaldoDatosRepository;
    private final UsuarioInternoClientService usuarioInternoClientService;

    // Tiendas

    public TiendaResponseDTO crearTienda(TiendaRequestDTO request) {
        validarHorarios(request.getHorarioApertura(), request.getHorarioCierre());

        Tienda tienda = Tienda.builder()
                .nombre(request.getNombre().trim())
                .ciudad(request.getCiudad().trim())
                .horarioApertura(request.getHorarioApertura())
                .horarioCierre(request.getHorarioCierre())
                .politicasLocales(normalizarTextoOpcional(request.getPoliticasLocales()))
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        Tienda tiendaGuardada = tiendaRepository.save(tienda);
        log.info("Tienda creada correctamente. idTienda={}, nombre={}", tiendaGuardada.getIdTienda(), tiendaGuardada.getNombre());

        return mapearTienda(tiendaGuardada);
    }

    public TiendaResponseDTO actualizarTienda(Long idTienda, TiendaRequestDTO request) {
        validarHorarios(request.getHorarioApertura(), request.getHorarioCierre());

        Tienda tienda = obtenerTiendaPorId(idTienda);
        tienda.setNombre(request.getNombre().trim());
        tienda.setCiudad(request.getCiudad().trim());
        tienda.setHorarioApertura(request.getHorarioApertura());
        tienda.setHorarioCierre(request.getHorarioCierre());
        tienda.setPoliticasLocales(normalizarTextoOpcional(request.getPoliticasLocales()));
        tienda.setFechaActualizacion(LocalDateTime.now());

        log.info("Tienda actualizada. idTienda={}", idTienda);
        return mapearTienda(tiendaRepository.save(tienda));
    }

    public TiendaResponseDTO consultarTienda(Long idTienda) {
        log.info("Consultando tienda. idTienda={}", idTienda);
        return mapearTienda(obtenerTiendaPorId(idTienda));
    }

    public List<TiendaResponseDTO> listarTiendas() {
        List<TiendaResponseDTO> tiendas = tiendaRepository.findAll().stream()
                .map(this::mapearTienda)
                .toList();
        log.info("Listado de tiendas. total={}", tiendas.size());
        return tiendas;
    }

    // Personal

    public AsignacionPersonalResponseDTO asignarPersonal(AsignacionPersonalRequestDTO request) {
        Tienda tienda = obtenerTiendaPorId(request.getIdTienda());

        // Comunicacion REST con MS usuarios e identidad para validar que el usuario
        // Existe y es un usuario interno activo (IE 2.4.1)
        log.info("Validando usuario interno en MS Usuarios. idUsuarioInterno={}", request.getIdUsuarioInterno());
        usuarioInternoClientService.validarUsuarioInternoExiste(request.getIdUsuarioInterno());

        AsignacionPersonal asignacion = AsignacionPersonal.builder()
                .idUsuarioInterno(request.getIdUsuarioInterno())
                .tienda(tienda)
                .cargo(request.getCargo().trim())
                .activa(true)
                .fechaAsignacion(LocalDateTime.now())
                .build();

        AsignacionPersonal asignacionGuardada = asignacionPersonalRepository.save(asignacion);
        log.info("Personal asignado correctamente. idAsignacion={}, idTienda={}, idUsuarioInterno={}",
                asignacionGuardada.getIdAsignacion(), request.getIdTienda(), request.getIdUsuarioInterno());

        return mapearAsignacion(asignacionGuardada);
    }

    public List<AsignacionPersonalResponseDTO> listarPersonalPorTienda(Long idTienda) {
        obtenerTiendaPorId(idTienda);

        List<AsignacionPersonalResponseDTO> personal = asignacionPersonalRepository
                .findByTiendaIdTiendaAndActivaTrue(idTienda).stream()
                .map(this::mapearAsignacion)
                .toList();

        log.info("Personal listado para tienda. idTienda={}, total={}", idTienda, personal.size());
        return personal;
    }

    // Tickets de soporte

    public TicketSoporteResponseDTO crearTicketSoporte(TicketSoporteRequestDTO request) {
        TicketSoporte ticket = TicketSoporte.builder()
                .asunto(request.getAsunto().trim())
                .descripcion(request.getDescripcion().trim())
                .nombreContacto(request.getNombreContacto().trim())
                .correoContacto(request.getCorreoContacto().trim().toLowerCase())
                .prioridad(request.getPrioridad())
                .estado(EstadoTicket.ABIERTO)
                .fechaCreacion(LocalDateTime.now())
                .build();

        TicketSoporte ticketGuardado = ticketSoporteRepository.save(ticket);
        log.info("Ticket de soporte creado. idTicket={}, prioridad={}", ticketGuardado.getIdTicket(), ticketGuardado.getPrioridad());

        return mapearTicket(ticketGuardado);
    }

    public TicketSoporteResponseDTO consultarTicket(Long idTicket) {
        log.info("Consultando ticket de soporte. idTicket={}", idTicket);
        return mapearTicket(obtenerTicketPorId(idTicket));
    }

    public List<TicketSoporteResponseDTO> listarTickets() {
        List<TicketSoporteResponseDTO> tickets = ticketSoporteRepository.findAll().stream()
                .map(this::mapearTicket)
                .toList();
        log.info("Listado de tickets. total={}", tickets.size());
        return tickets;
    }

    public TicketSoporteResponseDTO actualizarEstadoTicket(Long idTicket, EstadoTicket estado) {
        TicketSoporte ticket = obtenerTicketPorId(idTicket);
        EstadoTicket estadoAnterior = ticket.getEstado();
        ticket.setEstado(estado);
        ticket.setFechaActualizacion(LocalDateTime.now());

        log.info("Estado de ticket actualizado. idTicket={}, estadoAnterior={}, estadoNuevo={}", idTicket, estadoAnterior, estado);
        return mapearTicket(ticketSoporteRepository.save(ticket));
    }

    public RespuestaSoporteResponseDTO responderTicket(Long idTicket, RespuestaSoporteRequestDTO request) {
        TicketSoporte ticket = obtenerTicketPorId(idTicket);

        RespuestaSoporte respuesta = RespuestaSoporte.builder()
                .ticket(ticket)
                .mensaje(request.getMensaje().trim())
                .respondidoPor(request.getRespondidoPor().trim())
                .fechaRespuesta(LocalDateTime.now())
                .build();

        if (ticket.getEstado() == EstadoTicket.ABIERTO) {
            ticket.setEstado(EstadoTicket.EN_ATENCION);
            ticket.setFechaActualizacion(LocalDateTime.now());
            ticketSoporteRepository.save(ticket);
            log.info("Ticket pasado a EN_ATENCION automaticamente. idTicket={}", ticket.getIdTicket());
        }

        RespuestaSoporte respuestaGuardada = respuestaSoporteRepository.save(respuesta);
        log.info("Respuesta de ticket registrada. idRespuesta={}, idTicket={}", respuestaGuardada.getIdRespuesta(), ticket.getIdTicket());

        return mapearRespuesta(respuestaGuardada);
    }

    public List<RespuestaSoporteResponseDTO> listarRespuestasTicket(Long idTicket) {
        obtenerTicketPorId(idTicket);

        List<RespuestaSoporteResponseDTO> respuestas = respuestaSoporteRepository.findByTicketIdTicket(idTicket).stream()
                .map(this::mapearRespuesta)
                .toList();

        log.info("Respuestas listadas para ticket. idTicket={}, total={}", idTicket, respuestas.size());
        return respuestas;
    }

    // Metricas y alertas del sistema

    public MetricaSistemaResponseDTO registrarMetrica(MetricaSistemaRequestDTO request) {
        MetricaSistema metrica = MetricaSistema.builder()
                .microservicio(request.getMicroservicio().trim())
                .disponible(request.getDisponible())
                .tiempoRespuestaMs(request.getTiempoRespuestaMs())
                .erroresDetectados(request.getErroresDetectados())
                .fechaRegistro(LocalDateTime.now())
                .build();

        MetricaSistema metricaGuardada = metricaSistemaRepository.save(metrica);
        log.info("Metrica registrada. microservicio={}, disponible={}, errores={}",
                request.getMicroservicio(), request.getDisponible(), request.getErroresDetectados());

        if (Boolean.FALSE.equals(request.getDisponible()) || request.getErroresDetectados() > 0) {
            String tipoAlerta = Boolean.FALSE.equals(request.getDisponible())
                    ? "MICROSERVICIO_NO_DISPONIBLE"
                    : "ERRORES_DETECTADOS";

            AlertaSistema alerta = AlertaSistema.builder()
                    .microservicio(request.getMicroservicio().trim())
                    .tipoAlerta(tipoAlerta)
                    .descripcion("Se detecto una condicion de alerta en el microservicio " + request.getMicroservicio().trim())
                    .resuelta(false)
                    .fechaGeneracion(LocalDateTime.now())
                    .build();

            alertaSistemaRepository.save(alerta);
            log.warn("Alerta generada automaticamente. microservicio={}, tipo={}", request.getMicroservicio(), tipoAlerta);
        }

        return mapearMetrica(metricaGuardada);
    }

    public List<MetricaSistemaResponseDTO> listarMetricas() {
        List<MetricaSistemaResponseDTO> metricas = metricaSistemaRepository.findAll().stream()
                .map(this::mapearMetrica)
                .toList();
        log.info("Listado de metricas. total={}", metricas.size());
        return metricas;
    }

    public List<AlertaSistemaResponseDTO> listarAlertasActivas() {
        List<AlertaSistemaResponseDTO> alertas = alertaSistemaRepository.findByResueltaFalse().stream()
                .map(this::mapearAlerta)
                .toList();
        log.info("Listado de alertas activas. total={}", alertas.size());
        return alertas;
    }

    public AlertaSistemaResponseDTO registrarAlerta(AlertaSistemaRequestDTO request) {
        AlertaSistema alerta = AlertaSistema.builder()
                .microservicio(request.getMicroservicio().trim())
                .tipoAlerta(request.getTipoAlerta().trim())
                .descripcion(request.getDescripcion().trim())
                .resuelta(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        AlertaSistema alertaGuardada = alertaSistemaRepository.save(alerta);
        log.info("Alerta manual registrada. idAlerta={}, microservicio={}", alertaGuardada.getIdAlerta(), request.getMicroservicio());

        return mapearAlerta(alertaGuardada);
    }

    public AlertaSistemaResponseDTO resolverAlerta(Long idAlerta) {
        AlertaSistema alerta = alertaSistemaRepository.findById(idAlerta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alerta no encontrada con id: " + idAlerta));

        alerta.setResuelta(true);
        alerta.setFechaResolucion(LocalDateTime.now());

        log.info("Alerta resuelta. idAlerta={}", idAlerta);
        return mapearAlerta(alertaSistemaRepository.save(alerta));
    }

    // Respaldos

    public RespaldoDatosResponseDTO programarRespaldo(RespaldoDatosRequestDTO request) {
        RespaldoDatos respaldo = RespaldoDatos.builder()
                .origenDatos(request.getOrigenDatos().trim())
                .frecuencia(request.getFrecuencia().trim())
                .responsable(request.getResponsable().trim())
                .estado("PROGRAMADO")
                .resultado("Respaldo programado correctamente")
                .fechaProgramada(request.getFechaProgramada())
                .build();

        RespaldoDatos respaldoGuardado = respaldoDatosRepository.save(respaldo);
        log.info("Respaldo programado. idRespaldo={}, origen={}", respaldoGuardado.getIdRespaldo(), request.getOrigenDatos());

        return mapearRespaldo(respaldoGuardado);
    }

    public RespaldoDatosResponseDTO ejecutarRespaldo(Long idRespaldo) {
        RespaldoDatos respaldo = obtenerRespaldoPorId(idRespaldo);

        respaldo.setEstado("EJECUTADO");
        respaldo.setResultado("Respaldo ejecutado correctamente");
        respaldo.setFechaEjecucion(LocalDateTime.now());

        log.info("Respaldo ejecutado. idRespaldo={}", idRespaldo);
        return mapearRespaldo(respaldoDatosRepository.save(respaldo));
    }

    public RespaldoDatosResponseDTO restaurarRespaldo(Long idRespaldo) {
        RespaldoDatos respaldo = obtenerRespaldoPorId(idRespaldo);

        if (!"EJECUTADO".equalsIgnoreCase(respaldo.getEstado())) {
            log.warn("Intento de restaurar respaldo en estado incorrecto. idRespaldo={}, estado={}", idRespaldo, respaldo.getEstado());
            throw new IllegalArgumentException("Solo se puede restaurar un respaldo ejecutado");
        }

        respaldo.setEstado("RESTAURADO");
        respaldo.setResultado("Restauracion ejecutada correctamente");
        respaldo.setFechaRestauracion(LocalDateTime.now());

        log.info("Respaldo restaurado. idRespaldo={}", idRespaldo);
        return mapearRespaldo(respaldoDatosRepository.save(respaldo));
    }

    public List<RespaldoDatosResponseDTO> listarRespaldos() {
        List<RespaldoDatosResponseDTO> respaldos = respaldoDatosRepository.findAll().stream()
                .map(this::mapearRespaldo)
                .toList();
        log.info("Listado de respaldos. total={}", respaldos.size());
        return respaldos;
    }

    // Metodos privados de busqueda (lanzan 404)

    private Tienda obtenerTiendaPorId(Long idTienda) {
        return tiendaRepository.findById(idTienda)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tienda no encontrada con id: " + idTienda));
    }

    private TicketSoporte obtenerTicketPorId(Long idTicket) {
        return ticketSoporteRepository.findById(idTicket)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket de soporte no encontrado con id: " + idTicket));
    }

    private RespaldoDatos obtenerRespaldoPorId(Long idRespaldo) {
        return respaldoDatosRepository.findById(idRespaldo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Respaldo no encontrado con id: " + idRespaldo));
    }


    @Transactional
    public void eliminarTienda(Long idTienda) {
        log.info("Eliminando tienda. idTienda={}", idTienda);
        if (!tiendaRepository.existsById(idTienda)) {
            throw new RecursoNoEncontradoException("Tienda no encontrada con id: " + idTienda);
        }
        tiendaRepository.deleteById(idTienda);
        log.info("Tienda eliminada correctamente. idTienda={}", idTienda);
    }

        @Transactional
    public void eliminarTicket(Long idTicket) {
        log.info("Eliminando ticket. idTicket={}", idTicket);
        if (!ticketSoporteRepository.existsById(idTicket)) {
            throw new RecursoNoEncontradoException("Ticket no encontrado con id: " + idTicket);
        }
        ticketSoporteRepository.deleteById(idTicket);
        log.info("Ticket eliminado correctamente. idTicket={}", idTicket);
    }

    @Transactional
    public void eliminarRespaldo(Long idRespaldo) {
        log.info("Eliminando respaldo. idRespaldo={}", idRespaldo);
        RespaldoDatos respaldo = respaldoDatosRepository.findById(idRespaldo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Respaldo no encontrado con id: " + idRespaldo));
        respaldoDatosRepository.delete(respaldo);
        log.info("Respaldo eliminado correctamente. idRespaldo={}", idRespaldo);
    }

        private void validarHorarios(java.time.LocalTime apertura, java.time.LocalTime cierre) {
        if (!apertura.isBefore(cierre)) {
            throw new IllegalArgumentException("El horario de apertura debe ser anterior al horario de cierre");
        }
    }

    private String normalizarTextoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    // Mapeo model a DTO

    private TiendaResponseDTO mapearTienda(Tienda tienda) {
        return TiendaResponseDTO.builder()
                .idTienda(tienda.getIdTienda())
                .nombre(tienda.getNombre())
                .ciudad(tienda.getCiudad())
                .horarioApertura(tienda.getHorarioApertura())
                .horarioCierre(tienda.getHorarioCierre())
                .politicasLocales(tienda.getPoliticasLocales())
                .activa(tienda.getActiva())
                .fechaCreacion(tienda.getFechaCreacion())
                .fechaActualizacion(tienda.getFechaActualizacion())
                .build();
    }

    private AsignacionPersonalResponseDTO mapearAsignacion(AsignacionPersonal asignacion) {
        return AsignacionPersonalResponseDTO.builder()
                .idAsignacion(asignacion.getIdAsignacion())
                .idUsuarioInterno(asignacion.getIdUsuarioInterno())
                .idTienda(asignacion.getTienda().getIdTienda())
                .cargo(asignacion.getCargo())
                .activa(asignacion.getActiva())
                .fechaAsignacion(asignacion.getFechaAsignacion())
                .build();
    }

    private TicketSoporteResponseDTO mapearTicket(TicketSoporte ticket) {
        return TicketSoporteResponseDTO.builder()
                .idTicket(ticket.getIdTicket())
                .asunto(ticket.getAsunto())
                .descripcion(ticket.getDescripcion())
                .nombreContacto(ticket.getNombreContacto())
                .correoContacto(ticket.getCorreoContacto())
                .prioridad(ticket.getPrioridad())
                .estado(ticket.getEstado())
                .fechaCreacion(ticket.getFechaCreacion())
                .fechaActualizacion(ticket.getFechaActualizacion())
                .build();
    }

    private RespuestaSoporteResponseDTO mapearRespuesta(RespuestaSoporte respuesta) {
        return RespuestaSoporteResponseDTO.builder()
                .idRespuesta(respuesta.getIdRespuesta())
                .idTicket(respuesta.getTicket().getIdTicket())
                .mensaje(respuesta.getMensaje())
                .respondidoPor(respuesta.getRespondidoPor())
                .fechaRespuesta(respuesta.getFechaRespuesta())
                .build();
    }

    private MetricaSistemaResponseDTO mapearMetrica(MetricaSistema metrica) {
        return MetricaSistemaResponseDTO.builder()
                .idMetrica(metrica.getIdMetrica())
                .microservicio(metrica.getMicroservicio())
                .disponible(metrica.getDisponible())
                .tiempoRespuestaMs(metrica.getTiempoRespuestaMs())
                .erroresDetectados(metrica.getErroresDetectados())
                .fechaRegistro(metrica.getFechaRegistro())
                .build();
    }

    private AlertaSistemaResponseDTO mapearAlerta(AlertaSistema alerta) {
        return AlertaSistemaResponseDTO.builder()
                .idAlerta(alerta.getIdAlerta())
                .microservicio(alerta.getMicroservicio())
                .tipoAlerta(alerta.getTipoAlerta())
                .descripcion(alerta.getDescripcion())
                .resuelta(alerta.getResuelta())
                .fechaGeneracion(alerta.getFechaGeneracion())
                .fechaResolucion(alerta.getFechaResolucion())
                .build();
    }

    private RespaldoDatosResponseDTO mapearRespaldo(RespaldoDatos respaldo) {
        return RespaldoDatosResponseDTO.builder()
                .idRespaldo(respaldo.getIdRespaldo())
                .origenDatos(respaldo.getOrigenDatos())
                .frecuencia(respaldo.getFrecuencia())
                .responsable(respaldo.getResponsable())
                .estado(respaldo.getEstado())
                .resultado(respaldo.getResultado())
                .fechaProgramada(respaldo.getFechaProgramada())
                .fechaEjecucion(respaldo.getFechaEjecucion())
                .fechaRestauracion(respaldo.getFechaRestauracion())
                .build();
    }
}
