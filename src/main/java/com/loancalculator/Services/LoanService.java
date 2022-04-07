package com.loancalculator.Services;
import com.loancalculator.ErrorHandling.ErrorException;
import com.loancalculator.Models.AmortizationSchedule;
import com.loancalculator.Models.Dto.LoanRequestDto;
import com.loancalculator.Models.Loan;
import com.loancalculator.Repository.AmortizationScheduleRepository;
import com.loancalculator.Repository.LoanRepository;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {


    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AmortizationScheduleRepository amortizationScheduleRepository;


    public Loan saveLoan(Loan loan) { return loanRepository.saveAndFlush(loan); }

    public AmortizationSchedule saveAmortization(AmortizationSchedule amortizationSchedule) { return amortizationScheduleRepository.saveAndFlush(amortizationSchedule); }

    public List<AmortizationSchedule> saveAmortizationList(List<AmortizationSchedule> amortizationScheduleList) { return amortizationScheduleRepository.saveAll(amortizationScheduleList); }

    public List<Loan> getAll(){
        return loanRepository.findAll();
    }


    public Loan calculateLoan(LoanRequestDto loanRequestDto){
        Loan loan = new Loan();
        List<AmortizationSchedule> amortizationScheduleList = new ArrayList<>();
        AmortizationSchedule amortizationSchedule = new AmortizationSchedule();
        Boolean validFlag = false;
        try {

            validFlag = dataIsValid(loanRequestDto.getLoanAmount(), loanRequestDto.getInterestRate(), loanRequestDto.getLoanTerm());

            Double LoanAmount = loanRequestDto.getLoanAmount();
            Double InterestRate = loanRequestDto.getInterestRate();
            Integer LoanTerm = loanRequestDto.getLoanTerm();


            if(validFlag) {
                //percent for interest per month
                double interestPerMonth = InterestRate / 100 / 12;
                double pow = Math.pow(1 + interestPerMonth, LoanTerm);

                //calculate montly rate
                double montlyRate = ((LoanAmount * interestPerMonth * pow) / (pow - 1));
                double TotalWithInterest = montlyRate * LoanTerm;
                double Interest = TotalWithInterest - LoanAmount;
                loan.setLoanAmount(Precision.round(LoanAmount, 1));
                loan.setLoanTerm(LoanTerm);
                loan.setInterestRate(Precision.round(InterestRate, 1));
                loan.setTotalInterest(Precision.round(Interest, 1));
                loan.setTotalPayments(Precision.round(TotalWithInterest, 1));

                //loop for each month
                for (int i = 1; i <= LoanTerm; i++) {
                    amortizationSchedule = new AmortizationSchedule();

                    double InterestAmount = 0;
                    double principalAmount = 0;
                    InterestAmount = LoanAmount * InterestRate / 100 / 12;
                    LoanAmount = LoanAmount - (montlyRate - InterestAmount);
                    principalAmount = montlyRate - InterestAmount;

                    if (LoanAmount < 0)
                        LoanAmount = 0.00;

                    amortizationSchedule.setIdLoan(i);
                    amortizationSchedule.setBalanceOwed(Precision.round(LoanAmount,1));
                    amortizationSchedule.setInterestAmount(Precision.round(InterestAmount, 1));
                    amortizationSchedule.setPrincipalAmount(Precision.round(principalAmount, 1));
                    amortizationSchedule.setPaymentAmount(Precision.round(montlyRate, 1));
                    amortizationSchedule.setAmortizationId(loan.getIdLoan());
                    amortizationSchedule.setMonth(i);
                    amortizationScheduleList.add(amortizationSchedule);
                }

                loan.setScheduleList(amortizationScheduleList);
                saveLoan(loan);
                amortizationScheduleList.forEach(o -> o.setIdLoan(loan.getIdLoan()));
                saveAmortizationList(amortizationScheduleList);
            }
        } catch (NullPointerException e) {
            throw new ErrorException(HttpStatus.NOT_FOUND, e.getMessage(), loanRequestDto);
        }

        return loan;
    }

    public boolean dataIsValid(Double LoanAmount, Double InterestRate, Integer LoanTerm){
        if(LoanAmount > 0 && InterestRate > 0 && LoanTerm > 0)
            return true;
        else
            throw new ErrorException(HttpStatus.NOT_FOUND, "Loan Amount, Interest Rate and Loan Term must be >= 0!");
    }

}
