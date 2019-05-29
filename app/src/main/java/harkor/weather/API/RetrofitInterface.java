package harkor.weather.API;

import harkor.weather.Model.ActualWeatherResponse;
import harkor.weather.Model.ForecastResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("/data/2.5/weather")
    Call<ActualWeatherResponse> getActualWeather(@Query("appid")String appId, @Query("lon")String lon, @Query("lat")String lat);
    @POST("/data/2.5/forecast")
    Call<ForecastResponse> getForecast (@Query("appid")String appId, @Query("lon")String lon, @Query("lat") String lat);
}
