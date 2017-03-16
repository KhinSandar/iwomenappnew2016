package org.smk.iwomen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alexbbb.uploadservice.MultipartUploadRequest;
import com.alexbbb.uploadservice.UploadNotificationConfig;
import com.alexbbb.uploadservice.UploadServiceBroadcastReceiver;
import com.android.camera.CropImageIntentBuilder;
import com.google.gson.Gson;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.smk.skalertmessage.SKToastMessage;
import com.squareup.picasso.Picasso;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.adapter.QuestionImageListAdapter;
import org.smk.application.MCrypt;
import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.model.Answer;
import org.smk.model.AnswerList;
import org.smk.model.CompetitionQuestion;
import org.smk.model.MutipleAnswer;
import org.smk.model.Option;
import org.smk.model.PhotoUpload;
import org.smk.model.Question;
import org.undp_iwomen.iwomen.BuildConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.activity.MainActivity;
import org.undp_iwomen.iwomen.ui.widget.CustomButton;
import org.undp_iwomen.iwomen.ui.widget.CustomCheckBox;
import org.undp_iwomen.iwomen.ui.widget.CustomEditText;
import org.undp_iwomen.iwomen.ui.widget.CustomRadioButton;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;

public class CompetitionSubmitAnswerActivity extends BaseActionBarActivity implements ImageChooserListener {

	private int REQUEST_CROP_PICTURE = 100;
	private TextView txt_question;
	private TextView txt_description;
	private CompetitionQuestion competitionQuestion;
	private Button btn_save;
	private Button btn_submit;
	private org.smk.model.AnswerList AnswerList;
	private Integer groupUserId;
	private Button btn_go_back;
	private LinearLayout layout_question;
	private Integer text_id = 100;
	private Integer checkbox_id = 200;
	private Integer radio_group_id = 300;
	private Integer radio_id = 400;
	private Integer image_id = 500;
	private Integer upload_image_id = 600;
	private Integer upload_image_view_id = 700;
	private Integer upload_audio_id = 800;
	private SharedPreferences langRef;
	private int chooserType;
	private ImageChooserManager imageChooserManager;
	private File croppedImageFile;
	private String filePath;
	private AudioPicker audioPicker;
	private ProgressDialog pgDialog;
	private ZProgressHUD dialog;

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
		
		langRef = getSharedPreferences(Utils.PREF_SETTING, MODE_PRIVATE);
		if(langRef.getString(Utils.PREF_SETTING_LANG,"").equals("mm")){
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestionMm()));
			txt_description.setText(Html.fromHtml(competitionQuestion.getAnswerSubmitDescriptionMm()));
		}else{
			txt_question.setText(Html.fromHtml(competitionQuestion.getQuestion()));
			txt_description.setText(Html.fromHtml(competitionQuestion.getAnswerSubmitDescription()));
		}

		layout_question = (LinearLayout) findViewById(R.id.layout_question);
		
		btn_save = (Button) findViewById(R.id.btn_competition_answer_save);
		btn_submit = (Button) findViewById(R.id.btn_competition_answer_submit);
		btn_go_back = (Button) findViewById(R.id.btn_go_back);

		List<Question> questionList = StoreUtil.getInstance().selectFrom("multipleQuestion_"+competitionQuestion.getId());
		if(questionList != null && questionList.size() > 0){
			competitionQuestion.setMultipleQuestion(questionList);
		}

		
		btn_save.setOnClickListener(clickListener);
		btn_submit.setOnClickListener(clickListener);
		btn_go_back.setOnClickListener(clickListener);

        if(competitionQuestion.getUserCount() > 1){
            btn_go_back.setVisibility(View.VISIBLE);
        }else{
            btn_go_back.setVisibility(View.GONE);
        }

		if(competitionQuestion.getMultipleQuestion() != null && competitionQuestion.getMultipleQuestion().size() > 0){
			for(Question question: competitionQuestion.getMultipleQuestion()){
				if(question.getType().equals("text")){
					generateTextQuestion(question);
				}
				if(question.getType().equals("checkbox")){
					generateCheckboxQuestion(question);
				}
				if(question.getType().equals("radio")){
					generateRadioQuestion(question);
				}
				if(question.getType().equals("image")){
					generateImageQuestion(question);
				}
				if(question.getType().equals("upload_photo")){
					generatePhotoQuestion(question);
				}
				if(question.getType().equals("upload_audio")){
					generateAudioQuestion(question);
				}

			}
		}
	}

	private void generateTextQuestion(Question question){
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 16, 0, 16); // llp.setMargins(left, top, right, bottom);

		// For Text Question
		CustomTextView txt_question_text = new CustomTextView(this);
		CustomEditText edt_question_text = new CustomEditText(this);

		txt_question_text.setLayoutParams(llp);

		if(langRef.getString("lang","").equals("mm"))
			txt_question_text.setText(Html.fromHtml(question.getQuestionMm()));
		else
			txt_question_text.setText(Html.fromHtml(question.getQuestion()));

		txt_question_text.setTextColor(getResources().getColor(R.color.competition_text_color));
		txt_question_text.setTextSize(18f);

		LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp2.setMargins(10, 0, 10, 0);
		edt_question_text.setLayoutParams(llp2);
		edt_question_text.setBackgroundResource(R.drawable.competition_answer_edittext);
		edt_question_text.setId(question.getId() + text_id);
		//Checking for Local Saved
		if(question.getAnswer() != null && question.getAnswer().length() > 0){
			edt_question_text.setText(question.getAnswer());
		}
		//Checking for Already Submitted
		if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
			edt_question_text.setEnabled(false);
			for(Answer answer:AnswerList.getAnswers()){
				if(answer.getMutipleQuestionId() == question.getId()){
					edt_question_text.setText(answer.getAnswer());
				}
			}
		}
		text_id++;
		layout_question.addView(txt_question_text);
		layout_question.addView(edt_question_text);
	}

	private void generateCheckboxQuestion(Question question){

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 16, 0, 10); // llp.setMargins(left, top, right, bottom);

		// For checkbox Question
		CustomTextView txt_question_checkbox = new CustomTextView(this);
		LinearLayout layout_checkbox = new LinearLayout(this);
		layout_checkbox.setOrientation(LinearLayout.VERTICAL);

		txt_question_checkbox.setLayoutParams(llp);
		if(langRef.getString("lang","").equals("mm"))
			txt_question_checkbox.setText(Html.fromHtml(question.getQuestionMm()));
		else
			txt_question_checkbox.setText(Html.fromHtml(question.getQuestion()));
		txt_question_checkbox.setTextColor(getResources().getColor(R.color.competition_text_color));
		txt_question_checkbox.setTextSize(18f);

		for(Option option: question.getOption()){
			CustomCheckBox chk_question = new CustomCheckBox(this);
			if(langRef.getString("lang","").equals("mm"))
				chk_question.setText(Html.fromHtml(option.getOptionMm()));
			else
				chk_question.setText(Html.fromHtml(option.getOption()));
			chk_question.setTextColor(getResources().getColor(R.color.competition_text_color));
			chk_question.setId(question.getId() + checkbox_id);
			// Checking for Local Saved
			if(question.getAnswer() != null && question.getAnswer().length() > 0){
				String[] answers = question.getAnswer().split(",");
				for(String ans: answers){
					if(option.getOption().equals(ans) || option.getOptionMm().equals(ans)){
						chk_question.setChecked(true);
					}
				}
			}
			//Checking for Already Submitted
			if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
				chk_question.setEnabled(false);
				for(Answer answer:AnswerList.getAnswers()){
					if(answer.getMutipleQuestionId() == question.getId()){
						String[] answers = answer.getAnswer().split(",");
						for(String ans: answers){
							if(option.getOption().equals(ans) || option.getOptionMm().equals(ans)){
								chk_question.setChecked(true);
							}else{
								chk_question.setChecked(false);
							}
						}
					}
				}
			}
			layout_checkbox.addView(chk_question);
			checkbox_id++;
		}

		layout_question.addView(txt_question_checkbox);
		layout_question.addView(layout_checkbox);
	}

	private void generateRadioQuestion(Question question){

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 16, 0, 10); // llp.setMargins(left, top, right, bottom);

		// For Radio Question
		CustomTextView txt_question_radio = new CustomTextView(this);
		RadioGroup layout_radio = new RadioGroup(this);
		layout_radio.setOrientation(LinearLayout.VERTICAL);

		txt_question_radio.setLayoutParams(llp);
		if(langRef.getString("lang","").equals("mm"))
			txt_question_radio.setText(Html.fromHtml(question.getQuestionMm()));
		else
			txt_question_radio.setText(Html.fromHtml(question.getQuestion()));
		txt_question_radio.setTextColor(getResources().getColor(R.color.competition_text_color));
		txt_question_radio.setTextSize(18f);

		layout_radio.setId(question.getId() + radio_group_id);
		radio_group_id++;

		for(Option option: question.getOption()){
			CustomRadioButton rdo_question = new CustomRadioButton(this);
			if(langRef.getString("lang","").equals("mm"))
				rdo_question.setText(Html.fromHtml(option.getOptionMm()));
			else
				rdo_question.setText(Html.fromHtml(option.getOption()));
			rdo_question.setTextColor(getResources().getColor(R.color.competition_text_color));
			rdo_question.setId(question.getId() + radio_id);
			//Checking for Local Saved
			if(question.getAnswer() != null && question.getAnswer().length() > 0){
				if(option.getOption().equals(question.getAnswer()) || option.getOptionMm().equals(question.getAnswer())){
					rdo_question.setChecked(true);
				}
			}
			//Checking for Already Submitted
			if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
				rdo_question.setEnabled(false);
				for(Answer answer:AnswerList.getAnswers()){
					if(answer.getMutipleQuestionId() == question.getId()){
						String[] answers = answer.getAnswer().split(",");
						for(String ans: answers){
							if(option.getOption().equals(ans) || option.getOptionMm().equals(ans)){
								rdo_question.setChecked(true);
							}
						}
					}
				}
			}
			layout_radio.addView(rdo_question);
			radio_id++;
		}
		layout_question.addView(txt_question_radio);
		layout_question.addView(layout_radio);

	}

	private void generateImageQuestion(Question question){
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 16, 0, 16); // llp.setMargins(left, top, right, bottom);

		// For Text Question
		CustomTextView txt_question_text = new CustomTextView(this);
		GridView grd_question = new GridView(this);

		txt_question_text.setLayoutParams(llp);
		if(langRef.getString("lang","").equals("mm"))
			txt_question_text.setText(Html.fromHtml(question.getQuestionMm()));
		else
			txt_question_text.setText(Html.fromHtml(question.getQuestion()));
		txt_question_text.setTextColor(getResources().getColor(R.color.competition_text_color));
		txt_question_text.setTextSize(18f);

		LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp2.setMargins(10, 0, 10, 0);
		grd_question.setLayoutParams(llp2);
		grd_question.setId(question.getId() + image_id);
		grd_question.setNumColumns(3);
		grd_question.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
		grd_question.setAdapter(new QuestionImageListAdapter(this, question.getOption()));
		setGridViewHeightBasedOnChildren(grd_question, 3);
		if(question.getAnswer() != null && question.getAnswer().length() > 0){
			for(int i=0; i < question.getOption().size(); i++){
				if(question.getOption().get(i).getOption().equals(question.getAnswer())){
					grd_question.setItemChecked(i, true);
				}
			}
		}
		//Checking for Already Submitted
		if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
			grd_question.setEnabled(false);
			for(Answer answer:AnswerList.getAnswers()){
				if(answer.getMutipleQuestionId() == question.getId()){
					for(int i=0; i < question.getOption().size(); i++){
						if(question.getOption().get(i).getOption().equals(answer.getAnswer())){
							grd_question.setItemChecked(i, true);
						}
					}
				}
			}
		}
		image_id++;
		layout_question.addView(txt_question_text);
		layout_question.addView(grd_question);

		if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
			btn_save.setEnabled(false);
			btn_submit.setEnabled(false);
		}
	}

	private void generatePhotoQuestion(Question question){
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 16, 0, 16); // llp.setMargins(left, top, right, bottom);

		// For Text Question
		CustomTextView txt_question_text = new CustomTextView(this);
		CustomButton image_upload_question = new CustomButton(this);
		ImageView image_upload_view = new ImageView(this);

		txt_question_text.setLayoutParams(llp);
		if(langRef.getString("lang","").equals("mm"))
			txt_question_text.setText(Html.fromHtml(question.getQuestionMm()));
		else
			txt_question_text.setText(Html.fromHtml(question.getQuestion()));
		txt_question_text.setTextColor(getResources().getColor(R.color.competition_text_color));
		txt_question_text.setTextSize(18f);

		LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp2.setMargins(10, 0, 10, 0);
		image_upload_view.setLayoutParams(llp2);
		image_upload_view.setId(question.getId()+upload_image_view_id);
		image_upload_view.setVisibility(View.GONE);

		LinearLayout.LayoutParams llp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp3.setMargins(10, 0, 10, 0);
		image_upload_question.setLayoutParams(llp3);
		image_upload_question.setId(question.getId() + upload_image_id);
		image_upload_question.setText(getResources().getString(R.string.str_upload_image));
		image_upload_question.setTag(image_upload_view.getId());
		image_upload_question.setBackgroundResource(android.R.color.transparent);
		image_upload_question.setTextSize(22f);
		image_upload_question.setCompoundDrawablePadding(16);
		image_upload_question.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_competition_camera, 0, 0, 0);
		image_upload_question.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				//TODO for Upload Image
				chooserType = ChooserType.REQUEST_PICK_PICTURE;
				imageChooserManager = new ImageChooserManager(CompetitionSubmitAnswerActivity.this,ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
				REQUEST_CROP_PICTURE = (int)v.getTag();
				imageChooserManager.setImageChooserListener(CompetitionSubmitAnswerActivity.this);
				try {
					filePath = imageChooserManager.choose();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//Checking for Local Saved
		if(question.getAnswer() != null && question.getAnswer().length() > 0){
			Picasso.with(this).load(question.getAnswer()).into(image_upload_view);
			image_upload_view.setVisibility(View.VISIBLE);
		}

		//Checking for Already Submitted
		if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
			image_upload_question.setEnabled(false);
			for(Answer answer:AnswerList.getAnswers()){
				if(answer.getMutipleQuestionId() == question.getId()){
					Picasso.with(this).load(answer.getAnswer()).into(image_upload_view);
					image_upload_view.setVisibility(View.VISIBLE);
				}
			}
		}
		upload_image_id++;
		upload_image_view_id++;
		layout_question.addView(txt_question_text);
		layout_question.addView(image_upload_view);
		layout_question.addView(image_upload_question);
	}

	private void generateAudioQuestion(Question question){
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 16, 0, 16); // llp.setMargins(left, top, right, bottom);

		// For Text Question
		CustomTextView txt_question_text = new CustomTextView(this);
		CustomButton audio_upload_question = new CustomButton(this);

		txt_question_text.setLayoutParams(llp);
		if(langRef.getString("lang","").equals("mm"))
			txt_question_text.setText(Html.fromHtml(question.getQuestionMm()));
		else
			txt_question_text.setText(Html.fromHtml(question.getQuestion()));
		txt_question_text.setTextColor(getResources().getColor(R.color.competition_text_color));
		txt_question_text.setTextSize(18f);

		LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llp2.setMargins(10, 0, 10, 0);
		audio_upload_question.setLayoutParams(llp2);
		audio_upload_question.setId(question.getId() + upload_audio_id);
		audio_upload_question.setTag(audio_upload_question.getId());
		audio_upload_question.setText(getResources().getString(R.string.str_upload_audio));
		audio_upload_question.setBackgroundResource(android.R.color.transparent);
		audio_upload_question.setTextSize(22f);
		audio_upload_question.setCompoundDrawablePadding(16);
		audio_upload_question.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_competition_audio, 0, 0, 0);
		audio_upload_question.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				//TODO for Upload Image
				audioPicker = new AudioPicker(CompetitionSubmitAnswerActivity.this);
				audioPicker.setAudioPickerCallback(new AudioPickerCallback() {
					@Override
					public void onAudiosChosen(List<ChosenAudio> files) {
						Log.i("Audio Choose: ", files.get(0).getOriginalPath());
						uploadingAudioFile("http://api.iwomenapp.org/api/v1/file/audioUpload", files.get(0).getOriginalPath(), String.valueOf(v.getTag()));
					}

					@Override
					public void onError(String message) {
						Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
					}
				});

				audioPicker.pickAudio();
			}
		});
		if(question.getAnswer() != null && question.getAnswer().length() > 0){
			audio_upload_question.setText(getResources().getString(R.string.str_ready_submit));
		}

		//Checking for Already Submitted
		if(AnswerList.getAnswers() != null && AnswerList.getAnswers().size() > 0){
			audio_upload_question.setEnabled(false);
			for(Answer answer:AnswerList.getAnswers()){
				if(answer.getMutipleQuestionId() == question.getId()){
					if(answer.getAnswer() != null && answer.getAnswer().length() > 0){
						audio_upload_question.setText(getResources().getString(R.string.str_already_uploaded));
					}
				}
			}
		}
		upload_audio_id++;
		layout_question.addView(txt_question_text);
		layout_question.addView(audio_upload_question);
	}

	private void reinitializeImageChooser() {
		imageChooserManager = new ImageChooserManager(this, chooserType,"myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(filePath);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		if(resultCode == RESULT_OK && requestCode == REQUEST_CROP_PICTURE){
			int get_upload_image_view_id = 700;
			for (int i=0; i<competitionQuestion.getMultipleQuestion().size();i++){
				if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("upload_photo")){
					if (REQUEST_CROP_PICTURE == (competitionQuestion.getMultipleQuestion().get(i).getId()+get_upload_image_view_id)) {
						// TODO for show upload image
						dialog = new ZProgressHUD(this);
						dialog.show();
						final ImageView img_answer = (ImageView) findViewById(competitionQuestion.getMultipleQuestion().get(i).getId()+get_upload_image_view_id);
						MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
						multipartTypedOutput.addPart("image", new TypedFile("image/png", croppedImageFile.getAbsoluteFile()));
						NetworkEngine.getInstance().uploadImage(multipartTypedOutput, new Callback<PhotoUpload>() {
							@Override
							public void success(PhotoUpload photoUpload, Response response) {
								dialog.dismissWithSuccess();
								img_answer.setVisibility(View.VISIBLE);
								img_answer.setImageBitmap(BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath()));
								img_answer.setTag(photoUpload.getResizeUrl().get(0));
							}

							@Override
							public void failure(RetrofitError error) {
								dialog.dismissWithFailure();
							}
						});

					}
					get_upload_image_view_id++;
				}

			}
		}

		if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
			if (imageChooserManager == null) {
				reinitializeImageChooser();
			}
			imageChooserManager.submit(requestCode, data);
		}

		if (requestCode == Picker.PICK_AUDIO && resultCode == RESULT_OK) {
			audioPicker.submit(data);
		}


		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", chooserType);
	}

	private void uploadPhoto(){

	}

	private UploadNotificationConfig getNotificationConfig() {
		return new UploadNotificationConfig()
				.setIcon(R.mipmap.ic_launcher)
				.setTitle(getString(R.string.app_name))
				.setInProgressMessage("Uploading Audio File")
				.setCompletedMessage("Your audio was successfully uploaded.")
				.setErrorMessage("Error: Can't upload your audio.")
				.setAutoClearOnSuccess(false)
				.setClickIntent(new Intent(getApplicationContext(), MainActivity.class))
				.setClearOnAction(true)
				.setRingToneEnabled(true);
	}

	private void uploadingAudioFile(String url, String filePath, String audioId) {
		final String serverUrlString = url;
		final String fileToUploadPath = filePath;
		final String uploadID = audioId.toString();
		pgDialog = new ProgressDialog(this);
		pgDialog.setTitle("Your audio file is uploading");
		pgDialog.setCancelable(false);
		pgDialog.setProgress(0);
		pgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pgDialog.show();
		try {
			new MultipartUploadRequest(this, uploadID, serverUrlString)
					.addFileToUpload(fileToUploadPath, "uploaded_file")
					.setNotificationConfig(getNotificationConfig())
					.setCustomUserAgent("UploadService/" + BuildConfig.VERSION_NAME)
					.setMaxRetries(2)
					.startUpload();

			// these are the different exceptions that may be thrown
		} catch (FileNotFoundException exc) {

		} catch (IllegalArgumentException exc) {

		} catch (MalformedURLException exc) {

		}
	}

	private String TAG = "File Upload";
	private final UploadServiceBroadcastReceiver uploadReceiver =
			new UploadServiceBroadcastReceiver() {

				@Override
				public void onProgress(String uploadId, int progress) {
					if (pgDialog != null)
						pgDialog.setProgress(progress);
					Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
				}

				@Override
				public void onError(String uploadId, Exception exception) {
					if (pgDialog != null)
						pgDialog.dismiss();
					Log.e(TAG, "Error in upload with ID: " + uploadId + ". "
							+ exception.getLocalizedMessage(), exception);
				}

				@Override
				public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
					if (pgDialog != null)
						pgDialog.dismiss();

					Log.i(TAG, "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
							+ serverResponseMessage);

					int get_upload_audio_id = 800;
					for (int i=0;i< competitionQuestion.getMultipleQuestion().size(); i++){
						if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("upload_audio")){
							Log.i(TAG, uploadId+"=>"+(competitionQuestion.getMultipleQuestion().get(i).getId()+get_upload_audio_id));
							if (Integer.valueOf(uploadId)  == (competitionQuestion.getMultipleQuestion().get(i).getId()+get_upload_audio_id)) {
								// TODO for show upload image
								competitionQuestion.getMultipleQuestion().get(i).setAnswer(serverResponseMessage.replace("\"",""));
							}
							get_upload_audio_id++;
						}

					}
					//TODO for save return audio file

				}
			};


	private void saveData(){
		Integer get_text_id = 100;
		Integer get_checkbox_id = 200;
		Integer get_radio_group_id = 300;
		Integer get_radio_id = 400;
		Integer get_image_id = 500;
		Integer get_upload_image_view_id = 700;


		if(competitionQuestion.getMultipleQuestion() != null && competitionQuestion.getMultipleQuestion().size() > 0){
			for(int i=0; i<competitionQuestion.getMultipleQuestion().size(); i++){
				if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("text")){
					EditText edt_question = (EditText) findViewById(competitionQuestion.getMultipleQuestion().get(i).getId()+get_text_id);
                    if(edt_question.getText().length() > 0)
					    competitionQuestion.getMultipleQuestion().get(i).setAnswer(edt_question.getText().toString());
					get_text_id++;
				}
				if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("checkbox")){
					competitionQuestion.getMultipleQuestion().get(i).setAnswer("");
					for(Option option: competitionQuestion.getMultipleQuestion().get(i).getOption()){
						CheckBox chk_question = (CheckBox) findViewById(competitionQuestion.getMultipleQuestion().get(i).getId()+get_checkbox_id);
						if(chk_question.isChecked()){
							competitionQuestion.getMultipleQuestion().get(i).appendAnswer(chk_question.getText().toString());
						}
						get_checkbox_id++;
					}
				}	
				if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("radio")){
					RadioGroup rdo_group = (RadioGroup) findViewById(competitionQuestion.getMultipleQuestion().get(i).getId()+get_radio_group_id);
					RadioButton rdo_question = (RadioButton) findViewById(rdo_group.getCheckedRadioButtonId());
                    if(rdo_question != null && rdo_question.getText().length() > 0)
					    competitionQuestion.getMultipleQuestion().get(i).setAnswer(rdo_question.getText().toString());
					get_radio_group_id++;
				}
				if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("image")){
					GridView grd_question = (GridView) findViewById(competitionQuestion.getMultipleQuestion().get(i).getId()+get_image_id);
					int clickedItemPosition = grd_question.getCheckedItemPosition();
					for(Option option:  competitionQuestion.getMultipleQuestion().get(i).getOption()){
						if(option.equals(competitionQuestion.getMultipleQuestion().get(i).getOption().get(clickedItemPosition))){
							competitionQuestion.getMultipleQuestion().get(i).setAnswer(option.getOption());
						}
					}
					get_image_id++;

				}
				if(competitionQuestion.getMultipleQuestion().get(i).getType().equals("upload_photo")){
					ImageView uploadPhoto = (ImageView) findViewById(competitionQuestion.getMultipleQuestion().get(i).getId()+get_upload_image_view_id);
					if(uploadPhoto.getTag() != null)
						competitionQuestion.getMultipleQuestion().get(i).setAnswer(uploadPhoto.getTag().toString());
					get_upload_image_view_id++;
				}
			}
		}

		StoreUtil.getInstance().saveTo("multipleQuestion_"+competitionQuestion.getId(),competitionQuestion.getMultipleQuestion());
	}

	public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int items = listAdapter.getCount();
		int rows = 0;

		View listItem = listAdapter.getView(0, null, gridView);
		listItem.measure(0, 0);
		totalHeight = listItem.getMeasuredHeight();

		float x = 1;
		if (items > columns) {
			x = items / columns;
			rows = (int) (x + 1);
			totalHeight *= x;
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);

	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == btn_save && btn_save.isEnabled()){
				saveData();
				SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this,getResources().getString(R.string.game_save), SKToastMessage.SUCCESS);// "Successfully saved"

			}
			
			if(arg0 == btn_submit && btn_submit.isEnabled()){
				if(AnswerList.getAnswers().size() == 0)
					postMutipleAnswer();
				else
					SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this,getResources().getString(R.string.str_already_submited), SKToastMessage.SUCCESS);
			}
			if(arg0 == btn_go_back){
				onBackPressed();
			}
		}
	};

	private void postMutipleAnswer(){
		dialog = new ZProgressHUD(this);
		dialog.show();
		saveData();
		List<MutipleAnswer> mutipleAnswers = new ArrayList<>();
		for(Question question: competitionQuestion.getMultipleQuestion()){
			if(question.getAnswer() != null && question.getAnswer().length() > 0){
				mutipleAnswers.add(new MutipleAnswer(question.getId(), question.getAnswer()));
			}
		}
		Log.i("Submit Data: ", mutipleAnswers.toString());
		NetworkEngine.getInstance().postCompetitionMutipleAnswer(MCrypt.getInstance().encrypt(mutipleAnswers.toString()), groupUserId, new Callback<String>() {
			@Override
			public void success(String s, Response response) {
				dialog.dismissWithSuccess();
				SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this,s,SKToastMessage.SUCCESS);
				Intent returnIntent = new Intent();
				setResult(RESULT_OK,returnIntent);
				finish();
			}

			@Override
			public void failure(RetrofitError error) {
				dialog.dismissWithFailure();
			}
		});
	}
	/*
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
	*/
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
		super.onBackPressed();
	}

	@Override
	public void onImageChosen(final ChosenImage image) {
		Log.i("Image Chooser: ", image.getFilePathOriginal().toString());
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (image != null) {
					croppedImageFile = new File(image.getFilePathOriginal());
					Log.i("Image Chooser: ", croppedImageFile.toString());
					Uri croppedImage = Uri.fromFile(croppedImageFile);
					CropImageIntentBuilder cropImage = new CropImageIntentBuilder(512, 512, croppedImage);
					cropImage.setSourceImage(croppedImage);
					startActivityForResult(cropImage.getIntent(getApplicationContext()), REQUEST_CROP_PICTURE);
				}
			}
		});
	}

	@Override
	public void onError(final String s) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onImagesChosen(ChosenImages chosenImages) {

	}

	@Override
	public void onResume() {
		super.onResume();
		uploadReceiver.register(this);

	}


	@Override
	public void onPause() {
		super.onPause();
		uploadReceiver.unregister(this);
	}
}
