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
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class post_async extends AsyncTask<JSONObject, Integer,String>{
	
	
	/**
	 * kauser edited this*/
	private QuestionPage ques=null;
	
	private String resultString;
	
	/*private EmailLoginForm loginformClass=null;
	private SignUpForm signupformClass = null;
	private MyPage mypageClass = null;	
	*/
	
	/*public post_async(EmailLoginForm r) {

		this.loginformClass = r;
	}
	
	
	public post_async(SignUpForm signupForm) {
		// TODO Auto-generated constructor stub
		this.signupformClass = signupForm;
	}

	public post_async(MyPage myPage) {
		// TODO Auto-generated constructor stub
		this.mypageClass = myPage;
	}*/
	
	
	/**
	 * kauser edited this 
	 * */
	public post_async(QuestionPage ques) {
		// TODO Auto-generated constructor stub
		this.ques = ques;
	}

	@Override
	protected String doInBackground(JSONObject... params) {

		
		JSONObject ob = params[0];
		
		connect_post(ob);
		
		return null;
	}
	
	protected void onPostExecute(String result) {
	    super.onPostExecute(result);
	    /*Log.d("rupam","its inside man");
	    Toast.makeText(loginformClass, "hoise re", Toast.LENGTH_LONG).show();
	    Log.d("rupam","its inside man again");*/
	    
	    sendResult();
	}

	
	private void connect_post(JSONObject obj){
		try {
			String url = obj.getString("url");
			
			HttpClient httpclient=new DefaultHttpClient();
			HttpPost httppost=new HttpPost(url);
			
			JSONArray paramArray=obj.getJSONArray("parameters");
			List<NameValuePair> namePair=new  ArrayList<NameValuePair>(paramArray.length());
			for(int i=0;i<paramArray.length();i++){
				JSONObject job = paramArray.getJSONObject(i);
				namePair.add(new BasicNameValuePair(job.getString("label"), job.getString("value")));
			}
			
			httppost.setEntity(new UrlEncodedFormEntity(namePair));
			HttpResponse response =httpclient.execute(httppost);
			String rs=inputStreamToString(response.getEntity().getContent());
			Log.d("rupam -> ", rs);
			
			//sendResult(rs);
			resultString = rs;
		} 
		catch (Exception e) {
			Log.d("rupam","connect_post_error>>"+e.getMessage());
		}
	}
	
	/*public void sendResult(){
		try{
			if(this.loginformClass!=null){
				loginformClass.getData(new JSONObject(resultString));
			}
			else if(this.signupformClass != null ){
				signupformClass.getData(new JSONObject(resultString));
			}
			else if(this.mypageClass != null){
				mypageClass.getData(new JSONObject(resultString));
			}
		}
		catch(Exception e){
			Log.d("rupam", e.getMessage());
		}
		
		
	}*/
	
	public void sendResult(){
		try{
			/*if(this.loginformClass!=null){
				loginformClass.getData(new JSONObject(resultString));
			}
			else if(this.signupformClass != null ){
				signupformClass.getData(new JSONObject(resultString));
			}
			else if(this.mypageClass != null){
				mypageClass.getData(new JSONObject(resultString));
			}
			else*/
			if(this.ques!=null){
				ques.getData(new JSONObject(resultString));
			}
		}
		catch(Exception e){
			Log.d("rupam", e.getMessage());
		}
		
		
	}
	
	private String inputStreamToString(InputStream is) {
		Log.i("tag","after response ");
		String s = "";
	    String line = "";
	    
	    // Wrap a BufferedReader around the InputStream
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    
	    // Read response until the end
	    try {
			while ((line = rd.readLine()) != null) { s += line; }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // Return full string
	    return s;
	}
}
