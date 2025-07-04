package com.calculadora.calculadora.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.calculadora.dto.LoanRequestDTO;
import com.calculadora.calculadora.dto.LoanResponseDTO;
import com.calculadora.calculadora.service.LoanService;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    @Autowired
    private LoanService loanNewService;

    @PostMapping("/simulate")
    public ResponseEntity<LoanResponseDTO> simulateLoan(@RequestBody LoanRequestDTO loanRequest) {
        LoanResponseDTO details = loanNewService.simulateLoan(loanRequest);
        return ResponseEntity.ok(details);
    }
}
