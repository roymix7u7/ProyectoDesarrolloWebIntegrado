package com.cursosplatform.application.usecase;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.application.mapper.CursoMapper;
import com.cursosplatform.domain.exception.ResourceNotFoundException;
import com.cursosplatform.domain.model.Curso;
import com.cursosplatform.domain.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    @Transactional(readOnly = true)
    public List<CursoDTO> obtenerTodos() {
        return cursoRepository.findAll().stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoDTO obtenerPorId(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", "id", id));
        return cursoMapper.toDTO(curso);
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> obtenerPorCategoria(String categoria) {
        return cursoRepository.findByCategoria(categoria).stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CursoDTO crear(CursoDTO cursoDTO) {
        Curso curso = cursoMapper.toEntity(cursoDTO);
        curso = cursoRepository.save(curso);
        return cursoMapper.toDTO(curso);
    }

    @Transactional
    public CursoDTO actualizar(Integer id, CursoDTO cursoDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso", "id", id));

        cursoMapper.updateEntityFromDTO(cursoDTO, curso);
        curso = cursoRepository.save(curso);
        return cursoMapper.toDTO(curso);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso", "id", id);
        }
        cursoRepository.deleteById(id);
    }
}
