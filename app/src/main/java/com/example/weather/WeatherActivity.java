package com.example.weather;
/*
   1、读取文件步骤：
           1、Manifest中添加权限 android.permission.WRITE_EXTERNAL_STORAGE x写入外部存储器
           2、申请权限
           3、读写
   2、天气的查看
           1、读取
           2、解析
           3、显示
 */
import android.app.Activity;
import android.os.Build;
;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.widget.TextView;

import com.example.angel.R;

public class WeatherActivity extends Activity {

    private TextView didian;
    private TextView high;
    private TextView low;
    private TextView tianqi;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_weather);
        didian = (TextView)findViewById(R.id.didian);
        high = (TextView)findViewById(R.id.high);
        low = (TextView)findViewById(R.id.low);
        tianqi = (TextView)findViewById(R.id.tianqi);
        handler = new Handler();
       HTTPThread httpThread = new HTTPThread();
       httpThread.start();

    }

    public class HTTPThread extends Thread{
        public void run(){
            super.run();
            //读取数据
            HTTPRetrival hr = new HTTPRetrival();
            String weatherString = hr.HTTPWeatherGet( "101270101" );
            //天气信息实例化
            WeatherInfo wi = new WeatherInfo();
            //解析
            JSONParser jp = new JSONParser();
            wi = jp.weatherparse( weatherString );
            final WeatherInfo WI = wi;
            //查看
            handler.post(new Runnable() {
                @Override
                public void run() {
                    didian.setText("当前城市是："+WI.getCity());
                    high.setText("今日最高温度："+WI.getHighTemp());
                    low.setText("今日最低温度："+WI.getLowTemp());
                    tianqi.setText("今日的天气情况："+WI.getWeatherDescription());
                }
            });
        }

    }
}
