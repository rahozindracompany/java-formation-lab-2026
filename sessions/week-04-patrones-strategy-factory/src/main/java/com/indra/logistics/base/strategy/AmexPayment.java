package com.indra.logistics.base.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class AmexPayment extends PercentagePaymentStrategy {

    public AmexPayment() {
        super("AMEX", BigDecimal.valueOf(0.03),
                "Pago con tarjeta American Express procesado, se aplica comisión bancaria.", true);
    }
}