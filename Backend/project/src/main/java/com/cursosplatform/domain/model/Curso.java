package com.cursosplatform.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "titulo", length = 200, nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "precio", precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @Column(name = "duracion_horas")
    private Integer duracionHoras;

    @Column(name = "nivel", length = 50)
    @Enumerated(EnumType.STRING)
    private NivelCurso nivel;

    @Column(name = "modalidad", length = 50)
    @Enumerated(EnumType.STRING)
    private ModalidadCurso modalidad;

    @Column(name = "estado", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCurso estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "imagen_portada", length = 255)
    private String imagenPortada;

    @Column(name = "requisitos_previos", columnDefinition = "TEXT")
    private String requisitosPrevios;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Inscripcion> inscripciones = new HashSet<>();

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Compra> compras = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoCurso.ACTIVO;
        }
    }

    public enum NivelCurso {
        BASICO, INTERMEDIO, AVANZADO
    }

    public enum ModalidadCurso {
        ONLINE, PRESENCIAL, HIBRIDO
    }

    public enum EstadoCurso {
        ACTIVO, INACTIVO, BORRADOR
    }
}
