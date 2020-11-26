package com.gnufling.loanCalculatorDemo.resources;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnufling.enums.DaycountConventionType;
import com.gnufling.loanCalculatorDemo.models.LoanCalculated;
import com.gnufling.loanCalculatorDemo.models.LoanRequest;
import com.gnufling.loanCalculatorDemo.models.amortizationParameters.FixedInterestRateBondAnnuityAmortizationParameters;
import com.gnufling.utils.CalculatorUtils;

@RestController
@RequestMapping("/loanCalculated")
public class LoanCalculatorDemoResource {

	 private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping("/{principalAmount}")
	public LoanCalculated getLoanCalculated(@PathVariable("principalAmount") BigDecimal principalAmount) {
		
		//TODO: To be removed later:
		
		BigDecimal loanAmount = principalAmount;
		BigDecimal spotPrice = BigDecimal.valueOf(98.0d);
		Integer paymentsPerYear = 4;
		Integer maturityInNmbrOfPayments = 120;
		Boolean roundUpMaturityInNumberOfTerms = true;
		BigDecimal denomination = new BigDecimal("1.00");
		
		Calendar disbursementDate = Calendar.getInstance();
		disbursementDate.set(2020, Calendar.NOVEMBER, 25);
		
		DaycountConventionType daycountConventionType = DaycountConventionType.US_30_360;
		BigDecimal spreadMCI = new BigDecimal("0.003", CalculatorUtils.DEFAULT_MATH_CONTEXT);
		BigDecimal spreadMSP = new BigDecimal("0.005", CalculatorUtils.DEFAULT_MATH_CONTEXT);
		
		FixedInterestRateBondAnnuityAmortizationParameters amortizationParameters = new FixedInterestRateBondAnnuityAmortizationParameters();
		amortizationParameters.setFixedAmortRateProAnno(BigDecimal.valueOf(0.02d));
		amortizationParameters.setFixedInterestRateProAnno(BigDecimal.valueOf(0.02d));
		
		LoanCalculatorService loanCalculator = new LoanCalculatorService();
		LoanCalculated loanCalculated = loanCalculator.calculateLoan(loanAmount, spotPrice, paymentsPerYear, maturityInNmbrOfPayments, roundUpMaturityInNumberOfTerms, denomination, disbursementDate.getTime(), daycountConventionType, amortizationParameters, spreadMCI, spreadMSP);
		
		return loanCalculated;
	}
	
	@RequestMapping("/getWithRequestParam")
	public LoanCalculated getLoanCalculatedWithRequestParm(@RequestParam(value="loanRequest") String loanRequest ) 
		throws IOException {
		
		final LoanRequest loanRequestO = 
				new ObjectMapper().setDateFormat(simpleDateFormat).readValue(loanRequest, LoanRequest.class); 
		
		BigDecimal loanAmount =  loanRequestO.getLoanAmountRequest();
		BigDecimal spotPrice = loanRequestO.getSpotPrice();//BigDecimal.valueOf(98.0d);
		Integer paymentsPerYear = loanRequestO.getNumberOfPaymentsPerYear();
		Integer maturityInNmbrOfPayments = loanRequestO.getMaturityInNmbrOfPayments();
		Boolean roundUpMaturityInNumberOfTerms = loanRequestO.getRoundUpMaturityInNumberOfTerms();
		BigDecimal denomination = loanRequestO.getDenomination();//new BigDecimal("1.00");
		Date disbDate = loanRequestO.getDisbursementDate(); 
		
		Calendar disbursementDate = Calendar.getInstance();
		disbursementDate.set(2020, Calendar.NOVEMBER, 25);
		
		disbursementDate.set(disbDate.getYear(), disbDate.getMonth(), disbDate.getDate());
		
		//TODO : alle input skal komme fra loanRequest
		DaycountConventionType daycountConventionType = DaycountConventionType.US_30_360;
		BigDecimal spreadMCI = new BigDecimal("0.003", CalculatorUtils.DEFAULT_MATH_CONTEXT);
		BigDecimal spreadMSP = new BigDecimal("0.005", CalculatorUtils.DEFAULT_MATH_CONTEXT);
		
		//Amortizationparameters dannes ud fra ønsket lånetype:
		FixedInterestRateBondAnnuityAmortizationParameters amortizationParameters = new FixedInterestRateBondAnnuityAmortizationParameters();
		amortizationParameters.setFixedAmortRateProAnno(BigDecimal.valueOf(0.02d));
		amortizationParameters.setFixedInterestRateProAnno(BigDecimal.valueOf(0.02d));
		
		LoanCalculatorService loanCalculator = new LoanCalculatorService();
		
		//TODO : calculate loan skal overloades/kaldes med loanRequestO:
		LoanCalculated loanCalculated = loanCalculator.calculateLoan(loanAmount, spotPrice, paymentsPerYear, maturityInNmbrOfPayments, roundUpMaturityInNumberOfTerms, denomination, disbursementDate.getTime(), daycountConventionType, amortizationParameters, spreadMCI, spreadMSP);
		return loanCalculated;
	}
}
