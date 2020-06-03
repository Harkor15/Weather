package harkor.weather.ViewModel;

import android.util.Log;

import harkor.weather.API.ResponseMenager;
import harkor.weather.Interfaces.CurrentWeatherInterface;
import harkor.weather.Interfaces.CurrentWeatherSetAfterResponse;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.Model.WeatherData;
import harkor.weather.Model.WeatherObject;
import harkor.weather.Services.RealmDatabaseController;


public class CurrentWeatherViewModel implements CurrentWeatherSetAfterResponse {
    CurrentWeatherInterface currentWeatherInterface;

    public CurrentWeatherViewModel(CurrentWeatherInterface currentWeatherInterface) {
        this.currentWeatherInterface = currentWeatherInterface;
    }

     public void setData(){
         ResponseMenager responseMenager=new ResponseMenager();
         SingleCityPOJO singleCityPOJO=getMainCity();
         if(singleCityPOJO!=null){
             Log.d("weather-test",singleCityPOJO.getCity()+" "+singleCityPOJO.getLongitude()+" "+singleCityPOJO.getLatitude());
             responseMenager.getActualWeather(new WeatherData(singleCityPOJO.getLongitude().toString(),
                     singleCityPOJO.getLatitude().toString()),this);
         }
    }

    private SingleCityPOJO getMainCity(){
        RealmDatabaseController realmDatabaseController = new RealmDatabaseController();
        return realmDatabaseController.getMainCity();
    }

    @Override
    public void setDataAfterResponse(WeatherObject weatherObject) {
        currentWeatherInterface.tabOneSetData(weatherObject);
    }
}
