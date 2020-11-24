package com.gnufling.loanCalculatorDemo.models.amortizationParameters;

import java.math.BigDecimal;

public class FixedInterestAndAmortRatesAmortizationParameters extends FixedInterestRateAmortizationParameters {
	
	private BigDecimal fixedAmortRateProAnno;

	public BigDecimal getFixedAmortRateProAnno() {
		return fixedAmortRateProAnno;
	}

	public void setFixedAmortRateProAnno(BigDecimal fixedAmortRateProAnno) {
		this.fixedAmortRateProAnno = fixedAmortRateProAnno;
	}

}
