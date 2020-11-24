package com.gnufling.utils;

import java.math.BigDecimal;
import java.util.Calendar;

public interface DayCountConvention {

int daysBetweenNumerator(Calendar startCal, Calendar endCal) /*throws CalculatorException*/;
	
	int noOfDaysInMonthNumerator(Calendar cal) /*throws CalculatorException*/;
	
	int noOfDaysInYearNumerator(Calendar cal) /*throws CalculatorException*/;

	int daysBetweenDenominator(Calendar startCal, Calendar endCal) /*throws CalculatorException*/;

	int noOfDaysInMonthDenominator(Calendar cal) /* throws CalculatorException*/;
	
	int noOfDaysInYearDenominator(Calendar cal) /* throws CalculatorException*/;
	
	BigDecimal yearFrac(Calendar startCal, Calendar endCal)/* throws CalculatorException*/;
	
}
