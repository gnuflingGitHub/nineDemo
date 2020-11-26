package com.gnufling.loanCalculatorDemo.repaymentsCalculation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gnufling.loanCalculatorDemo.models.LoanInterestRate;
import com.gnufling.loanCalculatorDemo.models.amortizationParameters.AmortizationParameters;
import com.gnufling.loanCalculatorDemo.models.amortizationParameters.FixedInterestAndAmortRatesAmortizationParameters;
import com.gnufling.loanCalculatorDemo.models.amortizationParameters.FixedInterestRateAmortizationParameters;
import com.gnufling.loanCalculatorDemo.models.amortizationParameters.FixedInterestRateBondAnnuityAmortizationParameters;
import com.gnufling.utils.CalculatorUtils;

public abstract class RepaymentCalculatorImpl implements RepaymentCalculator {

	protected List<LoanInterestRate> bondInterestRatesForCalculation = new ArrayList<LoanInterestRate>();
	protected List<LoanInterestRate> amortizationInterestRatesForCalculation = new ArrayList<LoanInterestRate>();
	
	protected BigDecimal calculateBondInterestAmount_n(BigDecimal fraction, BigDecimal bondPrincipal, BigDecimal i_n){
		BigDecimal bondInteresAmount_n = fraction.multiply(bondPrincipal.multiply(i_n, CalculatorUtils.DEFAULT_MATH_CONTEXT), CalculatorUtils.DEFAULT_MATH_CONTEXT);
		return CalculatorUtils.round(bondInteresAmount_n);
	}
	
	protected BigDecimal calculatePayment(BigDecimal bondInterestAmount, BigDecimal bontPrincipalPayment, BigDecimal bondCancellationAmount){
		return bondInterestAmount.add(bontPrincipalPayment, CalculatorUtils.DEFAULT_MATH_CONTEXT).add(bondCancellationAmount, CalculatorUtils.DEFAULT_MATH_CONTEXT);
	}

	protected BigDecimal calculateBondDebtEndOfPeriod (BigDecimal bondBeginningOfPeriod, BigDecimal bondPrincipalPayment, BigDecimal bondCancellationAmount){
		return bondBeginningOfPeriod.subtract(bondPrincipalPayment, CalculatorUtils.DEFAULT_MATH_CONTEXT).subtract(bondCancellationAmount, CalculatorUtils.DEFAULT_MATH_CONTEXT);
	}
	
	protected BigDecimal calculateIterestAmount(BigDecimal fraction, BigDecimal mortgageInterestTotalAmountInklRounding, BigDecimal mortgageDebtBeginningOfPeriod, int t){
		//i_n = round(((Minterest_n/MDbop_n) * (t/fraction)), 5)
		BigDecimal minterestnDivMDbop_n = mortgageInterestTotalAmountInklRounding.divide(mortgageDebtBeginningOfPeriod, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		BigDecimal tDivFraction = BigDecimal.valueOf(t).divide(fraction, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		
		return CalculatorUtils.round(minterestnDivMDbop_n.multiply(tDivFraction, CalculatorUtils.DEFAULT_MATH_CONTEXT), 7) ;
	}

	//TODO: Rename
	protected BigDecimal calculateSpread_n(BigDecimal fraction_n, BigDecimal mDbop_n, BigDecimal sBbop_n, BigDecimal s_Mxx, int t){
		
		BigDecimal spreadBasis = mDbop_n.subtract(sBbop_n, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		BigDecimal spreadRate = s_Mxx.divide(BigDecimal.valueOf(t), CalculatorUtils.DEFAULT_MATH_CONTEXT);
		
		BigDecimal spreadAmount = spreadBasis.multiply(spreadRate, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		spreadAmount = spreadAmount.multiply(fraction_n, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		spreadAmount = CalculatorUtils.round(spreadAmount);
		
		return spreadAmount;
	}
	
	//TODO: Rename
	protected BigDecimal calculateTotpmt_n (BigDecimal pmt_n, BigDecimal mCIspread_n, BigDecimal mSPspread_n){
		
		BigDecimal totalSpread = mCIspread_n.add(mSPspread_n, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		BigDecimal totPmt_n = pmt_n.add(totalSpread, CalculatorUtils.DEFAULT_MATH_CONTEXT);
		
		return totPmt_n;
	}
	
	
	public static RepaymentCalculator getInstance(AmortizationParameters amortizationParameters) {
		
		// RepaymentCalculator repaymentCalculator = null;
		RepaymentCalculatorImpl repaymentCalculatorImpl = null;

		// FIXED INTEREST RATE BOND ANNUITY (PRODUCT NO 1):
		if (amortizationParameters instanceof FixedInterestRateBondAnnuityAmortizationParameters) {
			repaymentCalculatorImpl = new BondAnnuityRepaymentCalculatorImpl();

			BigDecimal fixedInterestRate = ((FixedInterestRateAmortizationParameters) amortizationParameters).getFixedInterestRateProAnno();
			repaymentCalculatorImpl.bondInterestRatesForCalculation.add(new LoanInterestRate(fixedInterestRate));
			
			BigDecimal fixedAmortizationInterestRate_fixiAmort = ((FixedInterestAndAmortRatesAmortizationParameters) amortizationParameters).getFixedAmortRateProAnno();
			repaymentCalculatorImpl.amortizationInterestRatesForCalculation.add(new LoanInterestRate(fixedAmortizationInterestRate_fixiAmort));
		} 
		
		
		/*
		
		// "Kontantlån" - FIXED INTEREST RATE (PRODUCT NO 2)
		else if(amortizationParameters instanceof FixedInterestRateCashAmortizationParameters_02_DO){
			
			repaymentCalculatorImpl = new FixedRateCashRepaymentCalculatorImpl();
			
			BigDecimal fixedInterestRate = ((FixedInterestRateCashAmortizationParameters_02_DO) amortizationParameters).getFixedInterestRateProAnno_r();
			BigDecimal fixiamort = ((FixedInterestRateCashAmortizationParameters_02_DO) amortizationParameters).getFixedAmortRateProAnno_fixiamort();
			
			repaymentCalculatorImpl.bondInterestRatesForCalculation_ibond.add(new InterestRateDO(fixedInterestRate));
			repaymentCalculatorImpl.amortizationInterestRatesForCalculation_iamort.add(new InterestRateDO(fixiamort));
		}

		// FIXED INTEREST CASH LOAN (PRODUCT NO 3)
		else if(amortizationParameters instanceof FixedInterestRateCashAnnuityAmortizationParameters_03_DO){
			
			repaymentCalculatorImpl = new CashAnnuityRepaymentCalculatorImpl();
			
			BigDecimal fixedInterestRate = ((FixedInterestRateCashAnnuityAmortizationParameters_03_DO) amortizationParameters).getFixedInterestRateProAnno_r();
			repaymentCalculatorImpl.bondInterestRatesForCalculation_ibond.add(new InterestRateDO(fixedInterestRate));
			repaymentCalculatorImpl.amortizationInterestRatesForCalculation_iamort.add(new InterestRateDO(fixedInterestRate));
		}
		
		// VARIABLE INTEREST RATE BOND ANNUITY (PRODUCT NO 7):
		else if (amortizationParameters instanceof VariableInterestRateAnnuityAmortizationParameters_07_DO)
		{
			repaymentCalculatorImpl = new BondAnnuityRepaymentCalculatorImpl();
			repaymentCalculatorImpl.bondInterestRatesForCalculation_ibond = ((VariableInterestRateAnnuityAmortizationParameters_07_DO) amortizationParameters).getInterestRates();
			repaymentCalculatorImpl.amortizationInterestRatesForCalculation_iamort = ((VariableInterestRateAnnuityAmortizationParameters_07_DO) amortizationParameters).getAmortInterestRates_iamort();
		}
		
		// VARIABLE RATES (PRODUCT NO 8)
		else if (amortizationParameters instanceof VariableRateCashAmortizationParameters_08_DO)
		{
			repaymentCalculatorImpl = new FixedRateCashRepaymentCalculatorImpl();
			repaymentCalculatorImpl.bondInterestRatesForCalculation_ibond = ((VariableRateCashAmortizationParameters_08_DO) amortizationParameters).getInterestRates();  
			repaymentCalculatorImpl.amortizationInterestRatesForCalculation_iamort = ((VariableRateCashAmortizationParameters_08_DO) amortizationParameters).getAmortInterestRates_iamort();
		}
		
		
		// BOND BALLOON VARIABLE RATES (PRODUCT NO 11)
		else if (amortizationParameters instanceof VariableRateBondBalloonAmortizationParameters_11_DO)
		{
			repaymentCalculatorImpl = new BondBalloonRepaymentCalculatorImpl();
			repaymentCalculatorImpl.bondInterestRatesForCalculation_ibond = ((VariableRateBondBalloonAmortizationParameters_11_DO) amortizationParameters).getInterestRates();  
			repaymentCalculatorImpl.amortizationInterestRatesForCalculation_iamort = ((VariableRateBondBalloonAmortizationParameters_11_DO) amortizationParameters).getInterestRates();
		}
		
		// VARIABLE RATE CASH BALLOON (PRODUCT NO 12)
		
		else if (amortizationParameters instanceof VariableRateCashBalloonAmortizationParameters_12_DO) 
		{
			repaymentCalculatorImpl = new CashBalloonRepaymentCalculatorImpl(((VariableRateCashBalloonAmortizationParameters_12_DO) amortizationParameters).getdAno()); 
			repaymentCalculatorImpl.bondInterestRatesForCalculation_ibond = ((VariableRateCashBalloonAmortizationParameters_12_DO) amortizationParameters).getInterestRates();
			//Her skal du også sætte Dano
					return repaymentCalculatorImpl;
				}
		*/
		else { // Must never get here. In case we do it is a programming error
			throw new IllegalArgumentException(
					"Unknown type of AmortizationParametersDO");
		}
		return repaymentCalculatorImpl;
	}

	protected void trimBondInterestRatesForCalculationList(int totalNumberOfPayments) {
		trimInterestRatesList(totalNumberOfPayments, bondInterestRatesForCalculation);
	}

	protected void trimAmortizationInterestRatesForCalculationList(int totalNumberOfPayments) {
		trimInterestRatesList(totalNumberOfPayments, amortizationInterestRatesForCalculation);
	}
	
	private void trimInterestRatesList(int totalNumberOfPayments, List<LoanInterestRate> interestRatesList) {

		int noOfInterestRatesInListForCalculation = interestRatesList.size();
		int differenceInNoOfRates = totalNumberOfPayments - noOfInterestRatesInListForCalculation;

		if (differenceInNoOfRates > 0) { // If some interest rates are missing, we add some, based on the last interest
											// rate in the list
			BigDecimal interestRateForMissingPeriods = interestRatesList.get(noOfInterestRatesInListForCalculation - 1)
					.getInterestRatePerYear();

			for (int i = 1; i <= differenceInNoOfRates; i++) {

				interestRatesList.add(new LoanInterestRate(interestRateForMissingPeriods));
			}
		}
		if (differenceInNoOfRates < 0) { // Too many interests rates - we remove the unnecessary ones

			for (int i = Math.abs(noOfInterestRatesInListForCalculation); i > Math.abs(differenceInNoOfRates); i--) {
				interestRatesList.remove(i - 1);
			}
		}
	}
}
