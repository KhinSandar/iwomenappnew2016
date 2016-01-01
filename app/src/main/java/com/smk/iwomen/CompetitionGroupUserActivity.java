package com.smk.iwomen;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.CommonConfig;
import com.smk.adapter.GroupUserListAdapter;
import com.smk.application.TimeSubtractionUtil;
import com.smk.application.TimeSubtractionUtil.Callback;
import com.smk.clientapi.NetworkEngine;
import com.smk.model.Answer;
import com.smk.model.AnswerList;
import com.smk.model.CompetitionQuestion;
import com.smk.model.GroupUser;
import com.smk.model.GroupUserList;
import com.smk.skalertmessage.SKToastMessage;

import org.undp_iwomen.iwomen.R;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class CompetitionGroupUserActivity extends BaseActionBarActivity {

	private ExpandableListView lst_group_user;
	private CompetitionQuestion competitionQuestion;
	private GroupUserList competitionGroupUserList;
	private ProgressBar progress_bar;
	private TextView txt_question;
	private TextView txt_question_desc;
	private TextView txt_group_name;
	private TextView txt_progress_label;
	private TextView txt_count_day;
	private TextView txt_count_hour;
	private TextView txt_count_minute;
	private TextView txt_count_second;
	private Button btn_go_to_answer;
	private GroupUserList competitionGroupUserListTemp;
	private RelativeLayout layout_progress_bar;
	private TextView txt_complete;
	private GroupUser groupUser = null;
	private SharedPreferences mSharedPreferencesUserInfo;
	private SharedPreferences.Editor mEditorUserInfo;
	private String user_name, user_obj_id, user_ph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_group_user);

		mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

		user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			competitionQuestion = new Gson().fromJson(bundle.getString("competition_question"), CompetitionQuestion.class);
			competitionGroupUserList = new Gson().fromJson(bundle.getString("competition_group"), GroupUserList.class);
			competitionGroupUserListTemp = new Gson().fromJson(bundle.getString("competition_group"), GroupUserList.class);
		}
		
		lst_group_user = (ExpandableListView) findViewById(R.id.lst_group_user);
		btn_go_to_answer = (Button) findViewById(R.id.btn_competition_go_answer);
		
		View groupHeader = View.inflate(this, R.layout.competition_group_list_header, null);
		txt_question = (TextView) groupHeader.findViewById(R.id.txt_competition_question);
		txt_question_desc = (TextView) groupHeader.findViewById(R.id.txt_competition_description);
		txt_group_name = (TextView) groupHeader.findViewById(R.id.txt_competition_group_name);
		txt_progress_label = (TextView) groupHeader.findViewById(R.id.txt_competition_progress_label);
		progress_bar = (ProgressBar) groupHeader.findViewById(R.id.txt_competition_progress);
		txt_complete = (TextView) groupHeader.findViewById(R.id.txt_competition_complete);
		layout_progress_bar = (RelativeLayout) groupHeader.findViewById(R.id.layout_progress_bar);
		lst_group_user.addHeaderView(groupHeader);
		SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
		if(langRef.getString("lang","").equals("mm")){
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestionMm()));
			txt_question_desc.setText(Html.fromHtml(competitionQuestion.getGroupDescriptionMm()));
			txt_group_name.setText(competitionGroupUserList.getGroupUsers().get(0).getGroupName());
		}else{
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestion()));
			txt_question_desc.setText(Html.fromHtml(competitionQuestion.getGroupDescription()));
			txt_group_name.setText(competitionGroupUserList.getGroupUsers().get(0).getGroupName());
		}
		
		
		txt_count_day = (TextView) groupHeader.findViewById(R.id.txt_count_day);
		txt_count_hour = (TextView) groupHeader.findViewById(R.id.txt_count_hour);
		txt_count_minute = (TextView) groupHeader.findViewById(R.id.txt_count_minutes);
		txt_count_second = (TextView) groupHeader.findViewById(R.id.txt_count_second);
		
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

		timeUtil.setEndDate(null,competitionQuestion.getEndDate());
		setAdapter();
	}
	
	private void setAdapter(){
		for(GroupUser user : competitionGroupUserListTemp.getGroupUsers()){
			if(user.getUserId().equals(user_obj_id)){
				if(user.getAnswer().size() >= 3)
					btn_go_to_answer.setText(getResources().getString(R.string.competition_go_to_answer));
				else
					btn_go_to_answer.setText(getResources().getString(R.string.competition_enter_the_answer));
				groupUser = user;
			}
		}
		
		for (int i = 0; i < competitionGroupUserList.getGroupUsers().size(); i++) {
			if(competitionGroupUserList.getGroupUsers().get(i).getAnswer().size() > 0){
				competitionGroupUserList.getGroupUsers().get(i).getAnswer().remove(0);
			}
		}
		GroupUserListAdapter groupUserAdapter = new GroupUserListAdapter(this, competitionGroupUserList.getGroupUsers());
		groupUserAdapter.setOnCallbackListener(new GroupUserListAdapter.Callbacks() {
			
			@Override
			public void clickViber(String phoneNumber) {
				// TODO Auto-generated method stub
				try {
					Uri uri = Uri.parse("tel:" + Uri.encode(phoneNumber)); 
					Intent intent = new Intent("android.intent.action.VIEW");
					intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
					intent.setData(uri); 
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					// TODO: handle exception
					SKToastMessage.showMessage(CompetitionGroupUserActivity.this, "Please install Viber from any application market.", SKToastMessage.WARNING);
				}
				
			}
			
			@Override
			public void clickPhone(String phoneNumber) {
				// TODO Auto-generated method stub
				String number = "tel:" + phoneNumber;
		        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
		        startActivity(callIntent);
			}
		});
		
		lst_group_user.setAdapter(groupUserAdapter);
		
		if((int) calculatePercentage() > 99){
			txt_complete.setVisibility(View.VISIBLE);
			layout_progress_bar.setVisibility(View.GONE);
		}
		
		txt_progress_label.setText(competitionGroupUserList.getGroupUsers().get(0).getCurrentHasAnswer()+"/"+competitionGroupUserList.getGroupUsers().get(0).getTotalHasAnswer()+" Answers to Complete the Game!");
		progress_bar.setProgress((int) calculatePercentage());
		
		btn_go_to_answer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getCompetitionAnswer();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if(arg0 == 100 && arg1 == RESULT_OK){
			getCompetitionGroupUser();
			
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	private void getCompetitionGroupUser(){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.show();
		NetworkEngine.getInstance().getCompetitionGroupUser("", user_obj_id, competitionQuestion.getId(), new retrofit.Callback<List<GroupUser>>() {

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				// dialog.dismiss();
			}

			@Override
			public void success(List<GroupUser> arg0, Response arg1) {
				// TODO Auto-generated method stub
				competitionGroupUserListTemp = new GroupUserList(arg0);
				competitionGroupUserList = new GroupUserList(arg0);
				setAdapter();
				dialog.dismiss();
			}
		});
	}
	
	private void getCompetitionAnswer(){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.show();
		NetworkEngine.getInstance().getUserAnswer(groupUser.getId(), new retrofit.Callback<List<Answer>>() {
			
			@Override
			public void success(List<Answer> arg0, Response arg1) {
				// TODO Auto-generated method stub
				
				Bundle bundle = new Bundle();
				bundle.putInt("group_user_id", groupUser.getId());
				bundle.putString("competition_question", new Gson().toJson(competitionQuestion));
				if(arg0 != null)
					bundle.putString("user_answer", new Gson().toJson(new AnswerList(arg0)));
				else
					bundle.putString("user_answer", new Gson().toJson(new AnswerList(groupUser.getAnswer())));
				startActivityForResult(new Intent(getApplicationContext(), CompetitionSubmitAnswerActivity.class).putExtras(bundle), 100);
				
				dialog.dismiss();
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private float calculatePercentage(){
		// Percentage = (Obtained score x 100) / Total Score
		
		float percentage = (competitionGroupUserList.getGroupUsers().get(0).getCurrentHasAnswer() * 100) / competitionGroupUserList.getGroupUsers().get(0).getTotalHasAnswer();
		
		return percentage;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.competition_group_user, menu);
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
