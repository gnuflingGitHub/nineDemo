package com.gnufling.loanCalculatorDemo.repaymentsCalculation;

import java.math.BigDecimal;
import java.util.List;

import com.gnufling.loanCalculatorDemo.models.LoanPayment;

public interface RepaymentCalculator {

	public List<LoanPayment> calculateRepayments(BigDecimal oddFraction, BigDecimal loanAmount, BigDecimal bondPrincipal, int totalNumberOfPayments, int numberOfPaymentsPerYear, BigDecimal denomination, BigDecimal spreadMCI, BigDecimal spreadMSP);
	
//	public List<LoanPayment> calculateNewRepaymentsWithUnchangedTotalPayments(BigDecimal interestRatePerRepayment, List<Repayment> parmRepayments, BigDecimal principal, BigDecimal oddFraction, int scale);
//	public List<LoanPayment> calculateEvenUnroundedRepayments(BigDecimal interestRatePerRepayment, int requestedTotalNumberOfSettlingPeriods, BigDecimal principal);
//	public List<Repayment> calculateRepaymentsWithNewBasis(List<Repayment> oldRepayments, Boolean reducingMaturity, BigDecimal basisForAmortizationOld, BigDecimal basisForAmortizationNew, BigDecimal newDebtOutStanding, BigDecimal interestRatePerRepayment);
	
}
