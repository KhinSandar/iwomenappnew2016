package com.smk.iwomen;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;

import com.nullwire.trace.ExceptionHandler;
import com.path.android.jobqueue.JobManager;
import com.smk.application.DownloadManager;
import com.smk.clientapi.NetworkEngine;
import com.smk.model.APKVersion;
import com.smk.model.Download;
import com.smk.model.Review;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.manager.MainApplication;

import java.io.File;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseActionBarActivity extends AppCompatActivity{
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private static boolean isCheckedVersion = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExceptionHandler.register(this, "http://128.199.70.154/api-v1/error_report");
		// TODO it is for non application market.
		if( !isCheckedVersion ){
			checkAPKVersion();
		}
	}
	
	private void checkAPKVersion(){
		
		NetworkEngine.getInstance().getAPKVersion("", new Callback<APKVersion>() {
			
			@Override
			public void success(final APKVersion arg0, Response arg1) {
				// TODO Auto-generated method stub
				try {
					isCheckedVersion = true;
					int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

					if(arg0 != null) {

						if (arg0.getVersionId() > versionCode) {
							//TODO
							try {
								//Dialog True 4
								//Dialog False dismisss ---
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActionBarActivity.this);

								// set title
								alertDialogBuilder.setTitle("New Version "+arg0.getVersionName());

								// set dialog message
								alertDialogBuilder
										.setMessage("Do you download new version?")
										.setCancelable(true)
										.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												EventBus.getDefault().register(BaseActionBarActivity.this);
												JobManager jobManager = MainApplication.getInstance().getJobManager();
												DownloadManager downloadManager = new DownloadManager("http://128.199.70.154/apk/" + arg0.getName(), arg0.getName());
												jobManager.addJob(downloadManager);
											}
										})
										.setNegativeButton("No",new DialogInterface.OnClickListener() {
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
								e.printStackTrace();
								isCheckedVersion = false;
							}



						}
					}
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (NullPointerException ex){
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
		Button btn_now_know = (Button)convertView.findViewById(R.id.btn_not_now);
		Button btn_ok = (Button)convertView.findViewById(R.id.btn_ok);
		alertDialog.setView(convertView);
		alertDialog.show();

		btn_now_know.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				postReview(userId, Double.valueOf(be_inspired.getRating()), "Be Inspired");
				postReview(userId, Double.valueOf(be_knowledgeable.getRating()), "Be Knowledgeable");
				postReview(userId, Double.valueOf(be_together.getRating()), "Be Together");
				postReview(userId, Double.valueOf(talk_together.getRating()), "Talk Together");

				finish();

			}
		});
	}

	public static int getRatingIcon(Integer ratings){
		int ratingOf = 0;
		switch (ratings){
			case 1:
				ratingOf = R.drawable.ic_stars_1;
				break;
			case 2:
				ratingOf = R.drawable.ic_stars_2;
				break;
			case 3:
				ratingOf = R.drawable.ic_stars_3;
				break;
			case 4:
				ratingOf = R.drawable.ic_stars_4;
				break;
			case 5:
				ratingOf = R.drawable.ic_stars_5;
				break;
			default:
				break;
		}
		return ratingOf;
	}

	public static String getRatingDesc(Integer ratings){
		String ratingOfDesc = null;
		switch (ratings){
			case 1:
				ratingOfDesc = "Poor";
				break;
			case 2:
				ratingOfDesc = "Fair";
				break;
			case 3:
				ratingOfDesc = "Good";
				break;
			case 4:
				ratingOfDesc = "Very Good";
				break;
			case 5:
				ratingOfDesc = "Excellent";
				break;
			default:
				break;
		}
		return ratingOfDesc;
	}

	public void postReview(String userId, Double ratings, String function){
		NetworkEngine.getInstance().postReview(userId, ratings, function, new Callback<Review>() {
			@Override
			public void success(Review review, Response response) {

			}

			@Override
			public void failure(RetrofitError error) {

			}
		});
	}
	
	// This is processing when downloading file.
	public void onEventMainThread(final Download download) {
		// the posted event can be processed in the main thread
		Log.i("", "Hello downloading precent is : " + download.getDownloadPercent());
		int id = 1;
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new Builder(this);
		mBuilder.setContentTitle("IWomen Update Download")
		    .setContentText("Download in progress")
		    .setSmallIcon(R.drawable.ic_launcher);
		if(download.getStatus()){
            mBuilder.setContentText("Download is complete")
            	.setProgress(0,0,false);
            mNotifyManager.notify(id, mBuilder.build());
            
            // raise notification
    		Intent intent = new Intent(Intent.ACTION_VIEW);
    		intent.setDataAndType(Uri.fromFile(new File(download.getFilePath())), "application/vnd.android.package-archive");
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		
		}else{
			//TODO show progress
			mBuilder.setProgress(100, download.getDownloadPercent(), false);
			mNotifyManager.notify(id, mBuilder.build());
			
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
