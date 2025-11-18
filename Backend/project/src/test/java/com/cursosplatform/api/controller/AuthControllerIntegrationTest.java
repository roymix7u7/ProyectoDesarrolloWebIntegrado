package com.cursosplatform.api.controller;

import com.cursosplatform.application.dto.LoginDTO;
import com.cursosplatform.application.dto.RegistroDTO;
import com.cursosplatform.domain.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registro_DeberiaCrearUsuarioYRetornarToken() throws Exception {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .nombreCompleto("Test Usuario")
                .email("test" + System.currentTimeMillis() + "@example.com")
                .password("password123")
                .tipoUsuario(Usuario.TipoUsuario.ESTUDIANTE)
                .build();

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.usuario.email").value(registroDTO.getEmail()));
    }

    @Test
    void registro_DeberiaFallarConEmailInvalido() throws Exception {
        RegistroDTO registroDTO = RegistroDTO.builder()
                .nombreCompleto("Test Usuario")
                .email("emailinvalido")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isBadRequest());
    }
}
