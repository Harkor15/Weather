package harkor.weather.API;

import java.util.List;
import harkor.weather.Model.NominatimSearchResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitNominatimInterface {
    @POST("/search")
    Call<List<NominatimSearchResponse>> getNominatinSearch(@Query("q") String city, @Query("format") String format);
}
