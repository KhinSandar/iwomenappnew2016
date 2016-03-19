package org.undp_iwomen.iwomen.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smk.skconnectiondetector.SKConnectionDetector;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.smk.clientapi.NetworkEngine;
import org.smk.model.CalendarEvent;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.widget.CustomButton;
import org.undp_iwomen.iwomen.ui.widget.CustomEditText;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class NewEventFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences sharePrefLanguageUtil;
    private String lang;
    private Context mContext;

    private static final String TAG_START_DATE = "start_date";
    private static final String TAG_END_DATE = "end_date";
    private static final String TAG_START_TIME = "start_time";
    private static final String TAG_END_TIME = "end_time";

    private TextInputLayout mTitleTextInputLayout;
    private TextInputLayout mDescriptionInputLayout;
    private TextInputLayout mLocationInputLayout;
    private TextInputLayout mStartDateInputLayout;
    private TextInputLayout mStartTimeInputLayout;
    private TextInputLayout mEndDateInputLayout;
    private TextInputLayout mEndTimeInputLayout;

    private CustomEditText mTitleEditText;
    private CustomEditText mDescriptionEditText;
    private CustomEditText mLocationEditText;
    private CustomEditText mStartDateEditText;
    private CustomEditText mEndDateEditText;
    private CustomEditText mStartTimeEditText;
    private CustomEditText mEndTimeEditText;
    private CustomButton mSaveButton;

    private Date mStartDate;
    private Date mEndDate;
    private Date mStartTime;
    private Date mEndTime;

    private SharedPreferences mSharedPreferencesUserInfo;
    private String user_name, user_obj_id, user_id, user_role, user_ph, register_msg, user_img_path;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartDate = new Date();
        mEndDate = new Date();
        mStartTime = new Date();
        mEndTime = new Date();
    }

    public static NewEventFragment newInstance() {
        return new NewEventFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        mContext = getActivity().getApplicationContext();
        sharePrefLanguageUtil = getActivity().getSharedPreferences(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING, Context.MODE_PRIVATE);
        lang = sharePrefLanguageUtil.getString(org.undp_iwomen.iwomen.utils.Utils.PREF_SETTING_LANG, org.undp_iwomen.iwomen.utils.Utils.ENG_LANG);
        mSharedPreferencesUserInfo = getActivity().getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

        user_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);

        initViews(rootView);
        initViewsListeners();
        return rootView;
    }


    private void initViews(View rootView) {
        mTitleTextInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_title_input);
        mDescriptionInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_description_input);
        mLocationInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_location_input);
        mStartDateInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_start_date_input);
        mStartTimeInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_start_time_input);
        mEndDateInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_end_date_input);
        mEndTimeInputLayout = (TextInputLayout)rootView.findViewById(R.id.new_event_end_time_input);


        mTitleEditText = (CustomEditText) rootView.findViewById(R.id.new_event_title);
        mDescriptionEditText = (CustomEditText) rootView.findViewById(R.id.new_event_description);
        mLocationEditText = (CustomEditText) rootView.findViewById(R.id.new_event_location);
        mStartDateEditText = (CustomEditText) rootView.findViewById(R.id.new_event_start_date);
        mEndDateEditText = (CustomEditText) rootView.findViewById(R.id.new_event_end_date);
        mStartTimeEditText = (CustomEditText) rootView.findViewById(R.id.new_event_start_time);
        mEndTimeEditText = (CustomEditText) rootView.findViewById(R.id.new_event_end_time);
        mSaveButton = (CustomButton) rootView.findViewById(R.id.new_event_save_button);
    }

    private void initViewsListeners() {
        mStartDateEditText.setOnClickListener(this);
        mEndDateEditText.setOnClickListener(this);
        mStartTimeEditText.setOnClickListener(this);
        mEndTimeEditText.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_event_start_date:
                showStartDatePicker();
                break;
            case R.id.new_event_end_date:
                showEndDatePicker();
                break;
            case R.id.new_event_start_time:
                showStartTimePicker();
                break;
            case R.id.new_event_end_time:
                showEndTimePicker();
                break;
            case R.id.new_event_save_button:
                //Toast.makeText(getContext(), "Save Date ...", Toast.LENGTH_SHORT).show();
                saveEventDate();
                break;
        }
    }
    private void saveEventDate(){
        if (Connection.isOnline(getActivity())) {

            final String title = mTitleEditText.getText().toString().trim();
            final String description = mDescriptionEditText.getText().toString().trim();
            final String location = mLocationEditText.getText().toString().trim();
            final String start_date = mStartDateEditText.getText().toString().trim();
            final String end_date = mEndDateEditText.getText().toString().trim();
            final String start_time = mStartTimeEditText.getText().toString().trim();
            final String end_time = mEndTimeEditText.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                mTitleTextInputLayout.setError(getResources().getString(R.string.calendar_title_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_title_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_title_error));
                }

                return;
            } else {
                mTitleTextInputLayout.setErrorEnabled(false);
            }

            if (TextUtils.isEmpty(description)) {
                mDescriptionInputLayout.setError(getResources().getString(R.string.calendar_description_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_description_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_description_error));
                }

                return;
            } else {
                mDescriptionInputLayout.setErrorEnabled(false);
            }
            if (TextUtils.isEmpty(location)) {
                mLocationInputLayout.setError(getResources().getString(R.string.calendar_location_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_location_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_location_error));
                }

                return;
            } else {
                mLocationInputLayout.setErrorEnabled(false);
            }
            //
            if (TextUtils.isEmpty(start_date)) {
                mStartDateInputLayout.setError(getResources().getString(R.string.calendar_start_date_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_start_date_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_start_date_error));
                }

                return;
            } else {
                mStartDateInputLayout.setErrorEnabled(false);
            }
            //
            if (TextUtils.isEmpty(end_date)) {
                mEndDateInputLayout.setError(getResources().getString(R.string.calendar_end_date_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_end_date_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_end_date_error));
                }

                return;
            } else {
                mEndDateInputLayout.setErrorEnabled(false);
            }
            //
            if (TextUtils.isEmpty(start_time)) {
                mStartTimeInputLayout.setError(getResources().getString(R.string.calendar_start_time_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_start_time_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_start_time_error));
                }

                return;
            } else {
                mStartTimeInputLayout.setErrorEnabled(false);
            }
            //
            if (TextUtils.isEmpty(end_time)) {
                mEndTimeInputLayout.setError(getResources().getString(R.string.calendar_end_time_error));

                if (lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.calendar_end_time_error));
                } else if (lang.equals(Utils.MM_LANG)) {
                    Utils.doToastMM(mContext, getResources().getString(R.string.calendar_end_time_error));
                }

                return;
            } else {
                mEndTimeInputLayout.setErrorEnabled(false);
            }
            Log.e("<<Calendar>>","Date==>" +getServerDateString(mStartDate));
            //Date==>Wed Mar 02 23:51:09 GMT+06:30 2016
            if (lang.equals(Utils.ENG_LANG)) {
                NetworkEngine.getInstance().postCreateCalendarEventEng(user_id,description, title, location, getServerDateString(mStartDate), getServerDateString(mEndDate), start_time, end_time, new Callback<CalendarEvent>() {
                    @Override
                    public void success(CalendarEvent calendarEvent, Response response) {
                        Utils.doToastEng(mContext, getResources().getString(R.string.calendar_success));
                        cleardata();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }else{
                NetworkEngine.getInstance().postCreateCalendarEventMM(user_id,description, title, location, getServerDateString(mStartDate), getServerDateString(mEndDate), start_time, end_time, new Callback<CalendarEvent>() {
                    @Override
                    public void success(CalendarEvent calendarEvent, Response response) {
                        Utils.doToastMM(mContext, getResources().getString(R.string.calendar_success_mm));
                        cleardata();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }else {

            SKConnectionDetector.getInstance(getActivity()).showErrorMessage();

        }
    }
    private void cleardata(){
        mTitleEditText.setText("");
        mDescriptionEditText.setText("");
        mLocationEditText.setText("");
        mStartDateEditText.setText("");
        mEndDateEditText.setText("");
        mStartTimeEditText.setText("");
        mEndTimeEditText.setText("");
    }


    private void showStartDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mStartDate = calendar.getTime();
                mStartDateEditText.setText(getUserFriendlyDateString(mStartDate));
            }
        }, year, month, day);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        dialog.setMinDate(now);
        dialog.show(getActivity().getFragmentManager(), TAG_START_DATE);
    }

    private void showStartTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartTime);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mStartTime = calendar.getTime();
                mStartTimeEditText.setText(getUserFriendlyTimeString(mStartTime));
            }
        }, hour, minute, false);

        dialog.show(getActivity().getFragmentManager(), TAG_START_TIME);
    }

    private void showEndDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mEndDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mEndDate = calendar.getTime();
                mEndDateEditText.setText(getUserFriendlyDateString(mEndDate));
            }
        }, year, month, day);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        dialog.setMinDate(now);
        dialog.show(getActivity().getFragmentManager(), TAG_END_DATE);
    }

    private void showEndTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mEndTime);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        final TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                mEndTime = calendar.getTime();
                mEndTimeEditText.setText(getUserFriendlyTimeString(mEndTime));
            }
        }, hour, minute, false);

        dialog.show(getActivity().getFragmentManager(), TAG_END_TIME);
    }


    private String getUserFriendlyDateString(Date date){
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    private String getUserFriendlyTimeString(Date date){
        String format = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    private String getServerDateString(Date date){
        String format = "yyyy-MM-dd hh:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
