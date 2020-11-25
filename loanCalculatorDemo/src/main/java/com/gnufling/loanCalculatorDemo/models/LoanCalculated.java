package com.gnufling.loanCalculatorDemo.models;

import java.math.BigDecimal;
import java.util.List;

public class LoanCalculated {
	
	private BigDecimal principalAmount;
	private BigDecimal oddFractionFirstTerm;
	
	private List<LoanPayment> repayments;
	private List<RepaymentPeriod> repaymentPeriods;
	
	public List<LoanPayment> getRepayments() {
		return repayments;
	}
	public void setRepayments(List<LoanPayment> repayments) {
		this.repayments = repayments;
	}
	
	public BigDecimal getOddFractionFirstTerm() {
		return oddFractionFirstTerm;
	}
	public void setOddFractionFirstTerm(BigDecimal oddFractionFirstTerm) {
		this.oddFractionFirstTerm = oddFractionFirstTerm;
	}

	@SuppressWarnings("unused")
	private BigDecimal halfPrincipalAmount;
	
	
	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}
	
//	public BigDecimal getHalfPrincipalAmount() {
//		return principalAmount.divide(BigDecimal.valueOf(2d));
//	}
	
	public LoanCalculated() {
	}
	
	public LoanCalculated(BigDecimal principalAmount) {
		super();
		this.principalAmount = principalAmount;
	}
	public List<RepaymentPeriod> getRepaymentPeriods() {
		return repaymentPeriods;
	}
	public void setRepaymentPeriods(List<RepaymentPeriod> repaymentPeriods) {
		this.repaymentPeriods = repaymentPeriods;
	}
	
//	public void setHalfPrincipalAmount(BigDecimal halfPrincipalAmount) {
//		this.halfPrincipalAmount = halfPrincipalAmount;
//	}

}
