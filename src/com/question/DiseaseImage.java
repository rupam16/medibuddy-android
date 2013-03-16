package com.question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DiseaseImage extends Activity{
	String disease_id;
	String disease_name="";
	ListView lv;
	TextView tv ;
	ProgressDialog pd = null;
	MyData[] mData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disease_image);
		
		
		Bundle extra = getIntent().getExtras();
		try{
			disease_id = extra.getString("disease_id");
			disease_name = extra.getString("disease_name");
		}
		catch (Exception e) {
			// TODO: handle exception
			disease_id="1";
			disease_name = "";
		}
		tv= (TextView) findViewById(R.id.textView1);
		lv = (ListView) findViewById(R.id.listView1);
		
		sendDataRequest();
	}
	
	public void sendDataRequest(){
		try{
			/*JSONObject ob = new JSONObject();
			
			ob.put("url", "GetDiseaseImage.php");
			
			JSONArray arr = new JSONArray();
			
			JSONObject job = new JSONObject();
			job.put("label", "disease_id");
			job.put("value", disease_id);
			arr.put(job);
			
			ob.put("parameters", arr);*/
			
			
			pd = new ProgressDialog(this);
			pd.setMessage("Fetching Symptom . . . ");
			pd.show();
			//PostAsync pas = new PostAsync(DiseaseImage.this);
			//pas.execute(ob);
			
			//post_async pas = new post_async(DiseaseImage.this);
			//pas.execute(ob);
			StringTokenizer tok = new StringTokenizer(disease_name);
			String myRes = "";
			
			while(tok.hasMoreTokens()){
				myRes+=tok.nextToken()+"+";
			}
			
			myRes = myRes.substring(0, myRes.length()-1);
			
			new MyImageGet().execute("http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+myRes);
		}
		
		catch (Exception e) {
			Log.d("TAG", e.getMessage());
		}
	}
	
	public void getData(String resp) throws Exception{
		
		if(pd.isShowing()){
			pd.dismiss();
		}
		
		Log.d("Here", "here1");
		JSONObject obj = new JSONObject(resp);
		Log.d("Here", "here2");
		JSONObject responseData = obj.getJSONObject("responseData");
		Log.d("Here", "here3");
		JSONArray results = responseData.getJSONArray("results");
		Log.d("Here", "here4");
		parseData(results);
		Log.d("Here", "here5");
	}
	
	public void parseData(JSONArray array) throws Exception{
		
		int i;
		
		mData = new MyData[array.length()];
		for(i=0;i<array.length();i++){
			JSONObject ob = array.getJSONObject(i);
			
			mData[i]= new MyData();
			mData[i].url = ob.getString("url");
		}
		DataAdapter adapter = new DataAdapter(this, mData);
		lv.setAdapter(adapter);
	}
	
	class MyData{
		String url;
	}
	
	public class DataAdapter extends ArrayAdapter<MyData>{
		final Context context;
		final MyData[] data;
		
		public DataAdapter(Context c,MyData[] d) {
			// TODO Auto-generated constructor stub
			super(c,R.layout.image_row,d);
			this.context = c;
			 this.data = d;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.image_row, parent, false);
			
			WebView wv = (WebView) rowView.findViewById(R.id.webView1);
			String customhtml = "<html><body><img src=\""+data[position].url+"\" width=\"200\" height=\"200\"/></body></html>";
			wv.loadData(customhtml, "text/html", "utf-8");
			
			return rowView;
		}
	}
	
	class MyImageGet extends AsyncTask<String, Integer, String>{
		private String resultString;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				getData(resultString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			func(params[0]);
			
			return null;
		}
		
		public void func(String url){
			String rs="";
			HttpClient httpclient=new DefaultHttpClient();
			HttpGet httppost=new HttpGet(url);
			

			HttpResponse response;
			try {
				response = httpclient.execute(httppost);
				rs=inputStreamToString(response.getEntity().getContent());
				resultString = rs;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
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
}
