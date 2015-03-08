package com.ncqa.healthplanrankings.crawler.main;

import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import au.com.bytecode.opencsv.CSVWriter;

/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */

public class InsurancePlanCrawler {
	
	private static String currentPlan ;
	
	private static WebDriver driver ;
	
	private static WebDriverWait wait ;
	
	private static List<String[]> allPlansRecords;
	
	private static boolean lastPage = false ;
	
	private static String evaluationYear;
	
	
	public static void main(String args[])
	{
		currentPlan = Constants.PRIVATE_PLAN;
		initialSetup(Constants.PRIVATE_PLAN_URL);
		getPage(Constants.PRIVATE_PLAN_URL);
		
		currentPlan = Constants.MEDICARE_PLAN;
		initialSetup(Constants.MEDICARE_PLAN_URL);
		getPage(Constants.MEDICARE_PLAN_URL);
		
		currentPlan = Constants.MEDICAID_PLAN;
		initialSetup(Constants.MEDICAID_PLAN_URL);
		getPage(Constants.MEDICAID_PLAN_URL);
		
	}
	
	/*
	 * This function connects to the web page containing the data
	 */
	private static void getPage(String url)
	{

		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String title = doc.title();
		System.out.println("Title = " +title);
		evaluationYear = doc.title().substring(11);
		getTableContents(doc, url);
		
		//return;
	}
	
	/*
	 * This function gets the contents from the table on the NCQA web site
	 */
	private static void getTableContents(Document doc, String url)
	{
		String[] plansArray = null;
		String planName = null, planUrl = null, ncqaAccredition = null,
				states = null, type = null, overallScore = null, planRank = null;
		List<String> planDetailsList = null;
		
		
		doc.setBaseUri("http://healthplanrankings.ncqa.org/2014/");
				
		Element table = doc.select("table[class=displaytable]").first();
		for (Element row : table.select("tr:gt(0)")) 
		{			
			planName = row.getElementsByTag("td").get(1).text();
			System.out.println("Plan Name = "+ planName);
			if (planName.equalsIgnoreCase("Next") || planName.equalsIgnoreCase("First"))
			{	
				getNextPage(url);
				continue;
			}
			
			planDetailsList = new ArrayList<>();
			planDetailsList.add(planName);
			
			states = row.getElementsByTag("td").get(2).text();
			if (states.length() > 0)
				planDetailsList.add(states);
			else
				planDetailsList.add("\\N");
			System.out.println("States = "+states);
			
			planDetailsList.add(currentPlan);
			System.out.println("Plan category = "+currentPlan);
			
			type = row.getElementsByTag("td").get(3).text();
			if (type.length() > 0)
				planDetailsList.add(type);
			else
				planDetailsList.add("\\N");
			System.out.println("Type = "+type);
			
			planRank = row.getElementsByTag("td").get(0).text();
			if (planRank.length() > 0)
				planDetailsList.add(planRank);
			else
				planDetailsList.add("\\N");
			System.out.println("Plan Rank = "+planRank);
			
			overallScore = row.getElementsByTag("td").get(4).text();
			if (overallScore.length() > 0)
				planDetailsList.add(overallScore);
			else
				planDetailsList.add("\\N");
			System.out.println("Overall Score = "+overallScore);
			
			ncqaAccredition = row.getElementsByTag("td").get(5).text();
			if (ncqaAccredition.length() > 0)
				planDetailsList.add(ncqaAccredition);
			else 
				planDetailsList.add("\\N");
			System.out.println("ncqa Accredition = "+ncqaAccredition);
			
			planDetailsList.add(evaluationYear);
			
			Element link = row.getElementsByTag("td").get(1).select("a").first(); 
			planUrl = link.attr("abs:href");
			if (planUrl.length() == 0)
			{
				link.setBaseUri("http://healthplanrankings.ncqa.org/2014/");
				planUrl = link.attr("href");
			}
			planDetailsList.add(planUrl);
			System.out.println("Plan Url = "+ planUrl);
			System.out.println("*******************************************");
			
			planDetailsList = PlanDetailsCrawler.getPlanDetails(planUrl, planDetailsList);
			plansArray = new String[planDetailsList.size()];
			planDetailsList.toArray(plansArray);
			allPlansRecords.add(plansArray);
	    }
				
		System.out.println("Done");		
		//return;
	}

	/*
	 * This function gets the next page and again calls getTableContents()
	 */
	private static void getNextPage(String url) 
	{
		List<WebElement> elements = driver.findElements(By.linkText("Next"));		
		if (elements.size() > 0)
		{
			elements.get(0).click();
			String pageSource =  driver.getPageSource();
			Document doc = Jsoup.parse(pageSource);
			getTableContents(doc, url);
		}
		else
		{
			System.out.println("Last page done");
			driver.close();
			printListContentsInCSV();
			return;

		}		
	}
	
	/*
	 * This function prints the data in CSVs
	 */
	private static void printListContentsInCSV() 
	{
		System.out.println("Printing plans data into CSV");
		try {
			FileWriter fileWriter = new FileWriter(Constants.CSV_FILE_PATH + currentPlan + ".csv" );
			CSVWriter csvWriter = new CSVWriter(fileWriter, Constants.DELIMITER, 
					CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, "\n");	
			 csvWriter.writeAll(allPlansRecords); 
			 csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done printing");
	}

	private static void initialSetup(String url)
	{
		System.setProperty("webdriver.chrome.driver", 
				"/Applications/jars/chromedriver");
		
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS); 
		driver.get(url);
		
		allPlansRecords = new ArrayList<String[]>();
		
		List<String> headerRecord = Headers.addHeader(currentPlan);
		String[] plansArray = new String[headerRecord.size()];
		headerRecord.toArray(plansArray);
		allPlansRecords.add(plansArray);
	}
	
	
}
