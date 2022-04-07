package com.loancalculator.Models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name= "amortizationschedule")
@Getter
@Setter
@NoArgsConstructor
public class AmortizationSchedule {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Integer AmortizationId;
    private Integer Month;
    private Double PaymentAmount;
    private Double PrincipalAmount;
    private Double InterestAmount;
    private Double BalanceOwed;
    private Integer IdLoan;

}



