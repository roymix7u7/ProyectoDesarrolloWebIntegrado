package com.cursosplatform.application.mapper;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.domain.model.Curso;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T19:25:56-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CursoMapperImpl implements CursoMapper {

    @Override
    public CursoDTO toDTO(Curso curso) {
        if ( curso == null ) {
            return null;
        }

        CursoDTO.CursoDTOBuilder cursoDTO = CursoDTO.builder();

        cursoDTO.categoria( curso.getCategoria() );
        cursoDTO.descripcion( curso.getDescripcion() );
        cursoDTO.duracionHoras( curso.getDuracionHoras() );
        cursoDTO.estado( curso.getEstado() );
        cursoDTO.fechaCreacion( curso.getFechaCreacion() );
        cursoDTO.idCurso( curso.getIdCurso() );
        cursoDTO.imagenPortada( curso.getImagenPortada() );
        cursoDTO.modalidad( curso.getModalidad() );
        cursoDTO.nivel( curso.getNivel() );
        cursoDTO.precio( curso.getPrecio() );
        cursoDTO.requisitosPrevios( curso.getRequisitosPrevios() );
        cursoDTO.titulo( curso.getTitulo() );

        return cursoDTO.build();
    }

    @Override
    public Curso toEntity(CursoDTO cursoDTO) {
        if ( cursoDTO == null ) {
            return null;
        }

        Curso.CursoBuilder curso = Curso.builder();

        curso.categoria( cursoDTO.getCategoria() );
        curso.descripcion( cursoDTO.getDescripcion() );
        curso.duracionHoras( cursoDTO.getDuracionHoras() );
        curso.estado( cursoDTO.getEstado() );
        curso.fechaCreacion( cursoDTO.getFechaCreacion() );
        curso.idCurso( cursoDTO.getIdCurso() );
        curso.imagenPortada( cursoDTO.getImagenPortada() );
        curso.modalidad( cursoDTO.getModalidad() );
        curso.nivel( cursoDTO.getNivel() );
        curso.precio( cursoDTO.getPrecio() );
        curso.requisitosPrevios( cursoDTO.getRequisitosPrevios() );
        curso.titulo( cursoDTO.getTitulo() );

        return curso.build();
    }

    @Override
    public void updateEntityFromDTO(CursoDTO cursoDTO, Curso curso) {
        if ( cursoDTO == null ) {
            return;
        }

        curso.setCategoria( cursoDTO.getCategoria() );
        curso.setDescripcion( cursoDTO.getDescripcion() );
        curso.setDuracionHoras( cursoDTO.getDuracionHoras() );
        curso.setEstado( cursoDTO.getEstado() );
        curso.setImagenPortada( cursoDTO.getImagenPortada() );
        curso.setModalidad( cursoDTO.getModalidad() );
        curso.setNivel( cursoDTO.getNivel() );
        curso.setPrecio( cursoDTO.getPrecio() );
        curso.setRequisitosPrevios( cursoDTO.getRequisitosPrevios() );
        curso.setTitulo( cursoDTO.getTitulo() );
    }
}
