package com.indra.logistics.base.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class PayPalPayment extends PercentagePaymentStrategy {

    public PayPalPayment() {
        super("PAYPAL", BigDecimal.valueOf(0.02),
                "Pago con PayPal procesado, comisión de plataforma aplicada.", false);
    }
}