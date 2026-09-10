package com.indra.logistics.base.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class BankTransferPayment extends PercentagePaymentStrategy {

    public BankTransferPayment() {
        super("BANK_TRANSFER", BigDecimal.valueOf(0.025),
                "Pago por transferencia bancaria registrado, comisión bancaria aplicada.", false);
    }
}