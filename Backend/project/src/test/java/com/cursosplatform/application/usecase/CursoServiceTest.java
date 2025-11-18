package com.cursosplatform.application.usecase;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.application.mapper.CursoMapper;
import com.cursosplatform.domain.exception.ResourceNotFoundException;
import com.cursosplatform.domain.model.Curso;
import com.cursosplatform.domain.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
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
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CursoMapper cursoMapper;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private CursoDTO cursoDTO;

    @BeforeEach
    void setUp() {
        curso = Curso.builder()
                .idCurso(1)
                .titulo("Java Avanzado")
                .descripcion("Curso completo de Java")
                .categoria("Programación")
                .precio(new BigDecimal("99.99"))
                .nivel(Curso.NivelCurso.AVANZADO)
                .estado(Curso.EstadoCurso.ACTIVO)
                .build();

        cursoDTO = CursoDTO.builder()
                .idCurso(1)
                .titulo("Java Avanzado")
                .descripcion("Curso completo de Java")
                .categoria("Programación")
                .precio(new BigDecimal("99.99"))
                .nivel(Curso.NivelCurso.AVANZADO)
                .estado(Curso.EstadoCurso.ACTIVO)
                .build();
    }

    @Test
    void obtenerTodos_DeberiaRetornarListaDeCursos() {
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso));
        when(cursoMapper.toDTO(any(Curso.class))).thenReturn(cursoDTO);

        List<CursoDTO> cursos = cursoService.obtenerTodos();

        assertNotNull(cursos);
        assertEquals(1, cursos.size());
        verify(cursoRepository).findAll();
    }

    @Test
    void obtenerPorId_DeberiaRetornarCurso() {
        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        when(cursoMapper.toDTO(curso)).thenReturn(cursoDTO);

        CursoDTO resultado = cursoService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Java Avanzado", resultado.getTitulo());
        verify(cursoRepository).findById(1);
    }

    @Test
    void obtenerPorId_DeberiaLanzarExcepcionCuandoNoExiste() {
        when(cursoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cursoService.obtenerPorId(999));
    }

    @Test
    void obtenerPorCategoria_DeberiaRetornarCursosPorCategoria() {
        when(cursoRepository.findByCategoria("Programación")).thenReturn(Arrays.asList(curso));
        when(cursoMapper.toDTO(curso)).thenReturn(cursoDTO);

        List<CursoDTO> cursos = cursoService.obtenerPorCategoria("Programación");

        assertNotNull(cursos);
        assertEquals(1, cursos.size());
        assertEquals("Programación", cursos.get(0).getCategoria());
        verify(cursoRepository).findByCategoria("Programación");
    }

    @Test
    void actualizar_DeberiaActualizarCursoExitosamente() {
        CursoDTO cursoActualizadoDTO = CursoDTO.builder()
                .titulo("Java Avanzado 2024")
                .precio(new BigDecimal("119.99"))
                .build();

        when(cursoRepository.findById(1)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);
        when(cursoMapper.toDTO(curso)).thenReturn(cursoActualizadoDTO);

        CursoDTO resultado = cursoService.actualizar(1, cursoActualizadoDTO);

        assertNotNull(resultado);
        verify(cursoRepository).findById(1);
        verify(cursoMapper).updateEntityFromDTO(cursoActualizadoDTO, curso);
        verify(cursoRepository).save(curso);
    }

    @Test
    void actualizar_DeberiaLanzarExcepcionCuandoCursoNoExiste() {
        when(cursoRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cursoService.actualizar(999, cursoDTO));
    }

    @Test
    void eliminar_DeberiaLanzarExcepcionCuandoCursoNoExiste() {
        when(cursoRepository.existsById(999)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cursoService.eliminar(999));
    }

    @Test
    void crear_DeberiaCrearCursoExitosamente() {
        when(cursoMapper.toEntity(cursoDTO)).thenReturn(curso);
        when(cursoRepository.save(curso)).thenReturn(curso);
        when(cursoMapper.toDTO(curso)).thenReturn(cursoDTO);

        CursoDTO resultado = cursoService.crear(cursoDTO);

        assertNotNull(resultado);
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void eliminar_DeberiaEliminarCursoExitosamente() {
        when(cursoRepository.existsById(1)).thenReturn(true);

        cursoService.eliminar(1);

        verify(cursoRepository).deleteById(1);
    }
}
