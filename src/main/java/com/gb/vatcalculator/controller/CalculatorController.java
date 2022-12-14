package com.gb.vatcalculator.controller;

import com.gb.vatcalculator.dto.CalculationDto;
import com.gb.vatcalculator.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping
    public CalculationDto calculate(BigDecimal net, BigDecimal vatAmount, BigDecimal gross, BigDecimal vatPercent) {
        return calculatorService.calculate(net, vatAmount, gross, vatPercent);
    }
}
