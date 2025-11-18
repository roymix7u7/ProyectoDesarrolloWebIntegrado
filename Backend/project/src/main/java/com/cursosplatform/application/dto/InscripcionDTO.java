package com.cursosplatform.application.dto;

import com.cursosplatform.domain.model.Inscripcion;
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
public class InscripcionDTO {

    private Integer idInscripcion;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El ID del curso es obligatorio")
    private Integer idCurso;

    private LocalDateTime fechaInscripcion;

    private Integer progreso;

    private Inscripcion.EstadoInscripcion estadoInscripcion;

    private LocalDateTime fechaCompletado;

    private String nombreUsuario;

    private String tituloCurso;
}
