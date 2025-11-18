package com.cursosplatform.api.controller;

import com.cursosplatform.application.dto.CursoDTO;
import com.cursosplatform.application.dto.LoginDTO;
import com.cursosplatform.application.dto.RegistroDTO;
import com.cursosplatform.domain.model.Curso;
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
@DisplayName("Pruebas de Integración para CursoController")
class CursoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenEstudiante;

    @BeforeEach
    void setUp() throws Exception {
        tokenAdmin = obtenerTokenAdmin();
        tokenEstudiante = obtenerTokenEstudiante();
    }

    private String obtenerTokenAdmin() throws Exception {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .nombreCompleto("Admin Test")
                .email("admin.test" + System.currentTimeMillis() + "@example.com")
                .password("admin123")
                .tipoUsuario(Usuario.TipoUsuario.ADMIN)
                .build();

        MvcResult result = mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    private String obtenerTokenEstudiante() throws Exception {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .nombreCompleto("Estudiante Test")
                .email("estudiante.test" + System.currentTimeMillis() + "@example.com")
                .password("estudiante123")
                .tipoUsuario(Usuario.TipoUsuario.ESTUDIANTE)
                .build();

        MvcResult result = mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    @DisplayName("Debe listar todos los cursos sin autenticación")
    void listarCursos_SinAutenticacion_DeberiaRetornarCursos() throws Exception {
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Debe obtener un curso por ID sin autenticación")
    void obtenerCursoPorId_SinAutenticacion_DeberiaRetornarCurso() throws Exception {
        CursoDTO cursoDTO = crearCursoParaPrueba();

        mockMvc.perform(get("/api/cursos/" + cursoDTO.getIdCurso()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value(cursoDTO.getTitulo()));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando curso no existe")
    void obtenerCursoPorId_CursoNoExiste_DeberiaRetornar404() throws Exception {
        mockMvc.perform(get("/api/cursos/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"));
    }

    @Test
    @DisplayName("Admin debe poder crear un curso")
    void crearCurso_ComoAdmin_DeberiaCrearExitosamente() throws Exception {
        CursoDTO cursoDTO = CursoDTO.builder()
                .titulo("Curso Test " + System.currentTimeMillis())
                .descripcion("Descripción del curso test")
                .categoria("Programación")
                .precio(new BigDecimal("99.99"))
                .duracionHoras(40)
                .nivel(Curso.NivelCurso.INTERMEDIO)
                .modalidad(Curso.ModalidadCurso.ONLINE)
                .estado(Curso.EstadoCurso.ACTIVO)
                .build();

        mockMvc.perform(post("/api/cursos")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value(cursoDTO.getTitulo()))
                .andExpect(jsonPath("$.precio").value(99.99));
    }

    @Test
    @DisplayName("Estudiante no debe poder crear un curso")
    void crearCurso_ComoEstudiante_DeberiaRetornar403() throws Exception {
        CursoDTO cursoDTO = CursoDTO.builder()
                .titulo("Curso Test")
                .descripcion("Descripción")
                .categoria("Programación")
                .precio(new BigDecimal("99.99"))
                .build();

        mockMvc.perform(post("/api/cursos")
                        .header("Authorization", "Bearer " + tokenEstudiante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Debe validar campos requeridos al crear curso")
    void crearCurso_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        CursoDTO cursoDTO = CursoDTO.builder().build();

        mockMvc.perform(post("/api/cursos")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error de validación"));
    }

    @Test
    @DisplayName("Admin debe poder actualizar un curso")
    void actualizarCurso_ComoAdmin_DeberiaActualizarExitosamente() throws Exception {
        CursoDTO cursoCreado = crearCursoParaPrueba();

        CursoDTO cursoActualizado = CursoDTO.builder()
                .titulo("Curso Actualizado")
                .descripcion("Descripción actualizada")
                .categoria("Programación")
                .precio(new BigDecimal("149.99"))
                .duracionHoras(50)
                .nivel(Curso.NivelCurso.AVANZADO)
                .modalidad(Curso.ModalidadCurso.ONLINE)
                .estado(Curso.EstadoCurso.ACTIVO)
                .build();

        mockMvc.perform(put("/api/cursos/" + cursoCreado.getIdCurso())
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Curso Actualizado"))
                .andExpect(jsonPath("$.precio").value(149.99));
    }

    @Test
    @DisplayName("Admin debe poder eliminar un curso")
    void eliminarCurso_ComoAdmin_DeberiaEliminarExitosamente() throws Exception {
        CursoDTO cursoCreado = crearCursoParaPrueba();

        mockMvc.perform(delete("/api/cursos/" + cursoCreado.getIdCurso())
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cursos/" + cursoCreado.getIdCurso()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe filtrar cursos por categoría")
    void filtrarCursosPorCategoria_DeberiaRetornarCursosFiltrados() throws Exception {
        CursoDTO cursoDTO = crearCursoParaPrueba();

        mockMvc.perform(get("/api/cursos/categoria/" + cursoDTO.getCategoria()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Debe retornar 401 al intentar crear curso sin token")
    void crearCurso_SinToken_DeberiaRetornar401() throws Exception {
        CursoDTO cursoDTO = CursoDTO.builder()
                .titulo("Curso Test")
                .precio(new BigDecimal("99.99"))
                .build();

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isUnauthorized());
    }

    private CursoDTO crearCursoParaPrueba() throws Exception {
        CursoDTO cursoDTO = CursoDTO.builder()
                .titulo("Curso Test " + System.currentTimeMillis())
                .descripcion("Descripción del curso test")
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
        return objectMapper.readValue(response, CursoDTO.class);
    }
}
