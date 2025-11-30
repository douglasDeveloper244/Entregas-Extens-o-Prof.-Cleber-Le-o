package com.deliverytech.delivery_api.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuração de Cache da aplicação
 * Utiliza ConcurrentMapCache para cache local em memória
 */
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

        /**
         * Configura o gerenciador de cache com caches pré-definidos
         * 
         * @return CacheManager configurado
         */
        @Bean
        @Override
        public CacheManager cacheManager() {
                ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
                // Define os nomes dos caches que serão utilizados
                cacheManager.setCacheNames(Arrays.asList("clientes", "restaurantes", "produtos", "pedidos"));
                // Permite criação dinâmica de novos caches se necessário
                cacheManager.setAllowNullValues(false);

                System.out.println(
                                "✅ Cache Manager inicializado com caches: clientes, restaurantes, produtos, pedidos");
                return cacheManager;
        }

        /**
         * Gerador de chaves customizado para o cache
         * Usa o nome do método + parâmetros como chave
         */
        @Bean
        @Override
        public KeyGenerator keyGenerator() {
                return (target, method, params) -> {
                        StringBuilder key = new StringBuilder();
                        key.append(target.getClass().getSimpleName());
                        key.append("_");
                        key.append(method.getName());
                        for (Object param : params) {
                                key.append("_");
                                key.append(param != null ? param.toString() : "null");
                        }
                        return key.toString();
                };
        }

        /**
         * Handler de erros de cache
         * Garante que erros de cache não quebrem a aplicação
         */
        @Bean
        @Override
        public CacheErrorHandler errorHandler() {
                return new SimpleCacheErrorHandler();
        }
}
