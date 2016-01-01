package com.smk.application;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import android.widget.TextView;

public class TimeSubtractionUtil {
	
	private Callback mCallback;
	
	private TextView view;

	private boolean stopTimer = false;
	
	public void runOnList(){
		Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        	  @Override
        	  public void run() {
        		 if(mCallback != null){
            	    mCallback.runOnList();
            	 }
        	  }
        	}, 1000, 1000);
	}
	
	public void setEndDate(final String endDate){
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        	  @Override
        	  public void run() {
        		 if(mCallback != null && getView() != null){
            	    mCallback.run(getTimeSubtraction(endDate), getView());
            	    mCallback.runOnList();
            	 }
        	  }
        	}, 1000, 1000);
	}
	
	public void setEndDate(final String currentDate, final String endDate){
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
        	  @Override
        	  public void run() {
        		 if(mCallback != null && stopTimer == false){
            	    mCallback.run(getTimeSubtraction(currentDate,endDate));
            	 }
        	  }
        	}, 1000, 1000);
	}
	
	private void destory(){
		stopTimer  = true;
	}
	
	public String getTimeSubtraction(String date){
		final String givenDateTime = date;
		final String currentDateTime = getToday();
		long startDate = changeFrom(currentDateTime);
		long endDate = changeFrom(givenDateTime);
		if(startDate < endDate){
			long betweenDateTime = endDate - startDate;
			
			long secondsInMilli = 1000;
			long minutesInMilli = secondsInMilli * 60;
			long hoursInMilli = minutesInMilli * 60;
			long daysInMilli = hoursInMilli * 24;
	 
			long elapsedDays = betweenDateTime / daysInMilli;
			betweenDateTime = betweenDateTime % daysInMilli;
	 
			long elapsedHours = betweenDateTime / hoursInMilli;
			betweenDateTime = betweenDateTime % hoursInMilli;
	 
			long elapsedMinutes = betweenDateTime / minutesInMilli;
			betweenDateTime = betweenDateTime % minutesInMilli;
	 
			long elapsedSeconds = betweenDateTime / secondsInMilli;
	 
    	    String diffTime =elapsedDays + " : " + (elapsedHours<10 ? "0" + elapsedHours : elapsedHours) + " : " + (elapsedMinutes < 10 ? "0" + elapsedMinutes : elapsedMinutes) + " : " + (elapsedSeconds < 10 ? "0" + elapsedSeconds : elapsedSeconds);
    	    
    	    return diffTime;
		}else{
			return "00:00:00";
		}
	}
	
	public String getTimeSubtraction(String cDate,String eDate){
		final String givenDateTime = eDate;
		final String currentDateTime = getToday();
		long startDate = changeFrom(currentDateTime);
		long endDate = changeFrom(givenDateTime);
		if(startDate < endDate){
			long betweenDateTime = endDate - startDate;
			
			long secondsInMilli = 1000;
			long minutesInMilli = secondsInMilli * 60;
			long hoursInMilli = minutesInMilli * 60;
			long daysInMilli = hoursInMilli * 24;
	 
			long elapsedDays = betweenDateTime / daysInMilli;
			betweenDateTime = betweenDateTime % daysInMilli;
	 
			long elapsedHours = betweenDateTime / hoursInMilli;
			betweenDateTime = betweenDateTime % hoursInMilli;
	 
			long elapsedMinutes = betweenDateTime / minutesInMilli;
			betweenDateTime = betweenDateTime % minutesInMilli;
	 
			long elapsedSeconds = betweenDateTime / secondsInMilli;
	 
    	    String diffTime =elapsedDays + " : " + (elapsedHours<10 ? "0" + elapsedHours : elapsedHours) + " : " + (elapsedMinutes < 10 ? "0" + elapsedMinutes : elapsedMinutes) + " : " + (elapsedSeconds < 10 ? "0" + elapsedSeconds : elapsedSeconds);
    	    
    	    return diffTime;
		}else{
			destory();
			return null;
		}
	}
	
	
	public void setCallbackListener(Callback callback){
		mCallback = callback;
	}
	
	public interface Callback{
		void run(String diffTime, TextView view);
		void run(String diffTime);
		void runOnList();
	}
	
	public TextView getView() {
		return view;
	}


	public void setView(TextView view) {
		this.view = view;
	}

	private String addingSecond(String cDate){
		final long ONE_SECOND_IN_MILLIS=1000;
		
		long t = changeFrom(cDate);
		Date afterAddingOneSecond=new Date(t + (1 * ONE_SECOND_IN_MILLIS));
		
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(afterAddingOneSecond);
		
	}

	private String getToday(){
		Calendar c = Calendar.getInstance(Locale.getDefault());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}
    
    private long changeFrom(String str){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			return df.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
    }
    
    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
    
    public String convertDayTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd HH:mm:ss");
        return format.format(date);
    }
}
