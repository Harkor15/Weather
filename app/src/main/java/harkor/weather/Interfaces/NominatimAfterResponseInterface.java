package harkor.weather.Interfaces;

import java.util.ArrayList;
import harkor.weather.Model.NominatimObject;

public interface NominatimAfterResponseInterface {
    void nominatimList(ArrayList<NominatimObject> nominatimObjects, String cityName);
}