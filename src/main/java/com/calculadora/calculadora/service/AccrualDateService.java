package com.calculadora.calculadora.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculadora.calculadora.helper.DateHelper;

@Service
public class AccrualDateService {

    @Autowired
    private DateHelper dateHelper;

    /**
     * Gera as datas do empréstimo
     * @param initialDate
     * @param finalDate
     * @param firstPayment
     * @return Lista com todas as datas do emréstimo
     */
    public List<LocalDate> generateAccrualDates(LocalDate initialDate, LocalDate finalDate, LocalDate firstPayment) {
        List<LocalDate> accrualDates = new ArrayList<>();
        accrualDates.add(initialDate);

        LocalDate currentDate = initialDate;
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        if (!lastDayOfMonth.equals(currentDate) && lastDayOfMonth.isBefore(firstPayment)) {
            accrualDates.add(dateHelper.adjustToBusinessDay(lastDayOfMonth));
        }

        if (firstPayment.isAfter(lastDayOfMonth) && !firstPayment.isAfter(finalDate)) {
            accrualDates.add(dateHelper.adjustToBusinessDay(firstPayment));
        } else if (firstPayment.isAfter(currentDate) && !firstPayment.isAfter(finalDate)) {
            accrualDates.add(dateHelper.adjustToBusinessDay(firstPayment));
        }

        LocalDate nextDate = firstPayment;
        boolean alternate = true;

        while (true) {
            if (alternate) {
                nextDate = nextDate.withDayOfMonth(nextDate.lengthOfMonth());
            } else {
                int day = firstPayment.getDayOfMonth();
                int lastDay = nextDate.plusMonths(1).lengthOfMonth();
                int currentDay = Math.min(day, lastDay);
                nextDate = nextDate.plusMonths(1).withDayOfMonth(currentDay);
            }

            if (nextDate.isAfter(finalDate)) break;
            accrualDates.add(dateHelper.adjustToBusinessDay(nextDate));
            alternate = !alternate;
        }

        LocalDate adjustedFinal = dateHelper.adjustToBusinessDay(finalDate);
        if (!accrualDates.contains(adjustedFinal)) {
            accrualDates.add(adjustedFinal);
        }

        return accrualDates;
    }
}