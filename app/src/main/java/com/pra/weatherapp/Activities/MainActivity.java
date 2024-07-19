package com.pra.weatherapp.Activities;

import static com.pra.weatherapp.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pra.weatherapp.Adapters.HourlyAdapter;
import com.pra.weatherapp.Domains.Hourly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tempMain, statusMain, dateMain, humidityMain, rainMain, windMain;
    Button refresh;
    ArrayList<Hourly> items = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        tempMain = findViewById(id.tempMain);
        statusMain = findViewById(id.statusMain);
        dateMain = findViewById(id.dateMain);
        humidityMain = findViewById(id.humidity);
        rainMain = findViewById(id.rain);
        windMain = findViewById(id.windSpeed);

        initRecyclerView();

        setVariable();
        getWeatherInfo();
    }

    private void getWeatherInfo(){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=13e5825a5f604a86a72131204241104&q=Jaipur&days=1&aqi=yes&alerts=yes";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String temp = response.getJSONObject("current").getString("temp_c");
                    tempMain.setText(temp+"°C");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    statusMain.setText(condition);
                    //image
                    String humidity = response.getJSONObject("current").getString("humidity");
                    humidityMain.setText(humidity+"%");
                    String rain = response.getJSONObject("current").getString("precip_in");
                    rainMain.setText(rain);
                    String wind = response.getJSONObject("current").getString("wind_kph");
                    windMain.setText(wind+"km/hr");
                    String date = response.getJSONObject("location").getString("localtime");
                    dateMain.setText(date);

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArr = forecast0.getJSONArray("hour");

//                    for (int i = 0; i < hourArr.length(); i++) {
//                        JSONObject hourObj = hourArr.getJSONObject(i);
//                        String time = hourObj.getString("time");
//                        String temperature = hourObj.getString("temp_c");
//                        items.add(new Hourly(time,temperature,"sun"));
//                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "some error occurred...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(objectRequest);
    }

    private void setVariable() {
        TextView next7dayBtn = findViewById(id.nxt_btn);
        next7dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TomorrowActivity.class));
            }
        });
        refresh = (Button)findViewById(id.refreshBtn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInfo();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items.add(new Hourly("2 pm","36","sun"));
        items.add(new Hourly("3 pm","36","sun"));
        items.add(new Hourly("4 pm","35","cloudy_sunny"));
        items.add(new Hourly("5 pm","35","cloudy_sunny"));
        items.add(new Hourly("6 pm","33","cloudy_sunny"));
        items.add(new Hourly("7 pm","31","cloudy"));
        items.add(new Hourly("8 pm","29","cloudy_3"));
        items.add(new Hourly("9 pm","28","cloudy_3"));
        items.add(new Hourly("10 pm","27","cloudy_3"));
        items.add(new Hourly("11 pm","26","cloudy_3"));
        items.add(new Hourly("12 am","25","cloudy_3"));

        HourlyAdapter adapterHourly = new HourlyAdapter(items,this);
        recyclerView.setAdapter(adapterHourly);

    }
}