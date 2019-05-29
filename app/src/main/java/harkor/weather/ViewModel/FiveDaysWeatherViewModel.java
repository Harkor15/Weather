package harkor.weather.ViewModel;

import android.util.Log;


import java.util.ArrayList;

import harkor.weather.API.ResponseMenager;
import harkor.weather.Interfaces.FiveDaysWeatherInterface;
import harkor.weather.Interfaces.FiveDaysWeatherSetAfterResponse;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.Model.WeatherData;
import harkor.weather.Model.WeatherObject;
import harkor.weather.Services.RealmDatabaseController;

public class FiveDaysWeatherViewModel implements FiveDaysWeatherSetAfterResponse {
    FiveDaysWeatherInterface fiveDaysWeatherInterface;

    public FiveDaysWeatherViewModel(FiveDaysWeatherInterface fiveDaysWeatherInterface) {
        this.fiveDaysWeatherInterface = fiveDaysWeatherInterface;
    }

    public void setListViewData(){
        SingleCityPOJO singleCityPOJO=getMainCity();
        if(singleCityPOJO!=null){
            WeatherData weatherData=new WeatherData(singleCityPOJO.getLongitude().toString(),singleCityPOJO.getLatitude().toString());
            Log.d("weather-test-setListV","Lon: "+weatherData.getLongitude()+" Lat: "+weatherData.getLatitude());
            ResponseMenager responseMenager=new ResponseMenager();
            responseMenager.getForecast(weatherData,this);
        }
    }

    private SingleCityPOJO getMainCity(){
        RealmDatabaseController realmDatabaseController = new RealmDatabaseController();
        return realmDatabaseController.getMainCity();
    }

    @Override
    public void setDataAfterResponse(ArrayList<WeatherObject> weatherObjects) {
    fiveDaysWeatherInterface.setListViewAdapter(weatherObjects.toArray(new WeatherObject[weatherObjects.size()]));
    }
}
