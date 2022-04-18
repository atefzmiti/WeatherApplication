package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String APP_ID = "d24a6fbb39c7dff019ab662db44523d9";
    final String Weather_Url = "https://api.openweathermap.org/data/2.5/weather";
    final long mint = 5000;
    final float mind = 1000;
    final int code = 101;

    String location = LocationManager.GPS_PROVIDER;
    TextView Nameofcity, weatherState, Temperature;
    ImageView weatherIcon;
    RelativeLayout cityfind;

    LocationManager locationManager;
    LocationListener locationlistener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        Nameofcity = findViewById(R.id.CityName);
        cityfind = findViewById(R.id.CityFinder);

        cityfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityFinder.class);
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getWeatherforCurrentLocation();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent mintent = getIntent();
        String city = mintent.getStringExtra("City");

        if(city!=null) {
            getweatherfornewcity(city);
        }
        else
        {
            getWeatherforCurrentLocation();
        }
    }

    private void getweatherfornewcity(String city){
        RequestParams params = new RequestParams();
        params.put("q",city);
        params.put("appid", APP_ID);
        somenetwork(params);
    }

    private void getWeatherforCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                RequestParams params = new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid", APP_ID);


                somenetwork(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
            return;
        }
        locationManager.requestLocationUpdates(location, mint, mind, locationlistener);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==code)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Getting location successfully",Toast.LENGTH_SHORT).show();
                getWeatherforCurrentLocation();
            }
            else
            {
                //
            }
        }
    }


    private  void somenetwork(RequestParams params){


        AsyncHttpClient client= new AsyncHttpClient();
        client.get(Weather_Url,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(MainActivity.this,"getting data successfully",Toast.LENGTH_SHORT).show();

                        WeatherData weatherData =  WeatherData.getJson(response);
                        updateUI(weatherData);
                        //super.onSuccess(statusCode, headers, response);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                }

        );
    }

    private void updateUI(WeatherData weather){
        Temperature.setText(weather.getTemperature());
        Nameofcity.setText(weather.getCity());
        weatherState.setText(weather.getWeathertype());
        int resourceID=getResources().getIdentifier(weather.getIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null)
        {
            locationManager.removeUpdates(locationlistener);
        }
    }
}
