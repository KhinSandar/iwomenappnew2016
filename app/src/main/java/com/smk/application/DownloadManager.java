package com.smk.application;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

@SuppressWarnings("serial")
public class DownloadManager extends Job {
	/**
	 * 
	 */
	public String url = "";
	public static final int PRIORITY = 1;

	private DownloadTask downloadTask;
	private String fileName;
	
	public DownloadManager(String url, String fileName) {
		super(new Params(PRIORITY).requireNetwork().persist());
		// TODO Auto-generated constructor stub
		this.url = url;
		this.fileName = fileName;
	}
	
	@Override
	public void onAdded() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onCancel() {
		// TODO Auto-generated method stub
		Log.i("","Hello Cancel..");
	}

	@Override
	public void onRun() throws Throwable {
		downloadTask = new DownloadTask(this.url, fileName);
		downloadTask.startDownload();
		
	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable throwable) {
		// TODO Auto-generated method stub
		return false;
	}

}
