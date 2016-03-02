package org.smk.iwomen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smk.skalertmessage.SKToastMessage;

import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.model.AnswerList;
import org.smk.model.CompetitionQuestion;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.CustomEditText;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CompetitionSubmitAnswerActivity extends BaseActionBarActivity {

	private TextView txt_question;
	private TextView txt_description;
	private EditText edt_answer_1;
	private EditText edt_answer_2;
	private EditText edt_answer_3;
	private CompetitionQuestion competitionQuestion;
	private Button btn_save;
	private Button btn_submit;
	private Integer answer3Id;
	private Integer answer2Id;
	private Integer answer1Id;
	private org.smk.model.AnswerList AnswerList;
	private int groupUserId;
	private Button btn_go_back;
	private LinearLayout layout_question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_submit_answer);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			competitionQuestion = new Gson().fromJson(bundle.getString("competition_question"), CompetitionQuestion.class);
			AnswerList = new Gson().fromJson(bundle.getString("user_answer"), AnswerList.class);
			groupUserId = bundle.getInt("group_user_id");
		}
		
		txt_question = (TextView) findViewById(R.id.txt_competition_question);
		txt_description = (TextView) findViewById(R.id.txt_competition_description);
		
		SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
		if(langRef.getString("lang","").equals("mm")){
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestionMm()));
			txt_description.setText(Html.fromHtml(competitionQuestion.getAnswerSubmitDescriptionMm()));
		}else{
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestion()));
			txt_description.setText(Html.fromHtml(competitionQuestion.getAnswerSubmitDescription()));
		}

		layout_question = (LinearLayout) findViewById(R.id.layout_question);
		
		edt_answer_1 = (EditText) findViewById(R.id.edt_competition_answer_1);
		edt_answer_2 = (EditText) findViewById(R.id.edt_competition_answer_2);
		edt_answer_3 = (EditText) findViewById(R.id.edt_competition_answer_3);
		
		btn_save = (Button) findViewById(R.id.btn_competition_answer_save);
		btn_submit = (Button) findViewById(R.id.btn_competition_answer_submit);
		btn_go_back = (Button) findViewById(R.id.btn_go_back);
		
		
		String answer1 = StoreUtil.getInstance().selectFrom("answer1_"+groupUserId);
		if(answer1 != null){
			edt_answer_1.setText(answer1);
		}
		String answer2 = StoreUtil.getInstance().selectFrom("answer2_"+groupUserId);
		if(answer2 != null){
			edt_answer_2.setText(answer2);
		}
		String answer3 = StoreUtil.getInstance().selectFrom("answer3_"+groupUserId);
		if(answer3 != null){
			edt_answer_3.setText(answer3);
		}
		
		if(AnswerList.getAnswers().size() >= 1){
			answer1Id = AnswerList.getAnswers().get(0).getId();
			if(langRef.getString("lang","").equals("mm")){
				edt_answer_1.setText(AnswerList.getAnswers().get(0).getAnswerMm());		
			}else{
				edt_answer_1.setText(AnswerList.getAnswers().get(0).getAnswer());
			}
			edt_answer_1.setEnabled(false);
		}
		if(AnswerList.getAnswers().size() >= 2){
			answer2Id = AnswerList.getAnswers().get(1).getId();
			if(langRef.getString("lang","").equals("mm")){
				edt_answer_2.setText(AnswerList.getAnswers().get(1).getAnswerMm());		
			}else{
				edt_answer_2.setText(AnswerList.getAnswers().get(1).getAnswer());
			}
			edt_answer_2.setEnabled(false);
		}
		if(AnswerList.getAnswers().size() >= 3){
			answer3Id = AnswerList.getAnswers().get(2).getId();
			if(langRef.getString("lang","").equals("mm")){
				edt_answer_3.setText(AnswerList.getAnswers().get(2).getAnswerMm());		
			}else{
				edt_answer_3.setText(AnswerList.getAnswers().get(2).getAnswer());
			}
			edt_answer_3.setEnabled(false);
			btn_save.setEnabled(false);
			btn_submit.setEnabled(false);
		}
		
		btn_save.setOnClickListener(clickListener);
		btn_submit.setOnClickListener(clickListener);
		btn_go_back.setOnClickListener(clickListener);
	}

	private void generateUi(){
		// For Text Question
		CustomTextView txt_question_text = new CustomTextView(this);
		CustomEditText edt_question_text = new CustomEditText(this);
		txt_question_text.setText("Question 1.....");
		layout_question.addView(txt_question_text);
		layout_question.addView(edt_question_text);

		// For checkbox Question
		CustomTextView txt_question_checkbox = new CustomTextView(this);
		LinearLayout layout_checkbox = new LinearLayout(this);
		layout_checkbox.setOrientation(LinearLayout.VERTICAL);
		txt_question_checkbox.setText("Question 2.....");

		for(int i=0; i<5; i++){
			CheckBox chk_question = new CheckBox(this);
			chk_question.setText("Chk "+ (i+1));
			layout_checkbox.addView(chk_question);
		}

		layout_question.addView(txt_question_checkbox);
		layout_question.addView(layout_checkbox);

		// For Radio Question
		CustomTextView txt_question_radio = new CustomTextView(this);
		RadioGroup layout_radio = new RadioGroup(this);
		layout_radio.setOrientation(LinearLayout.VERTICAL);
		txt_question_radio.setText("Question 3.....");

		for(int i=0; i<5; i++){
			RadioButton chk_question = new RadioButton(this);
			chk_question.setText("Rdo "+ (i+1));
			layout_checkbox.addView(chk_question);
		}

		layout_question.addView(txt_question_radio);
		layout_question.addView(layout_checkbox);



	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == btn_save){
				if(edt_answer_1.getText().toString().trim().length() > 0){
					StoreUtil.getInstance().saveTo("answer1_"+groupUserId, edt_answer_1.getText().toString());
				}
				if(edt_answer_2.getText().toString().trim().length() > 0){
					StoreUtil.getInstance().saveTo("answer2_"+groupUserId, edt_answer_2.getText().toString());
				}
				if(edt_answer_3.getText().toString().trim().length() > 0){
					StoreUtil.getInstance().saveTo("answer3_"+groupUserId, edt_answer_3.getText().toString());
				}
				SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this, "Successfully saved", SKToastMessage.SUCCESS);

			}
			
			if(arg0 == btn_submit){
				postCompetitionAnswer();
			}
			if(arg0 == btn_go_back){
				onBackPressed();
			}
		}
	};
	
	private void postCompetitionAnswer(){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.show();
		NetworkEngine.getInstance().postCompetitionAnswer(
				"",
				answer1Id, 
				edt_answer_1.getText().toString(), 
				edt_answer_1.getText().toString(), 
				answer2Id, 
				edt_answer_2.getText().toString(), 
				edt_answer_2.getText().toString(), 
				answer3Id, 
				edt_answer_3.getText().toString(), 
				edt_answer_3.getText().toString(), 
				competitionQuestion.getId(), 
				groupUserId,
				new Callback<String>() {
					
					@Override
					public void success(String arg0, Response arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this, arg0, SKToastMessage.SUCCESS);
						Intent returnIntent = new Intent();
						//returnIntent.putExtra("result",result);
						setResult(RESULT_OK,returnIntent);
						finish();
					}
					
					@Override
					public void failure(RetrofitError arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if(arg0.getResponse() != null){
							switch (arg0.getResponse().getStatus()) {
							case 400:
								String error = (String) arg0.getBodyAs(String.class);
								SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this, error, SKToastMessage.ERROR);
								break;

							default:
								break;
							}
						}
					}
				});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
		super.onBackPressed();
	}
	
	
}
