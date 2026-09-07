package com.indra.logistics.base.factory;


import org.springframework.stereotype.Component;

import com.indra.logistics.base.strategy.PaymentStrategy;

/** Factory */
@Component
public class PaymentStrategyFactoryImpl implements PaymentStrategyFactory {

   
    @Override
    public PaymentStrategy getStrategy(String methodCode) { 
       //TODO: Implementar lógica de selección de estrategia según el código del método de pago
       return null;
    }
}
