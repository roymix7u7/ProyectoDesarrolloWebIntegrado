package com.cursosplatform.application.mapper;

import com.cursosplatform.application.dto.UsuarioDTO;
import com.cursosplatform.domain.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T19:25:55-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO.UsuarioDTOBuilder usuarioDTO = UsuarioDTO.builder();

        usuarioDTO.email( usuario.getEmail() );
        usuarioDTO.estado( usuario.getEstado() );
        usuarioDTO.fechaRegistro( usuario.getFechaRegistro() );
        usuarioDTO.fotoPerfil( usuario.getFotoPerfil() );
        usuarioDTO.idUsuario( usuario.getIdUsuario() );
        usuarioDTO.nombreCompleto( usuario.getNombreCompleto() );
        usuarioDTO.telefono( usuario.getTelefono() );
        usuarioDTO.tipoUsuario( usuario.getTipoUsuario() );
        usuarioDTO.ultimoAcceso( usuario.getUltimoAcceso() );

        return usuarioDTO.build();
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario.UsuarioBuilder usuario = Usuario.builder();

        usuario.email( usuarioDTO.getEmail() );
        usuario.estado( usuarioDTO.getEstado() );
        usuario.fechaRegistro( usuarioDTO.getFechaRegistro() );
        usuario.fotoPerfil( usuarioDTO.getFotoPerfil() );
        usuario.idUsuario( usuarioDTO.getIdUsuario() );
        usuario.nombreCompleto( usuarioDTO.getNombreCompleto() );
        usuario.telefono( usuarioDTO.getTelefono() );
        usuario.tipoUsuario( usuarioDTO.getTipoUsuario() );
        usuario.ultimoAcceso( usuarioDTO.getUltimoAcceso() );

        return usuario.build();
    }

    @Override
    public void updateEntityFromDTO(UsuarioDTO usuarioDTO, Usuario usuario) {
        if ( usuarioDTO == null ) {
            return;
        }

        usuario.setEmail( usuarioDTO.getEmail() );
        usuario.setEstado( usuarioDTO.getEstado() );
        usuario.setFotoPerfil( usuarioDTO.getFotoPerfil() );
        usuario.setNombreCompleto( usuarioDTO.getNombreCompleto() );
        usuario.setTelefono( usuarioDTO.getTelefono() );
        usuario.setTipoUsuario( usuarioDTO.getTipoUsuario() );
        usuario.setUltimoAcceso( usuarioDTO.getUltimoAcceso() );
    }
}
