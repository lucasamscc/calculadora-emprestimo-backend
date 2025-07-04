package com.calculadora.calculadora.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.calculadora.dto.InstallmentDetailDTO;
import com.calculadora.calculadora.helper.DateHelper;
import com.calculadora.calculadora.model.Loan;

@Service
public class InstallmentCalculatorService {

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    @Autowired
    private InterestCalculatorService interestCalculator;

    @Autowired
    private DateHelper dateHelper;

    /**
     * Calcula o valor da parcela fixa do empréstimo.
     * @param loanValue
     * @param interestRate
     * @param numberOfInstallments
     * @return
     */
    public BigDecimal calculateInstallmentValue(BigDecimal loanValue, BigDecimal interestRate, int numberOfInstallments) {
        if (interestRate.compareTo(BigDecimal.ZERO) == 0) {
            return loanValue.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
        }

        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), MC);
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate, MC);
        BigDecimal factor = BigDecimal.ONE.divide(onePlusRate.pow(numberOfInstallments, MC), MC);
        BigDecimal denominator = BigDecimal.ONE.subtract(factor, MC);

        return loanValue.multiply(monthlyRate, MC)
                        .divide(denominator, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula os detalhes de cada parcela do empréstimo.
     * @param loan
     * @param accrualDates
     * @param numberOfInstallments
     * @param fixedInstallment
     * @return
     */
    public List<InstallmentDetailDTO> calculateInstallmentDetails(
            Loan loan,
            List<LocalDate> accrualDates,
            int numberOfInstallments
    ) {
        List<InstallmentDetailDTO> details = new ArrayList<>();
        BigDecimal loanValue = loan.getPrincipalAmount();
        BigDecimal amortization = loanValue.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
        BigDecimal balance = loanValue;
        BigDecimal interestRate = loan.getInterestRate();
        BigDecimal accumulatedInterest = BigDecimal.ZERO;
        LocalDate previousDate = loan.getLoanStartDate();
        int installmentCounter = 0;

        for (LocalDate currentDate : accrualDates) {

            if (balance.compareTo(BigDecimal.ZERO) <= 0 && !currentDate.equals(loan.getLoanStartDate())) {
                break;
            }

            long daysBetween = ChronoUnit.DAYS.between(previousDate, currentDate);
            BigDecimal provision = calculateProvision(balance, accumulatedInterest, interestRate, daysBetween);
            accumulatedInterest = accumulatedInterest.add(provision).setScale(2, RoundingMode.HALF_UP);

            boolean isPaymentDay = dateHelper.isPaymentDay(currentDate, loan.getFirstPaymentDate());

            BigDecimal interestPaid = BigDecimal.ZERO;
            BigDecimal principal = BigDecimal.ZERO;
            BigDecimal installment = BigDecimal.ZERO;

            if (isPaymentDay && installmentCounter >= numberOfInstallments) {
                continue;
            }

            if (isPaymentDay) {
                interestPaid = accumulatedInterest.setScale(2, RoundingMode.HALF_UP);
                principal = amortization.setScale(2, RoundingMode.HALF_UP);

                if (principal.compareTo(balance) > 0) {
                    principal = balance;
                }

                installment = principal.add(interestPaid);
                balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);
                accumulatedInterest = BigDecimal.ZERO;
                installmentCounter++;
            }

            BigDecimal outstandingBalance = isPaymentDay
                ? balance
                : balance.add(accumulatedInterest).setScale(2, RoundingMode.HALF_UP);

            String installmentNumber = isPaymentDay ? installmentCounter + "/" + numberOfInstallments : "";

            InstallmentDetailDTO dto = buildInstallmentDetailDTO(
                currentDate,
                balance,
                outstandingBalance,
                provision,
                interestPaid,
                principal,
                accumulatedInterest,
                installment,
                installmentNumber,
                loanValue,
                currentDate.equals(loan.getLoanStartDate())
            );

            details.add(dto);
            previousDate = currentDate;
        }

        return details;
    }

    /**
     * Calcula o número de parcelas entre a data do primeiro pagamento e a data final.
     * @param firstPaymentDate
     * @param endDate
     * @return
     */
    public int calculateNumberOfInstallments(LocalDate firstPaymentDate, LocalDate endDate) {
        return (int) ChronoUnit.MONTHS.between(
            firstPaymentDate.withDayOfMonth(1),
            endDate.withDayOfMonth(1)
        ) + 1;
    }

    /**
     * Calcula a provisão de juros para o período entre a data anterior e a data atual.
     * @param balance
     * @param accumulated
     * @param rate
     * @param days
     * @return
     */
    private BigDecimal calculateProvision(BigDecimal balance, BigDecimal accumulated, BigDecimal rate, long days) {
        if (days <= 0) return BigDecimal.ZERO;
        return interestCalculator
                .calculateInterest(balance, accumulated, rate, days)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Constrói um objeto InstallmentDetailDTO com os detalhes da parcela.
     * @param dueDate
     * @param balance
     * @param outstandingBalance
     * @param provision
     * @param interestPaid
     * @param amortization
     * @param accumulatedInterest
     * @param installmentValue
     * @param installmentNumber
     * @return
     */
    private InstallmentDetailDTO buildInstallmentDetailDTO(
        LocalDate dueDate,
        BigDecimal balance,
        BigDecimal outstandingBalance,
        BigDecimal provision,
        BigDecimal interestPaid,
        BigDecimal amortization,
        BigDecimal accumulatedInterest,
        BigDecimal installmentValue,
        String installmentNumber,
        BigDecimal principalAmount,
        boolean isFirstRow
    ) {
        return InstallmentDetailDTO.builder()
            .dueDate(dueDate)
            .balance(balance.setScale(2, RoundingMode.HALF_UP))
            .outstandingBalance(outstandingBalance)
            .provision(provision)
            .installmentValue(installmentValue)
            .amortization(amortization)
            .interestPaid(interestPaid)
            .accruedInterest(installmentValue.signum() > 0 ? BigDecimal.ZERO : accumulatedInterest)
            .installmentNumber(installmentNumber)
            .principalAmount(isFirstRow ? principalAmount : BigDecimal.ZERO)
            .build();
    }
}
