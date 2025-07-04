package com.calculadora.calculadora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.calculadora.dto.InstallmentDetailDTO;
import com.calculadora.calculadora.dto.LoanRequestDTO;
import com.calculadora.calculadora.dto.LoanResponseDTO;
import com.calculadora.calculadora.helper.LoanHelper;
import com.calculadora.calculadora.model.Loan;

@Service
public class LoanService {

    @Autowired
    private LoanHelper loanHelper;

    @Autowired
    private InstallmentCalculatorService installmentCalculatorService;

    @Autowired
    private AccrualDateService accrualDateService;

    /**
     * Simula o empréstimo
     * @param loanRequest
     * @return LoanResponseDTO com os detalhes das parcelas
     */
    public LoanResponseDTO simulateLoan(LoanRequestDTO loanRequest) {
        Loan loan = loanHelper.buildLoanObject(loanRequest);

        LocalDate startDate = loan.getLoanStartDate();
        LocalDate firstPaymentDate = loan.getFirstPaymentDate();
        LocalDate endDate = loan.getLoanEndDate();

        List<LocalDate> accrualDates = accrualDateService.generateAccrualDates(
            startDate, 
            endDate, 
            firstPaymentDate
        );

        int numberOfInstallments = installmentCalculatorService.calculateNumberOfInstallments(firstPaymentDate, endDate);

        List<InstallmentDetailDTO> details = installmentCalculatorService.calculateInstallmentDetails(
            loan,
            accrualDates,
            numberOfInstallments
        );

        LoanResponseDTO response = new LoanResponseDTO();
        response.setDetails(details);
        return response;
    }
}
