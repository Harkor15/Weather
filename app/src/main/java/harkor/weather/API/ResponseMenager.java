package harkor.weather.API;


import android.util.Log;

import java.util.ArrayList;

import harkor.weather.Interfaces.CurrentWeatherSetAfterResponse;
import harkor.weather.Interfaces.FiveDaysWeatherSetAfterResponse;
import harkor.weather.Model.ActualWeatherResponse;
import harkor.weather.Model.ForecastResponse;
import harkor.weather.Model.WeatherData;
import harkor.weather.Model.WeatherObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseMenager {
    private RetrofitService retrofitService=new RetrofitService();

    public void getActualWeather(WeatherData weatherData, final CurrentWeatherSetAfterResponse currentWeatherSetAfterResponse){
        final Call<ActualWeatherResponse> actualWeatherCall=retrofitService.getActualWeather(weatherData);
        actualWeatherCall.enqueue(new Callback<ActualWeatherResponse>() {
            @Override
            public void onResponse(Call<ActualWeatherResponse> call, Response<ActualWeatherResponse> response) {
                try{
                    ActualWeatherResponse actualWeatherResponse=response.body();
                    WeatherObject weatherObject=actualWeatherResponse.getActualWeatherObject();
                    currentWeatherSetAfterResponse.setDataAfterResponse(weatherObject);
                }catch (Exception e){
                    Log.v("Error retrofit", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ActualWeatherResponse> call, Throwable t) {
                Log.v("Error retrofit", t.toString());
            }
        });
    }

    public void getForecast(WeatherData weatherData, final FiveDaysWeatherSetAfterResponse fiveDaysWeatherSetAfterResponse) {
        final Call<ForecastResponse> forecastResponseCall=retrofitService.getForecast(weatherData);
        forecastResponseCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                Log.d("retrofit","onResponse"+ response.body());
                try{
                    ForecastResponse forecastResponse=response.body();
                    ArrayList<WeatherObject> weatherObjects=forecastResponse.getWeatherObjects();
                    fiveDaysWeatherSetAfterResponse.setDataAfterResponse(weatherObjects);
                }catch (Exception e){
                    Log.v("Error retrofit", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.v("Error retrofit", t.toString());
            }
        });

    }
}
