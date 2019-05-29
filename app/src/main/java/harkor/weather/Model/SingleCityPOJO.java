package harkor.weather.Model;

public class SingleCityPOJO {
    String city;
    Double longitude;
    Double latitude;

    public SingleCityPOJO(String city, Double longitude, Double latitude) {
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
