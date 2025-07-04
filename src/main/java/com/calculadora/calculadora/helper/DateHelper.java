package com.calculadora.calculadora.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class DateHelper {

    /**
     * Ajusta a data para o próximo dia útil.
     * @param date
     * @return
     */
    public LocalDate adjustToBusinessDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY) {
            return date.plusDays(2);
        } else if (day == DayOfWeek.SUNDAY) {
            return date.plusDays(1);
        }
        return date;
    }

    /**
     * Verifica se a data atual é um dia de pagamento.
     * @param currentDate
     * @param firstPayment
     * @return
     */
    public boolean isPaymentDay(LocalDate currentDate, LocalDate firstPayment) {
        int monthsSinceFirst = (int) ChronoUnit.MONTHS.between(
            firstPayment.withDayOfMonth(1),
            currentDate.withDayOfMonth(1)
        );

        LocalDate theoreticalPayment = firstPayment.plusMonths(monthsSinceFirst);

        LocalDate adjustedPayment = adjustToBusinessDay(theoreticalPayment);

        return currentDate.equals(adjustedPayment);
    }
}
