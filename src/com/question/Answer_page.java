package com.question;

import org.json.JSONArray;
import org.json.JSONObject;

import com.question.QuestionPage.Question;
import com.question.QuestionPage.QuestionAdapter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Answer_page extends Activity implements OnClickListener{

	public String question_id ;
	public String question_body;
	
	TextView question;
	Button giveAns;
	ListView answerlist;
	private ProgressDialog pd;
	private PostAsync pas;

	private JSONArray jArray;

	private Answer[] answer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.answer_page);
		
		Intent i=getIntent();
		question_id=i.getStringExtra("q_id");
		question_body=i.getStringExtra("q_body");
		
		giveAns = (Button) findViewById(R.id.button1);
		giveAns.setOnClickListener(this);
		question=(TextView) findViewById(R.id.question_anser_tv);
		answerlist = (ListView) findViewById(R.id.answer_list);

		question.setText(question_body);
		
		handle_answer();

	}

	private void handle_answer() {
		// TODO Auto-generated method stub
		pd = new ProgressDialog(Answer_page.this);
		pd.setTitle("getting answer");
		pd.setMessage("Please wait");
		pd.show();
		JSONObject ob = createAnswerReq();
		pas = new PostAsync(Answer_page.this);
		pas.execute(ob);

	}

	private JSONObject createAnswerReq() {

		JSONObject ob = new JSONObject();

		try {
			ob.put("url", "GetAnswers.php");

			JSONArray array = new JSONArray();

			JSONObject job1 = new JSONObject();
			job1.put("label", "q_id");
			job1.put("value",question_id);

			array.put(job1);

			ob.put("parameters", array);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ob;
	}

	public void getData(String st) {
		
		if (pd.isShowing()) {
			pd.dismiss();
		}

		String stats, message;

		try {
			JSONObject ob=new JSONObject(st);
			stats = ob.getString("Status");
			Log.d("kauser", "get question req = " + stats);

			if (stats.equals("OK")) {
				populate(ob);
			} else {
				message = ob.getString("Message");
				makeToast(message);
			}

		} catch (Exception e) {
			Log.d("kauser", "hello error heer in get question ");
			Log.d("rupam", e.getMessage());
			e.printStackTrace();
		}

	}

	private void populate(JSONObject jsonObj) {
		try {

			Log.d("question>>>>", jsonObj.toString());

			jArray = jsonObj.getJSONArray("Data");
			Log.d("question>>>>", jArray.toString());

			answer = new Answer[jArray.length()];

			Log.d("array size", " " + jArray.length());

			for (int i = 0; i < jArray.length(); i++) {

				JSONObject json_data = jArray.getJSONObject(i);
				answer[i] = new Answer(json_data.getString("ans_body"),json_data.getString( "ans_id"), json_data.getString("ans_docid"), json_data.getString("q_id"), json_data.getString("a_rank"));
				Log.d("data", i + " " + json_data.getString("q_id"));

			}
			AnswerAdapter adapter = new AnswerAdapter(
					getApplicationContext(), answer);
			answerlist.setAdapter(adapter);

			Log.d("getting data->", "complete");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	public void makeToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	class Answer {
		public String ans_body,ans_id,ans_docid,q_id,a_rank;
		
		public Answer(String ans_body,String ans_id,String ans_docid,String q_id,String a_rank) {
			this.ans_body=ans_body;
			this.ans_id=ans_id;
			this.ans_docid=ans_docid;
			this.q_id=q_id;
			this.a_rank=a_rank;
		}

		public void setRank(String i) {
			this.a_rank = i;
		}
	}
	
	class AnswerAdapter extends ArrayAdapter<Answer>{

		final Context context;
		final Answer[] data;
		
		public AnswerAdapter(Context c,Answer[] fd){
			super(c,R.layout.answer_row,fd);
			this.context=c;
			this.data=fd;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int i_rank=0;
			final int pos=position;
			final TextView rank_final;
			
			
			
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.answer_row,parent,false);
			
			TextView rankTv=(TextView)rowView.findViewById(R.id.rating_answer_tv);
			rankTv.setText(data[position].a_rank);
			rank_final=rankTv;
			
			TextView answer=(TextView) rowView.findViewById(R.id.answer_tv);
			answer.setText("answer : "+data[pos].ans_body);
			
			TextView docid=(TextView) rowView.findViewById(R.id.docid_answer_tv);
			docid.setText("doctor id : "+data[pos].ans_docid);
			
			Button increase=(Button) rowView.findViewById(R.id.inc_anser_button);
			Button decrease=(Button) rowView.findViewById(R.id.dec_answer_button);
			
			increase.setOnClickListener(new OnClickListener() {
				boolean upnotclicked=true;
				@Override
				public void onClick(View v) {
					if(is_loggedin()){
						if(upnotclicked){
							String i=Integer.toString((Integer.parseInt(data[pos].a_rank)+1));
							data[pos].setRank(i);
							rank_final.setText(i);
							upnotclicked=false;
						}
					}
					else{
						/*showDialog(LOGIN_DIALOG_ID);*/
					}
					
				}
			});
			
			decrease.setOnClickListener(new OnClickListener() {
				boolean downnotclicked=true;
				@Override
				public void onClick(View v) {
					if(is_loggedin()){
						if(downnotclicked){
							String i=Integer.toString((Integer.parseInt(data[pos].a_rank)-1));
							data[pos].setRank(i);
							rank_final.setText(i);
							downnotclicked=false;
							
							// upload data to set rank 
							
						}
					}
					else{
						/*showDialog(LOGIN_DIALOG_ID);*/
					}
					
				}
			});
			
		
			return rowView;
		}
	}
	
	 private boolean is_loggedin(){
	    	
		   	SharedPreferences perfs = getSharedPreferences("my_app_pref", MODE_PRIVATE);
		   	
			String id =perfs.getString("user_id",null);
			
			
			
			if(id!=null)return true;
			else return false;
			
			
	    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			giveAnswer();
			break;

		default:
			break;
		}
	}
	
	public void giveAnswer(){
		Intent ans_in = new Intent(Answer_page.this,GiveAnswer.class);
		ans_in.putExtra("q_id", question_id);
		ans_in.putExtra("q_body", question_body);
		ans_in.putExtra("user_id", "1");
		startActivity(ans_in);
	}

}
