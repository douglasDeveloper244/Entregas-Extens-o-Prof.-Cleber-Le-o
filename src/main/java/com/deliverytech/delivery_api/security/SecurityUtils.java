package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Usuario usuario)) {
            return null;
        }

        return usuario;
    }

    public static boolean isAdmin() {
        Usuario u = getCurrentUser();
        return u != null && u.getRole() == Role.ADMIN;
    }

    public static boolean isCliente() {
        Usuario u = getCurrentUser();
        return u != null && u.getRole() == Role.CLIENTE;
    }

    public static boolean isRestaurante() {
        Usuario u = getCurrentUser();
        return u != null && u.getRole() == Role.RESTAURANTE;
    }

    public static Long getCurrentRestauranteId() {
        Usuario u = getCurrentUser();
        return u != null ? u.getRestauranteId() : null;
    }
}
