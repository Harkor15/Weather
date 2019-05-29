package harkor.weather.Model;

public class WeatherObject {
    String clouds;
    String time;
    String weather;
    String pressure;
    String wind;
    Double temp;
    String humidity;

    public WeatherObject(String time, String weather, String pressure, String wind, Double temp,String humidity, String clouds) {
        this.time = time;
        this.weather = weather;
        this.pressure = pressure;
        this.wind = wind;
        this.temp = temp;
        this.humidity=humidity;
        this.clouds=clouds;
    }

    public String getTime() {
        return time;
    }

    public String getWeather() {
        return weather;
    }

    public String getPressure() {
        return pressure;
    }

    public String getWind() {
        return wind;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getClouds() {
        return clouds;
    }
}
