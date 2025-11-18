package com.cursosplatform.application.dto;

import com.cursosplatform.domain.model.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CursoDTO {

    private Integer idCurso;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String descripcion;

    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal precio;

    private Integer duracionHoras;

    private Curso.NivelCurso nivel;

    private Curso.ModalidadCurso modalidad;

    private Curso.EstadoCurso estado;

    private LocalDateTime fechaCreacion;

    private String imagenPortada;

    private String requisitosPrevios;
}
