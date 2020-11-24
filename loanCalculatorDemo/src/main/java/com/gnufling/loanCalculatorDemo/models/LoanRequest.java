package com.gnufling.loanCalculatorDemo.models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LoanRequest {
	
	private BigDecimal loanAmountRequest;
	private BigDecimal spotPrice;
	private Integer numberOfPaymentsPerYear;
	private Integer maturityInNmbrOfPayments;
	private Boolean roundUpMaturityInNumberOfTerms;
	private BigDecimal denomination;// = new BigDecimal("1.00");
	// Formats output date when this DTO is passed through JSON
	@JsonFormat(pattern = "dd/MM/yyyy")
	// Allows dd/MM/yyyy date to be passed into GET request in JSON
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date disbursementDate;
	
	public LoanRequest(BigDecimal loanAmountRequest, BigDecimal spotPrice, Integer numberOfPaymentsPerYear) {
		this.loanAmountRequest = loanAmountRequest;
		this.spotPrice = spotPrice;
		this.numberOfPaymentsPerYear = numberOfPaymentsPerYear;
	}
	
	public LoanRequest() {
	}
	public BigDecimal getLoanAmountRequest() {
		return loanAmountRequest;
	}
	public void setLoanAmountRequest(BigDecimal loanAmountRequest) {
		this.loanAmountRequest = loanAmountRequest;
	}
	public BigDecimal getSpotPrice() {
		return spotPrice;
	}
	public void setSpotPrice(BigDecimal spotPrice) {
		this.spotPrice = spotPrice;
	}
	public Integer getNumberOfPaymentsPerYear() {
		return numberOfPaymentsPerYear;
	}
	public void setNumberOfPaymentsPerYear(Integer numberOfPaymentsPerYear) {
		this.numberOfPaymentsPerYear = numberOfPaymentsPerYear;
	}
	
	
	
	
	public Integer getMaturityInNmbrOfPayments() {
		return maturityInNmbrOfPayments;
	}
	
	
	public void setMaturityInNmbrOfPayments(Integer maturityInNmbrOfPayments) {
		this.maturityInNmbrOfPayments = maturityInNmbrOfPayments;
	}
	
	
	public Boolean getRoundUpMaturityInNumberOfTerms() {
		return roundUpMaturityInNumberOfTerms;
	}
	
	
	public void setRoundUpMaturityInNumberOfTerms(Boolean roundUpMaturityInNumberOfTerms) {
		this.roundUpMaturityInNumberOfTerms = roundUpMaturityInNumberOfTerms;
	}
	
	
	public BigDecimal getDenomination() {
		return denomination;
	}
	
	
	public void setDenomination(BigDecimal denomination) {
		this.denomination = denomination;
	}

	public Date getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(Date disbursementDate) {
		this.disbursementDate = disbursementDate;
	}
}

