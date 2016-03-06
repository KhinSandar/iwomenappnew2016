package org.undp_iwomen.iwomen.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.smk.skconnectiondetector.SKConnectionDetector;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.smk.clientapi.NetworkEngine;
import org.smk.model.CalendarEvent;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.ui.adapter.CalendarViewPagerFragmentAdapter;
import org.undp_iwomen.iwomen.ui.fragment.CaldroidSampleCustomFragment;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@SuppressLint("SimpleDateFormat")
public class CalendarActivity extends AppCompatActivity {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    //MY CUSTOM CODE
    private ViewPager viewPager;
    private CalendarViewPagerFragmentAdapter mAdapter;
    private ScrollView hsv;
    private CustomTextView textViewTitle;
    private SharedPreferences mSharedPreferencesUserInfo;
    private SharedPreferences sharePrefLanguageUtil;
    private String mstr_lang;
    private String user_name, user_obj_id, user_id, user_role, user_ph, register_msg, user_img_path;

    private ZProgressHUD zPDialog;

    private void setCustomResourceForDates(String month_dates) {

        /*********Get the Date From Server**************/

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");



        if (Connection.isOnline(getApplicationContext())) {

            zPDialog = new ZProgressHUD(this);
            zPDialog.show();
            NetworkEngine.getInstance().getCalendarListByDateMothEvent(month_dates, new Callback<List<CalendarEvent>>() {
                @Override
                public void success(List<CalendarEvent> calendarEvents, Response response) {

                    zPDialog.dismissWithSuccess();
                    for (int i = 0; i < calendarEvents.size(); i++) {

                        Date color_date = null;
                        try {
                            color_date = formatter.parse(calendarEvents.get(i).getStartDate());


                            caldroidFragment.setBackgroundResourceForDate(R.color.primary,
                                    color_date);
                            caldroidFragment.setTextColorForDate(R.color.white, color_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    zPDialog.dismissWithSuccess();
                }
            });

        } else {
            SKConnectionDetector.getInstance(CalendarActivity.this).showErrorMessage();
        }

        /**********Get the Date From Server**************/

       /* Calendar cal = Calendar.getInstance();
        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();*/

        /*if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.accent,
                    blueDate);
            caldroidFragment.setBackgroundResourceForDate(R.color.primary,
                    greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_calendar_custom_main);

        /**********Set up the ToolBar**************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        textViewTitle = (CustomTextView) toolbar.findViewById(R.id.title_action2);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewTitle.setText(getResources().getString(R.string.calendar_name));

        /**********Set up the ToolBar**************/

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");


        mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        sharePrefLanguageUtil = getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        mstr_lang = sharePrefLanguageUtil.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
        user_role = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ROLE, null);


        user_name = mSharedPreferencesUserInfo.getString(CommonConfig.USER_NAME, null);
        user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);


        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        //caldroidFragment = new CaldroidFragment();

        /**********View Pager**************/
        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
        caldroidFragment = new CaldroidSampleCustomFragment();
        mAdapter = new CalendarViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.calendar_pager);
        hsv = (ScrollView) findViewById(R.id.hsv_calendar);
        viewPager.setAdapter(mAdapter);


        viewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        hsv.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        hsv.requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        hsv.requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        hsv.requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return viewPager.onTouchEvent(event);
            }
        });


        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        final SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        /*Calendar cal = Calendar.getInstance();
        Date todayDate = cal.getTime();//formatter.format(todayDate).toString()
        setCustomResourceForDates(formatter2.format(todayDate).toString());*/

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
                if (user_role.equalsIgnoreCase("User")) {
                    showPermissionDialog();
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int month = cal.get(Calendar.MONTH);

                    Intent i = new Intent(getApplicationContext(), ViewEventsActivity.class);

                    i.putExtra("Date", formatter.format(date).toString());
                    i.putExtra("CalculateDate", formatter2.format(date).toString());
                    i.putExtra("Month", month);

                    startActivity(i);
                }


            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;

                viewPager.setCurrentItem(month - 1);
                String monthly_date= year + "-"+month+"-"+"1";

                Toast.makeText(getApplicationContext(), monthly_date + viewPager.getCurrentItem(),
                        Toast.LENGTH_SHORT).show();

                setCustomResourceForDates(monthly_date);

            }

            @Override
            public void onLongClickDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    /*Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        /*final TextView textView = (TextView) findViewById(R.id.textview);

        final Button customizeButton = (Button) findViewById(R.id.customize_button);

        // Customize the calendar
        customizeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (undo) {
                    customizeButton.setText(getString(R.string.customize));
                    textView.setText("");

                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;
                    return;
                }

                // Else
                undo = true;
                customizeButton.setText(getString(R.string.undo));
                Calendar cal = Calendar.getInstance();

                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }

                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());

                String text = "Today: " + formatter.format(new Date()) + "\n";
                text += "Min Date: " + formatter.format(minDate) + "\n";
                text += "Max Date: " + formatter.format(maxDate) + "\n";
                text += "Select From Date: " + formatter.format(fromDate)
                        + "\n";
                text += "Select To Date: " + formatter.format(toDate) + "\n";
                for (Date date : disabledDates) {
                    text += "Disabled Date: " + formatter.format(date) + "\n";
                }

                textView.setText(text);
            }
        });

        Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);

        final Bundle state = savedInstanceState;
        showDialogButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(listener);

                // If activity is recovered from rotation
                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getSupportFragmentManager(), state,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }

                dialogCaldroidFragment.show(getSupportFragmentManager(),
                        dialogTag);
            }
        });*/
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void showPermissionDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View convertView = View.inflate(this, R.layout.custom_premission_dialog, null);

        Button btn_ok = (Button) convertView.findViewById(R.id.permission_dialog_btn_ok);
        alertDialog.setView(convertView);
        alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

}
