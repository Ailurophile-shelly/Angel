package com.example.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

/*
   天气的取回
 */
public class HTTPRetrival {
    /*
       读取数据
       返回值 String
       参数 城市代码
     */
    public String HTTPWeatherGet(String cityCode){
        String res = "";
        //将给定的数据转换成字符串的生成器,比Buffer快
        StringBuilder sb = new StringBuilder(  );

        String urlString = "http://www.weather.com.cn/data/cityinfo/" + cityCode + ".html";
        try {
            URL url = new URL( urlString );
            //建立天气连接
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            //设置连接超时
            http.setConnectTimeout( 10000 );
            //获取天气
            InputStreamReader isr = new InputStreamReader( http.getInputStream() );
            //缓存
            BufferedReader br = new BufferedReader( isr );
            String temp = null;
            //读取天气信息
            while ((temp = br.readLine()) != null){
                //将获取到的数据依次加入到生成器末端存储
                sb.append( temp );
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //读取出
        res = sb.toString();
        return res;
    }

}
