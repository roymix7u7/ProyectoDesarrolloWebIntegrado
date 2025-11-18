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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final InscripcionMapper inscripcionMapper;

    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerTodas() {
        return inscripcionRepository.findAll().stream()
                .map(inscripcionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InscripcionDTO obtenerPorId(Integer id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción", "id", id));
        return inscripcionMapper.toDTO(inscripcion);
    }

    @Transactional(readOnly = true)
    public List<InscripcionDTO> obtenerPorUsuario(Integer idUsuario) {
        return inscripcionRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(inscripcionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InscripcionDTO crear(InscripcionDTO inscripcionDTO) {
        Usuario usuario = usuarioRepository.findById(inscripcionDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", inscripcionDTO.getIdUsuario()));

        Curso curso = cursoRepository.findById(inscripcionDTO.getIdCurso())
                .orElseThrow(() -> new ResourceNotFoundException("Curso", "id", inscripcionDTO.getIdCurso()));

        Inscripcion inscripcion = inscripcionMapper.toEntity(inscripcionDTO);
        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(curso);

        inscripcion = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.toDTO(inscripcion);
    }

    @Transactional
    public InscripcionDTO actualizarProgreso(Integer id, Integer progreso) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción", "id", id));

        inscripcion.setProgreso(progreso);
        inscripcion = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.toDTO(inscripcion);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscripción", "id", id);
        }
        inscripcionRepository.deleteById(id);
    }
}
