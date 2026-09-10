package com.indra.logistics.base.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class MastercardPayment extends PercentagePaymentStrategy {

    public MastercardPayment() {
        super("MASTERCARD", BigDecimal.valueOf(0.03),
                "Pago con tarjeta de crédito Mastercard procesado, se aplica comisión bancaria.", true);
    }
}