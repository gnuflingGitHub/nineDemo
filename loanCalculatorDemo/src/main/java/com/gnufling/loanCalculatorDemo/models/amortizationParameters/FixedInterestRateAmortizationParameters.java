package com.gnufling.loanCalculatorDemo.models.amortizationParameters;

import java.math.BigDecimal;

public class FixedInterestRateAmortizationParameters extends AmortizationParameters{
	
	private BigDecimal fixedInterestRateProAnno;

	
	
	public BigDecimal getFixedInterestRateProAnno() {
		return fixedInterestRateProAnno;
	}

	public void setFixedInterestRateProAnno(BigDecimal fixedInterestRateProAnno) {
		this.fixedInterestRateProAnno = fixedInterestRateProAnno;
	}
	

}
