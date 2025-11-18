package com.cursosplatform.application.dto;

import com.cursosplatform.domain.model.Compra;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {

    private Integer idCompra;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El ID del curso es obligatorio")
    private Integer idCurso;

    private LocalDateTime fechaCompra;

    @NotNull(message = "El monto total es obligatorio")
    private BigDecimal montoTotal;

    private Compra.EstadoCompra estadoCompra;

    private String metodoPago;

    private String comprobante;

    private BigDecimal descuento;

    private String nombreUsuario;

    private String tituloCurso;
}
