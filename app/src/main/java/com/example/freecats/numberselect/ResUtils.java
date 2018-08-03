package com.example.freecats.numberselect;


import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Description: Resource Utils
 * <br>Date: 2018-08-03 AM 10:07
 * <br>@author freecats
 */
public class ResUtils {
    public static ResUtils instance = null;
    private static Context mContext;

    public static ResUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (ResUtils.class) {
                if (instance == null) {
                    instance = new ResUtils();
                    mContext = context.getApplicationContext();
                }
            }
        }
        return instance;
    }

    public int getColor(int res) {
        return ContextCompat.getColor(mContext, res);
    }

}
