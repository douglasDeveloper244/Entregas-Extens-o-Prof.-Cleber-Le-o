package com.deliverytech.delivery_api.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

        @InjectMocks
        private ClienteServiceImpl clienteService;

        @Mock
        private ClienteRepository clienteRepository;

        @Mock
        private ModelMapper modelMapper;

        private ClienteRequestDTO clienteRequestDTO;
        private Cliente cliente;
        private ClienteResponseDTO clienteResponseDTO;

        @BeforeEach
        void setUp() {
                clienteRequestDTO = new ClienteRequestDTO();
                clienteRequestDTO.setNome("João Silva");
                clienteRequestDTO.setEmail("joao@email.com");
                clienteRequestDTO.setTelefone("11999999999");
                clienteRequestDTO.setEndereco("Rua Teste, 123");

                cliente = new Cliente();
                cliente.setId(1L);
                cliente.setNome("João Silva");
                cliente.setEmail("joao@email.com");
                cliente.setAtivo(true);

                clienteResponseDTO = new ClienteResponseDTO();
                clienteResponseDTO.setId(1L);
                clienteResponseDTO.setNome("João Silva");
                clienteResponseDTO.setEmail("joao@email.com");
        }

        @Test
        void salvarCliente_ComDadosValidos_DeveRetornarClienteResponseDTO() {
                when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
                when(modelMapper.map(any(ClienteRequestDTO.class), eq(Cliente.class))).thenReturn(cliente);
                when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
                when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(clienteResponseDTO);

                ClienteResponseDTO result = clienteService.cadastrar(clienteRequestDTO);

                assertNotNull(result);
                assertEquals(clienteResponseDTO.getEmail(), result.getEmail());
                verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        void salvarCliente_ComEmailDuplicado_DeveLancarException() {
                when(clienteRepository.existsByEmail(anyString())).thenReturn(true);

                assertThrows(BusinessException.class, () -> clienteService.cadastrar(clienteRequestDTO));
                verify(clienteRepository, never()).save(any(Cliente.class));
        }

        @Test
        void buscarPorId_ComIdExistente_DeveRetornarCliente() {
                when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
                when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(clienteResponseDTO);

                ClienteResponseDTO result = clienteService.buscarPorId(1L);

                assertNotNull(result);
                assertEquals(1L, result.getId());
        }

        @Test
        void buscarPorId_ComIdInexistente_DeveLancarException() {
                when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

                assertThrows(BusinessException.class, () -> clienteService.buscarPorId(1L));
        }

        @Test
        void listarClientes_DeveRetornarListaDeClientesAtivos() {
                when(clienteRepository.findByAtivoTrue()).thenReturn(Arrays.asList(cliente));
                when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(clienteResponseDTO);

                List<ClienteResponseDTO> result = clienteService.listarAtivos();

                assertNotNull(result);
                assertFalse(result.isEmpty());
                assertEquals(1, result.size());
        }

        @Test
        void listarTodos_ComPaginacao_DeveRetornarPaginaDeClientes() {
                Pageable pageable = PageRequest.of(0, 10);
                Page<Cliente> pageCliente = new PageImpl<>(Arrays.asList(cliente));

                when(clienteRepository.findAll(pageable)).thenReturn(pageCliente);
                when(modelMapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(clienteResponseDTO);

                Page<ClienteResponseDTO> result = clienteService.listarTodos(pageable);

                assertNotNull(result);
                assertEquals(1, result.getTotalElements());
                assertEquals(1, result.getContent().size());
                verify(clienteRepository).findAll(pageable);
        }
}
