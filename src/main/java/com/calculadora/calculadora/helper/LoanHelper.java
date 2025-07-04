package com.calculadora.calculadora.helper;

import org.springframework.stereotype.Component;

import com.calculadora.calculadora.dto.LoanRequestDTO;
import com.calculadora.calculadora.model.Loan;

@Component
public class LoanHelper {
    /**
     * Constrói o objeto Loan a partir do LoanRequestDTO
     * @param loanRequest
     * @return Loan
     */
    public Loan buildLoanObject(LoanRequestDTO loanRequest) {
        Loan loan = new Loan();
        loan.setLoanStartDate(loanRequest.getLoanStartDate());
        loan.setLoanEndDate(loanRequest.getLoanEndDate());
        loan.setFirstPaymentDate(loanRequest.getFirstPaymentDate());
        loan.setPrincipalAmount(loanRequest.getPrincipalAmount());
        loan.setInterestRate(loanRequest.getInterestRate());
        
        return loan;
    }
}
