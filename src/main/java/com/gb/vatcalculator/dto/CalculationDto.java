package com.gb.vatcalculator.dto;

import java.math.BigDecimal;

public record CalculationDto(BigDecimal net, BigDecimal vatAmount, BigDecimal gross, BigDecimal vatPercent) {
}
