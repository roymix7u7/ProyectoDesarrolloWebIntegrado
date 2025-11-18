package com.cursosplatform.application.dto;

import com.cursosplatform.domain.model.TicketSoporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSoporteDTO {

    private Integer idTicket;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private TicketSoporte.PrioridadTicket prioridad;

    private TicketSoporte.EstadoTicket estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaResolucion;

    private String nombreUsuario;
}
