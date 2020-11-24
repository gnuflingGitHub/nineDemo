package com.gnufling.utils;

import java.util.Calendar;

public class DayCountConventionAmerican30_360Impl extends DayCountConventionImpl{

	private static final int DAYS_IN_YEAR = 360;
	private static final int DAYS_IN_MONTH = 30;
	private static final int LAST_DAY_FEBRUARY = 28;
	private static final int LAST_DAY_FEBRUARY_LEAPYEAR = 29;

	public DayCountConventionAmerican30_360Impl() {
		super();
	}
	
	
	/**
	 * The daysBetween is calculated as:
	 * 
	 * daysBetween = (endDay-startDay) + ( 30*(endMonth-startMonth) ) + ( 360*(endYear-startYear) )
	 * 
	 * Adjustment rules:
	 * 1 Only if both endDay and startDay are the last day of february in that year set endDay to 30
	 * 2 If startDay is the last day of february set startDay to 30 
	 * 3 If endDay is 31 and startDay is 30 or 31 set endDay to 30 
	 * 4 If startDay is 31 then set startDay to 30 
	 */
	public int daysBetweenNumerator(Calendar startCal, Calendar endCal) /*throws CalculatorException*/ {
		int sign = 1;
		int res = 0;
		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			sign = -1;
			Calendar temp = startCal;
			startCal = endCal;
			endCal = temp;
		}
		int d1 = startCal.get(Calendar.DAY_OF_MONTH);
		int m1 = startCal.get(Calendar.MONTH) + 1;
		int y1 = startCal.get(Calendar.YEAR);
		int d2 = endCal.get(Calendar.DAY_OF_MONTH);
		int m2 = endCal.get(Calendar.MONTH) + 1;
		int y2 = endCal.get(Calendar.YEAR);

		// Adjustments 
		int lastDayFebruaryStartCal = 0;
		int lastDayFebruaryEndCal = 0;
		if (startCal.getActualMaximum(Calendar.DAY_OF_YEAR) == 365) {
			lastDayFebruaryStartCal = LAST_DAY_FEBRUARY;
		} else {
			lastDayFebruaryStartCal = LAST_DAY_FEBRUARY_LEAPYEAR;
		}
		if (endCal.getActualMaximum(Calendar.DAY_OF_YEAR) == 365) {
			lastDayFebruaryEndCal = LAST_DAY_FEBRUARY;
		} else {
			lastDayFebruaryEndCal = LAST_DAY_FEBRUARY_LEAPYEAR;
		}
		// rule 1
		if (m1 == 2 && m2 == 2) {
			d2 = (d1 == lastDayFebruaryStartCal && d2 == lastDayFebruaryEndCal ? DAYS_IN_MONTH : d2);
		}

		// rule 2
		if (m1 == 2) {
			d1 = (d1 == lastDayFebruaryStartCal ? DAYS_IN_MONTH : d1);
		}

		// rule 3
		d2 = (d2 > DAYS_IN_MONTH && d1 >= DAYS_IN_MONTH ? DAYS_IN_MONTH : d2);
		
		// rule 4
		d1 = (d1 > DAYS_IN_MONTH ? DAYS_IN_MONTH : d1);

		res = (d2 - d1) + DAYS_IN_MONTH * (m2 - m1) + DAYS_IN_YEAR * (y2 - y1);
		return sign * res;
	};
	
	/** 
	 * The number of days in month is always 30. 
	 */
	public int noOfDaysInMonthNumerator(Calendar cal) /*throws CalculatorException*/ {

		return DAYS_IN_MONTH;
	};

	/** 
	 * The number of days in year is always 360. 
	 */
	public int noOfDaysInYearNumerator(Calendar cal) /*throws CalculatorException*/ {

		return DAYS_IN_YEAR;
	};

	/**
	 * The daysBetween is calculated as:
	 * 
	 * daysBetween = (endDay-startDay) + ( 30*(endMonth-startMonth) ) + ( 360*(endYear-startYear) )
	 * 
	 * Adjustment rules:
	 * 1 Only if both endDay and startDay are the last day of february in that year set endDay to 30
	 * 2 If startDay is the last day of february set startDay to 30 
	 * 3 If endDay is 31 and startDay is 30 or 31 set endDay to 30 
	 * 4 If startDay is 31 then set startDay to 30 
	 */
	public int daysBetweenDenominator(Calendar startCal, Calendar endCal) /*throws CalculatorException*/ {
	
		return daysBetweenNumerator(startCal, endCal);
	};

	/** 
	 * The number of days in month is always 30. 
	 */
	public int noOfDaysInMonthDenominator(Calendar cal) /*throws CalculatorException */{

		return noOfDaysInMonthNumerator(cal);
	};


	/** 
	 * The number of days in year is always 360.
	 */
	public int noOfDaysInYearDenominator(Calendar cal) /*throws CalculatorException*/ {

		return noOfDaysInYearNumerator(cal);
	};
	
	

}
