package com.cursosplatform.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(name = "monto_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "estado_compra", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCompra estadoCompra;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "comprobante", length = 255)
    private String comprobante;

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pago> pagos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (fechaCompra == null) {
            fechaCompra = LocalDateTime.now();
        }
        if (descuento == null) {
            descuento = BigDecimal.ZERO;
        }
    }

    public enum EstadoCompra {
        PENDIENTE, COMPLETADA, CANCELADA, REEMBOLSADA
    }
}
