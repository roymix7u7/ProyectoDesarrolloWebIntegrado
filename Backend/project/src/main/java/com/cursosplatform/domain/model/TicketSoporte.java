package com.cursosplatform.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets_soporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "asunto", length = 200, nullable = false)
    private String asunto;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "prioridad", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PrioridadTicket prioridad;

    @Column(name = "estado", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoTicket estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoTicket.ABIERTO;
        }
        if (prioridad == null) {
            prioridad = PrioridadTicket.MEDIA;
        }
    }

    public enum PrioridadTicket {
        BAJA, MEDIA, ALTA, URGENTE
    }

    public enum EstadoTicket {
        ABIERTO, EN_PROCESO, RESUELTO, CERRADO
    }
}
