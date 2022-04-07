package com.loancalculator;

import com.loancalculator.Models.Dto.LoanRequestDto;
import com.loancalculator.Models.Loan;
import com.loancalculator.Services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    /**
     * @author Bojan Stavreski 04/04/2022
     * Loan and amortization schedule information is recorded in the database.
     * Loan details are returned with total calculations redistributed depending on the total number of months.
     *
     * @param  loanRequestDto  total loan, interest rate and loan term in month
     *     postman call example  {
     *                            "loanAmount": 45000,
     *                            "interestRate": 5,
     *                            "loanTerm": 15
     *                          }
     *
     * @return Loan details and amortization schedule
     *
     * @see <a href=”https://www.calculatorsoup.com/calculators/financial/amortization-schedule-calculator.php“>Calculator Example
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/saveNewLoan", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Loan Calculate(@RequestBody LoanRequestDto loanRequestDto) {

        return loanService.calculateLoan(loanRequestDto);
    }


}
