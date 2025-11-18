package com.cursosplatform.domain.repository;

import com.cursosplatform.domain.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Integer> {

    Optional<Certificado> findByCodigoCertificado(String codigoCertificado);

    Optional<Certificado> findByInscripcionIdInscripcion(Integer idInscripcion);
}
