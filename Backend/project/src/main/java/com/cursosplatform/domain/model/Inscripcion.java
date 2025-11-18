package com.cursosplatform.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Integer idInscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDateTime fechaInscripcion;

    @Column(name = "progreso")
    private Integer progreso;

    @Column(name = "estado_inscripcion", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estadoInscripcion;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    @OneToOne(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Certificado certificado;

    @PrePersist
    protected void onCreate() {
        if (fechaInscripcion == null) {
            fechaInscripcion = LocalDateTime.now();
        }
        if (progreso == null) {
            progreso = 0;
        }
        if (estadoInscripcion == null) {
            estadoInscripcion = EstadoInscripcion.EN_PROGRESO;
        }
    }

    public enum EstadoInscripcion {
        EN_PROGRESO, COMPLETADO, CANCELADO
    }
}
