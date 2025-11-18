package com.cursosplatform.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Integer idCertificado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscripcion", nullable = false, unique = true)
    private Inscripcion inscripcion;

    @Column(name = "codigo_certificado", length = 50, unique = true, nullable = false)
    private String codigoCertificado;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "url_pdf", length = 255)
    private String urlPdf;

    @Column(name = "verificable", nullable = false)
    private Boolean verificable;

    @Column(name = "calificacion", precision = 5, scale = 2)
    private BigDecimal calificacion;

    @PrePersist
    protected void onCreate() {
        if (fechaEmision == null) {
            fechaEmision = LocalDateTime.now();
        }
        if (verificable == null) {
            verificable = true;
        }
    }
}
