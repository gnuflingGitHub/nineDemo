package com.gnufling.utils;

import java.util.Calendar;

public class DayCountConvention_IMCA_Actual_Actual_Impl extends DayCountConventionImpl {

	public int daysBetweenNumerator(Calendar startCal, Calendar endCal) //throws CalculatorException 
	{
		int sign = 1;
		int res = 0;
	
		if (startCal.after(endCal)) {
			sign = -1;
			Calendar temp = startCal;
			startCal = endCal;
			endCal = temp;
		}
		int startYear = startCal.get(Calendar.YEAR);
		int endYear = endCal.get(Calendar.YEAR);
		if (startYear == endYear) {
			res = endCal.get(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR);
		} else {
			// First we calculates the number of days between now and the end of the start year:
			Calendar cal = Calendar.getInstance();
			res = cal.getActualMaximum(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR);
			// Then we add the number of days in the end year:
			res += endCal.get(Calendar.DAY_OF_YEAR);
			// Then we add the days in intervening years.
			// If startYear is 1999 and endYear is 2006, we count the days from
			// year 2000 to 2005,
			// since startYear and endYear are counted.
			for (int i = startYear + 1; i < endYear; i++) {
				cal.set(Calendar.YEAR, i);
				res += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
			}
		}
		return res * sign;
	}
	
	/** 
	 * The number of days in month is calculated as:
	 * 
	 * The actual days in the month of the parameter cal. 
	 */
	public int noOfDaysInMonthNumerator(Calendar cal) //throws CalculatorException 
	{
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/** 
	 * The number of days in year is calculated as:
	 * 
	 * The actual days in the year of the parameter cal. 
	 */
	public int noOfDaysInYearNumerator(Calendar cal) //throws CalculatorException 
	{
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	}
	
	/** 
	 * The daysBetween for the denominator (bottom part of a fraction) is calculated as:
	 * 
	 * The actual days between the startCal and the endCal. 
	 * 
	 */
	public int daysBetweenDenominator(Calendar startCal, Calendar endCal) //throws CalculatorException 
	{
		
		return daysBetweenNumerator(startCal, endCal);
	}
		
	/** 
	 * The number of days in month is calculated as:
	 * 
	 * The actual days in the month of the parameter cal. 
	 */
	public int noOfDaysInMonthDenominator(Calendar cal) //throws CalculatorException 
	{
		return noOfDaysInMonthNumerator(cal);
	}
	
		/** 
	 * The number of days in year is calculated as:
	 * 
	 * The actual days in the year of the parameter cal. 
	 */
	public int noOfDaysInYearDenominator(Calendar cal) //throws CalculatorException 
	{
		return noOfDaysInYearNumerator(cal);
	}

	
	
}
