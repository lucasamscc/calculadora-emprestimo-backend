# Calculadora de Empréstimos

Este projeto é uma API REST desenvolvida em Spring Boot para simulação de empréstimos, cálculo de parcelas, amortização e juros. O sistema permite que o usuário envie os dados do empréstimo e receba o detalhamento das parcelas, incluindo datas, valores pagos, saldo devedor, juros e amortização.

## Funcionalidades

- Simulação de empréstimos com cálculo detalhado das parcelas.
- Cálculo de datas de pagamento ajustadas para dias úteis.
- Cálculo de juros compostos e amortização.
- API REST pronta para integração com frontends ou outros sistemas.

## Estrutura do Projeto

- `controller/LoanController.java`: Endpoint principal para simulação de empréstimos.
- `service/LoanService.java`: Opera a simulação do empréstimo.
- `service/InstallmentCalculatorService.java`: Lógica de cálculo das parcelas.
- `service/InterestCalculatorService.java`: Lógica de cálculo de juros.
- `service/AccrualDateService.java`: Geração das datas de pagamento.
- `helper/DateHelper.java`: Helper com datas.
- `helper/LoanHelper.java`: Conversão de DTOs para entidades.
- `dto/`: Objetos de transferência de dados.
- `model/Loan.java`: Modelo de dados do empréstimo.

## Como usar

1. **Requisitos**: Java 17+, Maven, Spring Boot.
2. **Build**:  
   ```
   mvn clean install
   ```
3. **Executar**:  
   ```
   mvn spring-boot:run
   ```
4. **Simular empréstimo**:  
   Envie um POST para `/api/v1/loans/simulate` com um JSON contendo os dados do empréstimo (datas, valor, taxa de juros).

## Exemplo de Requisição

```json
POST /api/v1/loans/simulate
Content-Type: application/json

{
  "loanStartDate": "2024-01-01",
  "loanEndDate": "2026-01-01",
  "firstPaymentDate": "2034-01-01",
  "principalAmount": 140000.00,
  "interestRate": 0.07
}
```

## Exemplo de Resposta

```json
{
  [
    {
      "dueDate": "2025-08-01",
      "principalAmount": 10000.00,
      "outstandingBalance": 9000.00,
      "installmentNumber": "1/12",
      "installmentValue": 950.00,
      "amortization": 833.33,
      "balance": 9000.00,
      "provision": 166.67,
      "accruedInterest": 0.00,
      "interestPaid": 166.67,
      "amountPaid": 950.00
    },
    ...
  ]
}
```

Desenvolvido por Lucas.
