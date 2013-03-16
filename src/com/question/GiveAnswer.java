package com.question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GiveAnswer extends Activity implements OnClickListener{
	String q_id,q_body,user_id;
	EditText body ;
	Button okButton,cancelButton;
	TextView tv ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.giveanswer);
		
		
		Bundle extra = getIntent().getExtras();
		try{
			q_id  = extra.getString("q_id");
			q_body = extra.getString("q_body");
			user_id = extra.getString("user_id");
		}
		catch (Exception e) {
			q_id="1";
			q_body= "Blank";
			user_id="1";
		}
		
		Log.d("QID", q_id);
		Log.d("BODY", q_body);
		Log.d("USERID", user_id);
		
		tv = (TextView) findViewById(R.id.textView1);
		tv.setText(q_body);
		body = (EditText) findViewById(R.id.editText1);
		
		okButton = (Button) findViewById(R.id.button1);
		cancelButton = (Button) findViewById(R.id.button2);
		
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			try {
				postAnswer();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.button2:
			finish();
			break;
		default:
			break;
		}
	}
	
	public void postAnswer() throws JSONException{
		String answer_body = body.getText().toString();
		
		if(answer_body.length()==0) {
			Toast.makeText(getApplicationContext(), "Please fill the box", Toast.LENGTH_LONG).show();
			return;
			
		}
		
		JSONObject ob = new JSONObject();
		ob.put("url", "PostAnswer.php");
		
		JSONArray array = new JSONArray();
		JSONObject job = new JSONObject();
		job.put("label", "ans_body");
		job.put("value", answer_body);
		
		JSONObject job1 = new JSONObject();
		job1.put("label", "ans_docid");
		job1.put("value", user_id);
		
		JSONObject job2 = new JSONObject();
		job2.put("label", "q_id");
		job2.put("value", q_id);
		
		array.put(job);
		array.put(job1);
		array.put(job2);
		
		ob.put("parameters", array);
		
		PostAsync pas = new PostAsync(GiveAnswer.this);
		pas.execute(ob);
	}
	
	public void getData(String resp) throws Exception{
		JSONObject ob = new JSONObject(resp);
		
		Toast.makeText(getApplicationContext(), ob.getString("Message"), Toast.LENGTH_LONG).show();
		if(ob.getString("Status").equals("OK")){
			finish();
		}
	}
}
