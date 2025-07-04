package com.calculadora.calculadora.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstallmentDetailDTO {
    // Emprestimo
    private LocalDate dueDate;                 
    private BigDecimal principalAmount;        
    private BigDecimal outstandingBalance;

    // Parcela
    private String installmentNumber;
    private BigDecimal installmentValue;    
    
    // Principal
    private BigDecimal amortization;
    private BigDecimal balance;

    // Juros
    private BigDecimal provision;
    private BigDecimal accruedInterest;
    private BigDecimal interestPaid;
}