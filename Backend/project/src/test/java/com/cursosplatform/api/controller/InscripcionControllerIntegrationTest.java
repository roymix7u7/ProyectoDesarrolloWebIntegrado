package com.cursosplatform.api.controller;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.application.dto.InscripcionDTO;
import com.cursosplatform.application.dto.RegistroDTO;
import com.cursosplatform.domain.model.Curso;
import com.cursosplatform.domain.model.Inscripcion;
import com.cursosplatform.domain.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Integración para InscripcionController")
class InscripcionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenEstudiante;
    private Integer idUsuarioEstudiante;
    private Integer idCursoPrueba;

    @BeforeEach
    void setUp() throws Exception {
        var adminData = crearUsuarioYObtenerToken(Usuario.TipoUsuario.ADMIN, "admin");
        tokenAdmin = adminData[0];

        var estudianteData = crearUsuarioYObtenerToken(Usuario.TipoUsuario.ESTUDIANTE, "estudiante");
        tokenEstudiante = estudianteData[0];
        idUsuarioEstudiante = Integer.parseInt(estudianteData[1]);

        idCursoPrueba = crearCursoPrueba();
    }

    private String[] crearUsuarioYObtenerToken(Usuario.TipoUsuario tipo, String prefix) throws Exception {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .nombreCompleto(prefix + " Test")
                .email(prefix + ".test" + System.currentTimeMillis() + "@example.com")
                .password(prefix + "123")
                .tipoUsuario(tipo)
                .build();

        MvcResult result = mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        String token = json.get("token").asText();
        String idUsuario = json.get("usuario").get("idUsuario").asText();

        return new String[]{token, idUsuario};
    }

    private Integer crearCursoPrueba() throws Exception {
        CursoDTO cursoDTO = CursoDTO.builder()
                .titulo("Curso Test " + System.currentTimeMillis())
                .descripcion("Curso para pruebas")
                .categoria("Programación")
                .precio(new BigDecimal("99.99"))
                .duracionHoras(40)
                .nivel(Curso.NivelCurso.INTERMEDIO)
                .modalidad(Curso.ModalidadCurso.ONLINE)
                .estado(Curso.EstadoCurso.ACTIVO)
                .build();

        MvcResult result = mockMvc.perform(post("/api/cursos")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("idCurso").asInt();
    }

    @Test
    @DisplayName("Estudiante debe poder crear una inscripción")
    void crearInscripcion_ComoEstudiante_DeberiaCrearExitosamente() throws Exception {
        InscripcionDTO inscripcionDTO = InscripcionDTO.builder()
                .idUsuario(idUsuarioEstudiante)
                .idCurso(idCursoPrueba)
                .estadoInscripcion(Inscripcion.EstadoInscripcion.EN_PROGRESO)
                .build();

        mockMvc.perform(post("/api/inscripciones")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(idUsuarioEstudiante))
                .andExpect(jsonPath("$.idCurso").value(idCursoPrueba))
                .andExpect(jsonPath("$.progreso").value(0))
                .andExpect(jsonPath("$.estadoInscripcion").value("EN_PROGRESO"));
    }

    @Test
    @DisplayName("Debe validar que el usuario existe al crear inscripción")
    void crearInscripcion_UsuarioNoExiste_DeberiaRetornar404() throws Exception {
        InscripcionDTO inscripcionDTO = InscripcionDTO.builder()
                .idUsuario(99999)
                .idCurso(idCursoPrueba)
                .build();

        mockMvc.perform(post("/api/inscripciones")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"));
    }

    @Test
    @DisplayName("Debe validar que el curso existe al crear inscripción")
    void crearInscripcion_CursoNoExiste_DeberiaRetornar404() throws Exception {
        InscripcionDTO inscripcionDTO = InscripcionDTO.builder()
                .idUsuario(idUsuarioEstudiante)
                .idCurso(99999)
                .build();

        mockMvc.perform(post("/api/inscripciones")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"));
    }

    @Test
    @DisplayName("Estudiante debe poder ver sus inscripciones")
    void obtenerInscripcionesPorUsuario_ComoEstudiante_DeberiaRetornarInscripciones() throws Exception {
        crearInscripcionPrueba();

        mockMvc.perform(get("/api/inscripciones/usuario/" + idUsuarioEstudiante)
                        .header("Authorization", "Bearer " + tokenEstudiante))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Debe poder actualizar el progreso de una inscripción")
    void actualizarProgreso_DeberiaActualizarExitosamente() throws Exception {
        Integer idInscripcion = crearInscripcionPrueba();

        mockMvc.perform(patch("/api/inscripciones/" + idInscripcion + "/progreso")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .param("progreso", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progreso").value(50));
    }

    @Test
    @DisplayName("Debe validar que la inscripción existe al actualizar progreso")
    void actualizarProgreso_InscripcionNoExiste_DeberiaRetornar404() throws Exception {
        mockMvc.perform(patch("/api/inscripciones/99999/progreso")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .param("progreso", "50"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Admin debe poder ver todas las inscripciones")
    void obtenerTodasInscripciones_ComoAdmin_DeberiaRetornarTodas() throws Exception {
        crearInscripcionPrueba();

        mockMvc.perform(get("/api/inscripciones")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Estudiante no debe poder ver todas las inscripciones")
    void obtenerTodasInscripciones_ComoEstudiante_DeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/api/inscripciones")
                        .header("Authorization", "Bearer " + tokenEstudiante))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin debe poder eliminar una inscripción")
    void eliminarInscripcion_ComoAdmin_DeberiaEliminarExitosamente() throws Exception {
        Integer idInscripcion = crearInscripcionPrueba();

        mockMvc.perform(delete("/api/inscripciones/" + idInscripcion)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/inscripciones/" + idInscripcion)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe retornar 401 al intentar crear inscripción sin token")
    void crearInscripcion_SinToken_DeberiaRetornar401() throws Exception {
        InscripcionDTO inscripcionDTO = InscripcionDTO.builder()
                .idUsuario(idUsuarioEstudiante)
                .idCurso(idCursoPrueba)
                .build();

        mockMvc.perform(post("/api/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debe obtener una inscripción por ID")
    void obtenerInscripcionPorId_DeberiaRetornarInscripcion() throws Exception {
        Integer idInscripcion = crearInscripcionPrueba();

        mockMvc.perform(get("/api/inscripciones/" + idInscripcion)
                        .header("Authorization", "Bearer " + tokenEstudiante))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInscripcion").value(idInscripcion))
                .andExpect(jsonPath("$.idUsuario").value(idUsuarioEstudiante))
                .andExpect(jsonPath("$.idCurso").value(idCursoPrueba));
    }

    private Integer crearInscripcionPrueba() throws Exception {
        InscripcionDTO inscripcionDTO = InscripcionDTO.builder()
                .idUsuario(idUsuarioEstudiante)
                .idCurso(idCursoPrueba)
                .estadoInscripcion(Inscripcion.EstadoInscripcion.EN_PROGRESO)
                .build();

        MvcResult result = mockMvc.perform(post("/api/inscripciones")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inscripcionDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("idInscripcion").asInt();
    }
}
