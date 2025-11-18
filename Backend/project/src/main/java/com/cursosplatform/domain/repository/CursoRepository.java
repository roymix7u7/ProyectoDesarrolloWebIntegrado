package com.cursosplatform.domain.repository;

import com.cursosplatform.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    List<Curso> findByCategoria(String categoria);

    List<Curso> findByEstado(Curso.EstadoCurso estado);
}
