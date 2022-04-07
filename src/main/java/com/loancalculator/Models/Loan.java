package com.loancalculator.Models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= "loan")
@Getter
@Setter
@NoArgsConstructor
public class Loan {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private Integer IdLoan;
    private Double LoanAmount;
    private Double InterestRate;
    private Integer LoanTerm;
    private Double TotalPayments;
    private Double TotalInterest;
    @Transient
    @OneToMany(mappedBy = "AmortizationSchedule", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name="IdLoan")
    private List<AmortizationSchedule> scheduleList;

}
