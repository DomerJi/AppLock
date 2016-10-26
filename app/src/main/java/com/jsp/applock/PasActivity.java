package com.jsp.applock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jsp.applock.utils.PreferencesUtils;
import com.jsp.applock.view.PasDialog;

public class PasActivity extends AppCompatActivity {
    PasDialog pasDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_pas);
        if(pasDialog!=null&&pasDialog.isShowing()){
            return;
        }
        pasDialog = new PasDialog(mContext, R.style.bottom_dialog);
        pasDialog.setOnPasCallBack(new PasDialog.OnPasCallBack() {
            @Override
            public void pasCallBack(String pas) {
                if (PreferencesUtils.getString(mContext, MainActivity.KEY_PAS, "").equals(pas)) {
                    pasDialog.dismiss();
                    finish();
                } else {

                }
            }
        });
        pasDialog.setOnKeyDownListener(new PasDialog.OnKeyDownListener() {
            @Override
            public void onKeyDown(int keyCode, KeyEvent event) {

            }
        });
        pasDialog.show();
    }
}
