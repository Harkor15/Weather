package harkor.weather.API;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import harkor.weather.Model.NominatimSearchResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNominatimService {
    Gson gson=new GsonBuilder().create();
    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl(" https://nominatim.openstreetmap.org")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    RetrofitNominatimInterface retrofitNominatinInterface =retrofit.create(RetrofitNominatimInterface.class);

    public Call<List<NominatimSearchResponse>> getNominatinSerach(String cityName){
        return retrofitNominatinInterface.getNominatinSearch(cityName,"json");
    }

}
