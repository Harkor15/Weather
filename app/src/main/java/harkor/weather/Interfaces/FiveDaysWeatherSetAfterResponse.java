package harkor.weather.Interfaces;

import java.util.ArrayList;
import harkor.weather.Model.WeatherObject;

public interface FiveDaysWeatherSetAfterResponse {
    void setDataAfterResponse(ArrayList<WeatherObject> weatherObjects);
}
