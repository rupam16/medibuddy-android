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

public class PostAsync extends AsyncTask<JSONObject, Integer,String>{
//	static String base_url = "http://medibuddy.phpfogapp.com/";
	static String base_url = "http://10.0.2.2/medibuddy-server/";
	private String resultString;

	private SymptomInput mySymptomInput=null;
	private SearchResult mySearchResult = null;
	private Main myMain = null ;
	private DiseasePageSymptom dps = null; 
	private DiseaseImage dimg = null;
	private DiseaseDrugs ddrg = null;
	private GiveAnswer gv = null;
	private AskQuestion quest_post=null;
	private Signup signUp = null;
	private Answer_page answer_page = null;
	
	public PostAsync(Answer_page answerPage) {
		// TODO Auto-generated constructor stub
		this.answer_page=answerPage;
	}

	public PostAsync(DiseasePageSymptom diseasePageSymptom) {
		this.dps = diseasePageSymptom;
	}

	public PostAsync(DiseaseImage diseaseImage) {
		this.dimg = diseaseImage;
	}

	public PostAsync(DiseaseDrugs diseaseDrugs) {
		this.ddrg = diseaseDrugs;
	}
	public PostAsync(SymptomInput symptomInput) {
		// TODO Auto-generated constructor stub
		this.mySymptomInput=symptomInput;
	}

	public PostAsync(SearchResult searchResult) {
		// TODO Auto-generated constructor stub
		this.mySearchResult=searchResult;
	}

	public PostAsync(Main main) {
		this.myMain = main;
	}

	public PostAsync(AskQuestion quesPost) {
		this.quest_post = quesPost;
	}

	public PostAsync(Signup signup) {
		this.signUp = signup;
	}

	public PostAsync(GiveAnswer giveAnswer) {
		this.gv = giveAnswer;
	}

	@Override
	protected String doInBackground(JSONObject... params) {
		JSONObject ob = params[0];
		connect_post(ob);
		return null;
	}
	
	protected void onPostExecute(String result) {
	    super.onPostExecute(result);
	    sendResult();
	}
	
	public void sendResult(){
		try{
			if(mySymptomInput!=null){
				mySymptomInput.getData(resultString);
			}
			
			else if(mySearchResult!=null){
				mySearchResult.getResult(resultString);
			}
			
			else if(myMain!=null){
				myMain.loginResponse(resultString);
			}
			
			else if(dps!=null){
				dps.getData(resultString);
			}
			
			else if(dimg!=null){
				dimg.getData(resultString);
			}
			
			else if(ddrg!=null){
				ddrg.getData(resultString);
			}
			
			else if(quest_post!=null){
				
				quest_post.getData(resultString);
			}
			
			else if(signUp!=null){
				
				signUp.getData(resultString);
			}
			
			else if(answer_page!=null){
				answer_page.getData(resultString);
			}
			
			else if(gv!=null){
				gv.getData(resultString);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}

	/**************** no need to modify *************************/
	private void connect_post(JSONObject obj){
		try {
			String url = base_url+obj.getString("url");
			
			HttpClient httpclient=new DefaultHttpClient();
			HttpPost httppost=new HttpPost(url);
			
			try{
				JSONArray paramArray=obj.getJSONArray("parameters");
				if(paramArray!=null){
					List<NameValuePair> namePair=new  ArrayList<NameValuePair>(paramArray.length());
					for(int i=0;i<paramArray.length();i++){
						JSONObject job = paramArray.getJSONObject(i);
						namePair.add(new BasicNameValuePair(job.getString("label"), job.getString("value")));
						httppost.setEntity(new UrlEncodedFormEntity(namePair));
					}
				}	
			}
			catch (Exception e) {}

			HttpResponse response =httpclient.execute(httppost);
			String rs=inputStreamToString(response.getEntity().getContent());
			Log.d("rupam -> ", rs);
			resultString = rs;
		} 
		catch (Exception e) {
			Log.d("rupam","connect_post_error>>"+e.getMessage());
		}
	}


	/****************** no need to modify it *******************/
	private String inputStreamToString(InputStream is) {
		Log.i("tag","after response ");
		String s = "";
	    String line = "";
	    
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    
	    
	    try {
			while ((line = rd.readLine()) != null) { s += line; }
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return s;
	}
}
