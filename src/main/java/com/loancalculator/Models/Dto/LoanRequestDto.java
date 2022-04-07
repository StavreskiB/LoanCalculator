package com.loancalculator.Models.Dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoanRequestDto {

    private Double LoanAmount;
    private Double InterestRate;
    private Integer LoanTerm;
}
