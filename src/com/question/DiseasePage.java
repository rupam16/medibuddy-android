package com.question;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class DiseasePage extends TabActivity{
	String disease_id="";
	String disease_name="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diseasepage);
		
		Bundle extra = getIntent().getExtras();
		try{
			disease_id = extra.getString("disease_id");
			disease_name = extra.getString("disease_name");
		}
		catch (Exception e) {
			// TODO: handle exception
			disease_id="1";
			disease_name= "";
		}
		
		TabHost tabHost = getTabHost(); 
		
		Intent image_in = new Intent(DiseasePage.this,DiseaseImage.class);
		image_in.putExtra("disease_id", disease_id);
		image_in.putExtra("disease_name", disease_name);
		TabSpec imageTab = tabHost.newTabSpec("Disease Image");
		imageTab.setIndicator("Images");
		imageTab.setContent(image_in);
		
		
		Intent drugs_in = new Intent(DiseasePage.this,DiseaseDrugs.class);
		drugs_in.putExtra("disease_id", disease_id);
		drugs_in.putExtra("disease_name", disease_name);
		TabSpec drugsTab = tabHost.newTabSpec("Disease Drugs");
		drugsTab.setIndicator("Drugs");
		drugsTab.setContent(drugs_in);
		
		Intent symptom_in = new Intent(DiseasePage.this,DiseasePageSymptom.class);
		symptom_in.putExtra("disease_id", disease_id);
		symptom_in.putExtra("disease_name", disease_name);
		TabSpec symptomTab = tabHost.newTabSpec("Disease Symptom");
		symptomTab.setIndicator("Symptoms");
		symptomTab.setContent(symptom_in);
		
		
		Intent quest_in = new Intent(DiseasePage.this,QuestionPage.class);
		quest_in.putExtra("disease_id", disease_id);
		quest_in.putExtra("disease_name", disease_name);
		TabSpec questTab = tabHost.newTabSpec("Disease Question");
		questTab.setIndicator("Questions");
		questTab.setContent(quest_in);
		
		Intent doc_in = new Intent(DiseasePage.this,DiseaseDoctor.class);
		doc_in.putExtra("disease_id", disease_id);
		doc_in.putExtra("disease_name", disease_name);
		TabSpec docTab = tabHost.newTabSpec("Disease Doctor");
		docTab.setIndicator("Doctors");
		docTab.setContent(doc_in);
		
		tabHost.addTab(imageTab);
		tabHost.addTab(drugsTab);
		tabHost.addTab(symptomTab);
		tabHost.addTab(questTab);
		tabHost.addTab(docTab);
		
		tabHost.setCurrentTab(0);
	}
}