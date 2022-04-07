package com.loancalculator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.text.DecimalFormat;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class LoanCalculatorApplication {
    public static void main(String[] args) { SpringApplication.run(LoanCalculatorApplication.class, args); }
}
