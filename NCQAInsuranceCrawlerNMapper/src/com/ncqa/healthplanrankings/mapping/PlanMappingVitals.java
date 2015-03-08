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

public class PlanMappingVitals {
		
	private static Set<String> commonWordsSet;
	  
	  static {
		  commonWordsSet = new HashSet<String>();
		  commonWordsSet.add("insurance"); commonWordsSet.add("care"); commonWordsSet.add("health");
		  commonWordsSet.add("plan");  commonWordsSet.add("life"); commonWordsSet.add("and");
		  commonWordsSet.add("services");
	  }
	  
	  private static final char DELIMITER = ',';

	public static void main(String[] args) {
				
		Connection connVitals = JDBCConnection.getConnectionVitals();
		Connection connNCQA = JDBCConnection.getConnectionNCQA();
		try {
			PlanRetrieval planRetrieval = new PlanRetrieval();
			Map<Integer,String> ncqaPlanList =  planRetrieval.getNCQAPlans(connNCQA);
			Map<String, String> vitalsPlanList = planRetrieval.getVitalsPlan(connVitals);
			doMappingByState(ncqaPlanList, vitalsPlanList);
			doMappingAcrossStates(ncqaPlanList, vitalsPlanList);
			System.out.println("Complete!!");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}		
	}
	
	//Map plans without considering states
	private static void doMappingAcrossStates(Map<Integer,String> ncqaPlanList, 
			Map<String, String> vitalsPlanList) throws IOException
	{
		System.out.println("Trying to map Vitals plans to NCQA plans (Across State)...");
		int minDistance = 1000;
		String matchingNcqaPlanId = null;
		String matchingNcqaPlan = null;
		List<String[]> records = new ArrayList<String[]>();
			
		FileWriter fileWriter = new FileWriter(MappingConfigurations.VITALS_MAPPING_ACROSS_STATES_FILEPATH);
		CSVWriter writer = new CSVWriter(fileWriter, DELIMITER, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                   CSVWriter.NO_ESCAPE_CHARACTER, "\n");
	
		List<String> recordHeader = new ArrayList<String>();
		recordHeader.add(String.valueOf("serialnumber")); 
		recordHeader.add(String.valueOf("standardinsuranceplanname"));    // ncqa plan names
		recordHeader.add(String.valueOf("standardStates"));  // ncqa plan states
		recordHeader.add(String.valueOf("source"));
		recordHeader.add(String.valueOf("sourceinsuranceplanname"));
		recordHeader.add(String.valueOf("sourceState"));
		recordHeader.add(String.valueOf("sourceprovidersid"));
		recordHeader.add(String.valueOf("LevenshteinDistance"));
		String[] recordArray = new String[recordHeader.size()];
		recordHeader.toArray(recordArray);
        records.add(recordArray);
		int j = 1;
		
		for(Entry<String, String> vitalsPlan : vitalsPlanList.entrySet())
		{
			minDistance = 1000;
			String[] nameAndStateVitals = vitalsPlan.getKey().split("\\|");
			String vitalsPlanName = nameAndStateVitals[0];
			String vitalsState = nameAndStateVitals[1];
			//similarPlanId = "NULL";
			//similarPlan = "NULL";
			for(Entry<Integer,String> ncqaPlan :ncqaPlanList.entrySet() )
			{
				String[] nameAndStateNCQA = ncqaPlan.getValue().split("\\|");
				String ncqaPlanName = nameAndStateNCQA[0];
				String ncqaPlanState = nameAndStateNCQA[1];
							
				
					// get vitals plan name
					String vitalsPlanStem = vitalsPlanName.toLowerCase(); 
					 
					//remove common words from vitals plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(vitalsPlanStem).find())
							vitalsPlanStem = vitalsPlanStem.replaceAll(commonWord, "");
					}
					vitalsPlanStem = vitalsPlanStem.replaceAll("\\s+", " ").trim();
					
					// get ncqa plan name
					String ncqaPlanStem = ncqaPlanName.toLowerCase();
					
					//remove common words from ncqa plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(ncqaPlanStem).find())
							ncqaPlanStem = ncqaPlanStem.replaceAll(commonWord, "");
					}
					ncqaPlanStem = ncqaPlanStem.replaceAll("\\s+", " ").trim();
					
					//System.out.println("VitalsPlan = "+vitalsPlanStem+"  NCQAPlan = "+ncqaPlanStem);
					int distance = LevenshteinDistance.getLevenshteinDistance(vitalsPlanStem, ncqaPlanStem);
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
			
			
			List<String> record = new ArrayList<String>();
			record.add(String.valueOf(j++));
			record.add(String.valueOf(matchingNcqaPlanName));
			record.add(String.valueOf(matchingNcqaPlanState));
			record.add(String.valueOf("Vitals.com"));
			record.add(String.valueOf(vitalsPlanName));
			record.add(String.valueOf(vitalsState));
			record.add(String.valueOf(vitalsPlan.getValue())); //corresponding Provider Ids
			record.add(String.valueOf(minDistance));
			recordArray = new String[record.size()];
			record.toArray(recordArray);
	        records.add(recordArray);
			
		}
		
		 writer.writeAll(records); 
		 writer.close();
		 
		 System.out.println("Done mapping.");}


	private static void doMappingByState(Map<Integer,String> ncqaPlanList, 
			Map<String, String> vitalsPlanList) throws IOException
	{
		System.out.println("Trying to map Vitals plans to NCQA plans (By State)...");
		int minDistance = 1000;
		String matchingNcqaPlanId = null;
		String matchingNcqaPlan = null;
		List<String[]> records = new ArrayList<String[]>();
			
		FileWriter fileWriter = new FileWriter(MappingConfigurations.VITALS_MAPPING_BY_STATES_FILEPATH);
		CSVWriter writer = new CSVWriter(fileWriter, DELIMITER, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                   CSVWriter.NO_ESCAPE_CHARACTER, "\n");
	
		List<String> recordHeader = new ArrayList<String>();
		//recordHeader.add(String.valueOf("serialnumber")); 
		recordHeader.add(String.valueOf("standardinsuranceplanname"));    // ncqa plan names
		recordHeader.add(String.valueOf("standardStates"));  // ncqa plan states
		recordHeader.add(String.valueOf("source"));
		recordHeader.add(String.valueOf("sourceinsuranceplanname"));
		recordHeader.add(String.valueOf("sourceState"));
		//recordHeader.add(String.valueOf("sourceprovidersid"));
		//recordHeader.add(String.valueOf("LevenshteinDistance"));
		String[] recordArray = new String[recordHeader.size()];
		recordHeader.toArray(recordArray);
        records.add(recordArray);
		int j = 1;
		
		for(Entry<String, String> vitalsPlan : vitalsPlanList.entrySet())
		{
			minDistance = 1000;
			String[] nameAndStateVitals = vitalsPlan.getKey().split("\\|");
			String vitalsPlanName = nameAndStateVitals[0];
			String vitalsState = nameAndStateVitals[1];
			//similarPlanId = "NULL";
			//similarPlan = "NULL";
			for(Entry<Integer,String> ncqaPlan :ncqaPlanList.entrySet() )
			{
				String[] nameAndStateNCQA = ncqaPlan.getValue().split("\\|");
				String ncqaPlanName = nameAndStateNCQA[0];
				String ncqaPlanState = nameAndStateNCQA[1];
							
				if (ncqaPlanState.contains(vitalsState))
				{
					// get vitals plan name
					String vitalsPlanStem = vitalsPlanName.toLowerCase(); 
					 
					//remove common words from vitals plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(vitalsPlanStem).find())
							vitalsPlanStem = vitalsPlanStem.replaceAll(commonWord, "");
					}
					vitalsPlanStem = vitalsPlanStem.replaceAll("\\s+", " ").trim();
					
					// get ncqa plan name
					String ncqaPlanStem = ncqaPlanName.toLowerCase();
					
					//remove common words from ncqa plan
					for(String commonWord : commonWordsSet)
					{
						if (Pattern.compile(".*\\b" + commonWord + "\\b.*").matcher(ncqaPlanStem).find())
							ncqaPlanStem = ncqaPlanStem.replaceAll(commonWord, "");
					}
					ncqaPlanStem = ncqaPlanStem.replaceAll("\\s+", " ").trim();
					
					//System.out.println("VitalsPlan = "+vitalsPlanStem+"  NCQAPlan = "+ncqaPlanStem);
					int distance = LevenshteinDistance.getLevenshteinDistance(vitalsPlanStem, ncqaPlanStem);
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
			
			if (minDistance == 0)
			{
				List<String> record = new ArrayList<String>();
				//record.add(String.valueOf(j++));
				record.add(String.valueOf(matchingNcqaPlanName));
				record.add(String.valueOf(matchingNcqaPlanState));
				record.add(String.valueOf("Vitals.com"));
				record.add(String.valueOf(vitalsPlanName));
				record.add(String.valueOf(vitalsState));
				//record.add(String.valueOf(vitalsPlan.getValue())); //corresponding Provider Ids
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