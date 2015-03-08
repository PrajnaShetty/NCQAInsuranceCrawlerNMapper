package com.ncqa.healthplanrankings.crawler.main;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */
public class PlanDetailsCrawler {
	
	/*
	 * This function gets the plan details by clicking on the insurance name link in the table
	 */
	
	public static List<String> getPlanDetails(String planUrl, List<String> planDetailsList)
	{
		planDetailsList = getPage(planUrl, planDetailsList);
		return planDetailsList;
	}
	
	private static List<String> getPage(String url,  List<String> planDetailsList)
	{
		Document doc = null;
		try 
		{
			doc = Jsoup.connect(url).get();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		planDetailsList = getTableContents(doc, planDetailsList);
		return planDetailsList;
	}

	private static List<String> getTableContents(Document doc,  List<String> planDetailsList)
	{

		Elements mainTables = doc.select("table[class=CompositeTable]");
		for (Element mainTable : mainTables)
		{
			Elements measureTables = mainTable.select("table[class=MeasureTable]");
			for (Element measureTable : measureTables)
			{	
				Element header = measureTable.select("thead").get(0);
				//String text = header.select("span[class=label report-details-header]").text();
				//System.out.println("text = "+ text);
				Element headerRow = null;
				if (header.select("tr").size() == 2)
					headerRow = header.select("tr").get(1);
				else
					headerRow = header.select("tr").get(0);
				String headerName = headerRow.select("td").get(1).text();
				//System.out.println("headerName = "+ headerName);
				Element headerRating = headerRow.select("td").get(2);
				Elements headerRatingImage = headerRating.select("img");
				String imgSrc = headerRatingImage.attr("src");
				String score = null;
				if (imgSrc != null && imgSrc.length() > 0)
				{	
					score = String.valueOf(imgSrc.charAt(12));
					if (score.equals("I") || score.equals("N"))
						score = "\\N";
					//System.out.println("Rating = "+imgSrc.charAt(12));
				}
				else
				{	
					score = "\\N";
					//System.out.println("Rating = " +"\\N");	
				}
				planDetailsList.add(score);
				
				Element body = measureTable.select("tbody").get(0);
				Element detailsTable = body.select("table[class=detailsTable]").first();
				
				Elements rows = detailsTable.select("tr[valign=middle]");
				for(Element row : rows)
				{
					Element criteriaColumn = row.select("td").get(0);
					criteriaColumn.select("font").remove();
					
					String criteria = criteriaColumn.text().replaceAll("\u00a0","").trim();
					//System.out.println("Criteria = "+criteria);
					
					Element rating = row.select("td").get(1);
					Elements ratingsImage = rating.select("img");
					imgSrc = ratingsImage.attr("src");
					score = null;
					if (imgSrc != null && imgSrc.length() > 0)
					{	
						score = String.valueOf(imgSrc.charAt(12));
						if (score.equals("I") || score.equals("N"))
							score = "\\N";
						//System.out.println("Rating = "+imgSrc.charAt(12));
					}
					else
					{	
						score = "\\N";
						//System.out.println("Rating = " +"\\N");	
					}					
					planDetailsList.add(score);
				}						
			}
		}
	
		return planDetailsList;
	}

}
