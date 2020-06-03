package harkor.weather.Model;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ForecastResponse {
    ArrayList<List> list;
    public void setList(ArrayList<List> list) {
        this.list = list;
    }
    public ArrayList<List> getList() {
        return list;
    }
    public ArrayList<WeatherObject> getWeatherObjects(){
        DecimalFormat df=new DecimalFormat("#");
        ArrayList<WeatherObject> weatherObjects=new ArrayList<>();
        for(int i = 0; list.size() > i; i++){
            weatherObjects.add(new WeatherObject(list.get(i).getDt_txt()+"",
                    list.get(i).getWeather().get(0).getMain()+"",
                    df.format(list.get(i).getMain().getPressure())+"",
                    list.get(i).getWind().getSpeed()+"",
                    list.get(i).getMain().getTemp(),
                    df.format(list.get(i).getMain().getHumidity())+"",
                    df.format(list.get(i).getClouds().getAll()) +""));
        }
        return weatherObjects;
    }

    class List {
        Main main;
        ArrayList<Weather> weather;
        Clouds clouds;
        Wind wind;
        String dt_txt;

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public void setClouds(Clouds clouds) {
            this.clouds = clouds;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public Main getMain() {
            return main;
        }

        public void setWeather(ArrayList<Weather> weather) {
            this.weather = weather;
        }

        public ArrayList<Weather> getWeather() {
            return weather;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public Wind getWind() {
            return wind;
        }

    }


    class Main{
        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public void setPressure(Double pressure) {
            this.pressure = pressure;
        }

        public void setHumidity(Double humidity) {
            this.humidity = humidity;
        }

        public Double getTemp() {
            return temp;
        }

        public Double getPressure() {
            return pressure;
        }

        public Double getHumidity() {
            return humidity;
        }

        Double temp;
        Double pressure;
        Double humidity;
    }


    class Weather{
        String main;

        public void setMain(String main) {
            this.main = main;
        }

        public String getMain() {
            return main;
        }
    }


    class Clouds{
        Double all;

        public void setAll(Double all) {
            this.all = all;
        }

        public Double getAll() {
            return all;
        }
    }


    class Wind {
        Double speed;

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Double getSpeed() {
            return speed;
        }
    }
}