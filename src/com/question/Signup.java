package com.question;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Signup extends Activity implements OnClickListener{
	private EditText usernameEditText,passwordEditText,emailEditText,docidEditText;
	private Spinner usertypeSpinner;
	private Button okButton,cancalButton;
	private String[] userOptions={"User","Doctor"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		emailEditText = (EditText) findViewById(R.id.editText1);
		usernameEditText = (EditText) findViewById(R.id.editText2);
		passwordEditText = (EditText) findViewById(R.id.editText3);
		docidEditText = (EditText) findViewById(R.id.editText4);
		
		usertypeSpinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,userOptions);
		usertypeSpinner.setAdapter(adapter);
		usertypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				if(pos==0){
					docidEditText.setVisibility(View.INVISIBLE);
				}
				else{
					docidEditText.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		okButton = (Button) findViewById(R.id.button1);
		cancalButton = (Button) findViewById(R.id.button2);
		okButton.setOnClickListener(this);
		cancalButton.setOnClickListener(this);
		
		docidEditText.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			sendSignupRequest();
			break;

		case R.id.button2:
			// cancel
			finish();
			break;
		default:
			break;
		}
	}
	
	public void sendSignupRequest(){
		int selectedPos = usertypeSpinner.getSelectedItemPosition();
		String email,username,password,docid="";
		email = emailEditText.getText().toString();
		username = usernameEditText.getText().toString();
		password = passwordEditText.getText().toString();
		if(selectedPos!=0){
			docid = docidEditText.getText().toString();
		}
		
		if(email.length()==0) {
			viewToast("Email Required");
			return ;
		}
		
		if(username.length()==0){
			viewToast("Username Required");
			return ;
		}
		
		if(password.length()==0){
			viewToast("Password Required");
			return ;
		}
		
		if(selectedPos!=0 && docid.length()==0){
			viewToast("Doctor Identification Required");
			return ;
		}
		
		try{
			JSONObject ob = new JSONObject();
			ob.put("url", "Signup.php");
			JSONArray array = new JSONArray();
			
			JSONObject job1 = new JSONObject();
			job1.put("label", "Username");
			job1.put("value", username);
			
			JSONObject job2 = new JSONObject();
			job2.put("label", "Email");
			job2.put("value", email);
			
			JSONObject job3 = new JSONObject();
			job3.put("label", "Password");
			job3.put("value", password);
			
			JSONObject job4 = new JSONObject();
			job4.put("label", "isDoctor");
			if(selectedPos==0)job4.put("value", "0");
			else job4.put("value", "1");
			
			JSONObject job5 = new JSONObject();
			job5.put("label", "DocId");
			if(selectedPos==0)job5.put("value", "0");
			else job5.put("value", docid);
			
			
			array.put(job1);
			array.put(job2);
			array.put(job3);
			array.put(job4);
			array.put(job5);
			
			ob.put("parameters", array);
			
			PostAsync pas = new PostAsync(Signup.this);
			pas.execute(ob);
		}
		catch (Exception e) {
			Log.d("SIGNUP", e.getMessage());
		}
	}
	
	public void getData(String resp) throws Exception{
		JSONObject ob = new JSONObject(resp);
		viewToast(ob.getString("Message"));
		if(ob.getString("Status").equals("OK")){
			finish();
		}
	}
	
	public void viewToast(String msg){
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}
