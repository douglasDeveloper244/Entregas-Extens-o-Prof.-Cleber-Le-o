package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClienteControllerIT {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                clienteRepository.deleteAll();
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void criarCliente_ComDadosValidos_DeveRetornar201() throws Exception {
                ClienteRequestDTO dto = new ClienteRequestDTO();
                dto.setNome("Maria Oliveira");
                dto.setEmail("maria@email.com");
                dto.setTelefone("11988888888");
                dto.setEndereco("Rua Nova, 456");

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.nome", is("Maria Oliveira")))
                                .andExpect(jsonPath("$.email", is("maria@email.com")));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void criarCliente_ComEmailDuplicado_DeveRetornar400() throws Exception {
                Cliente cliente = new Cliente();
                cliente.setNome("João Silva");
                cliente.setEmail("joao@email.com");
                cliente.setAtivo(true);
                clienteRepository.save(cliente);

                ClienteRequestDTO dto = new ClienteRequestDTO();
                dto.setNome("João Silva");
                dto.setEmail("joao@email.com"); // Email duplicado
                dto.setTelefone("11999999999");
                dto.setEndereco("Rua Teste, 123");

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void buscarPorId_ComIdExistente_DeveRetornar200() throws Exception {
                Cliente cliente = new Cliente();
                cliente.setNome("Carlos Souza");
                cliente.setEmail("carlos@email.com");
                cliente.setAtivo(true);
                cliente = clienteRepository.save(cliente);

                mockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(cliente.getId().intValue())))
                                .andExpect(jsonPath("$.nome", is("Carlos Souza")));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void buscarPorId_ComIdInexistente_DeveRetornar404() throws Exception {
                mockMvc.perform(get("/api/clientes/{id}", 999L))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void listarClientes_ComPaginacao_DeveRetornar200() throws Exception {
                Cliente c1 = new Cliente();
                c1.setNome("Ana");
                c1.setEmail("ana@email.com");
                c1.setAtivo(true);
                clienteRepository.save(c1);

                Cliente c2 = new Cliente();
                c2.setNome("Beto");
                c2.setEmail("beto@email.com");
                c2.setAtivo(true);
                clienteRepository.save(c2);

                mockMvc.perform(get("/api/clientes")
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(2)));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void atualizarCliente_ComDadosValidos_DeveRetornar200() throws Exception {
                Cliente cliente = new Cliente();
                cliente.setNome("Original");
                cliente.setEmail("original@email.com");
                cliente.setAtivo(true);
                cliente = clienteRepository.save(cliente);

                ClienteRequestDTO dto = new ClienteRequestDTO();
                dto.setNome("Atualizado");
                dto.setEmail("original@email.com");
                dto.setTelefone("11977777777");
                dto.setEndereco("Rua Atualizada, 789");

                mockMvc.perform(put("/api/clientes/{id}", cliente.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome", is("Atualizado")));
        }
}
