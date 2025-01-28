package com.maojie.evouchersystem.evouchermanagementsystem.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the payment mode which use for payment method discount
 * 
 */
public enum PaymentMethod {
    VISA(1),
    MASTERCARD(2),

    ;


    public final int paymentModeCode;

    private PaymentMethod(int paymentModeCode) {
        this.paymentModeCode = paymentModeCode;
    }

    private static final Map<Integer, PaymentMethod> BY_PAYMENTMODECODE = new HashMap<>();

    static {
        for (PaymentMethod e: values()) {
            BY_PAYMENTMODECODE.put(e.paymentModeCode, e);
        }
    }

    public static PaymentMethod valueOfPaymentModeCode(int paymentModeCode) {
        return BY_PAYMENTMODECODE.get(paymentModeCode);
    }

}
