package com.cursosplatform.application.usecase;

import com.cursosplatform.application.dto.AuthResponseDTO;
import com.cursosplatform.application.dto.LoginDTO;
import com.cursosplatform.application.dto.RegistroDTO;
import com.cursosplatform.application.dto.UsuarioDTO;
import com.cursosplatform.application.mapper.UsuarioMapper;
import com.cursosplatform.domain.exception.DuplicateResourceException;
import com.cursosplatform.domain.exception.ResourceNotFoundException;
import com.cursosplatform.domain.model.Usuario;
import com.cursosplatform.domain.repository.UsuarioRepository;
import com.cursosplatform.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public AuthResponseDTO registro(RegistroDTO registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", registroDTO.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombreCompleto(registroDTO.getNombreCompleto())
                .email(registroDTO.getEmail())
                .passwordHash(passwordEncoder.encode(registroDTO.getPassword()))
                .telefono(registroDTO.getTelefono())
                .tipoUsuario(registroDTO.getTipoUsuario() != null ?
                        registroDTO.getTipoUsuario() : Usuario.TipoUsuario.ESTUDIANTE)
                .estado(Usuario.EstadoUsuario.ACTIVO)
                .fechaRegistro(LocalDateTime.now())
                .build();

        usuario = usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario);

        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(usuarioDTO)
                .build();
    }

    @Transactional
    public AuthResponseDTO login(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", loginDTO.getEmail()));

        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        UsuarioDTO usuarioDTO = usuarioMapper.toDTO(usuario);

        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(usuarioDTO)
                .build();
    }
}
