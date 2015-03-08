package com.ncqa.healthplanrankings.crawler.main;

import java.util.ArrayList;
import java.util.List;


/*
 * @author - Prajna Shetty (pshet001@ucr.edu)
 */
public class Headers {

	
	public static List<String> addHeader(String currentPlan)
	{
		List<String> headerRecord= null;
		if (currentPlan == Constants.PRIVATE_PLAN)
			headerRecord = addPrivateHeader();
		else if (currentPlan == Constants.MEDICARE_PLAN)
			headerRecord = addMedicareHeader();
		else if (currentPlan == Constants.MEDICAID_PLAN)
			headerRecord = addMedicaidHeader();
		return headerRecord;
	}
	/*
	 * This function adds Headers to the Private Insurance CSV
	 */
	public static List<String> addPrivateHeader()
	{
		List<String> headerRecord = new ArrayList<>(); 
		headerRecord.add("Plan Name");
		headerRecord.add("States");
		headerRecord.add("Plan Category");
		headerRecord.add("Type");
		headerRecord.add("Rank");		
		headerRecord.add("Overall Score");
		headerRecord.add("NCQA Accredition");
		headerRecord.add("Evaluation Year");
		headerRecord.add("Plan URL");
		headerRecord.add("Consumer Satisfaction");
		headerRecord.add("Getting care");
		headerRecord.add("Getting care easily");
		headerRecord.add("Getting care quickly");
		headerRecord.add("Satisfaction with physicians");
		headerRecord.add("How well doctors communicate");
		headerRecord.add("Rating personal doctor");
		headerRecord.add("Rating specialists");
		headerRecord.add("Rating care received");
		headerRecord.add("Coordination of Care");
		headerRecord.add("Health promotion and education");
		headerRecord.add("Shared decision making");
		headerRecord.add("Satisfaction with health plan services");
		headerRecord.add("Handling claims");
		headerRecord.add("Rating health plan");
		headerRecord.add("Prevention");
		headerRecord.add("Children and adolescents");
		headerRecord.add("Well-child visits, infants");
		headerRecord.add("Well-child visits, ages 3-6");
		headerRecord.add("Access for children ages 7-11");
		headerRecord.add("Adolescent well-care visits");
		headerRecord.add("Early immunizations");
		headerRecord.add("Adolescent immunizations");
		headerRecord.add("BMI percentile assessment");
		headerRecord.add("Nutrition Counseling");
		headerRecord.add("Physical activity counseling");
		headerRecord.add("Women's reproductive health");
		headerRecord.add("Timeliness of prenatal checkups");
		headerRecord.add("Postpartum care");
		headerRecord.add("Cancer screening");
		headerRecord.add("Breast cancer screening");
		headerRecord.add("Colorectal cancer screening");
		headerRecord.add("Human Papillomavirus Vaccine");
		headerRecord.add("Other preventive services");
		headerRecord.add("Adult BMI assessment");
		headerRecord.add("Chlamydia screening");
		headerRecord.add("Flu vaccinations for Adults ages 18-64");
		headerRecord.add("Treatment");
		headerRecord.add("Asthma");
		headerRecord.add("Medication Management for People with Asthma");
		headerRecord.add("Asthma Medication Ratio");
		headerRecord.add("Diabetes");
		headerRecord.add("Blood pressure control (140/80)");
		headerRecord.add("Blood pressure control (140/90)");
		headerRecord.add("Retinal eye exams");
		headerRecord.add("Glucose testing");
		headerRecord.add("Glucose control");
		headerRecord.add("LDL cholesterol screening");
		headerRecord.add("LDL cholesterol control");
		headerRecord.add("Monitoring kidney disease");
		headerRecord.add("Heart disease");
		headerRecord.add("Beta blocker after heart attack");
		headerRecord.add("Controlling high blood pressure");
		headerRecord.add("LDL cholesterol screening");
		headerRecord.add("LDL cholesterol control");
		headerRecord.add("Mental and behavioral health");
		headerRecord.add("Depression--adhering to medication for 12 weeks");
		headerRecord.add("Depression--adhering to medication for six months");
		headerRecord.add("Follow-up after hospitalization for mental illness");
		headerRecord.add("Alcohol or drug dependence treatment initiated");
		headerRecord.add("Alcohol or drug dependence treated for 30 days");
		headerRecord.add("Follow-up after ADHD diagnosis");
		headerRecord.add("Continued follow-up after ADHD diagnosis");
		headerRecord.add("Other treatment measures");
		headerRecord.add("Use of aspirin");
		headerRecord.add("Appropriate antibiotic use, adults with acute bronchitis");
		headerRecord.add("Appropriate testing and care, children with pharyngitis");
		headerRecord.add("Medication for rheumatoid arthritis");
		headerRecord.add("Monitoring key long-term medications");
		headerRecord.add("Steroid after hospitalization for acute COPD");
		headerRecord.add("Bronchodilator after hospitalization for acute COPD");
		headerRecord.add("Testing for COPD");
		headerRecord.add("Appropriate antibiotic use, children with URI");
		headerRecord.add("Appropriate use of imaging studies for low back pain");		
		return headerRecord;
	}
	
	/*
	 * This function adds Headers to the Medicare Insurance CSV
	 */
	public static List<String> addMedicareHeader()
	{
		List<String> headerRecord = new ArrayList<>(); 
		headerRecord.add("Plan Name");
		headerRecord.add("States");
		headerRecord.add("Plan Category");
		headerRecord.add("Type");
		headerRecord.add("Rank");	
		headerRecord.add("Overall Score");
		headerRecord.add("NCQA Accredition");
		headerRecord.add("Evaluation Year");
		headerRecord.add("Plan URL");
		headerRecord.add("Consumer Satisfaction");
		headerRecord.add("Getting care");
		headerRecord.add("Getting care easily");
		headerRecord.add("Getting care quickly");
		headerRecord.add("Satisfaction with physicians");
		headerRecord.add("How well doctors communicate");
		headerRecord.add("Rating personal doctor");
		headerRecord.add("Rating specialists");
		headerRecord.add("Rating care received");
		headerRecord.add("Satisfaction with health plan services");
		headerRecord.add("Rating health plan");
		headerRecord.add("Customer Service");
		headerRecord.add("Prevention");
		headerRecord.add("Breast cancer screening");
		headerRecord.add("Colorectal cancer screening");
		headerRecord.add("Glaucoma screening");
		headerRecord.add("Evaluating mental health status");
		headerRecord.add("Evaluating physical health status");
		headerRecord.add("Flu vaccinations for Adults ages 18-64");
		headerRecord.add("Pneumonia shots");
		headerRecord.add("Adult BMI assessment");
		headerRecord.add("Treatment");
		headerRecord.add("Diabetes");
		headerRecord.add("Blood pressure control (140/80)");
		headerRecord.add("Blood pressure control (140/90)");
		headerRecord.add("Retinal eye exams");
		headerRecord.add("Glucose testing");
		headerRecord.add("Glucose control");
		headerRecord.add("LDL cholesterol screening");
		headerRecord.add("LDL cholesterol control");
		headerRecord.add("Monitoring kidney disease");
		headerRecord.add("Heart disease");
		headerRecord.add("Beta blocker after heart attack");
		headerRecord.add("Controlling high blood pressure");
		headerRecord.add("Smoking advice");
		headerRecord.add("LDL cholesterol screening");
		headerRecord.add("LDL cholesterol control");
		headerRecord.add("Mental and behavioral health");
		headerRecord.add("Depression--adhering to medication for 12 weeks");
		headerRecord.add("Depression--adhering to medication for six months");
		headerRecord.add("Follow-up after hospitalization for mental illness");
		headerRecord.add("Alcohol or drug dependence treatment initiated");
		headerRecord.add("Alcohol or drug dependence treated for 30 days");
		headerRecord.add("Other treatment measures");
		headerRecord.add("Managing risk of falls");
		headerRecord.add("Avoiding harmful drug and disease interactions");
		headerRecord.add("Medication for rheumatoid arthritis");
		headerRecord.add("Monitoring key long-term medications");
		headerRecord.add("Steroid after hospitalization for acute COPD");		
		headerRecord.add("Bronchodilator after hospitalization for acute COPD");
		headerRecord.add("Testing for osteoporosis");
		headerRecord.add("Managing osteoporosis in women after fracture");
		headerRecord.add("Testing for COPD");		
		headerRecord.add("Avoiding high-risk medications");	
		return headerRecord;
	}
	
	/*
	 * This function adds Headers to the Medicaid Insurance CSV
	 */
	public static List<String> addMedicaidHeader()
	{
		List<String> headerRecord = new ArrayList<>(); 
		headerRecord.add("Plan Name");
		headerRecord.add("States");
		headerRecord.add("Plan Category");
		headerRecord.add("Type");
		headerRecord.add("Rank");	
		headerRecord.add("Overall Score");
		headerRecord.add("NCQA Accredition");
		headerRecord.add("Evaluation Year");
		headerRecord.add("Plan URL");
		headerRecord.add("Consumer Satisfaction");
		headerRecord.add("Getting care");
		headerRecord.add("Getting care easily");
		headerRecord.add("Getting care quickly");
		headerRecord.add("Satisfaction with physicians");
		headerRecord.add("How well doctors communicate");
		headerRecord.add("Rating personal doctor");
		headerRecord.add("Rating specialists");
		headerRecord.add("Rating care received");
		headerRecord.add("Coordination of Care");
		headerRecord.add("Health promotion and education");
		headerRecord.add("Shared decision making");		
		headerRecord.add("Satisfaction with health plan services");
		headerRecord.add("Rating health plan");
		headerRecord.add("Customer Service");
		headerRecord.add("Prevention");
		headerRecord.add("Children and adolescents");
		headerRecord.add("Well-child visits, infants");
		headerRecord.add("Well-child visits, ages 3-6");
		headerRecord.add("Access for children ages 7-11");
		headerRecord.add("Adolescent well-care visits");
		headerRecord.add("Early immunizations");
		headerRecord.add("Adolescent immunizations");
		headerRecord.add("BMI percentile assessment");
		headerRecord.add("Nutrition Counseling");
		headerRecord.add("Physical activity counseling");
		headerRecord.add("Women's reproductive health");
		headerRecord.add("Timeliness of prenatal checkups");
		headerRecord.add("Postpartum care");
		headerRecord.add("Cancer screening");		
		headerRecord.add("Breast cancer screening");		
		headerRecord.add("Human Papillomavirus Vaccine");
		headerRecord.add("Other preventive services");
		headerRecord.add("Adult BMI assessment");
		headerRecord.add("Chlamydia screening");
		headerRecord.add("Lead screening");
		headerRecord.add("Treatment");
		headerRecord.add("Asthma");
		headerRecord.add("Asthma Medication Ratio");
		headerRecord.add("Medication Management for People with Asthma");		
		headerRecord.add("Diabetes");
		headerRecord.add("Blood pressure control (140/80)");
		headerRecord.add("Blood pressure control (140/90)");
		headerRecord.add("Retinal eye exams");
		headerRecord.add("Glucose testing");
		headerRecord.add("Glucose control");
		headerRecord.add("LDL cholesterol screening");
		headerRecord.add("LDL cholesterol control");
		headerRecord.add("Monitoring kidney disease");
		headerRecord.add("Heart disease");
		headerRecord.add("Controlling high blood pressure");
		headerRecord.add("LDL cholesterol screening");
		headerRecord.add("LDL cholesterol control");
		headerRecord.add("Mental and behavioral health");
		headerRecord.add("Depression--adhering to medication for 12 weeks");
		headerRecord.add("Depression--adhering to medication for six months");
		headerRecord.add("Follow-up after ADHD diagnosis");
		headerRecord.add("Continued follow-up after ADHD diagnosis");		
		headerRecord.add("Follow-up after hospitalization for mental illness");
		headerRecord.add("Alcohol or drug dependence treated for 30 days");
		headerRecord.add("Alcohol or drug dependence treatment initiated");
		headerRecord.add("Schizophrenia: Diabetes screening for schizophrenia or bipolar");		
		headerRecord.add("Other treatment measures");
		headerRecord.add("Appropriate antibiotic use, adults with acute bronchitis");
		headerRecord.add("Appropriate testing and care, children with pharyngitis");
		headerRecord.add("Monitoring key long-term medications");
		headerRecord.add("Testing for COPD");
		headerRecord.add("Appropriate antibiotic use, children with URI");	
		headerRecord.add("Appropriate use of imaging studies for low back pain");
		headerRecord.add("Steroid after hospitalization for acute COPD");
		headerRecord.add("Bronchodilator after hospitalization for acute COPD");
		headerRecord.add("Medication for rheumatoid arthritis");
		return headerRecord;
	}
}
