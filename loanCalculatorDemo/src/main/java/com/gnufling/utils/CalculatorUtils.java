package com.gnufling.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.gnufling.enums.PaymentFrequency;
import com.gnufling.loanCalculatorDemo.models.LoanPayment;

public class CalculatorUtils {
	
	private static final String DECIMAL_FORMAT = "###,###,##0.00";

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static final int AMOUNT_SCALE = 2;

	// approx same precision as double
	// to match Excel's rounding mode
	public static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(15, RoundingMode.UP);

	public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

	// top part of a fraction.
	public static final char NUMERATOR = 'n';
	// bottom part of a fraction.
	public static final char DENOMINATOR = 'd';

	public static String amountFormat(BigDecimal amount) {
		String temp = new DecimalFormat(DECIMAL_FORMAT).format(amount.doubleValue());
		StringBuilder sb = new StringBuilder();
		for (int i = temp.length(); i < 13; i++) {
			sb.append(" ");
		}
		sb.append(temp);
		return sb.toString();
	}
	
	public static String dateFormat(Date aDate) {
		return new SimpleDateFormat(YYYY_MM_DD).format(aDate);
	}

	public static String dateFormat(Calendar aDate) {
		return new SimpleDateFormat(YYYY_MM_DD).format(aDate.getTime());
	}

	public static Calendar date2Calendar(Date dateParm)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateParm);
		
		return cal;
	}
	
	/**
	 * Returns the date given as an integer for easy comparison (w/o time information)
	 * 
	 * @param date
	 * @return
	 */
	public static int date2Integer(Date date) {
		if ( date == null ) {
			return Integer.MIN_VALUE;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR)*1000+cal.get(Calendar.DAY_OF_YEAR);
	}

	
	public static BigDecimal round(BigDecimal number) {
		return round(number, AMOUNT_SCALE);
	}

	public static BigDecimal round(BigDecimal number, int noOfDecimals) {
		return number.setScale(noOfDecimals, ROUNDING_MODE);
	}

	public static BigDecimal roundup(BigDecimal number) {
		return roundup(number, AMOUNT_SCALE);
	}

	public static BigDecimal roundup(BigDecimal number, int noOfDecimals) {
		return number.setScale(noOfDecimals, BigDecimal.ROUND_UP);
	}

	// Return numbers of (quantity), which is amount rounded half up to the amount which is dividable by
	// denomination.
	public static BigDecimal roundToDenom(BigDecimal number, BigDecimal denomination) {
		return number.divide(denomination, 0, BigDecimal.ROUND_HALF_UP);
	}
	
	
	// Returns closest amount dividable by denomination, where denomination is given by at floating point
	// (BigDecimal) denomination.
	
	public static BigDecimal roundToDenominationFloatingPoint(BigDecimal amount, BigDecimal denomination){
		
		amount = CalculatorUtils.round(amount.divide(denomination, CalculatorUtils.DEFAULT_MATH_CONTEXT), 0);
		amount = CalculatorUtils.round(amount.multiply(denomination, CalculatorUtils.DEFAULT_MATH_CONTEXT));
		
		return amount;
	}

	// Return Date, which comes subsetMonth after Date AND is the last dag in that month.
	public static Calendar endOfMonth(Calendar aDate, int monthsToAdd) {
		Calendar endOfMonthDate = new GregorianCalendar(aDate.get(Calendar.YEAR), aDate.get(Calendar.MONTH), 1);
		endOfMonthDate.add(Calendar.MONTH, monthsToAdd);
		endOfMonthDate.set(Calendar.DAY_OF_MONTH, endOfMonthDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		return endOfMonthDate;
	}

	// Return numbers of (quantity), which is amount rounded down to the amount which is dividable by
	// denomination.
	public static BigDecimal roundToDenomDown(BigDecimal number, BigDecimal denomination) {
		return number.divide(denomination, 0, BigDecimal.ROUND_DOWN);
	}

	public static Calendar add(Calendar aDate, int daysToAdd, int monthsToAdd) {
		Calendar res = new GregorianCalendar(aDate.get(Calendar.YEAR), aDate.get(Calendar.MONTH), aDate.get(Calendar.DAY_OF_MONTH));
		res.add(Calendar.MONTH, monthsToAdd);
		res.add(Calendar.DAY_OF_MONTH, daysToAdd);
		return res;
	}

	/**
   * Do x / 100 in a nice simple and precise way...
   * 
   * @param x
   * @return x / 100
   */
	public static BigDecimal div100(BigDecimal x) {
		return new BigDecimal(x.unscaledValue(), x.scale() + 2);
	}

	/**
	 * Compares calendar instances only on values of day, month, and year - ignores values of hour, minute and second.
	 * @param c1
	 * @param c2
	 * @return
	 */
	/*
	public static boolean areFinancialCalendarDatesEqual(Calendar c1, Calendar c2){
		
		if (c1.get(5)==c2.get(5) && //Day 
				c1.get(2)==c2.get(2) && //Month
					c1.get(1)==c2.get(1)) { //Year
			return true;
		}
		return false;
	}
	*/
	
	public static BigDecimal calculateNetPresentValue(List<LoanPayment> repayments, BigDecimal discountRateProAnno, int numbersOfSettlingPeriodsProAnno ){

		BigDecimal discountRatePerSettlingPeriod = discountRateProAnno.divide(BigDecimal.valueOf(numbersOfSettlingPeriodsProAnno), DEFAULT_MATH_CONTEXT).divide(BigDecimal.valueOf(100.00), DEFAULT_MATH_CONTEXT);
		BigDecimal npv = BigDecimal.ZERO;
		
		for (int i = 0; i < repayments.size(); i++) {
			//npv += repayments.get(i).GetTotalpayment / Math.Pow((1+discountRatePerSettlingPeriod),i+1)
			
			//TODO:
			npv = npv.add(repayments.get(i).getTotalPayment().divide(BigDecimal.ONE.add(discountRatePerSettlingPeriod).pow(i+1, DEFAULT_MATH_CONTEXT), DEFAULT_MATH_CONTEXT), DEFAULT_MATH_CONTEXT);
		}
		return npv;
	}
	
	public static BigDecimal pmt(BigDecimal i, int n) {
		BigDecimal onePlusPeriodicInterestRate = BigDecimal.ONE.add(i, DEFAULT_MATH_CONTEXT);
		BigDecimal onePlusPeriodicInterestRatePowerMinusNumberOfPayments = onePlusPeriodicInterestRate.pow(-n, DEFAULT_MATH_CONTEXT);
		BigDecimal divisor = BigDecimal.ONE.subtract(onePlusPeriodicInterestRatePowerMinusNumberOfPayments, DEFAULT_MATH_CONTEXT);

		return i.divide(divisor, DEFAULT_MATH_CONTEXT);
	}

	/**
   * Giving next settlingDate using settlingrule terms.
   * 
   * @param date
   * @param numberOfSettlingPeriodProAnno
   * @param endOfMonth
   * @param firstDay
   * @param firstMonth
   * @return
   */
	
	public static Date calculateNearestComingSettlingDate(Date date, int numberOfSettlingPeriodProAnno, int firstDay, int firstMonth) {

		// Validation of numberOfSettlingPeriodProAnno
		switch (numberOfSettlingPeriodProAnno) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 6:
		case 12: {
			// System.out.println("Valid numberOfSettlingPeriodProAnno");
			break;
		}
		default: {
			System.out.println("Fejl i numberOfSettlingPeriodProAnno");
			throw new IllegalArgumentException("numberOfSettlingPeriodProAnno is invalid: " + numberOfSettlingPeriodProAnno);
			// break;
		}
		}

		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		
		Calendar nextSettlingDate = Calendar.getInstance();
		nextSettlingDate.set(Calendar.MILLISECOND, 0);
		
		nextSettlingDate.set(dateCal.get(Calendar.YEAR), firstMonth - 1, firstDay, 0, 0, 0);

		int monthsPerRepayment = 12 / numberOfSettlingPeriodProAnno;

		while (nextSettlingDate.compareTo(dateCal) < 0) {
			nextSettlingDate.add(Calendar.MONTH, monthsPerRepayment);
		}
		return nextSettlingDate.getTime();
	}
	
	
	public static Date calculateNextSettlingDate(Date date, int numberOfSettlingPeriodProAnno, int firstDay, int firstMonth) {

		// Validation of numberOfSettlingPeriodProAnno
		switch (numberOfSettlingPeriodProAnno) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 6:
		case 12: {
			// System.out.println("Valid numberOfSettlingPeriodProAnno");
			break;
		}
		default: {
			System.out.println("Fejl i numberOfSettlingPeriodProAnno");
			throw new IllegalArgumentException("numberOfSettlingPeriodProAnno is invalid: " + numberOfSettlingPeriodProAnno);
			// break;
		}}

		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		
		
		int dayOfMonth = dateCal.get(Calendar.DAY_OF_MONTH);
		
		if (dayOfMonth == firstDay) {
			dateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth+=1); 
		}
		

		Calendar nextSettlingDate = Calendar.getInstance();
		nextSettlingDate.set(Calendar.MILLISECOND, 0);

		
		nextSettlingDate.set(dateCal.get(Calendar.YEAR), firstMonth - 1, firstDay, 0, 0, 0);
		

		int monthsPerRepayment = 12 / numberOfSettlingPeriodProAnno;

		while (nextSettlingDate.compareTo(dateCal) < 0) {
			nextSettlingDate.add(Calendar.MONTH, monthsPerRepayment);
		}
		return nextSettlingDate.getTime();
	}
		

	public static BigDecimal calculateInterestRatePerRepayment(BigDecimal interestRateProAnno, int numberOfSettlingPeriodsProAnno) {
		return interestRateProAnno.divide(BigDecimal.valueOf(numberOfSettlingPeriodsProAnno), DEFAULT_MATH_CONTEXT);
	}
	
	/**
	 * THIS METHOD MIGHT RESULT IN LOSS OF PRECISION.
	 * The current implementation of Java (version 5) does not support a fractional exponent.
	 * It has been decided to accept the possible loss of precision by using double's.
	 * If a future version of Java supports a fractional exponent this method should be re-implemented
	 * using this new feature. 
	 * As of April 2008 an alternative exists in IBM's Java implementation, but it has been decided
	 * not to implement a dependency to a specific Java implementation.
	 * 
	 * @param base
	 * @param exponent
	 * @return 
	 */
	public static BigDecimal pow(BigDecimal base, BigDecimal exponent) {
		return BigDecimal.valueOf(Math.pow(base.doubleValue(), exponent.doubleValue()));
	}

	public static Calendar getDayBefore(Calendar parm)	{
		parm.add(Calendar.DAY_OF_MONTH, -1);
		return parm;
	}

	
	public static PaymentFrequency getPaymentFrequencyBySettlingDateAndNumberOfSettlingPeriodsProAnno(Calendar Settlingdate, int numberOfSettlingPeriodsProAnno) /*throws CalculatorException*/ {

		int settlingDateMonth = Settlingdate.get(Calendar.MONTH) + 1;
		
		if (numberOfSettlingPeriodsProAnno == 12) {
			return PaymentFrequency.FIRST_DAY_EVERY_MONTH;
		}

		if (settlingDateMonth%2 == 0) {
			return PaymentFrequency.FIRST_DAY_EVEN_MONTHS;
		}
		
		return PaymentFrequency.FIRST_DAY_ODD_MONTHS;
	}
	
	/* NOTE: This method gives you the latest valid settling data, before the dateParm. If dateParm is a settlingDate,
	 * this method returns latest settling date before dateParm - for example: if paymentFrequency is first day every month,
	 * a call with dateParm 1/4-2009 will return 1/3-2009. This opposed to another static method of this class 
	 * "getPreviousValidSettlingDate", which would return 1/4-2009 given the same example.	 * 
	 */
	public static Calendar getLatestValidSettlingDate(Calendar dateParm, PaymentFrequency paymentFrequency) /*throws CalculatorException*/{
		
				Calendar result = (Calendar)dateParm.clone();
		
				return getPreviousValidSettlingDate(CalculatorUtils.getDayBefore(result), paymentFrequency);
	}
	
	
	
	/*
	 * NOTE: This method assumes that a valid settling date must be the 1st in a month. It happens in this line:
	 * dateLastInvestor.add(Calendar.DAY_OF_MONTH, 1);
	 * 
	 * Returns the previous valid settling date relative to the given date, (e.g. used in calculation of accInterestFrac and bondInterestFrac)
	 */
	public static Calendar getPreviousValidSettlingDate(Calendar date, PaymentFrequency paymentFrequency) /*throws CalculatorException*/ {        
		int monthsToAdd = -1;
	
		Calendar dateLastInvestor = (Calendar) date.clone();
		do {
			dateLastInvestor = CalculatorUtils.endOfMonth(dateLastInvestor, monthsToAdd);
			dateLastInvestor.add(Calendar.DAY_OF_MONTH, 1); // Assuming 1st in month
			monthsToAdd--;
			if (monthsToAdd < -12) {
				// In this case we have tried "moving" the date 11 months back (one month at a time) and have still not found a valid settling date
				// something is wrong. Perhaps a valid settling date is something else but the 1st in a month
				throw new IllegalStateException(
						"Cannot calculate the dateLastInvestor. Have 'moved' the date 11 months back (one month at a time) and still not found a valid one. The prepaymentDate was: '"
								+ date.getTime() + "'");
			}
		} while (!isValidSettlingDate(dateLastInvestor, paymentFrequency));
		return dateLastInvestor;
	}

	/*
	 * NOTE: This method assumes that a valid settling date must be on the first day of a month.
	 */
	public static Calendar getNextValidSettlingDate(Calendar date, PaymentFrequency paymentFrequency) {
		Calendar dateNextPayment = (Calendar) date.clone();
		int loopCounter = 1;
		while (!isValidSettlingDate(dateNextPayment, paymentFrequency)) {
			if (loopCounter > 11) {
				// In this case we have tried "moving" the date 11 months forward (one month at a time) and have still not found a valid settling date
				// something is wrong. Perhaps a valid settling date is something else but the 1st in a month
				throw new IllegalStateException(
						"Cannot calculate the dateNextPayment. Have 'moved' the date 11 months forward (one month at a time) and still not found a valid one. The prepaymentDate was: '"
								+ date.getTime() + "'");
			}
			dateNextPayment = CalculatorUtils.endOfMonth(dateNextPayment, 0);
			dateNextPayment.add(Calendar.DAY_OF_MONTH, 1);
			loopCounter++;
		}
		return dateNextPayment;
	}

	public static boolean isValidSettlingDate(Calendar settlingDate, PaymentFrequency paymentFrequency) {
		if (paymentFrequency == PaymentFrequency.FIRST_DAY_EVERY_MONTH) {
			// 12 settlingPeriodsProAnno-->Any month is valid
			return settlingDate.get(Calendar.DAY_OF_MONTH) == 1 ? true : false;
		} else if (paymentFrequency == PaymentFrequency.FIRST_DAY_EVEN_MONTHS) {
			// 6 settlingPeriodsProAnno-->Only even months are valid...(feb, april...)
			if (settlingDate.get(Calendar.DAY_OF_MONTH) == 1) {
				int monthNumber = settlingDate.get(Calendar.MONTH) + 1; // January is 0 in the calendar class and we wan't it to be 1
				return monthNumber % 2 == 0; // Divisible with 2
			} else {
				return false;
			}
		} else if (paymentFrequency == PaymentFrequency.FIRST_DAY_ODD_MONTHS) {
			// 6 settlingPeriodsProAnno-->Only odd months are valid...(jan, march...)
			if (settlingDate.get(Calendar.DAY_OF_MONTH) == 1) {
			int monthNumber = settlingDate.get(Calendar.MONTH) + 1; // January is 0 in the calendar class and we wan't it to be 1
			return !(monthNumber % 2 == 0); // Divisible with 2
			}
			else {
				return false;
			}
		} else {
			throw new IllegalStateException("Invalid paymentFrequency. It was: '" + paymentFrequency + "'");
		}
	}



}
