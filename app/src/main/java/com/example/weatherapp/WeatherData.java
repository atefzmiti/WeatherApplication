package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;


public class WeatherData {


    private String temperature,icon,city,weathertype;
    private  int condition;

    public static WeatherData getJson(JSONObject jsonObject)
    {
        try
        {
            WeatherData weatherData=new WeatherData();
            weatherData.city=jsonObject.getString("name");
            weatherData.condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.weathertype=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherData.icon=updateweatherIcon(weatherData.condition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedvalue=(int)Math.rint(tempResult);
            weatherData.temperature=Integer.toString(roundedvalue);
            return weatherData;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateweatherIcon(int condition){
        if(condition>=0 && condition<=300)
        {
            return "thunderstorm1";
        }

        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }

        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else if(condition>=600 && condition<=700)
        {
            return "snow2";
        }

        else if(condition>=701 && condition<=772)
        {
            return "fog";
        }

        else if(condition>=772 && condition<800)
        {
            return "thunderstorm1";
        }

        else if(condition==800)
        {
            return "sunny";
        }

        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }

        else if(condition>=900 && condition<=902)
        {
            return "thunderstorm1";
        }
        else if(condition==903)
        {
            return "snow1";
        }
        else if(condition==904)
        {
            return "sunny";
        }
        else if(condition>=905 && condition<=1000)
        {
            return "thunderstorm2";
        }
        return  "dunno";
        }

    public String getTemperature() {
        return temperature +"°C";
    }

    public String getIcon() {
        return icon;
    }

    public String getCity() {
        return city;
    }

    public String getWeathertype() {
        return weathertype;
    }
}
