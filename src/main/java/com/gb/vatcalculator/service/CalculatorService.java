package com.gb.vatcalculator.service;

import com.gb.vatcalculator.dto.CalculationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Service
public class CalculatorService {

    private final BigDecimal[] validVatPercentValues;

    public CalculatorService(@Value("${validVatPercentValues}") BigDecimal[] validVatPercentValues) {
        this.validVatPercentValues = validVatPercentValues;
    }

    private void checkVatPercent(BigDecimal vatPercent) {
        if (vatPercent == null) {
            throw new IllegalArgumentException("Missing vatPercent");
        }
        if (Arrays.stream(validVatPercentValues).noneMatch(it -> it.compareTo(vatPercent) == 0)) {
            throw new IllegalArgumentException("Invalid vatPercent");
        }
    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount scale cannot be more than 2 digits");
        }
    }

    private CalculationDto calculateFromGross(BigDecimal gross, BigDecimal vatPercent) {
        checkAmount(gross);
        final var net = gross.divide(vatPercent.scaleByPowerOfTen(-2).add(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        final var vatAmount = gross.subtract(net);
        return new CalculationDto(net, vatAmount, gross, vatPercent);
    }

    private CalculationDto calculateFromNet(BigDecimal net, BigDecimal vatPercent) {
        checkAmount(net);
        final var gross = net.multiply(vatPercent.scaleByPowerOfTen(-2).add(BigDecimal.ONE))
                .setScale(2, RoundingMode.HALF_UP);
        final var vatAmount = gross.subtract(net);
        return new CalculationDto(net, vatAmount, gross, vatPercent);
    }

    private CalculationDto calculateFromVatAmount(BigDecimal vatAmount, BigDecimal vatPercent) {
        checkAmount(vatAmount);
        final var net = vatAmount.divide(vatPercent.scaleByPowerOfTen(-2), 2, RoundingMode.HALF_UP);
        final var gross = net.add(vatAmount);
        return new CalculationDto(net, vatAmount, gross, vatPercent);
    }

    public CalculationDto calculate(BigDecimal net, BigDecimal vatAmount, BigDecimal gross, BigDecimal vatPercent) {
        checkVatPercent(vatPercent);
        if (net != null && vatAmount == null && gross == null) {
            return calculateFromNet(net, vatPercent);
        }
        if (net == null && vatAmount != null && gross == null) {
            return calculateFromVatAmount(vatAmount, vatPercent);
        }
        if (net == null && vatAmount == null && gross != null) {
            return calculateFromGross(gross, vatPercent);
        }
        throw new IllegalArgumentException("Invalid combination of input parameters");
    }
}
