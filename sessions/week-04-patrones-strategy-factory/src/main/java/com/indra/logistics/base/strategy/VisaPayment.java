package com.indra.logistics.base.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class VisaPayment extends PercentagePaymentStrategy {

    public VisaPayment() {
        super("VISA", BigDecimal.valueOf(0.035),
                "Pago con tarjeta de crédito Visa procesado, se aplica comisión bancaria.", true);
    }
}