package com.question;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResult extends Activity implements OnClickListener{
	
	private Button backButton;
	private TextView counterView;
	private MyData[] mData;
	private ListView lv;
	private MyKey[] myKey;
	private SuggestedDiseases[] suggestedDiseases;
	private String post_value;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        
        Bundle extras = getIntent().getExtras();
        String params = extras.getString("params");
        Log.d("RUPAM", params);
        
        backButton = (Button) findViewById(R.id.button1);
        backButton.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.result_list);
        counterView = (TextView) findViewById(R.id.textView2);
        
        try {
			yourKey(params);
			populate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
			finish();
			break;

		default:
			break;
		}
	}
	
	public void populate(){
		try{
			
			
			JSONObject ob = new JSONObject();
			ob.put("url", "GetSearchResult.php");
			
			JSONArray arr = new JSONArray();
			JSONObject job = new JSONObject();
			job.put("label", "var");
			job.put("value", post_value);
			Log.d("RUPAM", "pass values is "+post_value);
			arr.put(job);
			
			ob.put("parameters", arr);
			PostAsync pas = new PostAsync(SearchResult.this);			
			pas.execute(ob);
			
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void getResult(String resp) throws Exception{
		//counterView.setText(resp);
		int i,j;
		JSONArray mainArray = new JSONArray(resp);
		suggestedDiseases = new SuggestedDiseases[mainArray.length()];
		counterView.setText(mainArray.length()+"Diseases Found");
		for(i=0;i<mainArray.length();i++){
			JSONObject dob=mainArray.getJSONObject(i);
			
			String d_name,d_id,d_matches;
			d_name = dob.getString("Disease Name");
			d_id = dob.getString("Disease ID");
			d_matches = dob.getString("Matches");
			suggestedDiseases[i]=new SuggestedDiseases(d_matches,d_name, d_id, dob.getJSONArray("Symptoms"));
		}
		
		DataAdapter adapter = new DataAdapter(this, suggestedDiseases);
		lv.setAdapter(adapter);
	}
	
	public void showMatchedSymptoms(SuggestedDiseases sg){
		try {
			int i, j;
			Symptom[] mChidls = sg.childs;
			String result = "";
			for (i = 0; i < mChidls.length; i++) {
				for (j = 0; j < myKey.length; j++) {
					if (mChidls[i].symptom_id.equals(myKey[j].id)) {
						result += mChidls[i].symptom_parent + " : "
								+ mChidls[i].symptom_name + "\n";
						break;
					}
				}
			}

			// Toast.makeText(getApplicationContext(), result,
			// Toast.LENGTH_SHORT).show();
			final Dialog d = new Dialog(this);
			d.setContentView(R.layout.match_dialog);
			d.setTitle("Matched Symptoms");

			TextView resView = (TextView) d.findViewById(R.id.textView1);
			resView.setText(result);
			Button okButton = (Button) d.findViewById(R.id.button1);
			okButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					d.dismiss();
				}
			});
			d.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void gotoDiseasePage(SuggestedDiseases sg){
		Intent dis_in = new Intent(SearchResult.this,DiseasePage.class);
		dis_in.putExtra("disease_id", sg.disease_id);
		dis_in.putExtra("disease_name", sg.disease_name);
		startActivity(dis_in);
	}
	
	public void yourKey(String myargs) throws Exception{
		JSONArray array = new JSONArray(myargs);
		JSONArray pass_post=new JSONArray();
		myKey = new MyKey[array.length()];
		for(int i=0;i<array.length();i++){
			JSONObject ob = array.getJSONObject(i);
			myKey[i]=new MyKey();
			myKey[i].parent = ob.getString("parent");
			myKey[i].symptom = ob.getString("symptom");
			myKey[i].id = ob.getString("symp_id");
			
			pass_post.put(myKey[i].id);
		}
		
		String temp = pass_post.toString();
		post_value = temp.substring(1,temp.length()-1);
	}
	
	class MyKey{
		String parent,symptom,id;
	}

	class SuggestedDiseases{
		String disease_name,disease_id,matches;
		Symptom[] childs;
		
		public SuggestedDiseases(String mat,String dn,String di,JSONArray arr){
			disease_name = dn;
			disease_id = di;
			matches = mat;
			int i;
			
			childs = new Symptom[arr.length()];
			try{
				for(i=0;i<arr.length();i++){
					JSONObject ob = arr.getJSONObject(i);
					childs[i]=new Symptom();
					childs[i].symptom_id = ob.getString("symptom_id");
					childs[i].symptom_name = ob.getString("symptom_name");
					childs[i].symptom_parent = ob.getString("parent_name");
					childs[i].symptom_parentid = ob.getString("parent_id");
				}	
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	class Symptom{
		String symptom_name,symptom_id,symptom_parent,symptom_parentid;
	}
	
	public void testpopulate(){
		// get the data
		// testData();
		
		
		// load the adapter
		//DataAdapter adapter = new DataAdapter(this, mData);
		//lv.setAdapter(adapter);
	}
	
	public void testData(){
		mData = new MyData[3];
		
		mData[0] = new MyData();
		mData[0].name="Disease 1";
		mData[0].imgname="img_1.jpg";
		mData[0].count = 5;
		
		mData[1] = new MyData();
		mData[1].name="Disease 2";
		mData[1].imgname="img_2.jpg";
		mData[1].count = 3;
		
		mData[2] = new MyData();
		mData[2].name="Disease 3";
		mData[2].imgname="img_3.jpg";
		mData[2].count = 1;
	}
	
	class MyData{
		String name;
		int count;
		String imgname;
	}

	class DataAdapter extends ArrayAdapter<SuggestedDiseases> implements OnClickListener{

		final Context context;
		final SuggestedDiseases[] data;
		public DataAdapter(Context c,SuggestedDiseases[] d){
			super(c,R.layout.disease_result_row,d);
			this.context = c;
			this.data=d;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.disease_result_row, parent, false);
			
			
			TextView tv = (TextView) rowView.findViewById(R.id.dis_name);
			tv.setText(data[position].disease_name);
			tv.setTag(data[position]);
			tv.setOnClickListener(this);
			Button bt = (Button) rowView.findViewById(R.id.match_symp);
			bt.setText(data[position].matches+"\nMatches");
			bt.setTag(data[position]);
			bt.setOnClickListener(this);
			
			WebView wv = (WebView) rowView.findViewById(R.id.dis_img);
			String img_url = "<html><body><img width=\"40\" height=\"40\" src=\"http://medibuddy.phpfogapp.com/images/img_3.jpg\" /></body></html>";
			wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

			wv.loadData(img_url, "text/html", "utf-8");
			
			return rowView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.match_symp:
				//Toast.makeText(getApplicationContext(),v.getTag().toString(),Toast.LENGTH_SHORT).show();
				showMatchedSymptoms((SuggestedDiseases)v.getTag());
				break;
				
			case R.id.dis_name:
				Toast.makeText(getApplicationContext(),v.getTag().toString(),Toast.LENGTH_SHORT).show();
				gotoDiseasePage((SuggestedDiseases)v.getTag());
				break;

			default:
				break;
			}
		}
	}
}

