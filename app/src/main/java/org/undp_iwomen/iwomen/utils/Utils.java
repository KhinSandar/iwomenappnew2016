package org.undp_iwomen.iwomen.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    //public static String HOCKEY_APPID = "ab8e61deeab46d679d4f6d14757e8c27";
    public static String PREF_SETTING = "mLanguage";//"settings"
    public static String PREF_SETTING_LANG = "lang";//"language"


    public static String ENG_LANG = "eng";//english
    public static String MM_LANG = "mm";//myanmar Old apk
    public static String PREF_THEME = "themecolor";

    public static String MM_LANG_UNI = "myanmar_uni";
    public static String MM_LANG_DEFAULT = "myanmar_default";


    //FOR SMK FONT Setting
    public static String ENG_LANG_FONT = "english";//english
    public static String MM_ZAWGYI_LANG_FONT = "zawgyione";//english
    public static String MM3_LANG_FONT = "myanmar3";//english
    public static String MM_DEFAULT_FONT = "myanmar_default";//english




    public static int THEME_BLUE = 11;
    public static int THEME_PINK = 12;
    public static int THEME_YELLOW = 13;


    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
        	
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
            	
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    public static void doToastEng(Context context, String toast){
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }
    public static void doToastMM(Context context, String toast){
        //Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        Toast toast_mm = Toast.makeText(context, toast, Toast.LENGTH_LONG);
        LinearLayout toastLayout = (LinearLayout) toast_mm.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(15);
        toastTV.setTypeface(MyTypeFace.get(context, MyTypeFace.ZAWGYI));
        toast_mm.show();
    }

    public static void changeToTheme(Activity activity)
    {
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));

    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        SharedPreferences sPref = activity.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        int sTheme = sPref.getInt(PREF_THEME, THEME_PINK);


        if(sTheme == THEME_PINK){
            activity.setTheme(R.style.AppTheme);
        }else if(sTheme == THEME_BLUE){
            activity.setTheme(R.style.AppTheme_Blue);
        }else if(sTheme == THEME_YELLOW){
            activity.setTheme(R.style.AppTheme_Yellow);
        }
    }

    /** Set the theme for animation */
    public static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow));
    }

    public static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }

    public static void showPermissionDialog(Activity activity) {
        final Dialog alertDialog = new Dialog(activity);
        View convertView = View.inflate(activity, R.layout.custom_premission_dialog, null);

        Button btn_ok = (Button) convertView.findViewById(R.id.permission_dialog_btn_ok);
        alertDialog.setContentView(convertView);
        alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finish();
                alertDialog.dismiss();

            }
        });
    }

    /**
     * **************** ListView WIthin ScrollView Step1 ********************
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}