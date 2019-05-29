package harkor.weather.API;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import harkor.weather.Model.ActualWeatherResponse;
import harkor.weather.Model.ForecastResponse;
import harkor.weather.Model.WeatherData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    Gson gson=new GsonBuilder().create();
    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);

    public Call<ActualWeatherResponse> getActualWeather(WeatherData weatherData){
        return retrofitInterface.getActualWeather(weatherData.getAppid(), weatherData.getLongitude(),weatherData.getLatitude());
    }
    public Call<ForecastResponse> getForecast(WeatherData weatherData){
        return  retrofitInterface.getForecast(weatherData.getAppid(),weatherData.getLongitude(),weatherData.getLatitude());
    }

}
