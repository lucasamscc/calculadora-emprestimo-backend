package com.calculadora.calculadora.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

@Service
public class InterestCalculatorService {

    private static final int BASE = 360;
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    /**
     * Calcula os juros acumulados de um saldo com base na taxa e no número de dias.
     * @param balance
     * @param accumulatedInterest
     * @param rate
     * @param days
     * @return
     */
    public BigDecimal calculateInterest(BigDecimal balance, BigDecimal accumulatedInterest, BigDecimal rate, long days) {
        double exponent = BigDecimal.valueOf(days)
            .divide(BigDecimal.valueOf(BASE), 10, RoundingMode.HALF_UP)
            .doubleValue();

        double growth = Math.pow(BigDecimal.ONE.add(rate, MC).doubleValue(), exponent);
        BigDecimal rateFactor = BigDecimal.valueOf(growth).subtract(BigDecimal.ONE, MC);

        return rateFactor.multiply(balance.add(accumulatedInterest), MC).setScale(2, RoundingMode.HALF_UP);
    }
}