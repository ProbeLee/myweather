package com.demo.weather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 凡 on 2018/5/31 0031.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;//记录县的名字
    private String weatherId;//对应天气Id
    private int cityId;//记录县所在城市的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName == null ? "" : countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName == null ? "" : countyName;
    }

    public String getWeatherId() {
        return weatherId == null ? "" : weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId == null ? "" : weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
