package com.indra.logistics.base.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.indra.logistics.base.UnknownPaymentMethodException;
import com.indra.logistics.base.strategy.PaymentStrategy;

@Component
public class PaymentStrategyFactoryImpl implements PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyFactoryImpl() {
        this(List.of(
                new com.indra.logistics.base.strategy.CashPayment(),
                new com.indra.logistics.base.strategy.VisaPayment(),
                new com.indra.logistics.base.strategy.PayPalPayment(),
                new com.indra.logistics.base.strategy.BankTransferPayment(),
                new com.indra.logistics.base.strategy.MastercardPayment(),
                new com.indra.logistics.base.strategy.DebitCardPayment(),
                new com.indra.logistics.base.strategy.AmexPayment()));
    }

    @Autowired
    public PaymentStrategyFactoryImpl(List<PaymentStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(PaymentStrategy::methodCode, Function.identity()));
    }

    @Override
    public PaymentStrategy getStrategy(String methodCode) {
        PaymentStrategy strategy = strategies.get(methodCode);
        if (strategy == null) {
            throw new UnknownPaymentMethodException(methodCode);
        }
        return strategy;
    }
}
