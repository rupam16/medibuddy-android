package com.question;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AskQuestion extends Activity {
	Button cancel=null;
	Button post =null;
	String user_id,disease_id;
	String disease_name;
	EditText question_text;
	TextView tv ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ask_question);
		
		question_text = (EditText) findViewById(R.id.editText1);
		tv = (TextView) findViewById(R.id.textView1);
		Bundle extra = getIntent().getExtras();
		try{
			disease_id = extra.getString("disease_id");
			disease_name = extra.getString("disease_name");
			user_id = extra.getString("user_id");
		}
		catch (Exception e) {
			// TODO: handle exception
			disease_id="1";
			disease_name="";
			user_id = "1";
		}
		
		tv.append("\n"+disease_name);
		
		cancel=(Button) findViewById(R.id.cancel_post_button);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		post=(Button) findViewById(R.id.post_ques_button);
		post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ask(question_text.getText().toString());
			}
		});
		
		
		
	}
	
	public void ask(String question){
		
		try{
			JSONObject ob = new JSONObject();
			ob.put("url", "AskQuestion.php");
			
			JSONArray arr = new JSONArray();
			
			JSONObject job1 = new JSONObject();
			job1.put("label","did");
			job1.put("value", disease_id);
			arr.put(job1);
			
			JSONObject job2 = new JSONObject();
			job2.put("label","uid");
			job2.put("value", user_id);
			arr.put(job2);
			
			JSONObject job3 = new JSONObject();
			job3.put("label","body");
			job3.put("value", question);
			arr.put(job3);
			
			ob.put("parameters", arr);
			
			PostAsync pas = new PostAsync(AskQuestion.this);
			pas.execute(ob);
		}
		
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void getData(String resp) throws Exception{
		JSONObject ob = new JSONObject(resp);
		Toast.makeText(getApplicationContext(), ob.getString("Message"), Toast.LENGTH_LONG).show();
		if(ob.getString("Status").equals("OK")){
			finish();
		}
	}
}
