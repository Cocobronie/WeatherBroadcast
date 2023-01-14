package com.example.weatherbroadcast;

import java.util.UUID;

/*
"daily": [
    {
      "fxDate": "2021-11-15"，
      "tempMax": "12",
      "tempMin": "-1",
      "iconDay": "101",
      "textDay": "多云",
      "windSpeedDay": "3",
      "humidity": "65",
      "pressure": "1020",
    },
 */

public class Weather {
    private UUID mId;
    private String mDate;    //日期
    private String mWeather;    //天气
    private String maxTemperature;    //最高温度
    private String minTemperature;    //最低温度
    private String iconUrl;    //天气图标
    private String wind;
    private String humidity;
    private String pressure;
    public Weather(){
        mId=UUID.randomUUID();
    }

    public void setId(UUID id) {
        mId = id;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setWeather(String weather) {
        mWeather = weather;
    }

    public UUID getId() {
        return mId;
    }

    public String getDate() {
        return mDate;
    }

    public String getWeather() {
        return mWeather;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getWind() {
        return wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}
