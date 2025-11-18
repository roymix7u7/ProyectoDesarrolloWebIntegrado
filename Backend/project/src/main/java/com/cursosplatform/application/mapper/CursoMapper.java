package com.cursosplatform.application.mapper;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.domain.model.Curso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CursoMapper {

    CursoDTO toDTO(Curso curso);

    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "compras", ignore = true)
    Curso toEntity(CursoDTO cursoDTO);

    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "compras", ignore = true)
    void updateEntityFromDTO(CursoDTO cursoDTO, @MappingTarget Curso curso);
}
