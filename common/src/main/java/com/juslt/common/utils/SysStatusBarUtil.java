package com.juslt.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.juslt.common.R;


/**
 * Created by wx on 2017/8/29.
 */

public class SysStatusBarUtil {

    public static void hideSystemUI(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions =   View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }
    public static void showSystemUI(Activity activity){
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }
    public static void setBarColor(Activity activity, int color){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
//            window.setNavigationBarColor(color);
        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(createStatusBarView(activity,color));
            setRootView(activity,true);
        }
    }
    //系统栏字体为深色
    public static void setDarkBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setBarColor(activity, ContextCompat.getColor(activity, R.color.color_white));
        }else {
            setBarColor(activity, ContextCompat.getColor(activity,R.color.blue));
        }

    }
    //设置透明或者半透明状态栏（沉浸式状态栏）
    public static void setTransparentBar(Activity activity,int color,int alpha){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            int finalColor = alpha==0? Color.TRANSPARENT
                    :Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));
//            window.setNavigationBarColor(finalColor);
            window.setStatusBarColor(finalColor);
        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int finalColor =alpha ==0?Color.TRANSPARENT:
                    Color.argb(alpha,Color.red(color),Color.green(color),Color.blue(color));
            decorView.addView(createStatusBarView(activity,finalColor));
        }
        //navigationBar暂不处理
//        if (navigationBarExist(activity)) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            decorView.addView(createNavBarView(activity, finalColor));
//        }
    }




    private static void setRootView(Activity activity, boolean fit) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fit);
                ((ViewGroup)childView).setClipToPadding(fit);
            }
        }
    }
    private static View createStatusBarView(Context context, @ColorInt int color) {
        View mStatusBarTintView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));
        params.gravity = Gravity.TOP;
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(color);
        return mStatusBarTintView;
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


}
