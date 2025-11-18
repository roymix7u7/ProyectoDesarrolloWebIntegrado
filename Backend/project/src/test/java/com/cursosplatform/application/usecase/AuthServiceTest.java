package com.cursosplatform.application.usecase;

import com.cursosplatform.application.dto.AuthResponseDTO;
import com.cursosplatform.application.dto.LoginDTO;
import com.cursosplatform.application.dto.RegistroDTO;
import com.cursosplatform.application.mapper.UsuarioMapper;
import com.cursosplatform.domain.model.Usuario;
import com.cursosplatform.domain.repository.UsuarioRepository;
import com.cursosplatform.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private AuthService authService;

    private RegistroDTO registroDTO;
    private LoginDTO loginDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        registroDTO = RegistroDTO.builder()
                .nombreCompleto("Juan Pérez")
                .email("juan@example.com")
                .password("password123")
                .tipoUsuario(Usuario.TipoUsuario.ESTUDIANTE)
                .build();

        loginDTO = LoginDTO.builder()
                .email("juan@example.com")
                .password("password123")
                .build();

        usuario = Usuario.builder()
                .idUsuario(1)
                .nombreCompleto("Juan Pérez")
                .email("juan@example.com")
                .passwordHash("hashedPassword")
                .tipoUsuario(Usuario.TipoUsuario.ESTUDIANTE)
                .estado(Usuario.EstadoUsuario.ACTIVO)
                .build();
    }

    @Test
    void registro_DeberiaCrearUsuarioExitosamente() {
        when(usuarioRepository.existsByEmail(registroDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registroDTO.getPassword())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(userDetailsService.loadUserByUsername(registroDTO.getEmail()))
                .thenReturn(User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getPasswordHash())
                        .authorities(Collections.emptyList())
                        .build());
        when(jwtUtil.generateToken(any())).thenReturn("token123");

        AuthResponseDTO response = authService.registro(registroDTO);

        assertNotNull(response);
        assertEquals("Bearer", response.getTipo());
        assertEquals("token123", response.getToken());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registro_DeberiaLanzarExcepcionCuandoEmailYaExiste() {
        when(usuarioRepository.existsByEmail(registroDTO.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.registro(registroDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void login_DeberiaAutenticarUsuarioExitosamente() {
        when(usuarioRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(userDetailsService.loadUserByUsername(loginDTO.getEmail()))
                .thenReturn(User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getPasswordHash())
                        .authorities(Collections.emptyList())
                        .build());
        when(jwtUtil.generateToken(any())).thenReturn("token123");

        AuthResponseDTO response = authService.login(loginDTO);

        assertNotNull(response);
        assertEquals("Bearer", response.getTipo());
        assertEquals("token123", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
