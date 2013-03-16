package com.question;

import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author User
 * 
 *         anik>>>>>>>
 * 
 *         >>>> for running this project use >> hvga emulator (320 x 480 ) >>
 *         density medium 160 >> >>>> for best performance
 * 
 */

public class ImageMapTestActivity extends Activity {
	ImageMap mImageMap;

	TextView region_name_tv;
	Button inc_button;
	Button dec_button;

	
	int count;
	final int limit=5;

	String reg_tv_text;
	String reducedTv_text;
	String tvtext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.human_body);
		
		count=0;
		tvtext="";
		reducedTv_text="";
		
		region_name_tv = (TextView) findViewById(R.id.item_selected_tv);
		dec_button = (Button) findViewById(R.id.cross_humanbody_button);
		inc_button = (Button) findViewById(R.id.check_humanbody_button);

		dec_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//removeText();
				removeArea();

			}
		});

		inc_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makeToast(tvtext.toString());
				StringTokenizer st=new StringTokenizer(tvtext,"+");
				String param ="";
				while(st.hasMoreElements()){
					param=param+"\""+st.nextToken()+"\",";
					Log.d("param",param);
				}
				if(param!="")
				{
					param=param.substring(0,param.length()-1);
					
					makeToast(param);
				}
				
				Intent go_in=new Intent(ImageMapTestActivity.this,SymptomInput.class);
				go_in.putExtra("params", param);
				startActivity(go_in);
			}
		});

		// find the image map in the view
		mImageMap = (ImageMap) findViewById(R.id.map);

		// add a click handler to react when areas are tapped
		mImageMap
				.addOnImageMapClickedHandler(new ImageMap.OnImageMapClickedHandler() {
					@Override
					public void onImageMapClicked(int id) {
						// when the area is tapped, show the name in a
						// text bubble
						mImageMap.showBubble(id);
						/*
						 * Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG
						 * ).show();
						 */
					}

					@Override
					public void onBubbleClicked(int id) {
						// react to info bubble for area being tapped
						/*
						 * Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG
						 * ).show();
						 */
						String tv = region_name_tv.getText().toString();

						switch (id) {
						case R.id.abdomen:
							addArea("abdomen");
							break;
						case R.id.ankle_left:
							addArea("ankle_left");
							break;
						case R.id.ankle_right:
							addArea("ankle_right");
							break;
						case R.id.chest:
							addArea("chest");
							break;
						case R.id.shoulder_left:
							addArea("shoulder_left");
							break;
						case R.id.shoulder_right:
							addArea("shoulder_right");
							break;
						case R.id.penis:
							addArea("penis");
							break;
						case R.id.palm_right:
							addArea("palm_right");
							break;
						case R.id.palm_left:
							addArea("palm_left");
							break;
						case R.id.neck:
							addArea("neck");
							break;
						case R.id.mouth:
							addArea("mouth");
							break;
						case R.id.leg_right:
							addArea("leg_right");
							break;
						case R.id.leg_left:
							addArea("leg_left");
							break;
						case R.id.head:
							addArea("head");
							break;
						case R.id.hand_right:
							addArea("hand_right");
							break;
						case R.id.hand_left:
							addArea("hand_left");
							break;
						case R.id.hair:
							addArea("hair");
							break;
						case R.id.feet_left:
							addArea("feet_left");
							break;
						case R.id.feet_right:
							addArea("feet_right");
							break;
						case R.id.ear:
							addArea("ear");
							break;
						case R.id.eye_left:
							addArea("eye_left");
							break;
						case R.id.eye_right:
							addArea("eye_right");
							break;

						default:
							break;

						}

					}
				});

	}

	/*public void addText(String areaname) {
		String tvtext = region_name_tv.getText().toString();
		int count = 0;
		for (int i = 0; i < tvtext.length(); i++) {
			if (tvtext.charAt(i) == '+')
				count++;
		}
		if (count < 3 && (tvtext.indexOf(areaname) == -1)) {

			if (!tvtext.equals("add_some_area_name")) {

				region_name_tv.append("+" + areaname);
			} else {
				region_name_tv.setText(areaname);
			}
		}
	}

	public void removeText() {

		reg_tv_text = region_name_tv.getText().toString();
		lastAddedIndex = reg_tv_text.lastIndexOf("+");
		if (lastAddedIndex != -1) {

			reducedTv_text = reg_tv_text.substring(0, lastAddedIndex);
			makeToast(reducedTv_text);
			region_name_tv.setText(reducedTv_text);
			
		} else {
			region_name_tv.setText("add_some_area_name");
		}

	}*/

	public void addArea(String areaname) {
		if(tvtext.indexOf(areaname)==-1){
			if(count==0){
				tvtext=areaname;
			}
			else{
				tvtext=tvtext+"+"+areaname;
			}
			count++;
			region_name_tv.setText(count+" item selected");
		}
	}
	
	public void removeArea() {
		int lastAddedIndex;
		if(count>0){
			
			
			reducedTv_text= tvtext;
			if(count==1){
				reducedTv_text="";
			}
			else{
				
				lastAddedIndex = reducedTv_text.lastIndexOf("+");
				reducedTv_text = reducedTv_text.substring(0, lastAddedIndex);
			}
			count--;
			region_name_tv.setText(count+" item selected");
			tvtext=reducedTv_text;
		}
	}
	

	/*public void removeArea() {
		reg_tv_text = tvtext;
		lastAddedIndex = reg_tv_text.lastIndexOf("+");
		if (lastAddedIndex != -1) {

			reducedTv_text = reg_tv_text.substring(0, lastAddedIndex);
			makeToast(reducedTv_text);

			int count = 0;
			for (int i = 0; i < reducedTv_text.length(); i++) {
				if (reducedTv_text.charAt(i) == '+')
					count++;
			}
			region_name_tv.setText(count + " item selected");
		} else {
			region_name_tv.setText("0 item selected");
		}
	}*/

	public void makeToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}