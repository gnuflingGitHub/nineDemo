package com.gnufling.loanCalculatorDemo.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gnufling.enums.DaycountConventionType;
import com.gnufling.loanCalculatorDemo.models.LoanCalculated;
import com.gnufling.loanCalculatorDemo.models.LoanPayment;
import com.gnufling.loanCalculatorDemo.models.RepaymentPeriod;
import com.gnufling.loanCalculatorDemo.models.amortizationParameters.AmortizationParameters;
import com.gnufling.loanCalculatorDemo.repaymentsCalculation.RepaymentCalculator;
import com.gnufling.loanCalculatorDemo.repaymentsCalculation.RepaymentCalculatorImpl;
import com.gnufling.utils.CalculatorUtils;
import com.gnufling.utils.DayCountConvention;
import com.gnufling.utils.DayCountConventionFactory;

public class LoanCalculatorService {

	LoanCalculated calculateLoan(BigDecimal loanAmount, BigDecimal spotPrice, 
			Integer numberOfPaymentsPerYear, Integer maturityInNumberOfPayments, Boolean roundUpMaturityInNumberOfTerms, BigDecimal denomination, Date disbursementDate,  DaycountConventionType daycountConventionType, AmortizationParameters amortizationParameters, BigDecimal spreadMCI, BigDecimal spreadMSP)/*throws CalculatorException*/{

		//		LoanCalculated loanCalculated = loanCalculator.calculateLoan(loanAmount, spotPrice, paymentsPerYear, maturityInNmbrOfPayments, roundUpMaturityInNumberOfTerms, denomination, disbursementDate.getTime(), daycountConventionType, amortizationParameters, spreadMCI, spreadMSP);

		LoanCalculated loanCalculated = new LoanCalculated();
		
		Date firstDueDate = CalculatorUtils.calculateNextSettlingDate(disbursementDate, numberOfPaymentsPerYear, 1, 1);
		
		DayCountConvention dayCountConventionimpl = DayCountConventionFactory.getInstance(daycountConventionType);
		BigDecimal oddFractionFirstPayment = calculateOddFraction(disbursementDate, firstDueDate, dayCountConventionimpl, numberOfPaymentsPerYear);
		loanCalculated.setOddFractionFirstTerm(CalculatorUtils.round(oddFractionFirstPayment));
		
		BigDecimal bondPrincipal = calculateBondPrincipal(loanAmount, spotPrice);
		loanCalculated.setPrincipalAmount(bondPrincipal);
		
		//CALCULATE REPAYMENTS:
		RepaymentCalculator repaymentCalculator = RepaymentCalculatorImpl.getInstance(amortizationParameters);
		loanCalculated.setRepayments(repaymentCalculator.calculateRepayments(oddFractionFirstPayment, loanAmount, bondPrincipal, maturityInNumberOfPayments, numberOfPaymentsPerYear, denomination, spreadMCI, spreadMSP));
	
		//SET REPAYMENTPERIODS:
		List<RepaymentPeriod> repaymentPeriods = calculateRepaymentPeriods(disbursementDate, firstDueDate, numberOfPaymentsPerYear, loanCalculated.getRepayments().size());
		setRepaymentPeriods(repaymentPeriods, loanCalculated.getRepayments());
		//loanCalculated.setRepaymentPeriods(repaymentPeriods);
		
		return loanCalculated;
		
		
	}

	private void setRepaymentPeriods (List<RepaymentPeriod> repaymentPeriods, List<LoanPayment> loanPayments) {
		for(int i = 0; i<repaymentPeriods.size(); i++) {
			 loanPayments.get(i).setSettlementDate(repaymentPeriods.get(i).getSettlingDate());
		}
	}
	
	private BigDecimal calculateBondPrincipal(BigDecimal requestedAmount, BigDecimal price) {
		BigDecimal principal = requestedAmount.divide(price, CalculatorUtils.DEFAULT_MATH_CONTEXT).multiply(BigDecimal.valueOf(100), CalculatorUtils.DEFAULT_MATH_CONTEXT);
		return CalculatorUtils.round(principal);
	}
	
	private BigDecimal calculateOddFraction(Date disbursementDate, Date firstSettlingDate, DayCountConvention dayCountConvention,
			Integer numberOfSettlingPeriodsProAnno) /*throws Calcul  :;atorException*/ {
			
		int numberOfMonthsPerSettlingPeriod = 12 / numberOfSettlingPeriodsProAnno;

		Calendar tempSettlingDate = Calendar.getInstance();
		Calendar previousSettlingDate = Calendar.getInstance();
		tempSettlingDate.setTime(firstSettlingDate);
		int noOfWholePeriods = 0;
		do {
			tempSettlingDate.add(Calendar.MONTH, -numberOfMonthsPerSettlingPeriod);
			noOfWholePeriods++;
		} while (tempSettlingDate.after(disbursementDate));
		previousSettlingDate.setTimeInMillis(tempSettlingDate.getTimeInMillis());
		tempSettlingDate.add(Calendar.MONTH, numberOfMonthsPerSettlingPeriod); 
		noOfWholePeriods--; 

		Calendar disbursementDateAsCalendar = Calendar.getInstance();
		disbursementDateAsCalendar.setTime(disbursementDate);
		int oddDays = dayCountConvention.daysBetweenNumerator(disbursementDateAsCalendar, tempSettlingDate);
		int denominator = dayCountConvention.daysBetweenDenominator(previousSettlingDate, tempSettlingDate);
		return BigDecimal.valueOf(oddDays).divide(BigDecimal.valueOf(denominator), 30, CalculatorUtils.ROUNDING_MODE).add(
				BigDecimal.valueOf(noOfWholePeriods));
	}
	
	public List<RepaymentPeriod > calculateRepaymentPeriods(Date startDate, Date firstSettlingDate, int numberOfSettlingPeriodsProAnno,
			int requestedTotalNumberOfSettlingPeriods) {

		if (requestedTotalNumberOfSettlingPeriods < 1) {
			return null;
		}

		Calendar disbursementDateAsCalendar = Calendar.getInstance();
		disbursementDateAsCalendar.setTime(startDate);

		Calendar firstSettlingDateAsCalendar = Calendar.getInstance();
		firstSettlingDateAsCalendar.setTime(firstSettlingDate);

		if (firstSettlingDateAsCalendar.get(Calendar.DAY_OF_MONTH) > 28) {
			throw new IllegalArgumentException("Firstsettlingdate must be less than 28. Value was: '"
					+ firstSettlingDateAsCalendar.get(Calendar.DAY_OF_MONTH) + "'");
		}

		List<RepaymentPeriod> repaymentPeriods = new ArrayList<RepaymentPeriod>();
		int numberOfRepaymentPeriodsInList = 0;

		// First settling period
		repaymentPeriods.add(new RepaymentPeriod(disbursementDateAsCalendar.getTime(), firstSettlingDateAsCalendar.getTime()));
		numberOfRepaymentPeriodsInList++;

		Calendar nextStartDate = firstSettlingDateAsCalendar;
		while (numberOfRepaymentPeriodsInList < requestedTotalNumberOfSettlingPeriods) {
			Calendar endDate = CalculatorUtils.add(nextStartDate, 0, 12 / numberOfSettlingPeriodsProAnno);
			repaymentPeriods.add(new RepaymentPeriod(nextStartDate.getTime(), endDate.getTime()));
			numberOfRepaymentPeriodsInList++;
			nextStartDate = endDate;
		}
		return repaymentPeriods;
	}
	
	
	
}
