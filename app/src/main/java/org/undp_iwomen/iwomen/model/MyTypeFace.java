package org.undp_iwomen.iwomen.model;

/**
 * Created by khinsandar on 5/7/15.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;


public class MyTypeFace {

    private static final String TAG = "Typefaces";
    public static final String BOLD = "bold";
    public static final String NORMAL = "normal";
    public static final String ITALIC = "italic";
    public static final String ZAWGYI = "zawgyi";
    public static final String UNI = "uni";


    private static Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {

                    if(assetPath.equals(NORMAL)) {
                        Typeface tf = Typeface.createFromAsset(c.getAssets(),
                                "fonts/robotomedium.ttf");

                        cache.put(assetPath, tf);
                    }else if(assetPath.equals(BOLD)) {
                        Typeface tf = Typeface.createFromAsset(c.getAssets(),
                                "fonts/roboto-bold.ttf");

                        cache.put(assetPath, tf);
                    }
                    /*else if(assetPath.equals(ITALIC)) {
                        Typeface tf = Typeface.createFromAsset(c.getAssets(),
                                "fonts/ciclesemiitalic.ttf");

                        cache.put(assetPath, tf);
                    }*/
                    else if(assetPath.equals(ZAWGYI)) {
                        Typeface tf = Typeface.createFromAsset(c.getAssets(),
                                "fonts/zawgyi.ttf");

                        cache.put(assetPath, tf);
                    }else if(assetPath.equals(UNI)) {
                        Typeface tf = Typeface.createFromAsset(c.getAssets(),
                                "fonts/mm3-multi-os.ttf");

                        cache.put(assetPath, tf);
                    }





                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }

    }

}

