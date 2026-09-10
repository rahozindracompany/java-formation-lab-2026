package com.indra.logistics.base.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class DebitCardPayment extends PercentagePaymentStrategy {

    public DebitCardPayment() {
        super("DEBIT_CARD", BigDecimal.valueOf(0.04),
                "Pago con tarjeta de débito procesado, se aplica comisión bancaria.", false);
    }
}