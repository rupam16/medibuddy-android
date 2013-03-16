package com.question;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener{
	private Button loginButton,signupButton,findButton,bodyFindButton,logoutButton;
	final int LOGIN_DIALOG=1;
	final int SIGNUP_DIALOG=2;
	final int LOGIN_ERROR_INCOMPLETE=3;
	final int LOGIN_ERROR_WRONG=4;
	
	String error_msg="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findButton = (Button) findViewById(R.id.button1);
		loginButton = (Button) findViewById(R.id.button2);
		signupButton = (Button) findViewById(R.id.button3);
		bodyFindButton = (Button) findViewById(R.id.button4);
		logoutButton = (Button) findViewById(R.id.button5);
		
		findButton.setOnClickListener(this);
		signupButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		bodyFindButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			// goto find
			Intent min = new Intent(Main.this,SymptomInput.class);
			startActivity(min);
			break;

		case R.id.button2:
			// do login
			showDialog(LOGIN_DIALOG);
			break;
			
		case R.id.button3:
			// do signup
			Intent sign_in = new Intent(Main.this,Signup.class);
			startActivity(sign_in);
			break;
			
		case R.id.button4:
			Intent mapiing_in = new Intent(Main.this,ImageMapTestActivity.class);
			startActivity(mapiing_in);
			break;
			
		case R.id.button5:
			logout();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LOGIN_DIALOG:
			final Dialog login_d = new Dialog(this);
			login_d.setContentView(R.layout.login);
			login_d.setTitle("MediBuddy Login");
			
			final EditText usernameField = (EditText) login_d.findViewById(R.id.editText1);
			final EditText passwordField = (EditText) login_d.findViewById(R.id.editText2);
			
			Button okButton = (Button) login_d.findViewById(R.id.login_ok);
			okButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					login_d.dismiss();
					if(usernameField.getText().toString().length()!=0 && 
							passwordField.getText().toString().length()!=0){
						login(usernameField.getText().toString(),passwordField.getText().toString());
					}
					
					else{
						showDialog(LOGIN_ERROR_INCOMPLETE);
					}
				}
			});
			
			Button cancelButton = (Button) login_d.findViewById(R.id.login_cancel);
			cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					login_d.dismiss();
				}
			});
			return login_d;

		case LOGIN_ERROR_INCOMPLETE:
			return makeErrorDialog("Please fill username and password");
			
		case LOGIN_ERROR_WRONG:
			return makeErrorDialog(error_msg);
		default:
			break;
		}
		return null;
	}
	
	public Dialog makeErrorDialog(String str){
		final Dialog d = new Dialog(this);
		d.setContentView(R.layout.error_dialog);
		d.setTitle("Error");
		TextView msgtv= (TextView) d.findViewById(R.id.textView1);
		msgtv.setText(str);
		
		Button okButton = (Button) d.findViewById(R.id.button1);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();
			}
		});
		
		return d;
	}
	
	public void login(String u,String p){
		try{
			JSONObject ob = new JSONObject();
			
			ob.put("url", "Login.php");
			
			JSONArray arr = new JSONArray();
			JSONObject job = new JSONObject();
			job.put("label", "Username");
			job.put("value", u);
			arr.put(job);
			
			JSONObject job1 = new JSONObject();
			job1.put("label", "Password");
			job1.put("value", p);
			arr.put(job1);
			
			ob.put("parameters", arr);
			
			PostAsync pas = new PostAsync(Main.this);
			pas.execute(ob);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void loginResponse(String resp) throws Exception{
		JSONObject ob = new JSONObject(resp);
		
		if(ob.getString("Status").equals("OK")){
			SharedPreferences pref = getSharedPreferences("my_app_pref", MODE_PRIVATE);
			Editor editor = pref.edit();
						
			JSONObject job = ob.getJSONObject("Data");
			editor.putString("role_id", job.getString("role_id"));
			editor.putString("username", job.getString("username"));
			editor.putString("user_id", job.getString("user_id"));
			editor.commit();
			
			Toast.makeText(getApplicationContext(), "Succeful login", Toast.LENGTH_LONG).show();
			Intent go_in = new Intent(Main.this,SymptomInput.class);
			startActivity(go_in);
		}
		else{
			error_msg = ob.getString("Message");
			showDialog(LOGIN_ERROR_WRONG);
		}
	}
	
	public void logout(){
		SharedPreferences pref = getSharedPreferences("my_app_pref", MODE_PRIVATE);
		Editor editor = pref.edit();
		
		editor.clear();
		editor.commit();
	}
}
