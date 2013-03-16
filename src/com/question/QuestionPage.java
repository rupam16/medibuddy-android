package com.question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class QuestionPage extends Activity implements OnClickListener{
	
	private static final int LOGIN_DIALOG_ID=0; 
	private static final int FB_POST=1;
	
	/**
	 * request_type=0 get question req
	 * request_type=1 login req 
	 * 
	 */
	int request_type;
	
	
	
	ListView ques_list=null;
	TextView diseasename_tv1;
	
	Button back_ques_button;
	Button ask_ques_button;
	
	
	JSONArray jArray;
	Question [] question;
	
	ProgressDialog pd=null;
	post_async pas=null;
	
	EditText email;
	EditText password;
	
	String question_body;
	String disease_id="1";
	String disease_name="";
	
	
	
	private Dialog close_loginform;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main_kauser);
        
        // facebook login 
       /* Fb_handler fbhand=new Fb_handler(question_answer.this);
		mfacebook=fbhand.get_var();*/
        
        Bundle extra = getIntent().getExtras();
		try{
			disease_id = extra.getString("disease_id");
			disease_name = extra.getString("disease_name");
			Log.d("RUPAM", "came with disease id"+disease_id);
		}
		catch (Exception e) {
			// TODO: handle exception
			disease_id="1";
		}
        
        
        Log.d("**error message","****1");
        
        try{
        	diseasename_tv1=(TextView) findViewById(R.id.diseasename_tv);
        	back_ques_button = (Button) findViewById(R.id.back_ques_button);
        	
            ask_ques_button=(Button)  findViewById(R.id.ask_ques_button);
            
            back_ques_button.setOnClickListener(this);
            ask_ques_button.setOnClickListener(this);
            
        }
        
        catch (Exception e) {
			e.printStackTrace();
		}
        
        ques_list=(ListView) findViewById(R.id.ques_list);
        
        
        GetQuestionFuntion();
    }
    
    public void populate(JSONObject jsonObj){

		try {
			
			
			Log.d("question>>>>",jsonObj.toString());
						
			
			diseasename_tv1.setText("Questions for disease "+disease_name);
			
			
			jArray = jsonObj.getJSONArray("Data");
			Log.d("question>>>>",jArray.toString());
			
			question = new Question[jArray.length()];
            
			Log.d("array size"," "+jArray.length());
			
			for(int i=0;i<jArray.length();i++){
				
                    JSONObject json_data = jArray.getJSONObject(i);   
                  question[i]=new Question(json_data.getString("q_id"),json_data.getString("q_description") , json_data.getString("no_of_comment"),json_data.getString("q_rank"),json_data.getString("user_id"));                     
                   Log.d("data", i+" "+json_data.getString("q_id"));
                   
             }
			QuestionAdapter adapter = new QuestionAdapter(getApplicationContext(),question);
			ques_list.setAdapter(adapter);
			
			Log.d("getting data->","complete");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
	}
	
    
    class QuestionAdapter extends ArrayAdapter<Question> implements OnClickListener{

		final Context context;
		final Question[] data;
		
		public QuestionAdapter(Context c,Question[] fd){
			super(c,R.layout.question_row,fd);
			this.context=c;
			this.data=fd;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int i_rank=0;
			final int pos=position;
			final TextView rank_final;
			
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.question_row,parent,false);
			
			TextView rankTv=(TextView)rowView.findViewById(R.id.rank_tv);
			rankTv.setText(data[position].Rank);
			rank_final=rankTv;
			
			TextView question=(TextView) rowView.findViewById(R.id.ques_tv);
			question.setText(data[position].Question_Body);
			
			TextView comment=(TextView) rowView.findViewById(R.id.comment_tv);
			comment.setText(data[position].Comment_count);
			
			Button increase=(Button) rowView.findViewById(R.id.up_button);
			Button decrease=(Button) rowView.findViewById(R.id.down_button);
			Button show_ans=(Button) rowView.findViewById(R.id.show_answer_button);
			show_ans.setTag(data[position]);
			show_ans.setOnClickListener(this);
			Button share_fb=(Button) rowView.findViewById(R.id.share_answer_button);
			
			
			increase.setOnClickListener(new OnClickListener() {
				boolean upnotclicked=true;
				@Override
				public void onClick(View v) {
					if(is_loggedin()){
						if(upnotclicked){
							String i=Integer.toString((Integer.parseInt(data[pos].Rank)+1));
							data[pos].setRank(i);
							rank_final.setText(i);
							upnotclicked=false;
						}
					}
					else{
						showDialog(LOGIN_DIALOG_ID);
					}
					
				}
			});
			
			decrease.setOnClickListener(new OnClickListener() {
				boolean downnotclicked=true;
				@Override
				public void onClick(View v) {
					if(is_loggedin()){
						if(downnotclicked){
							String i=Integer.toString((Integer.parseInt(data[pos].Rank)-1));
							data[pos].setRank(i);
							rank_final.setText(i);
							downnotclicked=false;
							
							// upload data to set rank 
							
						}
					}
					else{
						showDialog(LOGIN_DIALOG_ID);
					}
				
				}
			});
			
			
			share_fb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					question_body=data[pos].Question_Body;
					showDialog(FB_POST);
					
				}
			});
			
			return rowView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.show_answer_button:
				gotoAnswerPage((Question)v.getTag());
				break;

			default:
				break;
			}
		}
	}
	
	
    
    class Question{
    	public String QID,Question_Body,Comment_count,Rank,user_id;
		
		public Question(String QID,String Question_Body,String Comment_count,String Rank,String user_id) {
			this.QID=QID;
			this.Question_Body=Question_Body;
			this.Comment_count=Comment_count;
			this.Rank=Rank;
			this.user_id=user_id;
		}
		
		public void setRank(String i){
			this.Rank=i;
		}
		

    }
    
    public void gotoAnswerPage(Question obj){
    	//Intent ans_in = new Intent(QuestionPage.this,);
    	Toast.makeText(getApplicationContext(), obj.QID, Toast.LENGTH_LONG).show();
    	Intent answer_page=new Intent(QuestionPage.this,Answer_page.class);
		answer_page.putExtra("q_id",obj.QID);
		answer_page.putExtra("q_body",obj.Question_Body);
		startActivity(answer_page);
    }
    
    //
    protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
	    case LOGIN_DIALOG_ID:
	        // do the work to define the pause Dialog
	    	final Dialog login_form = new Dialog(this,android.R.style.Theme_Translucent);
			
			login_form.getWindow().setLayout(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			login_form.setContentView(R.layout.login_dialg);
			
			
			email=(EditText) login_form.findViewById(R.id.email_login_edittext);
			password=(EditText) login_form.findViewById(R.id.password_login_edittext);
			Button login=(Button) login_form.findViewById(R.id.login_login_button);
			Button cancel=(Button) login_form.findViewById(R.id.cancel_login_button);
			Button signup=(Button) login_form.findViewById(R.id.signup_login_button);
			
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					close_loginform=login_form;
					loginFuntion();
					
					
				}
			});
			
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					login_form.dismiss();
					return;
					
				}
			});
			
			signup.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					return ;
					
				}
			});
	    	
	        return login_form;
	        
	    case FB_POST:
	    	//
	    	final Dialog share_form = new Dialog(this,android.R.style.Theme_Translucent);
			
	    	share_form.getWindow().setLayout(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
	    	share_form.setContentView(R.layout.fb_share);
			
	    	Button postmywall=(Button)share_form.findViewById(R.id.postMy_fbshare_button);
	    	Button postfrwall=(Button) share_form.findViewById(R.id.postFr_fbshare_button);
	    	Button cancelshare=(Button) share_form.findViewById(R.id.cancel_fbshare_button);
	    	
	    	postfrwall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
				}
			});
	    	
	    	postmywall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					postOnWall("me");
					
				}
			});
	    	
			cancelshare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					share_form.dismiss();
					return ;
				}
			});
	    	
	        return share_form;
	    	//
	    	
	    
	    default:
	        dialog = null;
	        break;
	    }
	    return dialog;
	}
    
    public void loginFuntion(){
		if(email.getText().toString().length()==0 ){
			Toast.makeText(getApplicationContext(), 
					"Email Required", Toast.LENGTH_LONG).show();
			return;
		}
			
		
		if(password.getText().toString().length()==0 ){
			Toast.makeText(getApplicationContext(), 
					"Password Required", Toast.LENGTH_LONG).show();
			return;
		}
			
		pd = new ProgressDialog(QuestionPage.this);
		pd.setTitle("Verifying Login");
		pd.setMessage("Please wait");
		pd.show();
		JSONObject ob=createRequestObject();
		pas = new post_async(QuestionPage.this);
		pas.execute(ob);
	}
    
    /**
     * get question function 
     * @return
     */
    public void GetQuestionFuntion(){
    	
		pd = new ProgressDialog(QuestionPage.this);
		pd.setTitle("getting question");
		pd.setMessage("Please wait");
		pd.show();
		JSONObject ob=createQuestionReq();
		pas = new post_async(QuestionPage.this);
		pas.execute(ob);
	}
    
    
    
    public JSONObject createRequestObject(){
		
    	request_type=1;
    	
		JSONObject ob = new JSONObject();
		
		try {
			ob.put("url", "http://medibuddy.phpfogapp.com/Login.php");
			JSONArray array = new JSONArray();
			
			JSONObject job1 = new JSONObject();
			job1.put("label", "Username");
			job1.put("value",email.getText().toString());
			
			JSONObject job2 = new JSONObject();
			job2.put("label", "Password");
			job2.put("value", password.getText().toString());
			
			array.put(job1);
			array.put(job2);
			
			ob.put("parameters", array);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ob;
	}
    
    
    /**
     * function to get question to populate disease list 
     * 
     * @return jsonobject 
     */
    public JSONObject createQuestionReq(){
    	request_type=0;
    	
    	JSONObject ob = new JSONObject();
		
		try {
			ob.put("url", "http://medibuddy.phpfogapp.com/GetQuestions.php");
			
			JSONArray array = new JSONArray();
			
			JSONObject job1 = new JSONObject();
			job1.put("label", "DID");
			job1.put("value",disease_id);
			
			
			array.put(job1);
			
			
			ob.put("parameters", array);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ob;
    }
    

    public void getData(JSONObject ob){
		if(pd.isShowing()){
			pd.dismiss();
		}
		String stats,message;
		int err_code; // 1 for invalid user
						// 2 for invalid password
		
		switch(request_type){
			case 0:
				try {
					stats = ob.getString("Status");	
					Log.d("kauser", "get question req = "+stats);
					
			        if(stats.equals("OK")){
			        	populate(ob);
			        }
			        else{
			        	/*message =ob.getString("Message");
			        	if(message.equals("Query Error")){
			        		makeToast("query error ");
			        	}
			        	*/
			        	diseasename_tv1.setText(ob.getString("Message"));
			        }
					
				} 
				catch (Exception e) {
					Log.d("kauser","hello error heer in get question ");
					Log.d("rupam", e.getMessage());
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					stats = ob.getString("Status");
					
					
					Log.d("rupam", "Login status is = "+stats);
					
			        if(stats.equals("OK")){
			        	JSONObject jsob=new JSONObject(ob.getString("Data"));
			        	Log.d("kauser>>>",jsob.getString("user_id"));
			        	loginSuccess(jsob);
			        }
			        else{
			        	message =ob.getString("Message");
			        	if(message.equals("Email or Password not matched.")){
			        		loginFailure(1);
			        	}
			        	else{
			        		loginFailure(2);
			        	}
			        }
					
				} 
				catch (Exception e) {
					Log.d("kauser","hello error heerre in login part  ");
					Log.d("rupam", e.getMessage());
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
		
		
		
	}
    
    //
    
    public void loginSuccess(JSONObject job) throws JSONException{
		// setting userdata in devices shared preferences
    	SharedPreferences pref = getSharedPreferences("my_app_pref", MODE_PRIVATE);
		Editor editor = pref.edit();
		
		editor.putString("role_id", job.getString("role_id"));
		editor.putString("username", job.getString("username"));
		editor.putString("user_id", job.getString("user_id"));
		editor.commit();
		
		close_loginform.dismiss();
		
		Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
		
		/*Intent mypage_in = new Intent(EmailLoginForm.this, HomePage.class);
		mypage_in.putExtra("playerid", id);
		startActivity(mypage_in);*/
	}
	
	public void loginFailure(int id){

		password.setText("");
		if(id==1){
			email.setText("");
			password.setText("");
			Toast.makeText(getApplicationContext(), 
					"Wrong Mail or password  Address", Toast.LENGTH_LONG).show();
		}
		
		else{
			Toast.makeText(getApplicationContext(), 
					"database error", Toast.LENGTH_LONG).show();
		}
		
	}
	
	   private boolean is_loggedin(){
	    	
		   	SharedPreferences perfs = getSharedPreferences("my_app_pref", MODE_PRIVATE);
		   	
			String id =perfs.getString("user_id",null);
			
			
			
			if(id!=null)return true;
			else return false;
			
			
	    }
	   
	   public void postOnWall(String friendid) {
			Bundle params = new Bundle();
			if(!friendid.equals("me")){
				params.putString("to", friendid);
			}
			
           params.putString("caption", getString(R.string.app_name));
           params.putString("description",question_body);
           //params.putString("picture",getString(R.drawable.icon));
           //need picture er link
           //params.putString("name", getString(R.string.app_action));
           params.putString("link","http://hum-this.net/");
		}
	   
	  
	   
	   public void makeToast(String msg){
		   Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
	   }

    //
    
    
	String st="" +
	
	"{"+
    			"\"Status\":\"OK\", 	" +
    			"\"Disease Name\":\"AIDS\","+
    			"\"Total Questions\":\"3\","+
    			"\"Disease ID\":\"1\","+
			"\"Questions\":"+
			"["+
				"{"+
					"\"QID\":\"10\","+
					"\"Question Body\":\"this is question 1?\","+
					"\"Comment count\":\"15\","+
					"\"Rank\":\"3\","+
					"\"User id\":\"24\""+
				"},"+
				"{"+
					"\"QID\":\"15\","+
					"\"Question Body\":\"this is question 2?\","+
					"\"Comment count\":\"0\","+
					"\"Rank\":\"1\","+
					"\"User id\":\"3\""+
				"},"+
				"{"+
					"\"QID\":\"23\","+
					"\"Question Body\":\"this is question 3?\","+
					"\"Comment count\":\"0\","+
					"\"Rank\":\"0\","+
					"\"User id\":\"2\""+
				"}"+
			"]"+
		"}" +

	"";

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_ques_button:
			finish();
			break;
			
		case R.id.ask_ques_button:
			if(is_loggedin()){
				Intent in=new Intent(QuestionPage.this,AskQuestion.class);
				
				
				SharedPreferences perfs = getSharedPreferences("my_app_pref", MODE_PRIVATE);
				
				in.putExtra("user_id", perfs.getString("user_id", "1"));
				in.putExtra("disease_id", disease_id);
				in.putExtra("disease_name", disease_name);
				startActivity(in);
			}
			else{
				showDialog(LOGIN_DIALOG_ID);
			}
			break;

		default:
			break;
		}
	}
}