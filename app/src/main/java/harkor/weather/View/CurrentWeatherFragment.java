package harkor.weather.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.joda.time.DateTime;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import harkor.weather.Interfaces.CurrentWeatherInterface;
import harkor.weather.Model.WeatherObject;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;
import harkor.weather.ViewModel.CurrentWeatherViewModel;


public class CurrentWeatherFragment extends Fragment implements CurrentWeatherInterface {
    @BindView(R.id.weather_image) ImageView weatherImage;
    @BindView(R.id.weather_temp) TextView weatherTemp;
    @BindView(R.id.weather_clouds) TextView weatherClouds;
    @BindView(R.id.weather_pressure) TextView weatherPressure;
    @BindView(R.id.weather_humidity) TextView weatherHumidity;
    @BindView(R.id.weather_wind) TextView weatherWind;
    @BindView(R.id.image_refresh) ImageView refreshImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_current_weather,container,false);
        final CurrentWeatherViewModel tabOneViewModel=new CurrentWeatherViewModel(this);
        tabOneViewModel.setData();
        ButterKnife.bind(this,view);

        refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabOneViewModel.setData();
            }
        });

        return view;
    }

    @Override
    public void tabOneSetData(WeatherObject weatherObject) {
        weatherWind.setText(weatherObject.getWind()+"m/s");
        int tempSign=new RealmDatabaseController().getTempSign();
        DecimalFormat df=new DecimalFormat("#");
        if(tempSign==1){
                double temperature=weatherObject.getTemp()-273.15;
            weatherTemp.setText(df.format(temperature)+"°C");
        }else if(tempSign==2){
            weatherTemp.setText(df.format(weatherObject.getTemp())+"K");
        }else{
            double temperature=weatherObject.getTemp()*9/5-459.67;
            weatherTemp.setText(df.format(temperature)+"°F");
        }
        weatherClouds.setText(weatherObject.getClouds()+"%");
        weatherPressure.setText(weatherObject.getPressure()+"hPa");
        weatherHumidity.setText(weatherObject.getHumidity()+"%");
        String weatherDesc=weatherObject.getWeather();

        DateTime dateTime=new DateTime();
        int hour=dateTime.getHourOfDay();

        switch (weatherDesc){
            case "Thunderstorm":
                weatherImage.setImageResource(R.drawable.thunderstorm);
                break;
            case "Drizzle":
                weatherImage.setImageResource(R.drawable.rain);
                break;
            case "Rain":
                weatherImage.setImageResource(R.drawable.rain);
                break;
            case "Snow":
                weatherImage.setImageResource(R.drawable.snow);
                break;
            case "Clear":
                if(hour>=7&&hour<=21){
                    weatherImage.setImageResource(R.drawable.clear);
                }else{
                    weatherImage.setImageResource(R.drawable.clear_night);
                }
                break;
            case "Clouds":
                weatherImage.setImageResource(R.drawable.clouds);
                break;
                default:
                    weatherImage.setImageResource(R.drawable.atmosphere);
                    break;
        }
    }
}

