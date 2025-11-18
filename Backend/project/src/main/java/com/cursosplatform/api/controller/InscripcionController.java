package com.cursosplatform.api.controller;

import com.cursosplatform.application.dto.InscripcionDTO;
import com.cursosplatform.application.usecase.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InscripcionDTO>> obtenerTodas() {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerTodas();
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE', 'INSTRUCTOR')")
    public ResponseEntity<InscripcionDTO> obtenerPorId(@PathVariable Integer id) {
        InscripcionDTO inscripcion = inscripcionService.obtenerPorId(id);
        return ResponseEntity.ok(inscripcion);
    }

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<List<InscripcionDTO>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerPorUsuario(idUsuario);
        return ResponseEntity.ok(inscripciones);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<InscripcionDTO> crear(@Valid @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO nuevaInscripcion = inscripcionService.crear(inscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
    }

    @PatchMapping("/{id}/progreso")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
    public ResponseEntity<InscripcionDTO> actualizarProgreso(@PathVariable Integer id, @RequestParam Integer progreso) {
        InscripcionDTO inscripcionActualizada = inscripcionService.actualizarProgreso(id, progreso);
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
