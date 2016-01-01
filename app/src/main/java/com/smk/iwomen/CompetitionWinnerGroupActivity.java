package com.smk.iwomen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smk.model.CompetitionQuestion;
import com.smk.model.CorrectAnswer;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

public class CompetitionWinnerGroupActivity extends BaseActionBarActivity {

	private TextView txt_winner;
	private TextView txt_answer;
	private CompetitionQuestion competitionQuestion;
	private LinearLayout layout_answers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_winner_group);


		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			competitionQuestion = new Gson().fromJson(bundle.getString("competition_question"), CompetitionQuestion.class);
		}
		
		txt_winner = (TextView) findViewById(R.id.txt_winner_group);
		layout_answers = (LinearLayout) findViewById(R.id.layout_answers);
		SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
		if(langRef.getString("lang","").equals("mm")){
			boolean first = true;
			for (CorrectAnswer answer : competitionQuestion.getCorrectAnswer()){
				if(first) {
					txt_winner.setText(answer.getGroupUser().getGroupName());
					first = false;
				}else {
					txt_winner.append(", " + answer.getGroupUser().getGroupName());
				}

				CustomTextView textView1 = new CustomTextView(this);
				textView1.setText(answer.getGroupUser().getGroupName()+" "+getResources().getString(R.string.competition_answer_1));
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				llp.setMargins(0, 22, 0, 0); // llp.setMargins(left, top, right, bottom);
				textView1.setLayoutParams(llp);
				textView1.setTextSize(18f);
				textView1.setTextColor(getResources().getColor(R.color.competition_text));
				layout_answers.addView(textView1);

				CustomTextView ansTextView1 = new CustomTextView(this);
				ansTextView1.setText(answer.getAnswerMm());
				LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				llp1.setMargins(0, 22, 0, 0); // llp.setMargins(left, top, right, bottom);
				ansTextView1.setLayoutParams(llp1);
				ansTextView1.setTextSize(18f);
				ansTextView1.setTextColor(getResources().getColor(R.color.competition_text));
				layout_answers.addView(ansTextView1);
			}
		}else{
			boolean first = true;
			for (CorrectAnswer answer : competitionQuestion.getCorrectAnswer()){
				if(first) {
					txt_winner.setText(answer.getGroupUser().getGroupName());
					first = false;
				}else {
					txt_winner.append(", " + answer.getGroupUser().getGroupName());
				}

				CustomTextView textView1 = new CustomTextView(this);
				textView1.setText(answer.getGroupUser().getGroupName()+" "+getResources().getString(R.string.competition_answer_1));
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				llp.setMargins(0, 22, 0, 0); // llp.setMargins(left, top, right, bottom);
				textView1.setLayoutParams(llp);
				textView1.setTextSize(18f);
				textView1.setTextColor(getResources().getColor(R.color.competition_text));
				layout_answers.addView(textView1);

				CustomTextView ansTextView1 = new CustomTextView(this);
				ansTextView1.setText(answer.getAnswer());
				LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				llp1.setMargins(0, 22, 0, 0); // llp.setMargins(left, top, right, bottom);
				ansTextView1.setLayoutParams(llp1);
				ansTextView1.setTextSize(18f);
				ansTextView1.setTextColor(getResources().getColor(R.color.competition_text));
				layout_answers.addView(ansTextView1);
			}
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
