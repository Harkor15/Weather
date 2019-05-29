package harkor.weather.Model;

public class NominatimObject {
    Double lat;
    Double lon;
    String display_name;

    public NominatimObject(Double lon,Double lat, String display_name) {
        this.lat = lat;
        this.lon = lon;
        this.display_name = display_name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getDisplay_name() {
        return display_name;
    }

}
