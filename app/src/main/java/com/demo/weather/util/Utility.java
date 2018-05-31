package com.demo.weather.util;

import android.text.TextUtils;

import com.demo.weather.db.City;
import com.demo.weather.db.County;
import com.demo.weather.db.Province;
import com.demo.weather.gson.HeWeatherBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 凡 on 2018/5/31 0031.
 * 解析Gson
 */

public class Utility {
    //    省级数据
    public static boolean handleProvinceResponse(String response) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();//存到了数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //    市级数据
    public static boolean handleCityResponse(String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            try {
//  河南数据
// [{"id":67,"name":"郑州"},{"id":68,"name":"安阳"},{"id":69,"name":"新乡"},
// {"id":70,"name":"许昌"},{"id":71,"name":"平顶山"},{"id":72,"name":"信阳"},{"id":73,"name":"南阳"},
// {"id":74,"name":"开封"},{"id":75,"name":"洛阳"},{"id":76,"name":"商丘"},{"id":77,"name":"焦作"},
// {"id":78,"name":"鹤壁"},{"id":79,"name":"濮阳"},{"id":80,"name":"周口"},{"id":81,"name":"漯河"},
// {"id":82,"name":"驻马店"},{"id":83,"name":"三门峡"},{"id":84,"name":"济源"}]
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();//存到了数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //    县级数据
    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();//存到了数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //    将返回的JSON解析成Weather实体类
    public static HeWeatherBean handleWeatherResponse(String response) {
        try {
            JSONObject jsonArray = new JSONObject(response);
            JSONArray wheWeather = jsonArray.getJSONArray("HeWeather");
            String s = wheWeather.getJSONObject(0).toString();
            return new Gson().fromJson(s, HeWeatherBean.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
