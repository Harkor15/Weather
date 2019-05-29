package harkor.weather.Interfaces;

import java.util.ArrayList;
import harkor.weather.Model.NominatimObject;
import harkor.weather.Model.SingleCityPOJO;

public interface SettingsViewModelInterface{
    void noRespond();
    void oneRespond(SingleCityPOJO singleCityPOJO);
    void manyResponds(ArrayList<NominatimObject> nominatimObjects, String cityName);
}
