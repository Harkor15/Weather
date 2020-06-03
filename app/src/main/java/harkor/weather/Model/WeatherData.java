package harkor.weather.Model;

public class WeatherData {
    String longitude;
    String latitude;
    String appid;

    public WeatherData(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        appid="73ce5a6d43a2c4278462796311770958";
    }
    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAppid() {
        return appid;
    }
}
