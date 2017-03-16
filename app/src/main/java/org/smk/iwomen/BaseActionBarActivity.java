package org.smk.iwomen;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.path.android.jobqueue.JobManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.nullwire.trace.ExceptionHandler;
import org.smk.application.DownloadManager;
import org.smk.application.StoreUtil;
import org.smk.clientapi.NetworkEngine;
import org.smk.model.APKVersion;
import org.smk.model.Download;
import org.smk.model.Review;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.manager.MainApplication;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseActionBarActivity extends AppCompatActivity{
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private static boolean isCheckedVersion = false;
	public Integer  UsageCount = 0;
	public int versionCode = 0;
	public Tracker mTracker;
	private ProgressDialog pgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExceptionHandler.register(this, "http://128.199.70.154/api-v1/error_report");
		// TODO it is for non application market.
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		}catch (NameNotFoundException e){

		}

		MainApplication application = (MainApplication) getApplication();
		mTracker = application.getDefaultTracker();

		PackageManager packageManager = getPackageManager();
		try {
			ActivityInfo info = packageManager.getActivityInfo(getComponentName(), 0);
			Log.e("app", "Activity name:" + info.name);
			mTracker.setScreenName("Screen: "+ info.name);
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		//checkAPKVersion();
	}


	private void checkAPKVersion(){
		
		NetworkEngine.getInstance().getAPKVersion("", new Callback<APKVersion>() {
			
			@Override
			public void success(final APKVersion arg0, Response arg1) {
				// TODO Auto-generated method stub
				try {


					if(arg0 != null) {

						if (arg0.getVersionId() > versionCode) {
							//TODO
							try {
								//Dialog True 4
								//Dialog False dismisss ---
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActionBarActivity.this);

								// set title
								alertDialogBuilder.setTitle(getResources().getString(R.string.str_new_version)+arg0.getVersionName());

								// set dialog message
								alertDialogBuilder
										.setMessage(getResources().getString(R.string.str_new_version_message))
										.setCancelable(true)
										.setPositiveButton(getResources().getString(R.string.str_ok),new DialogInterface.OnClickListener() {
											@Subscribe
											public void onClick(DialogInterface dialog,int id) {
												EventBus.getDefault().register(BaseActionBarActivity.this);
												JobManager jobManager = MainApplication.getInstance().getJobManager();
												DownloadManager downloadManager = new DownloadManager(arg0.getName(), "iWomenAPK-"+arg0.getVersionName());
												jobManager.addJob(downloadManager);

												pgDialog = new ProgressDialog(BaseActionBarActivity.this);
												pgDialog.setTitle("iWomen Update Downloading");
												pgDialog.setCancelable(false);
												pgDialog.setProgress(0);
												pgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
												pgDialog.show();
											}
										})
										.setNegativeButton(getResources().getString(R.string.str_not_now),new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												// if this button is clicked, just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();

								// show it
								alertDialog.show();
							}catch (WindowManager.BadTokenException e){

							}



						}
					}
				} catch (NullPointerException ex){
					//ex.printStackTrace();
				}
				
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				
			}
		});		
	}

	public void showReviewDialog(final String userId){
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		View convertView = View.inflate(this,R.layout.dialog_reviews,null);

		final RatingBar be_inspired = (RatingBar) convertView.findViewById(R.id.rating_be_inspired);
		final RatingBar be_knowledgeable = (RatingBar) convertView.findViewById(R.id.rating_be_knowledgeable);
		final RatingBar be_together = (RatingBar) convertView.findViewById(R.id.rating_be_together);
		final RatingBar talk_together = (RatingBar) convertView.findViewById(R.id.rating_talk_together);

		final TextView rating_desc_be_inspired = (TextView) convertView.findViewById(R.id.txt_rating_desc_be_inspired);
		final TextView rating_desc_be_knowledgeable = (TextView) convertView.findViewById(R.id.txt_rating_desc_be_knowledgeable);
		final TextView rating_desc_be_together = (TextView) convertView.findViewById(R.id.txt_rating_desc_be_together);
		final TextView rating_desc_talk_together = (TextView) convertView.findViewById(R.id.txt_rating_desc_talk_together);
		Button btn_now_know = (Button)convertView.findViewById(R.id.btn_not_now);
		Button btn_ok = (Button)convertView.findViewById(R.id.btn_ok);
		alertDialog.setView(convertView);
		alertDialog.show();

		be_inspired.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_desc_be_inspired.setText(getRatingDesc(Double.valueOf(rating)));
			}
		});

		be_knowledgeable.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_desc_be_knowledgeable.setText(getRatingDesc(Double.valueOf(rating)));
			}
		});

		be_together.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_desc_be_together.setText(getRatingDesc(Double.valueOf(rating)));
			}
		});

		talk_together.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_desc_talk_together.setText(getRatingDesc(Double.valueOf(rating)));
			}
		});

		btn_now_know.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();


			}
		});

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				postReview(userId, Double.valueOf(be_inspired.getRating()), "Be Inspired", "");
				postReview(userId,  Double.valueOf(be_knowledgeable.getRating()), "Be Knowledgeable", "");
				postReview(userId, Double.valueOf(be_together.getRating()), "Be Together", "");
				postReview(userId, Double.valueOf(talk_together.getRating()), "Talk Together", "");

				showFeedBack(userId);
				finish();


			}
		});
	}

	
	public void showFeedbackDialog(final String userId, final Double be_inspired_rate, final Double be_knowledgeable, final Double be_together, final Double talk_together){
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		View convertView = View.inflate(this,R.layout.dialog_feedback,null);
		final EditText feedback = (EditText) convertView.findViewById(R.id.edt_review);
		Button btn_ok = (Button)convertView.findViewById(R.id.btn_ok);
		alertDialog.setView(convertView);
		alertDialog.show();

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				postReview(userId, be_inspired_rate, "Be Inspired", feedback.getText().toString());
				postReview(userId, be_knowledgeable, "Be Knowledgeable", feedback.getText().toString());
				postReview(userId, be_together, "Be Together", feedback.getText().toString());
				postReview(userId, talk_together, "Talk Together", feedback.getText().toString());

				finish();

			}
		});
	}

	public void showFeedBack(final String userId){
		//final Dialog alertDialog = new Dialog(BaseActionBarActivity.this);
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		View convertView = View.inflate(this,R.layout.dialog_feedback,null);
		final EditText feedback = (EditText) convertView.findViewById(R.id.edt_review);
		Button btn_ok = (Button)convertView.findViewById(R.id.btn_ok);
		Button btn_now_know = (Button)convertView.findViewById(R.id.btn_not_now);
		alertDialog.setView(convertView);
		alertDialog.show();
		//alertDialog.setContentView(convertView);
		//alertDialog.show();

		btn_now_know.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
				//alertDialog.dismiss();

			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				StoreUtil.getInstance().saveTo("feedback", true);

				finish();
			}
		});

		/*btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if(feedback.getText().length() > 0){
                    postReview(userId, 0.0, "Be Inspired", feedback.getText().toString());
                    postReview(userId, 0.0, "Be Knowledgeable", feedback.getText().toString());
                    postReview(userId, 0.0, "Be Together", feedback.getText().toString());
                    postReview(userId, 0.0, "Talk Together", feedback.getText().toString());
                    StoreUtil.getInstance().saveTo("feedback", true);
                    finish();
					//alertDialog.dismiss();

				}else{
                    SKToastMessage.showMessage(BaseActionBarActivity.this, getResources().getString(R.string.str_required), SKToastMessage.ERROR);
                }


			}
		});*/
	}

	public static int getRatingIcon(Double ratings){
		int ratingOf = 0;
		if(ratings >0 && ratings <= 1.5){
			ratingOf = R.drawable.ic_stars_1;
		}
		if(ratings >1.5 && ratings <= 2.5){
			ratingOf = R.drawable.ic_stars_2;
		}
		if(ratings >2.5 && ratings <= 3.5){
			ratingOf = R.drawable.ic_stars_3;
		}
		if(ratings >3.5 && ratings <= 4.5){
			ratingOf = R.drawable.ic_stars_4;
		}
		if(ratings >4.5) {
			ratingOf = R.drawable.ic_stars_5;
		}
		return ratingOf;
	}

	public String getRatingDesc(Double ratings){
		String ratingOfDesc = "";
		if(ratings >0 && ratings <= 1.5){
			ratingOfDesc = getResources().getString(R.string.str_poor);
		}
		if(ratings >1.5 && ratings <= 2.5){
			ratingOfDesc = getResources().getString(R.string.str_fair);
		}
		if(ratings >2.5 && ratings <= 3.5){
			ratingOfDesc = getResources().getString(R.string.str_good);
		}
		if(ratings >3.5 && ratings <= 4.5){
			ratingOfDesc = getString(R.string.str_very_good);
		}
		if(ratings >4.5) {
			ratingOfDesc = getResources().getString(R.string.str_excellent);
		}
		return ratingOfDesc;
	}

	public void postReview(final String userId, Double ratings, String function, String review){
		NetworkEngine.getInstance().postReview(userId, ratings, function, review, new Callback<Review>() {
			@Override
			public void success(Review review, Response response) {
				StoreUtil.getInstance().saveTo(userId+versionCode, review);
			}

			@Override
			public void failure(RetrofitError error) {

			}
		});
	}
	
	// This is processing when downloading file.
	@Subscribe
	public void onEventMainThread(final Download download) {
		// the posted event can be processed in the main thread
		int id = 1;
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new Builder(this);
		mBuilder.setContentTitle("IWomen Update Download")
		    .setContentText("Download in progress")
		    .setSmallIcon(R.drawable.ic_launcher);
		if(download.getStatus()){

			if (pgDialog != null)
				pgDialog.dismiss();

            mBuilder.setContentText("Download is complete")
            	.setProgress(0,0,false);
            mNotifyManager.notify(id, mBuilder.build());
            
            // raise notification
    		Intent intent = new Intent(Intent.ACTION_VIEW);
    		intent.setDataAndType(Uri.fromFile(new File(download.getFilePath())), "application/vnd.android.package-archive");
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		
		}else{
			mBuilder.setProgress(100, download.getDownloadPercent(), false);
			mNotifyManager.notify(id, mBuilder.build());

			if (pgDialog != null)
				pgDialog.setProgress(download.getDownloadPercent());
			
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
