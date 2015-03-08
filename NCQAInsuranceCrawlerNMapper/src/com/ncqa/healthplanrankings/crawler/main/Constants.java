package com.ncqa.healthplanrankings.crawler.main;



/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */

public class Constants {

	public static final String PRIVATE_PLAN_URL = "http://healthplanrankings.ncqa.org/2014/Default.aspx?"
			+ "PL=Commercial&SV=";
	
	public static final String MEDICARE_PLAN_URL = "http://healthplanrankings.ncqa.org/2014/Default.aspx?"
			+ "PL=Medicare&SV=";
	
	public static final String MEDICAID_PLAN_URL = "http://healthplanrankings.ncqa.org/2014/Default.aspx?"
			+ "PL=Medicaid&SV=";
	
	public static final String CSV_FILE_PATH = "/Users/prajnashetty/Documents"
			+ "/UCRcoursestuff/MS Project/CSV/ncqa_insurances_";
	
	public static final String PRIVATE_PLAN = "private";
	
	public static final String MEDICARE_PLAN = "medicare";
	
	public static final String MEDICAID_PLAN = "medicaid";
		
	public static final char DELIMITER = ',';
}
