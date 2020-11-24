package com.gnufling.utils;

import com.gnufling.enums.DaycountConventionType;

public class DayCountConventionFactory {
	
static public DayCountConvention getInstance(DaycountConventionType dayCountConventionType){
		
		if (dayCountConventionType == DaycountConventionType.US_30_360) {
			
			return new DayCountConventionAmerican30_360Impl();
			
		}
		
		else if (dayCountConventionType == DaycountConventionType.IMCA_ACTUAL_ACTUAL) {
			
			
			return new DayCountConvention_IMCA_Actual_Actual_Impl();
		}
		
		else {

			//DEFAULT:
			return new DayCountConventionAmerican30_360Impl();
			
		}
		
		
	}

}
