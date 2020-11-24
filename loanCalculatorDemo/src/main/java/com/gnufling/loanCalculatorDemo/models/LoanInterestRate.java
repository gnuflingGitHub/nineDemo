package com.gnufling.loanCalculatorDemo.models;

import java.math.BigDecimal;

public class LoanInterestRate implements Cloneable {
	
	private BigDecimal interestRatePerYear;
	//private int termNumber;
	
	public LoanInterestRate(BigDecimal interestRatePerYear){
		
		this.interestRatePerYear = interestRatePerYear;
		//this.termNumber = termNumber;		
	}
	
	public BigDecimal getInterestRatePerYear() {
		return interestRatePerYear;
	}
	public void setInterestRatePerYear(BigDecimal interestRatePerYear) {
		this.interestRatePerYear = interestRatePerYear;
	}
	
	/*
	public int getTermNumber() {
		return termNumber;
	}
	public void setTermNumber(int termNumber) {
		this.termNumber = termNumber;
	}
	*/

}
