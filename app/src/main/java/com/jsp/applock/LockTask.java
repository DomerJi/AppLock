package com.jsp.applock;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.jsp.applock.bean.AppInfo;
import com.jsp.applock.utils.PreferencesUtils;
import com.jsp.applock.view.PasDialog;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class LockTask extends TimerTask {
	public static final String TAG = "LockTask";
	private Context mContext;


	private ActivityManager mActivityManager;
	private final HashMap<String, Boolean> map;
	private final LiteOrm liteOrm;
	private PasDialog pasDialog;
	public static ArrayList stopApps = null;

	public LockTask(Context context) {
		mContext = context;
		mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		liteOrm = MyApplication.getLiteOrm();

		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		map = new HashMap<>();
		ArrayList<AppInfo> tempList = liteOrm.query(new QueryBuilder<AppInfo>(AppInfo.class));
		if(tempList!=null){
			for (AppInfo appInfo:tempList){
				map.put(appInfo.packagename, appInfo.islock);
			}
		}
		stopApps = new ArrayList();

		List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
		int size = packages.size();
		for(int i=0;i<size;i++) {

			PackageInfo packageInfo = packages.get(i);

			AppInfo tmpInfo = new AppInfo();

			tmpInfo.appname = packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();

			tmpInfo.packagename = packageInfo.packageName;

			tmpInfo.versionName = packageInfo.versionName;

			tmpInfo.versionCode = packageInfo.versionCode;

			tmpInfo.appicon = packageInfo.applicationInfo.loadIcon(mContext.getPackageManager());

			tmpInfo.islock = map.containsKey(tmpInfo.packagename)? map.get(tmpInfo.packagename):false;

			appList.add(tmpInfo);

		}
	}

	@Override
	public void run() {
		ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
		String packageName = topActivity.getPackageName();
		String className = topActivity.getClassName();
		Log.v(TAG, "packageName" + packageName);
		Log.v(TAG, "className" + className);
		ArrayList<AppInfo> tempList = liteOrm.query(new QueryBuilder<AppInfo>(AppInfo.class));
		if(tempList!=null){
			for (AppInfo appInfo:tempList){
				map.put(appInfo.packagename, appInfo.islock);
			}
		}
		if (map.containsKey(packageName)?map.get(packageName):false&&PreferencesUtils.getBoolean(mContext, MainActivity.KEY_ON_OFF,false)) {
			if(!className.equals("com.jsp.applock.PasActivity")){

				if (stopApps.contains(packageName)){

				}else {
					handler.sendMessage(handler.obtainMessage());
				}
			}

		}
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mContext.startActivity(new Intent(mContext, PasActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		}
	};
}