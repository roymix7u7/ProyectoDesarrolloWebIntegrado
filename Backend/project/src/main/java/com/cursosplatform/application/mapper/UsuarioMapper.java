package com.cursosplatform.application.mapper;

import com.cursosplatform.application.dto.UsuarioDTO;
import com.cursosplatform.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "compras", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "inscripciones", ignore = true)
    @Mapping(target = "compras", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    void updateEntityFromDTO(UsuarioDTO usuarioDTO, @MappingTarget Usuario usuario);
}
