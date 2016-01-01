package com.smk.iwomen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smk.application.TimeSubtractionUtil;
import com.smk.application.TimeSubtractionUtil.Callback;
import com.smk.clientapi.NetworkEngine;
import com.smk.model.CompetitionQuestion;
import com.smk.model.GroupUser;
import com.smk.model.GroupUserList;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class CompetitionNewGameActivity extends BaseActionBarActivity {

	private TextView txt_question;
	private TextView txt_question_desc;
	private TextView txt_instruction_desc;
	private TextView txt_instruction;
	private TextView txt_count_day;
	private TextView txt_count_hour;
	private TextView txt_count_minute;
	private TextView txt_count_second;
	private Button btn_take_challenge;
	private CompetitionQuestion competitionQuestion;
	private SharedPreferences mSharedPreferencesUserInfo;
	private SharedPreferences.Editor mEditorUserInfo;
	private String user_name, user_obj_id, user_ph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_new_game);
		mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

		user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);

		txt_question = (TextView)findViewById(R.id.txt_competition_question);
		txt_question_desc = (TextView) findViewById(R.id.txt_competition_description);
		txt_instruction = (TextView) findViewById(R.id.txt_competition_instruction);
		txt_instruction_desc = (TextView) findViewById(R.id.txt_competition_instruction_description);
		
		txt_count_day = (TextView) findViewById(R.id.txt_count_day);
		txt_count_hour = (TextView) findViewById(R.id.txt_count_hour);
		txt_count_minute = (TextView) findViewById(R.id.txt_count_minutes);
		txt_count_second = (TextView) findViewById(R.id.txt_count_second);
		
		btn_take_challenge = (Button) findViewById(R.id.btn_competition_take_challenge);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			competitionQuestion = new Gson().fromJson(bundle.getString("competition_question"), CompetitionQuestion.class);
		}
		SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
		if(langRef.getString("lang","").equals("mm")){
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestionMm()));
			txt_question_desc.setText(Html.fromHtml(competitionQuestion.getDescriptionMm()));
			txt_instruction_desc.setText(Html.fromHtml(competitionQuestion.getInstructionAboutGameMm()));
		}else{
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestion()));
			txt_question_desc.setText(Html.fromHtml(competitionQuestion.getDescription()));
			txt_instruction_desc.setText(Html.fromHtml(competitionQuestion.getInstructionAboutGame()));
		}
			
		
		TimeSubtractionUtil timeUtil = new TimeSubtractionUtil();
		timeUtil.setCallbackListener(new Callback() {
			
			@Override
			public void runOnList() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void run(final String diffTime) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(diffTime != null){
							String[] time = diffTime.split(":");
							txt_count_day.setText(time[0]);
							txt_count_hour.setText(time[1]);
							txt_count_minute.setText(time[2]);
							txt_count_second.setText(time[3]);
						}else{
							startActivity(new Intent(getApplicationContext(), GameOverActivity.class));
						}
						
					}
				});
			}
			
			@Override
			public void run(String diffTime, TextView view) {
				// TODO Auto-generated method stub
				
			}
		});
		timeUtil.setEndDate(null, competitionQuestion.getEndDate());
		
		btn_take_challenge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getCompetitionGroupUser();
			}
		});
	}
	
	private void getCompetitionGroupUser(){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.show();
		NetworkEngine.getInstance().getCompetitionGroupUser("", user_obj_id, competitionQuestion.getId(), new retrofit.Callback<List<GroupUser>>() {

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

			@Override
			public void success(List<GroupUser> arg0, Response arg1) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("competition_question", new Gson().toJson(competitionQuestion));
				bundle.putString("competition_group", new Gson().toJson(new GroupUserList(arg0)));
				startActivity(new Intent(getApplicationContext(), CompetitionGroupUserActivity.class).putExtras(bundle));
				dialog.dismiss();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.competition_new_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
