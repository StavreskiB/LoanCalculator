package com.loancalculator;

import com.loancalculator.Models.AmortizationSchedule;
import com.loancalculator.Models.Dto.LoanRequestDto;
import com.loancalculator.Models.Loan;
import com.loancalculator.Repository.AmortizationScheduleRepository;
import com.loancalculator.Repository.LoanRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoanRepositoryTests {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AmortizationScheduleRepository amortizationScheduleRepository;

    List<AmortizationSchedule> amortizationScheduleList = new ArrayList<>();
    Loan loan = new Loan();

    @Before
    public void setUp(){

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

    }

    @Test
    public void saveNewLoanTest(){
        Loan loanFromTest = new Loan();
        List<AmortizationSchedule> amortizationList = new ArrayList<>();
        List<Integer> listWithId = new ArrayList<>();
        loanFromTest = loanRepository.save(loan);
        amortizationList = amortizationScheduleRepository.saveAll(amortizationScheduleList);

        Assertions.assertThat(loanFromTest.getIdLoan()).isGreaterThan(0);
        org.junit.jupiter.api.Assertions.assertEquals(1, amortizationList.size());
        org.junit.jupiter.api.Assertions.assertEquals(45000d, loanFromTest.getLoanAmount());
        org.junit.jupiter.api.Assertions.assertEquals(loanFromTest.getLoanTerm(), amortizationList.size());
        org.junit.jupiter.api.Assertions.assertEquals(amortizationList.get(amortizationList.size()-1).getBalanceOwed(), 0);

        amortizationList.forEach(e -> listWithId.add(e.getAmortizationId()));

        amortizationScheduleRepository.deleteAllByIdInBatch(listWithId);
        loanRepository.deleteInBatch(Collections.singleton(loanFromTest));

    }
}
