package org.undp_iwomen.iwomen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.undp_iwomen.iwomen.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Toe Lie on 2/6/2016.
 */
public class NewEventFragment extends Fragment implements View.OnClickListener {

    private static final String TAG_START_DATE = "start_date";
    private static final String TAG_END_DATE = "end_date";
    private static final String TAG_START_TIME = "start_time";
    private static final String TAG_END_TIME = "end_time";

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private EditText mLocationEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private EditText mStartTimeEditText;
    private EditText mEndTimeEditText;
    private Button mSaveButton;

    private Date mStartDate;
    private Date mEndDate;
    private Date mStartTime;
    private Date mEndTime;

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
        initViews(rootView);
        initViewsListeners();
        return rootView;
    }


    private void initViews(View rootView) {
        mTitleEditText = (EditText) rootView.findViewById(R.id.new_event_title);
        mDescriptionEditText = (EditText) rootView.findViewById(R.id.new_event_description);
        mLocationEditText = (EditText) rootView.findViewById(R.id.new_event_location);
        mStartDateEditText = (EditText) rootView.findViewById(R.id.new_event_start_date);
        mEndDateEditText = (EditText) rootView.findViewById(R.id.new_event_end_date);
        mStartTimeEditText = (EditText) rootView.findViewById(R.id.new_event_start_time);
        mEndTimeEditText = (EditText) rootView.findViewById(R.id.new_event_end_time);
        mSaveButton = (Button) rootView.findViewById(R.id.new_event_save_button);
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
                Toast.makeText(getContext(), "Save Date ...", Toast.LENGTH_SHORT).show();
                break;
        }
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
}