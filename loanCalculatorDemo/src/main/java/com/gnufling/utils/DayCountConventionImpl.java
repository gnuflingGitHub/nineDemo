package com.gnufling.utils;

import java.math.BigDecimal;
import java.util.Calendar;

public abstract class DayCountConventionImpl implements DayCountConvention {
	
	public BigDecimal yearFrac(Calendar startCal, Calendar endCal) /*throws CalculatorException*/{
//		 Notice a convention introduced here:
			// We let the endDate of the interval decide which year is investigated
			// for being a LeapYear
			// (Only relevant for where InterestCalculationMethod has ACTUAL in the
			// denominator)
			BigDecimal numerator = BigDecimal.valueOf(daysBetweenNumerator(startCal, endCal));
			BigDecimal denominator = BigDecimal.valueOf(noOfDaysInYearDenominator(endCal));
			return numerator.divide(denominator, CalculatorUtils.DEFAULT_MATH_CONTEXT).abs();
		};


}
