package com.deliverytech.delivery_api.repository.projection;

import java.math.BigDecimal;

public interface VendaRestauranteProjection {
        String getRestaurante();

        BigDecimal getTotalVendas();
}
