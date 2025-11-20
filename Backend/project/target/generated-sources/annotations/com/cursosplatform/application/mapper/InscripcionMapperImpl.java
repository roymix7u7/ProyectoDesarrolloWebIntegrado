package com.cursosplatform.application.mapper;

import com.cursosplatform.application.dto.InscripcionDTO;
import com.cursosplatform.domain.model.Curso;
import com.cursosplatform.domain.model.Inscripcion;
import com.cursosplatform.domain.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T19:25:56-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class InscripcionMapperImpl implements InscripcionMapper {

    @Override
    public InscripcionDTO toDTO(Inscripcion inscripcion) {
        if ( inscripcion == null ) {
            return null;
        }

        InscripcionDTO.InscripcionDTOBuilder inscripcionDTO = InscripcionDTO.builder();

        inscripcionDTO.idUsuario( inscripcionUsuarioIdUsuario( inscripcion ) );
        inscripcionDTO.idCurso( inscripcionCursoIdCurso( inscripcion ) );
        inscripcionDTO.nombreUsuario( inscripcionUsuarioNombreCompleto( inscripcion ) );
        inscripcionDTO.tituloCurso( inscripcionCursoTitulo( inscripcion ) );
        inscripcionDTO.estadoInscripcion( inscripcion.getEstadoInscripcion() );
        inscripcionDTO.fechaCompletado( inscripcion.getFechaCompletado() );
        inscripcionDTO.fechaInscripcion( inscripcion.getFechaInscripcion() );
        inscripcionDTO.idInscripcion( inscripcion.getIdInscripcion() );
        inscripcionDTO.progreso( inscripcion.getProgreso() );

        return inscripcionDTO.build();
    }

    @Override
    public Inscripcion toEntity(InscripcionDTO inscripcionDTO) {
        if ( inscripcionDTO == null ) {
            return null;
        }

        Inscripcion.InscripcionBuilder inscripcion = Inscripcion.builder();

        inscripcion.estadoInscripcion( inscripcionDTO.getEstadoInscripcion() );
        inscripcion.fechaCompletado( inscripcionDTO.getFechaCompletado() );
        inscripcion.fechaInscripcion( inscripcionDTO.getFechaInscripcion() );
        inscripcion.idInscripcion( inscripcionDTO.getIdInscripcion() );
        inscripcion.progreso( inscripcionDTO.getProgreso() );

        return inscripcion.build();
    }

    private Integer inscripcionUsuarioIdUsuario(Inscripcion inscripcion) {
        if ( inscripcion == null ) {
            return null;
        }
        Usuario usuario = inscripcion.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        Integer idUsuario = usuario.getIdUsuario();
        if ( idUsuario == null ) {
            return null;
        }
        return idUsuario;
    }

    private Integer inscripcionCursoIdCurso(Inscripcion inscripcion) {
        if ( inscripcion == null ) {
            return null;
        }
        Curso curso = inscripcion.getCurso();
        if ( curso == null ) {
            return null;
        }
        Integer idCurso = curso.getIdCurso();
        if ( idCurso == null ) {
            return null;
        }
        return idCurso;
    }

    private String inscripcionUsuarioNombreCompleto(Inscripcion inscripcion) {
        if ( inscripcion == null ) {
            return null;
        }
        Usuario usuario = inscripcion.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        String nombreCompleto = usuario.getNombreCompleto();
        if ( nombreCompleto == null ) {
            return null;
        }
        return nombreCompleto;
    }

    private String inscripcionCursoTitulo(Inscripcion inscripcion) {
        if ( inscripcion == null ) {
            return null;
        }
        Curso curso = inscripcion.getCurso();
        if ( curso == null ) {
            return null;
        }
        String titulo = curso.getTitulo();
        if ( titulo == null ) {
            return null;
        }
        return titulo;
    }
}
