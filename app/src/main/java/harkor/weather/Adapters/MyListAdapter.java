package harkor.weather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import harkor.weather.Model.WeatherObject;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;

public class MyListAdapter extends BaseAdapter {
    Context context;
   WeatherObject[] weatherObjects;

    public MyListAdapter(Context context, WeatherObject[] weatherObjects) {
        this.context=context;
        this.weatherObjects=weatherObjects;
    }

    @Override
    public int getCount() {
        return weatherObjects.length;
    }

    @Override
    public Object getItem(int position) {
        return weatherObjects[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View rowView=layoutInflater.inflate(R.layout.weather_item,null);
        TextView timeTv=rowView.findViewById(R.id.time_tv);
        ImageView weatherImage=rowView.findViewById(R.id.image_iv);
        TextView pressureTv=rowView.findViewById(R.id.weather_pressure);
        TextView cloudsTv=rowView.findViewById(R.id.weather_clouds);
        TextView windTv=rowView.findViewById(R.id.weather_wind);
        TextView humidityTv=rowView.findViewById(R.id.weather_humidity);
        TextView tempTv=rowView.findViewById(R.id.weather_temp);

        String time=weatherObjects[position].getTime();
        String subTime=time.substring(0,time.length()-3);

        timeTv.setText(subTime);
        pressureTv.setText(weatherObjects[position].getPressure()+"hPa");
        windTv.setText(weatherObjects[position].getWind()+"m/s");
        humidityTv.setText(weatherObjects[position].getHumidity()+"%");
        cloudsTv.setText(weatherObjects[position].getClouds()+"%");

        int tempSign=new RealmDatabaseController().getTempSign();
        DecimalFormat df=new DecimalFormat("#");
        if(tempSign==1){
            double temperature=weatherObjects[position].getTemp()-273.15;
            tempTv.setText(df.format(temperature)+"°C");
        }else if(tempSign==2){
            tempTv.setText(df.format(weatherObjects[position].getTemp())+"K");
        }else{
            double temperature=weatherObjects[position].getTemp()*9/5-459.67;
            tempTv.setText(df.format(temperature)+"°F");
        }

        int dayTime=Integer.valueOf(time.substring(time.length()-8,time.length()-6));
        String weatherDesc=weatherObjects[position].getWeather();
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
                if(dayTime>=7&&dayTime<=21){
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

        return rowView;
    }
}
