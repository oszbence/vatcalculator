package com.gb.vatcalculator.service

import spock.lang.Specification

class CalculatorServiceSpec extends Specification {

    private final BigDecimal[] validVatPercentValues = [
            BigDecimal.valueOf(10L),
            BigDecimal.valueOf(13L),
            BigDecimal.valueOf(20L),
    ]
    private CalculatorService calculatorService

    def setup() {
        calculatorService = new CalculatorService(validVatPercentValues)
    }

    def "Calculate - Missing vatPercent"() {
        given:
        def net = BigDecimal.valueOf(100L)
        def vatAmount = null
        def gross = null
        def vatPercent = null

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Missing vatPercent"
    }

    def "Calculate - Invalid vatPercent"() {
        given:
        def net = BigDecimal.valueOf(100L)
        def vatAmount = null
        def gross = null
        def vatPercent = BigDecimal.valueOf(27L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid vatPercent"
    }

    def "Calculate - Invalid combination of input parameters"() {
        given:
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Invalid combination of input parameters"

        where:
        net                      | vatAmount               | gross
        null                     | null                    | null
        BigDecimal.valueOf(100L) | BigDecimal.valueOf(20L) | null
        BigDecimal.valueOf(100L) | null                    | BigDecimal.valueOf(120L)
        null                     | BigDecimal.valueOf(20L) | BigDecimal.valueOf(120L)
        BigDecimal.valueOf(100L) | BigDecimal.valueOf(20L) | BigDecimal.valueOf(120L)
    }

    def "calculateFromNet - Amount must be greater than 0"() {
        given:
        def net = BigDecimal.ZERO
        def vatAmount = null
        def gross = null
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Amount must be greater than 0"
    }

    def "calculateFromNet - Amount scale cannot be more than 2 digits"() {
        given:
        def net = BigDecimal.valueOf(1380L, 3)
        def vatAmount = null
        def gross = null
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Amount scale cannot be more than 2 digits"
    }

    def "calculateFromNet - valid calculations"() {
        when:
        def dto = calculatorService.calculate(net, null, null, vatPercent)

        then:
        vatAmount == dto.vatAmount()
        gross == dto.gross()

        where:
        net                           | vatAmount                    | gross                         | vatPercent
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(20L)      | BigDecimal.valueOf(120L)      | BigDecimal.valueOf(20L)
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(13L)      | BigDecimal.valueOf(113L)      | BigDecimal.valueOf(13L)
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(10L)      | BigDecimal.valueOf(110L)      | BigDecimal.valueOf(10L)
        BigDecimal.valueOf(77L)       | BigDecimal.valueOf(1001L, 2) | BigDecimal.valueOf(8701L, 2)  | BigDecimal.valueOf(13L)
        BigDecimal.valueOf(10619L, 2) | BigDecimal.valueOf(1380L, 2) | BigDecimal.valueOf(11999L, 2) | BigDecimal.valueOf(13L)
    }

    def "calculateFromVatAmount - Amount must be greater than 0"() {
        given:
        def net = null
        def vatAmount = BigDecimal.ZERO
        def gross = null
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Amount must be greater than 0"
    }

    def "calculateFromVatAmount - Amount scale cannot be more than 2 digits"() {
        given:
        def net = null
        def vatAmount = BigDecimal.valueOf(1380L, 3)
        def gross = null
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Amount scale cannot be more than 2 digits"
    }

    def "calculateFromVatAmount - valid calculations"() {
        when:
        def dto = calculatorService.calculate(null, vatAmount, null, vatPercent)

        then:
        net == dto.net()
        gross == dto.gross()

        where:
        net                           | vatAmount                    | gross                         | vatPercent
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(20L)      | BigDecimal.valueOf(120L)      | BigDecimal.valueOf(20L)
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(13L)      | BigDecimal.valueOf(113L)      | BigDecimal.valueOf(13L)
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(10L)      | BigDecimal.valueOf(110L)      | BigDecimal.valueOf(10L)
        BigDecimal.valueOf(77L)       | BigDecimal.valueOf(1001L, 2) | BigDecimal.valueOf(8701L, 2)  | BigDecimal.valueOf(13L)
        BigDecimal.valueOf(10615L, 2) | BigDecimal.valueOf(1380L, 2) | BigDecimal.valueOf(11995L, 2) | BigDecimal.valueOf(13L)
    }

    def "calculateFromGross - Amount must be greater than 0"() {
        given:
        def net = null
        def vatAmount = null
        def gross = BigDecimal.ZERO
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Amount must be greater than 0"
    }

    def "calculateFromGross - Amount scale cannot be more than 2 digits"() {
        given:
        def net = null
        def vatAmount = null
        def gross = BigDecimal.valueOf(1380L, 3)
        def vatPercent = BigDecimal.valueOf(20L)

        when:
        calculatorService.calculate(net, vatAmount, gross, vatPercent)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Amount scale cannot be more than 2 digits"
    }

    def "calculateFromGross - valid calculations"() {
        when:
        def dto = calculatorService.calculate(null, null, gross, vatPercent)

        then:
        net == dto.net()
        vatAmount == dto.vatAmount()

        where:
        net                           | vatAmount                    | gross                         | vatPercent
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(20L)      | BigDecimal.valueOf(120L)      | BigDecimal.valueOf(20L)
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(13L)      | BigDecimal.valueOf(113L)      | BigDecimal.valueOf(13L)
        BigDecimal.valueOf(100L)      | BigDecimal.valueOf(10L)      | BigDecimal.valueOf(110L)      | BigDecimal.valueOf(10L)
        BigDecimal.valueOf(77L)       | BigDecimal.valueOf(1001L, 2) | BigDecimal.valueOf(8701L, 2)  | BigDecimal.valueOf(13L)
        BigDecimal.valueOf(10619L, 2) | BigDecimal.valueOf(1380L, 2) | BigDecimal.valueOf(11999L, 2) | BigDecimal.valueOf(13L)
    }
}
