package harkor.weather.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import harkor.weather.Model.WeatherObject;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;

public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.MyViewHolder>{
    WeatherObject[] weatherObjects;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView weatherTime;
        public TextView weatherPressure;
        public TextView weatherClouds;
        public TextView weatherWind;
        public TextView weatherTemp;
        public TextView weatherHumidity;
        public ImageView weatherImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherTime=itemView.findViewById(R.id.time_tv);
            weatherPressure=itemView.findViewById(R.id.weather_pressure);
            weatherClouds=itemView.findViewById(R.id.weather_clouds);
            weatherWind=itemView.findViewById(R.id.weather_wind);
            weatherTemp=itemView.findViewById(R.id.weather_temp);
            weatherHumidity=itemView.findViewById(R.id.weather_humidity);
            weatherImage=itemView.findViewById(R.id.image_iv);
        }
    }

    public ForecastRecyclerViewAdapter(WeatherObject[] weatherObjects) {
        this.weatherObjects = weatherObjects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_item,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        String time=weatherObjects[position].getTime();
        DateTimeFormatter formatter= DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime=formatter.parseDateTime(time);
        String dateString=dateTime.getDayOfMonth()+" "+dateTime.toString("MMMMMMMMMMMMMMM")+" "+
                dateTime.getYear()+" "+dateTime.getHourOfDay()+":00";

        myViewHolder.weatherTime.setText(dateString);


        myViewHolder.weatherPressure.setText(weatherObjects[position].getPressure()+"hPa");
        myViewHolder.weatherWind.setText(weatherObjects[position].getWind()+"m/s");
        myViewHolder.weatherHumidity.setText(weatherObjects[position].getHumidity()+"%");
        myViewHolder.weatherClouds.setText(weatherObjects[position].getClouds()+"%");

        int tempSign=new RealmDatabaseController().getTempSign();
        DecimalFormat df=new DecimalFormat("#");
        if(tempSign==1){
            double temperature=weatherObjects[position].getTemp()-273.15;
            myViewHolder.weatherTemp.setText(df.format(temperature)+"°C");
        }else if(tempSign==2){
            myViewHolder.weatherTemp.setText(df.format(weatherObjects[position].getTemp())+"K");
        }else{
            double temperature=weatherObjects[position].getTemp()*9/5-459.67;
            myViewHolder.weatherTemp.setText(df.format(temperature)+"°F");
        }


        int dayTime=Integer.valueOf(time.substring(time.length()-8,time.length()-6));
        String weatherDesc=weatherObjects[position].getWeather();
        switch (weatherDesc){
            case "Thunderstorm":
                myViewHolder.weatherImage.setImageResource(R.drawable.thunderstorm);
                break;
            case "Drizzle":
                myViewHolder.weatherImage.setImageResource(R.drawable.rain);
                break;
            case "Rain":
                myViewHolder.weatherImage.setImageResource(R.drawable.rain);
                break;
            case "Snow":
                myViewHolder.weatherImage.setImageResource(R.drawable.snow);
                break;
            case "Clear":
                if(dayTime>=7&&dayTime<=21){
                    myViewHolder.weatherImage.setImageResource(R.drawable.clear);
                }else{
                    myViewHolder.weatherImage.setImageResource(R.drawable.clear_night);
                }
                break;
            case "Clouds":
                myViewHolder.weatherImage.setImageResource(R.drawable.clouds);
                break;
            default:
                myViewHolder.weatherImage.setImageResource(R.drawable.atmosphere);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return weatherObjects.length;
    }
}
