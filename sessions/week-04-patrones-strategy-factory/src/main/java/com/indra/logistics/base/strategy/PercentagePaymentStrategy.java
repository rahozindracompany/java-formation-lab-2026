package com.indra.logistics.base.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

abstract class PercentagePaymentStrategy implements PaymentStrategy {

    private static final BigDecimal COMMISSION_THRESHOLD = BigDecimal.valueOf(100);

    private final String code;
    private final BigDecimal rate;
    private final String message;
    private final boolean thresholdApplies;

    protected PercentagePaymentStrategy(String code, BigDecimal rate, String message, boolean thresholdApplies) {
        this.code = code;
        this.rate = rate;
        this.message = message;
        this.thresholdApplies = thresholdApplies;
    }

    @Override
    public String methodCode() {
        return code;
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        if (thresholdApplies && amount.compareTo(COMMISSION_THRESHOLD) < 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String confirmationMessage() {
        return message;
    }
}