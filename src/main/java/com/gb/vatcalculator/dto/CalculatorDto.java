package com.gb.vatcalculator.dto;

import java.math.BigDecimal;

public record CalculatorDto(BigDecimal net, BigDecimal vatAmount, BigDecimal gross, BigDecimal vatPercent) {
}
