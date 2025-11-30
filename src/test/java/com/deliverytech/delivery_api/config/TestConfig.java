package com.deliverytech.delivery_api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.modelmapper.ModelMapper;

@TestConfiguration
public class TestConfig {

        @Bean
        @Primary
        public ModelMapper modelMapper() {
                return new ModelMapper();
        }
}
