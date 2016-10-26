package com.jsp.applock;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jsp.applock.bean.AppInfo;
import com.jsp.applock.utils.PreferencesUtils;
import com.jsp.applock.view.PasDialog;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView mainLv;
    private ArrayList<AppInfo> appList;
    private ArrayList<AppInfo> mainList = new ArrayList<>();
    private LiteOrm liteOrm;
    private HashMap<String, Boolean> map;

    public static final String KEY_ON_OFF = "swith";
    public static final String KEY_PAS = "pas";

    private Context mContext;

    private  String tempPas = null;
    private MianLvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //有bug不启动
//        startService(new Intent(mContext,LockService.class));
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {

        liteOrm = MyApplication.getLiteOrm();

        appList = new ArrayList<AppInfo>();
        map = new HashMap<>();
        ArrayList<AppInfo> tempList = liteOrm.query(new QueryBuilder<AppInfo>(AppInfo.class));
        if(tempList!=null){
            for (AppInfo appInfo:tempList){
                map.put(appInfo.packagename, appInfo.islock);
            }
        }

        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        int size = packages.size();
        for(int i=0;i<size;i++) {

            PackageInfo packageInfo = packages.get(i);

            AppInfo tmpInfo = new AppInfo();

            tmpInfo.appname = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();

            tmpInfo.packagename = packageInfo.packageName;

            tmpInfo.versionName = packageInfo.versionName;

            tmpInfo.versionCode = packageInfo.versionCode;

            tmpInfo.appicon = packageInfo.applicationInfo.loadIcon(getPackageManager());

            tmpInfo.islock = map.containsKey(tmpInfo.packagename)? map.get(tmpInfo.packagename):false;

            appList.add(tmpInfo);

        }
        mainList.addAll(appList);

    }

    private void initView() {

        final ToggleButton swith = (ToggleButton) findViewById(R.id.fab);
        mainLv = (ListView) findViewById(R.id.main_lv);
        adapter = new MianLvAdapter();
        mainLv.setAdapter(adapter);
        swith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isChecked = swith.isChecked();
                swith.setChecked(!isChecked);
                if (PreferencesUtils.getString(mContext, KEY_PAS, "").length() == 0) {
                    final PasDialog dialog = new PasDialog(mContext, R.style.bottom_dialog);
                    dialog.setTitle("设置密码");
                    dialog.setOnPasCallBack(new PasDialog.OnPasCallBack() {
                        @Override
                        public void pasCallBack(String pas) {
                            if (tempPas == null) {
                                tempPas = pas;
                                dialog.reStart();
                                dialog.setTitle("确认密码");
                            } else if (pas.equals(tempPas)) {
                                Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                                PreferencesUtils.putString(mContext, KEY_PAS, pas);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(mContext, "两次密码不一致，请重新设置", Toast.LENGTH_SHORT);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {
                    final PasDialog dialog = new PasDialog(mContext, R.style.bottom_dialog);
                    dialog.setOnPasCallBack(new PasDialog.OnPasCallBack() {
                        @Override
                        public void pasCallBack(String pas) {
                            if (pas.equals(PreferencesUtils.getString(mContext, KEY_PAS, ""))) {
                                dialog.dismiss();
                                swith.setChecked(isChecked);
                                PreferencesUtils.putBoolean(mContext, KEY_ON_OFF, isChecked);
                            } else {
                                dialog.reStart();
                                Toast.makeText(mContext, "输入错误,请重新输入", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }else {
            if (id == R.id.menu_lock) {
                appList.clear();
                appList.addAll(mainList);
                for (AppInfo appInfo : mainList){
                    if(!appInfo.islock){
                        appList.remove(appInfo);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            } else if (id == R.id.menu_unlock) {
                appList.clear();
                appList.addAll(mainList);
                for (AppInfo appInfo : mainList){
                    if(appInfo.islock){
                        appList.remove(appInfo);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            } else if (id == R.id.menu_searche) {
                return true;
            } else if (id == R.id.menu_all) {
                appList.clear();
                appList.addAll(mainList);
                adapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
     public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){}
                catch(Exception e){}
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }



    class MianLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return appList.size();
        }

        @Override
        public Object getItem(int position) {
            return appList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.main_list_imp,null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.main_lv_title);
                holder.content = (TextView) convertView.findViewById(R.id.main_lv_content);
                holder.icon = (ImageView) convertView.findViewById(R.id.main_lv_icon);
                holder.onOff = (Switch) convertView.findViewById(R.id.main_lv_switch);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            AppInfo appInfo = appList.get(position);
            holder.icon.setImageDrawable(appInfo.appicon);
            holder.title.setText(appInfo.appname);
            holder.content.setText("版本:"+appInfo.versionName);
            holder.onOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkChange(position,v);
                }
            });
            holder.onOff.setChecked(appInfo.islock);
            return convertView;
        }
    }

    private void checkChange(final int position,View view){

        if(view instanceof Switch){
            final Switch swith = (Switch) view;
            final boolean isChecked = swith.isChecked();
            swith.setChecked(!isChecked);
            if (PreferencesUtils.getString(mContext,KEY_PAS,"").length()==0) {
                final PasDialog dialog = new PasDialog(mContext, R.style.bottom_dialog);
                dialog.setTitle("设置密码");
                dialog.setOnPasCallBack(new PasDialog.OnPasCallBack() {
                    @Override
                    public void pasCallBack(String pas) {
                        if (tempPas == null) {
                            tempPas = pas;
                            dialog.reStart();
                            dialog.setTitle("确认密码");
                        } else if (pas.equals(tempPas)) {
                            Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                            PreferencesUtils.putString(mContext, KEY_PAS, pas);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "两次密码不一致，请重新设置", Toast.LENGTH_SHORT);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }else{
                final PasDialog dialog = new PasDialog(mContext, R.style.bottom_dialog);
                dialog.setOnPasCallBack(new PasDialog.OnPasCallBack() {
                    @Override
                    public void pasCallBack(String pas) {
                        if(pas.equals(PreferencesUtils.getString(mContext,KEY_PAS,""))){
                            dialog.dismiss();
                            swith.setChecked(isChecked);
                            PreferencesUtils.putBoolean(mContext, KEY_ON_OFF, isChecked);
                            AppInfo appInfo = appList.get(position);
                            appInfo.islock = isChecked;
                            map.put(appInfo.packagename, isChecked);
                        }else {
                            dialog.reStart();
                            Toast.makeText(mContext, "输入错误,请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        }else {

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        liteOrm.save(appList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        liteOrm.save(appList);
    }

    class ViewHolder{
        ImageView icon;
        TextView title,content;
        Switch onOff;
    }

}
