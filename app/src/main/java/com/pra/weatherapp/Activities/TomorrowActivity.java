package com.pra.weatherapp.Activities;

import static com.pra.weatherapp.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pra.weatherapp.Adapters.TomorrowAdapter;
import com.pra.weatherapp.Domains.Hourly;
import com.pra.weatherapp.Domains.TomorrowDomain;
import com.pra.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TomorrowActivity extends AppCompatActivity {
    TextView tmrwDate, tmrwTempMain, tmrwStatusMain, tmrwHumid,tmrwWind, tmrwRain;
    RecyclerView recyclerView;
    ArrayList<TomorrowDomain> items = new ArrayList<>();

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_tomorrow);

        tmrwDate = findViewById(id.tmrwDate);
        tmrwTempMain = findViewById(id.tmrwTempMain);
        tmrwStatusMain = findViewById(id.tmrwStatusMain);
        tmrwHumid = findViewById(id.tmrwHumidity);
        tmrwWind = findViewById(id.tmrwWind);
        tmrwRain = findViewById(id.tmrwRain);

        initRecyclerView();
        setVariable();

        getWeatherInfo();
    }

    private void getWeatherInfo(){
        String url = "https://api.weatherapi.com/v1/future.json?key=13e5825a5f604a86a72131204241104&q=Jaipur&dt=2024-05-13";

        RequestQueue requestQueue = Volley.newRequestQueue(TomorrowActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);

                    String temp = forecast0.getJSONObject("day").getString("avgtemp_c");
                    tmrwTempMain.setText(temp+"°C");
                    String condition = forecast0.getJSONObject("day").getJSONObject("condition").getString("text");
                    tmrwStatusMain.setText(condition);
                    //image
                    String humidity = forecast0.getJSONObject("day").getString("avghumidity");
                    tmrwHumid.setText(humidity+"%");
                    String rain = forecast0.getJSONObject("day").getString("totalprecip_in");
                    tmrwRain.setText(rain);
                    String wind = forecast0.getJSONObject("day").getString("maxwind_kph");
                    tmrwWind.setText(wind+"km/hr");
//                    String date = response.getJSONObject("location").getString("localtime");
//                    tmrwDate.setText(date);



//                    for (int i = 0; i < hourArr.length(); i++) {
//                        JSONObject hourObj = hourArr.getJSONObject(i);
//                        String time = hourObj.getString("time");
//                        String temperature = hourObj.getString("temp_c");
//                        items.add(new TomorrowDomain(time,temperature,"sun"));
//                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TomorrowActivity.this, "some error occurred...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(objectRequest);
    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TomorrowActivity.this, MainActivity.class));
            }
        });
    }

    private void initRecyclerView() {

        recyclerView = findViewById(id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items.add(new TomorrowDomain("Sat","cloudy_sunny","Cloudy","35","22"));
        items.add(new TomorrowDomain("Sun","cloudy_sunny","Cloudy","35","22"));
        items.add(new TomorrowDomain("Mon","cloudy_sunny","Cloudy","36","22"));
        items.add(new TomorrowDomain("Tue","sun","Sunny","36","23"));
        items.add(new TomorrowDomain("Wed","sun","Sunny","37","22"));
        items.add(new TomorrowDomain("Thu","cloudy_3","Cloudy","37","25"));
//        items.add(new TomorrowDomain("Fri","sun","Sunny","23","12"));

        TomorrowAdapter adapterTomorrow = new TomorrowAdapter(items,this);
        recyclerView.setAdapter(adapterTomorrow);
    }
}
