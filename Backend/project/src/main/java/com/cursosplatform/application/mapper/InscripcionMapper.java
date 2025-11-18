package com.cursosplatform.application.mapper;

import com.cursosplatform.application.dto.InscripcionDTO;
import com.cursosplatform.domain.model.Inscripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "curso.idCurso", target = "idCurso")
    @Mapping(source = "usuario.nombreCompleto", target = "nombreUsuario")
    @Mapping(source = "curso.titulo", target = "tituloCurso")
    InscripcionDTO toDTO(Inscripcion inscripcion);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "certificado", ignore = true)
    Inscripcion toEntity(InscripcionDTO inscripcionDTO);
}
