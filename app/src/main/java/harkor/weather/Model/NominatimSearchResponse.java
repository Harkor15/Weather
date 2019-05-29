package harkor.weather.Model;

import java.util.ArrayList;

public class NominatimSearchResponse {
        String lat;
        String lon;
        String display_name;

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getDisplay_name() {
        return display_name;
    }
}
