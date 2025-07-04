package com.calculadora.calculadora.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Data;

@Data
public class LoanResponseDTO {
    private List<InstallmentDetailDTO> details;

    @JsonValue
    public List<InstallmentDetailDTO> getDetails() {
        return details;
    }
}
