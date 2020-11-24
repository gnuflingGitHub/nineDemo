package com.gnufling.loanCalculatorDemo.models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LoanPayment {
	
	private int n;
	// Formats output date when this DTO is passed through JSON
	@JsonFormat(pattern = "dd/MM/yy")
	// Allows dd/MM/yyyy date to be passed into GET request in JSON
	@DateTimeFormat(pattern = "dd/MM/yy")
	private Date settlementDate; 
	private BigDecimal bondDebtBeginningOfPeriod;
	private BigDecimal bondInterestAmountCalculated;
	private BigDecimal bondPrincipalPaymentCalculated;
	private BigDecimal bondDebtEndOfPeriod;
	private BigDecimal loanInterestAmountCalculated;
	private BigDecimal loanPayment;
	private BigDecimal SpreadMci;
	private BigDecimal SpreadMsp;
	private BigDecimal totalPayment;
	
	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public BigDecimal getBondDebtBeginningOfPeriod() {
		return bondDebtBeginningOfPeriod;
	}

	public void setBondDebtBeginningOfPeriod(BigDecimal principalBondAmount) {
		this.bondDebtBeginningOfPeriod = principalBondAmount;
	}

	public BigDecimal getBondInterestAmountCalculated() {
		return bondInterestAmountCalculated;
	}

	public void setBondInterestAmountCalculated(BigDecimal interestAmountCalculated) {
		this.bondInterestAmountCalculated = interestAmountCalculated;
	}

	public void setTotalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
	}

	public BigDecimal getTotalPayment() {
		return totalPayment;
	}

	public BigDecimal getBondDebtEndOfPeriod() {
		return bondDebtEndOfPeriod;
	}

	public void setBondDebtEndOfPeriod(BigDecimal bondDebtEndOfPeriod) {
		this.bondDebtEndOfPeriod = bondDebtEndOfPeriod;
	}

	public BigDecimal getLoanInterestAmountCalculated() {
		return loanInterestAmountCalculated;
	}

	public void setLoanInterestAmountCalculated(BigDecimal loanInterestAmountCalculated) {
		this.loanInterestAmountCalculated = loanInterestAmountCalculated;
	}

	public BigDecimal getBondPrincipalPaymentCalculated() {
		return bondPrincipalPaymentCalculated;
	}

	public void setBondPrincipalPaymentCalculated(BigDecimal bondPrincipalPaymentCalculated) {
		this.bondPrincipalPaymentCalculated = bondPrincipalPaymentCalculated;
	}

	public BigDecimal getLoanPayment() {
		return loanPayment;
	}

	public void setLoanPayment(BigDecimal loanPayment) {
		this.loanPayment = loanPayment;
	}

	public BigDecimal getSpreadMci() {
		return SpreadMci;
	}

	public void setSpreadMci(BigDecimal spreadMci) {
		SpreadMci = spreadMci;
	}

	public BigDecimal getSpreadMsp() {
		return SpreadMsp;
	}

	public void setSpreadMsp(BigDecimal spreadMsp) {
		SpreadMsp = spreadMsp;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

}
