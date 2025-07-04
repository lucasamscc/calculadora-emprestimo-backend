package com.calculadora.calculadora.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class LoanRequestDTO {
    private LocalDate loanStartDate;
    private LocalDate loanEndDate;
    private LocalDate firstPaymentDate;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
}
