package com.ecomarket.admin.repository;

import com.ecomarket.admin.model.RespuestaSoporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaSoporteRepository extends JpaRepository<RespuestaSoporte, Long> {

    List<RespuestaSoporte> findByTicketIdTicket(Long idTicket);
}
