package com.gnufling.loanCalculatorDemo.models;

import java.util.Date;

public class RepaymentPeriod {
	
	private Date startDate;
	private Date settlingDate;
	
	public RepaymentPeriod(Date startDate, Date settlingDate) {
		this.startDate = startDate;
		this.settlingDate = settlingDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getSettlingDate() {
		return settlingDate;
	}
	
	public void setSettlingDate(Date settlingDate) {
		this.settlingDate = settlingDate;
	}
}
