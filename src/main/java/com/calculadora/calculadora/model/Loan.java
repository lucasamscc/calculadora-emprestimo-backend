package com.calculadora.calculadora.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Loan {
    private LocalDate loanStartDate;
    private LocalDate loanEndDate;
    private LocalDate firstPaymentDate;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
}
