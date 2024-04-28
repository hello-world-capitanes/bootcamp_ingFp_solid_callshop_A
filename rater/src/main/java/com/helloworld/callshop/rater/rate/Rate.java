package com.helloworld.callshop.rater.rate;

import java.math.BigDecimal;

/**
 * Interface que representa una tarifa. Todas las tarifas (modelos de tarifa)
 * implementan esta interface
 */
public interface Rate {
    BigDecimal calculatePrice(RateableCall call);

    String getName();
}
