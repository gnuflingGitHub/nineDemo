package com.gnufling.loanCalculatorDemo.repaymentsCalculation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gnufling.loanCalculatorDemo.models.LoanPayment;
import com.gnufling.utils.CalculatorUtils;

public class BondAnnuityRepaymentCalculatorImpl extends RepaymentCalculatorImpl{

	@Override
	public List<LoanPayment> calculateRepayments(BigDecimal oddFraction, BigDecimal loanAmount,
			BigDecimal bondPrincipal, int totalNumberOfPayments, int numberOfPaymentsPerYear, BigDecimal denomination,
			BigDecimal spreadMCI, BigDecimal spreadMSP) {
		
		
		trimBondInterestRatesForCalculationList(totalNumberOfPayments);
		trimAmortizationInterestRatesForCalculationList(totalNumberOfPayments);
		
		
		List<LoanPayment> repayments = new ArrayList<LoanPayment>();
		
		LoanPayment loanPayment = calculateBondAnnuityRepayment(1, totalNumberOfPayments, numberOfPaymentsPerYear, bondPrincipal, denomination, spreadMCI, spreadMSP, oddFraction);
		repayments.add(loanPayment);
		
		BigDecimal bondPrincipalAmount_n;
		
		for (int i = 1; i < totalNumberOfPayments; i++) {

			bondPrincipalAmount_n = loanPayment.getBondDebtEndOfPeriod();
			
			loanPayment = calculateBondAnnuityRepayment(i+1, totalNumberOfPayments, numberOfPaymentsPerYear, bondPrincipalAmount_n, denomination, spreadMCI, spreadMSP, BigDecimal.ONE);
			//RepaymentDO fullRep = calculateFullBondRepayment(numberOfTerms_mact, i, noOfTermsPerYear_t, repayments.get(i - 2).getBondDebtEndOfPeriod_BDeop_n(), denomination_d, s_MCI, s_MSP);

			repayments.add(loanPayment);
		}

		return repayments;
	}
	
	private LoanPayment calculateBondAnnuityRepayment(int n, int totalNumberOfPayments, int numberOfPaymentsPerYear, BigDecimal bondPrincipal,
			BigDecimal denomination, BigDecimal spreadMCI, BigDecimal spreadMSP, BigDecimal oddFraction){

		//bondInterestRatePerYear
		BigDecimal bondInterestRatePerYear = bondInterestRatesForCalculation.get(n - 1).getInterestRatePerYear();
		BigDecimal ti_n = bondInterestRatePerYear.divide(BigDecimal.valueOf(numberOfPaymentsPerYear), CalculatorUtils.DEFAULT_MATH_CONTEXT);

		//amortInterestRatePerYear
		BigDecimal amortInterestRatePerYear = amortizationInterestRatesForCalculation.get(n - 1).getInterestRatePerYear();
	
		//bondInterestAmount
		BigDecimal bondInterestAmount = calculateBondInterestAmount_n(oddFraction, bondPrincipal, ti_n);

		//bondPrincipalPayment
		BigDecimal bondPrincipalPayment = calculateBondPrincipalPaymentPeriod_n(n, totalNumberOfPayments, numberOfPaymentsPerYear, amortInterestRatePerYear, bondPrincipal, denomination, oddFraction);
		
		//bondPrincipalCancellation
		BigDecimal bondPrincipalCancellation = BigDecimal.ZERO;
		//paymentFromBorrower
		BigDecimal paymentFromBorrower = calculatePayment(bondInterestAmount, bondPrincipalPayment, bondPrincipalCancellation);
		//BDeop_n
		BigDecimal bondDebtEndOfPeriod = calculateBondDebtEndOfPeriod(bondPrincipal, bondPrincipalPayment, bondPrincipalCancellation);
		//i_n
		BigDecimal i_n = calculateIterestAmount(oddFraction, bondInterestAmount, bondPrincipal, numberOfPaymentsPerYear);
		//mCIspread_n
		BigDecimal mCIspread_n = calculateSpread_n(oddFraction, bondPrincipal, BigDecimal.ZERO, spreadMCI, numberOfPaymentsPerYear);
		//mSPspread_n
		BigDecimal mSPspread_n = calculateSpread_n(oddFraction, bondPrincipal, BigDecimal.ZERO, spreadMSP, numberOfPaymentsPerYear);
		//totpmt_n
		BigDecimal totpmt_n = calculateTotpmt_n(paymentFromBorrower, mCIspread_n, mSPspread_n);

		return createLoanPayment_BondAnnuity(n, oddFraction, bondInterestRatePerYear, amortInterestRatePerYear, bondPrincipal, bondInterestAmount, bondPrincipalPayment, paymentFromBorrower, bondDebtEndOfPeriod, i_n, mCIspread_n, mSPspread_n, totpmt_n);
	}
	
	
	private LoanPayment createLoanPayment_BondAnnuity(int n, BigDecimal oddFraction, BigDecimal bondInterestRatePerYear, BigDecimal amortInterestRatePerYear, BigDecimal bondDebtBeginningOfPeriod, BigDecimal bondInterestAmount, BigDecimal bondPrincipalPayment, BigDecimal paymentFromBorrower,
			BigDecimal bondDebtEndOfPeriod, BigDecimal i_n, BigDecimal MCIspread_n, BigDecimal MSPspread_n, BigDecimal totpmt_n) {

		LoanPayment loanPayment = new LoanPayment();
		
		loanPayment.setN(n);
		
		loanPayment.setBondDebtBeginningOfPeriod(bondDebtBeginningOfPeriod);
		loanPayment.setBondInterestAmountCalculated(bondInterestAmount);
		loanPayment.setBondPrincipalPaymentCalculated(bondPrincipalPayment);
		loanPayment.setBondDebtEndOfPeriod(bondDebtEndOfPeriod);

		loanPayment.setLoanInterestAmountCalculated(bondInterestAmount);
		loanPayment.setLoanPayment(paymentFromBorrower);
		
		loanPayment.setSpreadMci(MCIspread_n);
		loanPayment.setSpreadMsp(MSPspread_n);
		
		loanPayment.setTotalPayment(totpmt_n);
	
		return loanPayment;
	}
	
	
	private BigDecimal calculateBondPrincipalPaymentPeriod_n(int paymentPeriod_n, int numberOfTerms_mact, int noOfTermsPerYear_t, BigDecimal iamort_n, BigDecimal bondDebtBeginningOfPeriod_BDbop,
			BigDecimal denomination_d, BigDecimal fraction) {

		BigDecimal bondPrincipalUnrounded = null;
		BigDecimal bondPrincipalPaymentPeriodN_Bprinc_n = null;
		//BigDecimal iamort_n = amortizationInterestRatesForCalculation_iamort.get(paymentPeriod_n - 1).getInterestRatePerYear();

		// (mact - n + 1): (used in formulas below)
		int mact_minus_n_plus_1 = -(numberOfTerms_mact - paymentPeriod_n + 1);

		if (iamort_n.compareTo(BigDecimal.ZERO) == 1) { // iamort_n > 0

			BigDecimal iamort_n_divide_t = iamort_n.divide(BigDecimal.valueOf(noOfTermsPerYear_t));
			// (1+iamort_n/t)pow(mact-n+1): (used in formula below)
			BigDecimal one_plus_iamort_n_divide_t = BigDecimal.ONE.add(iamort_n_divide_t);

			// (1+iamort_n/t)pow(mact-n+1)pow-(mact-n+1): (used in formula below)

			int x = mact_minus_n_plus_1;

			BigDecimal one_plus_iamort_n_divide_t_pow_mact_minus_n_plus_1 = one_plus_iamort_n_divide_t.pow(x, CalculatorUtils.DEFAULT_MATH_CONTEXT);
			BigDecimal een = BigDecimal.ONE;
			BigDecimal one_minus_one_plus_iamort_n_divide_t_pow_mact_minus_n_plus_1 = een.subtract(one_plus_iamort_n_divide_t_pow_mact_minus_n_plus_1,
					CalculatorUtils.DEFAULT_MATH_CONTEXT);

			// FRACTION_A : iamort_n/t / 1-(1+iamort_n/t)pow(mact-n+1)pow(mact-n+1):
			BigDecimal fraction_A = iamort_n_divide_t.divide(one_minus_one_plus_iamort_n_divide_t_pow_mact_minus_n_plus_1, CalculatorUtils.DEFAULT_MATH_CONTEXT);
			// FINAL foumula:
			BigDecimal fraction_final = fraction_A.subtract(iamort_n_divide_t);

			bondPrincipalUnrounded = bondDebtBeginningOfPeriod_BDbop.multiply(fraction_final);

		} else { // iamort <= 0 means Serial Loan:

			bondPrincipalUnrounded = bondDebtBeginningOfPeriod_BDbop.divide(BigDecimal.valueOf(mact_minus_n_plus_1));

		}

		bondPrincipalUnrounded = bondPrincipalUnrounded.multiply(fraction, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		bondPrincipalPaymentPeriodN_Bprinc_n = CalculatorUtils.roundToDenominationFloatingPoint(bondPrincipalUnrounded, denomination_d);
		return bondPrincipalPaymentPeriodN_Bprinc_n;

	}
	

}
