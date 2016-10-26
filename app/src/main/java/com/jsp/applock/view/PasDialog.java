package com.jsp.applock.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsp.applock.R;

/**
 * Created by admin on 2016/10/11.
 */
public class PasDialog extends Dialog implements View.OnClickListener{


    private TextView num1,num2,num3,num4,num5,num6,num7,num8,num9,num0,clear;
    private EditText et1,et2,et3,et4;
    private RelativeLayout numBack;

    private EditText[] ets = new EditText[4];
    private int index = 0;

    public PasDialog(Context context) {
        super(context);
        initView();
    }

    public PasDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public void setTitle(String title){
        ((TextView)findViewById(R.id.pas_title)).setText(title);
    }

    public void reStart(){
        index = 0;
        for(int i = 0;i < 4;i++){
            ets[i].setText("");
        }

        findViewById(R.id.pas_et_group).setVisibility(View.VISIBLE);
        findViewById(R.id.pas_anim).setVisibility(View.GONE);
    }

    private void initView(){
        setContentView(R.layout.pas_dialog);
        getWindow().setGravity(Gravity.BOTTOM);

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);


        findViewById();


    }

    private void findViewById() {
        num0 = (TextView) findViewById(R.id.pas_number_0);
        num0.setOnClickListener(this);
        num1 = (TextView) findViewById(R.id.pas_number_1);
        num1.setOnClickListener(this);
        num2 = (TextView) findViewById(R.id.pas_number_2);
        num2.setOnClickListener(this);
        num3 = (TextView) findViewById(R.id.pas_number_3);
        num3.setOnClickListener(this);
        num4 = (TextView) findViewById(R.id.pas_number_4);
        num4.setOnClickListener(this);
        num5 = (TextView) findViewById(R.id.pas_number_5);
        num5.setOnClickListener(this);
        num6 = (TextView) findViewById(R.id.pas_number_6);
        num6.setOnClickListener(this);
        num7 = (TextView) findViewById(R.id.pas_number_7);
        num7.setOnClickListener(this);
        num8 = (TextView) findViewById(R.id.pas_number_8);
        num8.setOnClickListener(this);
        num9 = (TextView) findViewById(R.id.pas_number_9);
        num9.setOnClickListener(this);

        clear = (TextView) findViewById(R.id.pas_clear);
        clear.setOnClickListener(this);

        et1 = (EditText) findViewById(R.id.pas_et_1);
        et2 = (EditText) findViewById(R.id.pas_et_2);
        et3 = (EditText) findViewById(R.id.pas_et_3);
        et4 = (EditText) findViewById(R.id.pas_et_4);

        ets[0] = et1;
        ets[1] = et2;
        ets[2] = et3;
        ets[3] = et4;


        numBack = (RelativeLayout) findViewById(R.id.pas_number_back);
        numBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pas_number_0:
                if(index<=3) {
                    setPasText("0");
                    index++;
                }
                break;
            case R.id.pas_number_1:
                if(index<=3) {
                    setPasText("1");
                    index++;
                }
                break;
            case R.id.pas_number_2:
                if(index<=3) {
                    setPasText("2");
                    index++;
                }
                break;
            case R.id.pas_number_3:
                if(index<=3) {
                    setPasText("3");
                    index++;
                }
                break;
            case R.id.pas_number_4:
                if(index<=3) {
                    setPasText("4");
                    index++;
                }
                break;
            case R.id.pas_number_5:
                if(index<=3) {
                    setPasText("5");
                    index++;
                }
                break;
            case R.id.pas_number_6:
                if(index<=3) {
                    setPasText("6");
                    index++;
                }
                break;
            case R.id.pas_number_7:
                if(index<=3) {
                    setPasText("7");
                    index++;
                }
                break;
            case R.id.pas_number_8:
                if(index<=3) {
                    setPasText("8");
                    index++;
                }
                break;
            case R.id.pas_number_9:
                if(index<=3) {
                    setPasText("9");
                    index++;
                }
                break;
            case R.id.pas_clear:
                dismiss();
                break;
            case R.id.pas_number_back:
                if(index>0){
                    index--;
                    setPasText("");
                }
                break;

        }
    }

    private void setPasText(String num){
        if(index<=3&&index>=0){
            if(index==3){
                pasCallBack();
            }
            ets[index].setText(num);
        }else {

        }
    }


    private OnPasCallBack callBack;
    public interface OnPasCallBack{
        public void pasCallBack(String pas);
    }

    public void setOnPasCallBack(OnPasCallBack callBack){
        this.callBack = callBack;
    }

    private void  pasCallBack(){
        findViewById(R.id.pas_et_group).setVisibility(View.GONE);
        findViewById(R.id.pas_anim).setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(callBack!=null){
                    String pas = "";
                    for(int i = 0;i < 4;i++){
                        pas += ets[i].getText().toString();
                    }
                    callBack.pasCallBack(pas);
                }

            }
        },1000);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(onKeyDownListener==null){
            return super.onKeyDown(keyCode,event);
        }else {
            onKeyDownListener.onKeyDown(keyCode,event);
            return true;
        }
    }

    private OnKeyDownListener onKeyDownListener;
    public interface OnKeyDownListener{
        public void onKeyDown(int keyCode, KeyEvent event);
    }
    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener){
        this.onKeyDownListener = onKeyDownListener;
    }
}
