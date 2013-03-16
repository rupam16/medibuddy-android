package com.question;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DoctorCard extends Activity {
	TextView doc_nameTextView, doc_expertiseTextView, doc_addressTextView,
			doc_visitTextView, doc_phoneTextView;

	JSONObject obj = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_card);
		
		doc_nameTextView = (TextView) findViewById(R.id.textView1);
		doc_expertiseTextView = (TextView) findViewById(R.id.textView2);
		doc_addressTextView = (TextView) findViewById(R.id.textView3);
		doc_phoneTextView = (TextView) findViewById(R.id.textView4);
		doc_visitTextView = (TextView) findViewById(R.id.textView5);
		
		try{
			Bundle extras = getIntent().getExtras();
			String js_ob = extras.getString("INFO");
			obj = new JSONObject(js_ob);
			populate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void populate() throws Exception{
		if(obj!=null){
			doc_nameTextView.setText(obj.getString("name"));
			doc_expertiseTextView.append(obj.getString("field"));
			doc_addressTextView.append(obj.getString("address"));
			doc_phoneTextView.append(obj.getString("phone"));
			doc_visitTextView.append(obj.getString("visit"));
		}
	}
}
