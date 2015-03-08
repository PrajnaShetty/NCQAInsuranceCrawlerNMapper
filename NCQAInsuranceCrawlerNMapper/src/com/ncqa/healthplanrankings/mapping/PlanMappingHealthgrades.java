package com.ncqa.healthplanrankings.mapping;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.ncqa.healthplanrankings.mapping.utilities.JDBCConnection;
import com.ncqa.healthplanrankings.mapping.utilities.LevenshteinDistance;
import com.ncqa.healthplanrankings.mapping.utilities.MappingConfigurations;

import au.com.bytecode.opencsv.CSVWriter;

/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */

public class PlanMappingHealthgrades {
		
	private static Set<String> commonWordsSet;
	  
	  static {
		  commonWordsSet = new HashSet<String>();
		  commonWordsSet.add("insurance"); commonWordsSet.add("care"); commonWordsSet.add("health");
		  commonWordsSet.add("plan");  commonWordsSet.add("life"); commonWordsSet.add("and");
		  commonWordsSet.add("services");
	  }
	  
	  private static final char DELIMITER = ',';

	public static void main(String[] args) {
				
		Connection connHealthgrades = JDBCConnection.getConnectionHealthGrades();
		Connection connNCQA = JDBCConnection.getConnectionNCQA();
		try {
			PlanRetrieval planRetrieval = new PlanRetrieval();
			Map<Integer,String> ncqaPlanList =  planRetrieval.getNCQAPlans(connNCQA);
			List<String> healthgradesPlanList = planRetrieval.getHealthgradesPlan(connHealthgrades);
			doMappingByState(ncqaPlanList, healthgradesPlanList);
			//across states takes a very very long time to run.. 
			//doMappingAcrossStates(ncqaPlanList, healthgradesPlanList);
			System.out.println("Complete!!");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}		
	}
	
	//Map plans without considering states
	private static void doMappingAcrossStates(Map<Integer,String> ncqaPlanList, 
			List<String> healthgradesPlanList) throws IOException
	{
		System.out.println("Trying to map Healthgrades plans to NCQA plans (Across State)...");
		int minDistance = 1000;
		String matchingNcqaPlanId = null;
		String matchingNcqaPlan = null;
		List<String[]> records = new ArrayList<String[]>();
			
		FileWriter fileWriter = new FileWriter(MappingConfigurations.HEALTHGRADES_MAPPING_ACROSS_STATES_FILEPATH);
		CSVWriter writer = new CSVWriter(fileWriter, DELIMITER, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                   CSVWriter.NO_ESCAPE_CHARACTER, "\n");
	
		List<String> recordHeader = new ArrayList<String>();
		recordHeader.add(String.valueOf("serialnumber")); 
		recordHeader.add(String.valueOf("standardinsuranceplanname"));    // ncqa plan names
		recordHeader.add(String.valueOf("standardStates"));  // ncqa plan states
		recordHeader.add(String.valueOf("source"));
		recordHeader.add(String.valueOf("sourceinsuranceplanname"));
		recordHeader.add(String.valueOf("sourcestate"));
		recordHeader.add(String.valueOf("LevenshteinDistance"));
		String[] recordArray = new String[recordHeader.size()];
		recordHeader.toArray(recordArray);
        records.add(recordArray);
		int j = 1;
		
		for(String vitalsPlan : healthgradesPlanList)
		{
			minDistance = 1000;
			String[] nameAndStateVitals = vitalsPlan.split("\\|");
			String healthgradesPlanName = nameAndStateVitals[0];
			String healthgradesState = nameAndStateVitals[1];
			//similarPlanId = "NULL";
			//similarPlan = "NULL";
			for(Entry<Integer,String> ncqaPlan :ncqaPlanList.entrySet() )
			{
				String[] nameAndStateNCQA = ncqaPlan.getValue().split("\\|");
				String ncqaPlanName = nameAndStateNCQA[0];
				String ncqaPlanState = nameAndStateNCQA[1];
				String ncqaPlanType = nameAndStateNCQA[2];
							
				
					// get vitals plan name
					String healthgradesPlanStem = healthgradesPlanName.toLowerCase(); 
					 
					//remove common words from vitals plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(healthgradesPlanStem).find())
							healthgradesPlanStem = healthgradesPlanStem.replaceAll(commonWord, "");
					}
					healthgradesPlanStem = healthgradesPlanStem.replaceAll("\\s+", " ").trim();
					
					// get ncqa plan name
					String ncqaPlanStem = ncqaPlanName.toLowerCase();
					
					//remove common words from ncqa plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(ncqaPlanStem).find())
							ncqaPlanStem = ncqaPlanStem.replaceAll(commonWord, "");
					}
					ncqaPlanStem = ncqaPlanStem.replaceAll("\\s+", " ").trim();
					
					//System.out.println("VitalsPlan = "+healthgradesPlanStem+"  NCQAPlan = "+ncqaPlanStem);
					int distance = LevenshteinDistance.getLevenshteinDistance(healthgradesPlanStem, ncqaPlanStem);
					if (distance < minDistance)
					{
						minDistance = distance;
						matchingNcqaPlanId = ncqaPlan.getKey().toString();
						matchingNcqaPlan = ncqaPlan.getValue();					
					}
						
			}
			
			String[] nameAndStateMatchingNcqaPlan = matchingNcqaPlan.split("\\|");
			String matchingNcqaPlanName = nameAndStateMatchingNcqaPlan[0];
			String matchingNcqaPlanState = nameAndStateMatchingNcqaPlan[1];
			String matchingNcqaPlanType = nameAndStateMatchingNcqaPlan[2];
			
			if (minDistance == 0)
			{
				List<String> record = new ArrayList<String>();
				record.add(String.valueOf(j++));
				record.add(String.valueOf(matchingNcqaPlanName));
				record.add(String.valueOf(matchingNcqaPlanState));
				record.add(String.valueOf("Healthgrades"));
				record.add(String.valueOf(healthgradesPlanName));
				record.add(String.valueOf(healthgradesState));
				record.add(String.valueOf(minDistance));
				recordArray = new String[record.size()];
				record.toArray(recordArray);
		        records.add(recordArray);
			}
			
		}
		
		 writer.writeAll(records); 
		 writer.close();
		 
		 System.out.println("Done mapping.");}


	private static void doMappingByState(Map<Integer,String> ncqaPlanList, 
			List<String> healthgradesPlanList) throws IOException
	{
		System.out.println("Trying to map Healthgrades plans to NCQA plans (By State)...");
		int minDistance = 1000;
		String matchingNcqaPlanId = null;
		String matchingNcqaPlan = null;
		List<String[]> records = new ArrayList<String[]>();
			
		FileWriter fileWriter = new FileWriter(MappingConfigurations.HEALTHGRADES_MAPPING_BY_STATES_FILEPATH);
		CSVWriter writer = new CSVWriter(fileWriter, DELIMITER, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                   CSVWriter.NO_ESCAPE_CHARACTER, "\n");
	
		List<String> recordHeader = new ArrayList<String>();
		//recordHeader.add(String.valueOf("serialnumber")); 
		recordHeader.add(String.valueOf("standardinsuranceplanname"));    // ncqa plan names
		recordHeader.add(String.valueOf("standardStates"));  // ncqa plan states
		recordHeader.add(String.valueOf("source"));
		recordHeader.add(String.valueOf("sourceinsuranceplanname"));
		recordHeader.add(String.valueOf("sourceState"));
		//recordHeader.add(String.valueOf("LevenshteinDistance"));
		String[] recordArray = new String[recordHeader.size()];
		recordHeader.toArray(recordArray);
        records.add(recordArray);
		int j = 1;
		
		for(String vitalsPlan : healthgradesPlanList)
		{
			minDistance = 1000;
			String[] nameAndStateVitals = vitalsPlan.split("\\|");
			String healthgradesPlanName = nameAndStateVitals[0];
			String healthgradesState = nameAndStateVitals[1];
			//similarPlanId = "NULL";
			//similarPlan = "NULL";
			for(Entry<Integer,String> ncqaPlan :ncqaPlanList.entrySet() )
			{
				String[] nameAndStateNCQA = ncqaPlan.getValue().split("\\|");
				String ncqaPlanName = nameAndStateNCQA[0];
				String ncqaPlanState = nameAndStateNCQA[1];
				String ncqaPlanType = nameAndStateNCQA[2];
							
				if (ncqaPlanState.contains(healthgradesState))
				{
					// get vitals plan name
					String healthgradesPlanStem = healthgradesPlanName.toLowerCase(); 
					 
					//remove common words from vitals plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(healthgradesPlanStem).find())
							healthgradesPlanStem = healthgradesPlanStem.replaceAll(commonWord, "");
					}
					healthgradesPlanStem = healthgradesPlanStem.replaceAll("\\s+", " ").trim();
					
					// get ncqa plan name
					String ncqaPlanStem = ncqaPlanName.toLowerCase();
					
					//remove common words from ncqa plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(ncqaPlanStem).find())
							ncqaPlanStem = ncqaPlanStem.replaceAll(commonWord, "");
					}
					ncqaPlanStem = ncqaPlanStem.replaceAll("\\s+", " ").trim();
					
					//System.out.println("VitalsPlan = "+healthgradesPlanStem+"  NCQAPlan = "+ncqaPlanStem);
					int distance = LevenshteinDistance.getLevenshteinDistance(healthgradesPlanStem, ncqaPlanStem);
					if (distance < minDistance)
					{
						minDistance = distance;
						matchingNcqaPlanId = ncqaPlan.getKey().toString();
						matchingNcqaPlan = ncqaPlan.getValue();					
					}
										
				}
			}
			
			String[] nameAndStateMatchingNcqaPlan = matchingNcqaPlan.split("\\|");
			String matchingNcqaPlanName = nameAndStateMatchingNcqaPlan[0];
			String matchingNcqaPlanState = nameAndStateMatchingNcqaPlan[1];
			String matchingNcqaPlanType = nameAndStateMatchingNcqaPlan[2];
			
			if (minDistance <= 4)
			{
				List<String> record = new ArrayList<String>();
				//record.add(String.valueOf(j++));
				record.add(String.valueOf(matchingNcqaPlanName));
				record.add(String.valueOf(matchingNcqaPlanState));
				record.add(String.valueOf("Healthgrades"));
				record.add(String.valueOf(healthgradesPlanName));
				record.add(String.valueOf(healthgradesState));
				//record.add(String.valueOf(minDistance));
				recordArray = new String[record.size()];
				record.toArray(recordArray);
		        records.add(recordArray);
			}
			
		}
		
		 writer.writeAll(records); 
		 writer.close();
		 
		 System.out.println("Done mapping.");
	}
}