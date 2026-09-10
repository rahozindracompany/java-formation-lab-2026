package com.indra.logistics.base;

import java.math.BigDecimal;
import com.indra.logistics.base.factory.PaymentStrategyFactory;
import com.indra.logistics.base.factory.PaymentStrategyFactoryImpl;
import com.indra.logistics.base.strategy.PaymentStrategy;

/**
 * BASE: toda la lógica de comisión vive en un if-else que crece con cada método de pago nuevo.
 * Agregar un método de pago implica editar esta clase y arriesgar los demás casos.
 */
public class PaymentService {

    private final PaymentStrategyFactory strategyFactory;

    public PaymentService() {
        this(new PaymentStrategyFactoryImpl());
    }

    public PaymentService(PaymentStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public PaymentResult process(PaymentRequest request) {
        BigDecimal amount = request.amount();
        String method = request.method();

        PaymentStrategy strategy = strategyFactory.getStrategy(method);
        BigDecimal fee = strategy.calculateFee(amount);
        BigDecimal total = amount.add(fee);
        return new PaymentResult(method, amount, fee, total, strategy.confirmationMessage());
    }
}
