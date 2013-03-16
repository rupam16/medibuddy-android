package com.question;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

public class DiseaseDrugs extends Activity{
	String disease_id;
	String disease_name = "";
	TextView tv;
	ListView lv;
	ProgressDialog pd = null;
	MyData[] mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disease_drug);
		
		
		Bundle extra = getIntent().getExtras();
		try{
			disease_id = extra.getString("disease_id");
			disease_name = extra.getString("disease_name");
		}
		catch (Exception e) {
			// TODO: handle exception
			disease_id="1";
			
		}
		tv= (TextView) findViewById(R.id.textView1);
		lv = (ListView) findViewById(R.id.listView1);
		
		sendDataRequest();
	}

	
	public void sendDataRequest(){
		try{
			JSONObject ob = new JSONObject();
			
			ob.put("url", "GetDiseaseDrugs.php");
			
			JSONArray arr = new JSONArray();
			
			JSONObject job = new JSONObject();
			job.put("label", "disease_id");
			job.put("value", disease_id);
			arr.put(job);
			
			ob.put("parameters", arr);
			
			pd = new ProgressDialog(this);
			pd.setMessage("Fetching Drugs . . . ");
			pd.show();
			PostAsync pas = new PostAsync(DiseaseDrugs.this);
			pas.execute(ob);
		}
		
		catch (Exception e) {
			Log.d("TAG", e.getMessage());
		}
	}
	
	public void getData(String resp) throws Exception{
		if(pd.isShowing()){
			pd.dismiss();
		}
		JSONObject ob = new JSONObject(resp);
		
		if(ob.getString("Status").equals("OK")){
			// data came
			tv.setText("Drugs Suggestion for "+disease_name);
			parseData(ob.getJSONArray("Data"));
		}
		
		else {
			tv.setText(ob.getString("Message"));
			Toast.makeText(getApplicationContext(), "Drugs Load Error"+ob.getString("Message"), Toast.LENGTH_LONG).show();
		}
	}
	
	public void parseData(JSONArray array) throws Exception{
		
		int i;
		
		mData = new MyData[array.length()];
		for(i=0;i<array.length();i++){
			JSONObject ob = array.getJSONObject(i);
			
			mData[i]= new MyData();
			mData[i].name = ob.getString("drug_name");
		}
		DataAdapter adapter = new DataAdapter(this, mData);
		lv.setAdapter(adapter);
	}
	
	class MyData{
		String name;
	}
	
	public class DataAdapter extends ArrayAdapter<MyData>{
		final Context context;
		final MyData[] data;
		
		public DataAdapter(Context c,MyData[] d) {
			// TODO Auto-generated constructor stub
			super(c,R.layout.drug_row,d);
			this.context = c;
			 this.data = d;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.drug_row, parent, false);
			
			TextView drugname = (TextView) rowView.findViewById(R.id.textView1);
			drugname.setText(data[position].name);
			
			return rowView;
		}
	}
}
