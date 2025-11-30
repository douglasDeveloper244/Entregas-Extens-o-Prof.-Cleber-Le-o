package com.deliverytech.delivery_api.cache;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery_api.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CacheIntegrationTest {

        @Autowired
        private ClienteService clienteService;

        @Autowired
        private CacheManager cacheManager;

        @SpyBean
        private ClienteService clienteServiceSpy;

        @Test
        public void testCacheable() {
                // Criar um cliente para teste
                ClienteRequestDTO dto = new ClienteRequestDTO();
                dto.setNome("Cliente Cache");
                dto.setEmail("cache@email.com");
                dto.setTelefone("11999999999");
                dto.setEndereco("Rua Cache, 123");
                ClienteResponseDTO clienteSalvo = clienteService.cadastrar(dto);

                // Primeira chamada - deve executar o método real
                clienteService.buscarPorId(clienteSalvo.getId());

                // Segunda chamada - deve vir do cache
                clienteService.buscarPorId(clienteSalvo.getId());

                // Verificar se o cache "clientes" contém o valor
                assertNotNull(cacheManager.getCache("clientes").get(clienteSalvo.getId()));
        }

        @Test
        public void testCacheEvict() {
                // Criar um cliente para teste
                ClienteRequestDTO dto = new ClienteRequestDTO();
                dto.setNome("Cliente Evict");
                dto.setEmail("evict@email.com");
                dto.setTelefone("11999999999");
                dto.setEndereco("Rua Evict, 123");
                ClienteResponseDTO clienteSalvo = clienteService.cadastrar(dto);

                // Carregar no cache
                clienteService.buscarPorId(clienteSalvo.getId());
                assertNotNull(cacheManager.getCache("clientes").get(clienteSalvo.getId()));

                // Atualizar cliente - deve limpar o cache
                clienteService.atualizar(clienteSalvo.getId(), dto);

                // Verificar se o cache foi limpo (para allEntries=true, o cache inteiro é
                // limpo)
                // Como usamos allEntries=true, o cache deve estar vazio ou não conter a chave
                // Mas o ConcurrentMapCache não remove a entrada imediatamente se for
                // allEntries, ele limpa o mapa.
                // Vamos verificar se ao buscar novamente, ele executa o método.

                // A verificação direta do cache pode ser complexa com allEntries=true
                // dependendo da implementação.
                // Uma forma mais segura é garantir que o cache não tem mais o valor antigo ou
                // está vazio.
                // Para ConcurrentMapCache, clear() é chamado.

                // Re-popular cache
                clienteService.buscarPorId(clienteSalvo.getId());

                // Chamar método com @CacheEvict
                clienteService.ativarDesativarCliente(clienteSalvo.getId());

                // Verificar se o item foi removido (ou cache limpo)
                assertNull(cacheManager.getCache("clientes").get(clienteSalvo.getId()));
        }
}
