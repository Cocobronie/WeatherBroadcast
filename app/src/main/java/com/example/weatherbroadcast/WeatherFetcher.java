package com.example.weatherbroadcast;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.weatherbroadcast.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherFetcher{
    private static final String TAG = "WeatherFetcher";
    private static final String API_KEY = "ab50d6348aa648ddb42472afaae7e8fc";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Weather> fetchItems() {
        SharedPreferences userInfo = NormalContext.getCustomApplicationContext().getSharedPreferences("set", MODE_PRIVATE);
        String Unit = userInfo.getString("unit", "m");//读取unit
        Log.i(TAG, "读取用户信息");
        Log.i(TAG, "unit:" + Unit);
        List<Weather> items = new ArrayList<>();

        //https://devapi.qweather.com/v7/weather/7d?key=ab50d6348aa648ddb42472afaae7e8fc&location=101010100
        try {
            String url = Uri.parse("https://devapi.qweather.com/v7/weather/7d/")
                    .buildUpon()
                    .appendQueryParameter("location", "101010100")
                    .appendQueryParameter("key", API_KEY)
                    //.appendQueryParameter("unit", Unit)
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return items;
    }

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
    private void parseItems(List<Weather> items, JSONObject jsonBody)
            throws IOException, JSONException {

        //JSONObject photosJsonObject = jsonBody.getJSONObject("daily");
        JSONArray weatherJsonArray = jsonBody.getJSONArray("daily");

        for (int i = 0; i < weatherJsonArray.length(); i++) {
            JSONObject photoJsonObject = weatherJsonArray.getJSONObject(i);
            Weather item = new Weather();
            //设置每一个item
            item.setDate(photoJsonObject.getString("fxDate"));
            item.setMaxTemperature(photoJsonObject.getString("tempMax"));
            item.setMinTemperature(photoJsonObject.getString("tempMin"));
            item.setIconUrl(photoJsonObject.getString("iconDay"));
            item.setWeather(photoJsonObject.getString("textDay"));
            item.setWind(photoJsonObject.getString("windSpeedDay"));
            item.setHumidity(photoJsonObject.getString("humidity"));
            item.setPressure(photoJsonObject.getString("pressure"));
            items.add(item);
        }
    }


}
