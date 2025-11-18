package com.cursosplatform.api.controller;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.application.usecase.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> obtenerTodos() {
        List<CursoDTO> cursos = cursoService.obtenerTodos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerPorId(@PathVariable Integer id) {
        CursoDTO curso = cursoService.obtenerPorId(id);
        return ResponseEntity.ok(curso);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<CursoDTO>> obtenerPorCategoria(@PathVariable String categoria) {
        List<CursoDTO> cursos = cursoService.obtenerPorCategoria(categoria);
        return ResponseEntity.ok(cursos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CursoDTO> crear(@Valid @RequestBody CursoDTO cursoDTO) {
        CursoDTO nuevoCurso = cursoService.crear(cursoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCurso);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CursoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CursoDTO cursoDTO) {
        CursoDTO cursoActualizado = cursoService.actualizar(id, cursoDTO);
        return ResponseEntity.ok(cursoActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
