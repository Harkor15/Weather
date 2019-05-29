package harkor.weather.API;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import harkor.weather.Interfaces.NominatimAfterResponseInterface;
import harkor.weather.Model.NominatimObject;
import harkor.weather.Model.NominatimSearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NominatimResponseMenager {

    private RetrofitNominatimService retrofitNominatimService = new RetrofitNominatimService();

    public void getNominatinResponse(final String cityName, final NominatimAfterResponseInterface nominatimAfterResponseInterface){
        final Call<List<NominatimSearchResponse>> nominatinSearchCall=retrofitNominatimService.getNominatinSerach(cityName);
        nominatinSearchCall.enqueue(new Callback<List<NominatimSearchResponse>>() {
            @Override
            public void onResponse(Call<List<NominatimSearchResponse>> call, Response<List<NominatimSearchResponse>> response) {
                ArrayList<NominatimObject> nominatimObjects= new ArrayList<>();
                for(int i=0; i<response.body().size();i++){
                    nominatimObjects.add(new NominatimObject(Double.parseDouble(response.body().get(i).getLon()),
                            Double.parseDouble(response.body().get(i).getLat()),response.body().get(i).getDisplay_name()));
                }
                nominatimAfterResponseInterface.nominatimList(nominatimObjects,cityName);
            }

            @Override
            public void onFailure(Call<List<NominatimSearchResponse>> call, Throwable t) {
                Log.d("Retrofit Error",t.toString());
            }
        });
    }
}
