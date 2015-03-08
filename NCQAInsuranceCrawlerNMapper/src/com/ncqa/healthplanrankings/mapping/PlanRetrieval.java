package com.ncqa.healthplanrankings.mapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */
public class PlanRetrieval {
	
	public Map<Integer,String>	getNCQAPlans(Connection conn) throws SQLException{
		
		int planId = 0;
		Map<Integer,String> ncqaPlanList = new HashMap<>();
		Statement selectPlanStmt = null;
		String planName = null, planType = null, codes = null;
		String states = null;
			
		String query = "select planName, planType, states from msprojectdb_ncqa.NCQAInsurancePlansRating";
			
		try {
			selectPlanStmt = conn.createStatement();
			ResultSet rs = selectPlanStmt.executeQuery(query);
			while(rs.next())
			{
				planName = rs.getString(1);
				planType = rs.getString(2);
				states = rs.getString(3);			
				ncqaPlanList.put(++planId, planName+"|"+states +"|"+planType);  //planId - auto increment serial number
			}
		} catch (SQLException e) {
		
			e.printStackTrace();
		}finally{
			if (selectPlanStmt != null)
					selectPlanStmt.close();
		}
		System.out.println("NCQA Plan List Size = " + ncqaPlanList.size());

		return ncqaPlanList;
}
	
	
	public Map<String, String> getVitalsPlan(Connection conn) throws SQLException{
		
		Map<String, String> vitalsPlanList = new HashMap<>();
		String providerid = null, state = null, vitalPlan = null;
		Statement selectPlanStmt = null;
		
		String queryVital = "select providerid, state, insurances from msprojectdb.providersCleaned";
		
		ResultSet rsVitals;
		try {
			selectPlanStmt = conn.createStatement();
			rsVitals = selectPlanStmt.executeQuery(queryVital);
	
			while(rsVitals.next()){

				providerid = rsVitals.getString(1);
				state = rsVitals.getString(2);
				vitalPlan = rsVitals.getString(3);
				
				if (vitalPlan != null)
				{
					String[] planList = vitalPlan.split(";");
					for(String plan:planList)
					{	
						if (vitalsPlanList.containsKey(plan+"|"+state))
						{	
							String pid = vitalsPlanList.get(plan+"|"+state);
							pid = pid + ";" + providerid;
							vitalsPlanList.put(plan+"|"+state, pid);
							
						}
						else
						{
							vitalsPlanList.put(plan+"|"+state, providerid);
						}
							
					}
				}
			}		
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (selectPlanStmt != null)
				selectPlanStmt.close();
		}
		
		System.out.println("Vitals Plan List Size = " + vitalsPlanList.size());
		//for (Entry<String, String> plan: vitalsPlanList.entrySet())
		//System.out.println(plan.getValue());
		return vitalsPlanList;
	}
	
	
	public List<String> getHealthgradesPlan(Connection conn) throws SQLException{
		
		List<String> healthgradesPlanList = new ArrayList<>();
		String state = null, healthgradePlan = null;
		Statement selectPlanStmt = null;
		
		String queryVital = "select InsurancePlan, State from msprojectdb_ncqa.HealthgradesInsurancePlans";
		
		ResultSet rsHealthgrades;
		try {
			selectPlanStmt = conn.createStatement();
			rsHealthgrades = selectPlanStmt.executeQuery(queryVital);
	
			while(rsHealthgrades.next()){

				healthgradePlan = rsHealthgrades.getString(1);
				state = rsHealthgrades.getString(2);
				
				
				if (healthgradePlan != null)
				{					
							healthgradesPlanList.add(healthgradePlan+"|"+state);					
				}
			}		
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (selectPlanStmt != null)
				selectPlanStmt.close();
		}
		
		System.out.println("Healthgrades Plan List Size = " + healthgradesPlanList.size());
		//for (Entry<String, String> plan: vitalsPlanList.entrySet())
		//System.out.println(plan.getValue());
		return healthgradesPlanList;
	}

}
