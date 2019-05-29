package harkor.weather.Model;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ActualWeatherResponse{
    ArrayList<Weather> weather = new ArrayList<Weather>();
    private String base;
    Main main;
    Wind wind;
    Clouds clouds;

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }


    public WeatherObject getActualWeatherObject() {
        DecimalFormat df=new DecimalFormat("#");
        return new WeatherObject("0000-00-00 00:00:00", weather.get(0).main + "",
                df.format(main.getPressure()) + "", df.format(wind.getSpeed()) + "",
                main.getTemp()  , df.format(main.getHumidity()) + "", df.format(clouds.getAll()) + "");
    }


    class Weather{
        int id;

        public void setId(int id) {
            this.id = id;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        String main;
        String description;
        String icon;
    }


    class Main{
        double temp;
        double pressure;
        double humidity;
        public void setTemp(double temp) {
            this.temp = temp;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getTemp() {
            return temp;
        }

        public double getPressure() {
            return pressure;
        }

        public double getHumidity() {
            return humidity;
        }

    }


    class Wind{
        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Double getSpeed() {
            return speed;
        }

        Double speed;
    }


    class Clouds{
        public void setAll(Double all) {
            this.all = all;
        }

        public Double getAll() {
            return all;
        }

        Double all;
    }
}