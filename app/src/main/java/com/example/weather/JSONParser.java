package com.example.weather;

import org.json.JSONException;
import org.json.JSONObject;

/*
  json语句的解析
 */
public class JSONParser {
    /*
      返回值 WeaherInfo
      参数 天气信息字符串
     */
    public WeatherInfo weatherparse(String weatherString){
        //天气信息对象
        WeatherInfo wi = new WeatherInfo();
        //json对象，就是一个键对应一个值，使用的是大括号{ }，如：{key:value}

        try {
            JSONObject jo = new JSONObject( weatherString );
            JSONObject joWeather = jo.getJSONObject( "weatherinfo" );
            wi.setCity( joWeather.getString( "city" ) );
            wi.setWeatherDescription( joWeather.getString( "weather" ) );
            wi.setHighTemp(joWeather.getString( "temp2" ));
            wi.setLowTemp( joWeather.getString( "temp1" ) );
            wi.setpTime(joWeather.getString( "ptime" ));;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wi;
    }

}
