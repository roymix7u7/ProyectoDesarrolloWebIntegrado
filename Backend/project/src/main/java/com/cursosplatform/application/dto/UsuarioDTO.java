package com.cursosplatform.application.dto;

import com.cursosplatform.domain.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Integer idUsuario;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;

    private Usuario.TipoUsuario tipoUsuario;

    private Usuario.EstadoUsuario estado;

    private LocalDateTime fechaRegistro;

    private LocalDateTime ultimoAcceso;

    private String fotoPerfil;
}
