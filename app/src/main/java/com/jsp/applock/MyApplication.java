package com.jsp.applock;

import android.app.Application;
import android.content.Context;

import com.jsp.applock.utils.L;
import com.jsp.applock.utils.SDCardUtils;
import com.litesuits.orm.LiteOrm;

/**
 * Created by admin on 2016/7/3.
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private static LiteOrm liteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        String  phonePicsPath;
        if(SDCardUtils.isSDCardEnable()){
            phonePicsPath = SDCardUtils.getSDCardPath();
        }else{
            phonePicsPath = getFilesDir().getAbsolutePath();
        }

        final String  DB_NAME = phonePicsPath+"applock/applock.db";
        liteOrm = LiteOrm.newCascadeInstance(instance, DB_NAME);
        liteOrm.setDebugged(true);

        L.e("jishuaipeng" + DB_NAME);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public static MyApplication getAppContext() {
        return instance;
    }
    public static LiteOrm getLiteOrm(){return liteOrm;};

}