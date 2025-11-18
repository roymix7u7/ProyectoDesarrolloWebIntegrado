package com.cursosplatform.domain.repository;

import com.cursosplatform.domain.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {

    List<Inscripcion> findByUsuarioIdUsuario(Integer idUsuario);

    List<Inscripcion> findByCursoIdCurso(Integer idCurso);
}
