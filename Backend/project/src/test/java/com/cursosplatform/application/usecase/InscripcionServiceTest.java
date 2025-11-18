package com.cursosplatform.application.usecase;

import com.cursosplatform.application.dto.InscripcionDTO;
import com.cursosplatform.application.mapper.InscripcionMapper;
import com.cursosplatform.domain.exception.ResourceNotFoundException;
import com.cursosplatform.domain.model.Curso;
import com.cursosplatform.domain.model.Inscripcion;
import com.cursosplatform.domain.model.Usuario;
import com.cursosplatform.domain.repository.CursoRepository;
import com.cursosplatform.domain.repository.InscripcionRepository;
import com.cursosplatform.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas TDD para InscripcionService")
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private InscripcionMapper inscripcionMapper;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Inscripcion inscripcion;
    private InscripcionDTO inscripcionDTO;
    private Usuario usuario;
    private Curso curso;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .idUsuario(1)
                .nombreCompleto("María García")
                .email("maria@example.com")
                .tipoUsuario(Usuario.TipoUsuario.ESTUDIANTE)
                .build();

        curso = Curso.builder()
                .idCurso(1)
                .titulo("Java Avanzado")
                .precio(new BigDecimal("99.99"))
                .nivel(Curso.NivelCurso.AVANZADO)
                .estado(Curso.EstadoCurso.ACTIVO)
                .build();

        inscripcion = Inscripcion.builder()
                .idInscripcion(1)
                .usuario(usuario)
                .curso(curso)
                .progreso(0)
                .estadoInscripcion(Inscripcion.EstadoInscripcion.EN_PROGRESO)
                .build();

        inscripcionDTO = InscripcionDTO.builder()
                .idInscripcion(1)
                .idUsuario(1)
                .idCurso(1)
                .progreso(0)
                .estadoInscripcion(Inscripcion.EstadoInscripcion.EN_PROGRESO)
                .nombreUsuario("María García")
                .tituloCurso("Java Avanzado")
                .build();
    }

    @Test
    @DisplayName("Debe obtener todas las inscripciones")
    void obtenerTodas_DeberiaRetornarTodasLasInscripciones() {
        when(inscripcionRepository.findAll()).thenReturn(Arrays.asList(inscripcion));
        when(inscripcionMapper.toDTO(any(Inscripcion.class))).thenReturn(inscripcionDTO);

        List<InscripcionDTO> inscripciones = inscripcionService.obtenerTodas();

        assertNotNull(inscripciones);
        assertEquals(1, inscripciones.size());
        verify(inscripcionRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener inscripción por ID")
    void obtenerPorId_DeberiaRetornarInscripcion() {
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        when(inscripcionMapper.toDTO(inscripcion)).thenReturn(inscripcionDTO);

        InscripcionDTO resultado = inscripcionService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdInscripcion());
        assertEquals("María García", resultado.getNombreUsuario());
        verify(inscripcionRepository).findById(1);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando inscripción no existe")
    void obtenerPorId_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(inscripcionRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inscripcionService.obtenerPorId(999));
    }

    @Test
    @DisplayName("Debe obtener inscripciones por usuario")
    void obtenerPorUsuario_DeberiaRetornarInscripcionesDelUsuario() {
        when(inscripcionRepository.findByUsuarioIdUsuario(1)).thenReturn(Arrays.asList(inscripcion));
        when(inscripcionMapper.toDTO(inscripcion)).thenReturn(inscripcionDTO);

        List<InscripcionDTO> inscripciones = inscripcionService.obtenerPorUsuario(1);

        assertNotNull(inscripciones);
        assertEquals(1, inscripciones.size());
        assertEquals(1, inscripciones.get(0).getIdUsuario());
        verify(inscripcionRepository).findByUsuarioIdUsuario(1);
    }

    @Test
    @DisplayName("Debe crear inscripción exitosamente")
    void crear_DeberiaCrearInscripcionExitosamente() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        when(inscripcionMapper.toEntity(inscripcionDTO)).thenReturn(inscripcion);
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);
        when(inscripcionMapper.toDTO(inscripcion)).thenReturn(inscripcionDTO);

        InscripcionDTO resultado = inscripcionService.crear(inscripcionDTO);

        assertNotNull(resultado);
        verify(usuarioRepository).findById(1);
        verify(cursoRepository).findById(1);
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no existe al crear")
    void crear_DeberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inscripcionService.crear(inscripcionDTO));
        verify(usuarioRepository).findById(any());
        verify(cursoRepository, never()).findById(any());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando curso no existe al crear")
    void crear_DeberiaLanzarExcepcionCuandoCursoNoExiste() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(cursoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inscripcionService.crear(inscripcionDTO));
        verify(usuarioRepository).findById(1);
        verify(cursoRepository).findById(any());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar progreso de inscripción")
    void actualizarProgreso_DeberiaActualizarProgresoExitosamente() {
        when(inscripcionRepository.findById(1)).thenReturn(Optional.of(inscripcion));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        InscripcionDTO inscripcionActualizadaDTO = InscripcionDTO.builder()
                .idInscripcion(1)
                .progreso(75)
                .build();
        when(inscripcionMapper.toDTO(inscripcion)).thenReturn(inscripcionActualizadaDTO);

        InscripcionDTO resultado = inscripcionService.actualizarProgreso(1, 75);

        assertNotNull(resultado);
        verify(inscripcionRepository).findById(1);
        verify(inscripcionRepository).save(inscripcion);
        assertEquals(75, inscripcion.getProgreso());
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar progreso de inscripción inexistente")
    void actualizarProgreso_DeberiaLanzarExcepcionCuandoInscripcionNoExiste() {
        when(inscripcionRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inscripcionService.actualizarProgreso(999, 50));
    }

    @Test
    @DisplayName("Debe eliminar inscripción exitosamente")
    void eliminar_DeberiaEliminarInscripcionExitosamente() {
        when(inscripcionRepository.existsById(1)).thenReturn(true);

        inscripcionService.eliminar(1);

        verify(inscripcionRepository).deleteById(1);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar inscripción inexistente")
    void eliminar_DeberiaLanzarExcepcionCuandoInscripcionNoExiste() {
        when(inscripcionRepository.existsById(999)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> inscripcionService.eliminar(999));
        verify(inscripcionRepository, never()).deleteById(any());
    }
}
