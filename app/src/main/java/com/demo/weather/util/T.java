package com.demo.weather.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/15 0015.
 * Toast统一管理类
 */

public class T {
    private T(){
        //cannot be instantiated
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message){
        if (isShow){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 短时间显示Toast
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message){
        if (isShow){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长时间显示Toast
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message){
        if (isShow){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 长时间显示Toast
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message){
        if (isShow){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 自定义Toast显示时间
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration){
        if (isShow){
            Toast.makeText(context,message,duration).show();
        }
    }

    /**
     * 自定义Toast显示时间
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration){
        if (isShow){
            Toast.makeText(context,message,duration).show();
        }
    }
}
