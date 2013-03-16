package com.question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SymptomInput extends Activity {
	LinearLayout mainLayout;
	TextView[] nameLabels;
	Spinner[] nameSpinners;
	HereSymptom[][] datas;
	ScrollView scrollView;
	
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
    	super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
    	scrollView = new ScrollView(this);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(1);
        mainLayout.setBackgroundColor(Color.GREEN);
        
        TextView header = new TextView(this);
        header.setText("Choose your symptoms");
        header.setTextColor(Color.GRAY);
        mainLayout.addView(header);
        JSONArray array;
        String param;
        Bundle extra = getIntent().getExtras();
        try{
        	param = extra.getString("params");
        	sendRequest1(param);
        }
        catch (Exception e) {
			
			array = new JSONArray();
			array.put("fever");
    		array.put("cough");
    		array.put("rash");
    		array.put("headache");
    		array.put("stool");
    		array.put("vomit");
    		sendRequest(array);
		}
        
        
        
    }
    
    public void sendRequest(JSONArray array){
    	try{
    		JSONObject ob = new JSONObject();
    		
    		ob.put("url", "GetSymptoms.php");

    		String pass = array.toString();
    		pass = pass.substring(1,pass.length()-1);
    		Log.d("Rupam", "passing"+pass);
    		
    		JSONArray param = new JSONArray();
    		JSONObject job = new JSONObject();
    		job.put("label", "names");
    		job.put("value", pass);
    		param.put(job);
    		
    		ob.put("parameters", param);
    		
    		new PostAsync(SymptomInput.this).execute(ob);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    }
    
    public void sendRequest1(String pass){
    	try{
    		JSONObject ob = new JSONObject();
    		
    		ob.put("url", "GetSymptoms.php");

    		
    		
    		Log.d("Rupam", "passing"+pass);
    		
    		JSONArray param = new JSONArray();
    		JSONObject job = new JSONObject();
    		job.put("label", "names");
    		job.put("value", pass);
    		param.put(job);
    		
    		ob.put("parameters", param);
    		
    		new PostAsync(SymptomInput.this).execute(ob);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
		}
    }
    
    public void getData(String resp){
    	
    	try{
    		JSONArray param = new JSONArray(resp);
    		int i,len = param.length();
    		
    		nameLabels = new TextView[len];
    		nameSpinners = new Spinner[len];
    		datas = new HereSymptom[len][];
    		
    		for(i=0;i<len;i++){
        		JSONObject ob = param.getJSONObject(i);
        		parse(i,ob);
        	}
    		
    		Button findButton = new Button(this);
    		findButton.setText("Suggest me");
    		findButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						checkAll();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
    		mainLayout.addView(findButton);
    		scrollView.addView(mainLayout);
    		setContentView(scrollView);
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void checkAll() throws JSONException{
    	int i, spinnerLength = nameSpinners.length;
    	
    	JSONArray array = new JSONArray();
    	int pos;
    	for(i=0;i<spinnerLength;i++){
    		pos = nameSpinners[i].getSelectedItemPosition();
    		if(pos!=0){
    			JSONObject job = new JSONObject();
    			job.put("parent", nameLabels[i]);
    			job.put("symptom", datas[i][pos-1].name);
    			job.put("symp_id", datas[i][pos-1].id);
    			array.put(job);
    		}
    	}
    	
    	String pass = array.toString();
    	//pass = pass.substring(1,pass.length()-1);
    	
    	Intent go_in = new Intent(SymptomInput.this,SearchResult.class);
    	go_in.putExtra("params", pass);
    	startActivity(go_in);
    }
    
    public void parse(int x,JSONObject ob) throws Exception{
    	nameLabels[x] = new TextView(this);
    	nameLabels[x].setText(ob.getString("Parent"));
    	nameLabels[x].setTextColor(Color.RED);
    	
    	nameSpinners[x] = new Spinner(this);
    	
    	JSONArray ar = ob.getJSONArray("Symptom");
    	int len = ar.length();
    	
    	datas[x] = new HereSymptom[len];
    	String symptom_names[] = new String[len+1];
    	symptom_names[0] = new String("Select");
    	for(int i=0;i<len;i++){
    		JSONObject job = ar.getJSONObject(i);
    		datas[x][i] = new HereSymptom();
    		datas[x][i].name = job.getString("symptom_name");
    		datas[x][i].id = job.getString("symptom_id");
    		symptom_names[i+1] =datas[x][i].name; 
    	}
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,symptom_names);
    	nameSpinners[x].setAdapter(adapter);
    	
    	mainLayout.addView(nameLabels[x]);
    	mainLayout.addView(nameSpinners[x]);
    }
    
    class HereSymptom{
    	String name,id;
    }
    
    
}