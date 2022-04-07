package com.loancalculator;

import com.loancalculator.Models.AmortizationSchedule;
import com.loancalculator.Models.Dto.LoanRequestDto;
import com.loancalculator.Models.Loan;
import com.loancalculator.Repository.AmortizationScheduleRepository;
import com.loancalculator.Repository.LoanRepository;
import com.loancalculator.Services.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class LoanServiceTests {

    @InjectMocks
    LoanService loanService;

    @Mock
    LoanRepository loanRepository;

    @Mock
    AmortizationScheduleRepository amortizationScheduleRepository;


    LoanService loanServiceSpy;

    double LoanAmount;
    double InterestRate;
    int LoanTerm;
    Loan loan = new Loan();
    List<AmortizationSchedule> amortizationScheduleList = new ArrayList<>();
    LoanRequestDto loanRequestDto = new LoanRequestDto();


    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(loanService);
        LoanAmount = 4.0;
        InterestRate = 4.0;
        LoanTerm = 15;

        AmortizationSchedule amortizationSchedule = new AmortizationSchedule();
        loan.setLoanAmount(45000d);
        loan.setLoanTerm(1);
        loan.setInterestRate(8.0);
        loan.setTotalInterest(300d);
        loan.setTotalPayments(45300d);
        loan.setIdLoan(1);

        amortizationSchedule.setIdLoan(1);
        amortizationSchedule.setBalanceOwed(0d);
        amortizationSchedule.setInterestAmount(300d);
        amortizationSchedule.setPrincipalAmount(45000d);
        amortizationSchedule.setPaymentAmount(45300d);
        amortizationSchedule.setAmortizationId(1);
        amortizationSchedule.setMonth(1);
        amortizationScheduleList.add(amortizationSchedule);

        loan.setScheduleList(amortizationScheduleList);

        loanRequestDto.setLoanTerm(1);
        loanRequestDto.setLoanAmount(45000d);
        loanRequestDto.setInterestRate(8d);

        loanServiceSpy = Mockito.spy(loanService);
    }

    @Test
    public void InjectedComponentsAreNotNull(){
        Assertions.assertThat(loanService).isNotNull();
        Assertions.assertThat(loanRepository).isNotNull();
    }

    @Test
    public void ShouldReturnValidValuesForAmount(){
        Boolean flag;
        flag = loanService.dataIsValid(LoanAmount, InterestRate, LoanTerm);
        org.junit.jupiter.api.Assertions.assertEquals(true, flag);
    }

    @Test
    public void ShouldReturnCalculatedLoans(){
        Mockito.doReturn(true).when(loanServiceSpy).dataIsValid(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt());
        Mockito.doReturn(loan).when(loanServiceSpy).saveLoan(loan);
        Mockito.doReturn(amortizationScheduleList).when(loanServiceSpy).saveAmortizationList(amortizationScheduleList);

        Loan loanFromTest = loanServiceSpy.calculateLoan(loanRequestDto);

        org.junit.jupiter.api.Assertions.assertEquals(loanFromTest.getLoanTerm(), loanFromTest.getScheduleList().size());
        org.junit.jupiter.api.Assertions.assertEquals(loan.getTotalInterest(), loanFromTest.getTotalInterest());
        org.junit.jupiter.api.Assertions.assertEquals(loan.getTotalPayments(), loanFromTest.getTotalPayments());
    }
}
