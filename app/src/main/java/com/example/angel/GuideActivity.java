package com.example.angel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class GuideActivity extends Activity {

    private Handler handler;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        sp = getSharedPreferences("mensttuation", MODE_PRIVATE);
        setHandler();
    }

    //通知更新整个UI
    private void setHandler(){
        handler = new Handler();
        //延时更新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //执行体
                Intent intent = new Intent();
                intent.setClass(GuideActivity.this, sp.getBoolean("menstruation", false) ? MainActivity.class : SetMenstruationDate.class);
                startActivity(intent);
            }
        },2000);
    }
}
