package com.indra.logistics.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.indra.logistics.base.factory.PaymentStrategyFactoryImpl;
import com.indra.logistics.base.strategy.AmexPayment;
import com.indra.logistics.base.strategy.BankTransferPayment;
import com.indra.logistics.base.strategy.CashPayment;
import com.indra.logistics.base.strategy.DebitCardPayment;
import com.indra.logistics.base.strategy.MastercardPayment;
import com.indra.logistics.base.strategy.PayPalPayment;
import com.indra.logistics.base.strategy.PaymentStrategy;
import com.indra.logistics.base.strategy.VisaPayment;

class PaymentStrategyTest {

    @Test
    void cashPayment_hasNoFee() {
        assertFee(new CashPayment(), "0.00");
    }

    @Test
    void visaPayment_appliesThreePointFivePercentFee() {
        assertFee(new VisaPayment(), "7.00");
    }

    @Test
    void payPalPayment_appliesTwoPercentFee() {
        assertFee(new PayPalPayment(), "4.00");
    }

    @Test
    void bankTransferPayment_appliesTwoPointFivePercentFee() {
        assertFee(new BankTransferPayment(), "5.00");
    }

    @Test
    void mastercardPayment_appliesThreePercentFee() {
        assertFee(new MastercardPayment(), "6.00");
    }

    @Test
    void debitCardPayment_appliesFourPercentFee() {
        assertFee(new DebitCardPayment(), "8.00");
    }

    @Test
    void amexPayment_appliesThreePercentFee() {
        assertFee(new AmexPayment(), "6.00");
    }

    @Test
    void factory_throwsForUnknownPaymentMethod() {
        PaymentStrategyFactoryImpl factory = new PaymentStrategyFactoryImpl(List.of(new CashPayment()));

        assertThrows(UnknownPaymentMethodException.class, () -> factory.getStrategy("CRYPTO"));
    }

    private void assertFee(PaymentStrategy strategy, String expectedFee) {
        assertEquals(new BigDecimal(expectedFee), strategy.calculateFee(new BigDecimal("200.00")));
    }
}