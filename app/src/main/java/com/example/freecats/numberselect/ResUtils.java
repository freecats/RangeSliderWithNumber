package com.example.freecats.numberselect;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

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

    public Drawable getDrawable(int res) {
        return ContextCompat.getDrawable(mContext, res);
    }
}
