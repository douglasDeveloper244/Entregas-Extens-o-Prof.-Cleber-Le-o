package com.deliverytech.delivery_api.services;

import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

        public Usuario getUsuarioAutenticado() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                if (auth == null || !(auth.getPrincipal() instanceof Usuario usuario)) {
                        throw new BusinessException("Usuário não autenticado");
                }

                return usuario;
        }
}
