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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DiseaseDoctor extends Activity {

	private String disease_id, disease_name;
	Doctors[] doctors;
	JSONObject[] doc_obj = null;
	ListView lv;
	ProgressDialog pd=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disease_doctor);

		lv = (ListView) findViewById(R.id.listView1);

		Bundle extra = getIntent().getExtras();
		try {
			disease_id = extra.getString("disease_id");
			disease_name = extra.getString("disease_name");
		}

		catch (Exception e) {
			// TODO: handle exception
			disease_id = "2";
			disease_name = "Whooping Cough";
		}

		sendDoctorRequest();
	}
	
	public void sendDoctorRequest() {
		try {
			JSONObject ob = new JSONObject();
			
			JSONArray array = new JSONArray();
			
			JSONObject job = new JSONObject();
			job.put("label", "disease_id");
			job.put("value", disease_id);
			array.put(job);
			
			ob.put("url", "GetDoctorsForDisease.php");
			ob.put("parameters", array);

			new MyPostAsync().execute(ob);
			pd = new ProgressDialog(this);
			pd.setTitle("MediBuddy");
			pd.setMessage("Fetching Doctors. . .");
			pd.show();
			
		} catch (Exception e) {
			Log.d("Error", e.getMessage());
		}
	}

	public void sendDoctorRequest1() {
		try {
			JSONObject ob = new JSONObject();
			ob.put("url", "GetDoctorsForDisease.php");

			JSONArray array = new JSONArray();

			JSONObject job1 = new JSONObject();
			// job1.put("label", "disease_id");
			// job1.put("value", disease_id);

			JSONArray expert = new JSONArray();

			expert.put("Cardiac");
			expert.put("ENT");
			expert.put("Chest");

			String pass = expert.toString();
			pass = pass.substring(1, pass.length() - 1);
			Log.d("RUPAM", pass);

			/************************/
			job1.put("label", "cat");
			job1.put("value", pass);
			/*************************/

			array.put(job1);

			ob.put("parameters", array);

			new MyPostAsync().execute(ob);
		} catch (Exception e) {
			Log.d("Error", e.getMessage());
		}
	}

	public void getDoctor(String resp) {
		if(pd!=null){
			if(pd.isShowing()){
				pd.dismiss();
			}
		}
		try {
			JSONObject ob = new JSONObject(resp);

			if (ob.getString("Status").equals("OK")) {
				populate(ob.getJSONArray("Data"));
			}

			else {
				Toast.makeText(getApplicationContext(),
						ob.getString("Message"), Toast.LENGTH_LONG);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void populate(JSONArray array) {

		try {
			int len = array.length(), i;
			doctors = new Doctors[len];
			doc_obj = new JSONObject[len];
			for (i = 0; i < len; i++) {

				JSONObject job = array.getJSONObject(i);
				doc_obj[i] = job;
				doctors[i] = new Doctors();
				doctors[i].name = job.getString("name");
				doctors[i].address = job.getString("location");
			}

			// DataAdapter adapter = new DataAdapter(this, doctors);
			DataAdapter adapter = new DataAdapter(getApplicationContext(),
					doctors);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parentView, View view,
						int pos, long id) {
					gotoDoctorCard(pos);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gotoDoctorCard(int pos){
		String pass = doc_obj[pos].toString();
		Intent doc_in = new Intent(DiseaseDoctor.this,DoctorCard.class);
		doc_in.putExtra("INFO", pass);
		startActivity(doc_in);
	}

	class Doctors {
		String name, address;
	}

	class DataAdapter extends ArrayAdapter<Doctors> {
		final Context context;
		final Doctors[] data;

		public DataAdapter(Context c, Doctors[] d) {
			super(c, R.layout.doctor_row, d);
			this.context = c;
			this.data = d;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.doctor_row, parent, false);

			// String n=data[position].name,a=data[position].address;

			TextView docname = (TextView) rowView.findViewById(R.id.textView1);
			docname.setText(data[position].name);

			TextView location = (TextView) rowView.findViewById(R.id.textView2);
			location.setText(data[position].address);

			return rowView;
		}
	}

	class MyPostAsync extends AsyncTask<JSONObject, Integer, String> {
		String base_url = "http://medibuddy.phpfogapp.com/";
		private String resultString = "";

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

		public void sendResult() {
			getDoctor(resultString);
		}

		/**************** no need to modify *************************/
		private void connect_post(JSONObject obj) {
			try {
				String url = base_url + obj.getString("url");

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				try {
					JSONArray paramArray = obj.getJSONArray("parameters");
					if (paramArray != null) {
						List<NameValuePair> namePair = new ArrayList<NameValuePair>(
								paramArray.length());
						for (int i = 0; i < paramArray.length(); i++) {
							JSONObject job = paramArray.getJSONObject(i);
							namePair
									.add(new BasicNameValuePair(job
											.getString("label"), job
											.getString("value")));
							httppost.setEntity(new UrlEncodedFormEntity(
									namePair));
						}
					}
				} catch (Exception e) {
				}

				HttpResponse response = httpclient.execute(httppost);
				String rs = inputStreamToString(response.getEntity()
						.getContent());
				Log.d("rupam -> ", rs);
				resultString = rs;
			} catch (Exception e) {
				Log.d("rupam", "connect_post_error>>" + e.getMessage());
			}
		}

		/****************** no need to modify it *******************/
		private String inputStreamToString(InputStream is) {
			Log.i("tag", "after response ");
			String s = "";
			String line = "";

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((line = rd.readLine()) != null) {
					s += line;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return s;
		}
	}

}
