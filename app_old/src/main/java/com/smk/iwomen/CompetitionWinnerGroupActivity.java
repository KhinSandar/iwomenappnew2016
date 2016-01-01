package com.smk.iwomen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smk.model.CompetitionQuestion;

import org.undp_iwomen.iwomen.R;

public class CompetitionWinnerGroupActivity extends BaseActionBarActivity {

	private TextView txt_winner;
	private TextView txt_answer;
	private CompetitionQuestion competitionQuestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_winner_group);


		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			competitionQuestion = new Gson().fromJson(bundle.getString("competition_question"), CompetitionQuestion.class);
		}
		
		txt_winner = (TextView) findViewById(R.id.txt_winner_group);
		txt_answer = (TextView) findViewById(R.id.txt_winner_answer);
		SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
		if(langRef.getString("lang","").equals("mm")){
			txt_winner.setText(competitionQuestion.getGroupUser().getGroupName());
			txt_answer.setText(competitionQuestion.getCorrectAnswer().getAnswerMm());		
		}else{
			txt_winner.setText(competitionQuestion.getGroupUser().getGroupName());
			txt_answer.setText(competitionQuestion.getCorrectAnswer().getAnswer());
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.competition_winner_group, menu);
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
