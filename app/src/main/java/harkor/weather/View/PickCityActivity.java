package harkor.weather.View;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;


public class PickCityActivity extends AppCompatActivity {
    @BindView(R.id.mapView)  MapView mapView;
    @BindView(R.id.cancel_btn) Button cancelButton;
    @BindView(R.id.add_city_btn) Button addCityButton;

    private MarkerViewManager markerViewManager;
    private SingleCityPOJO singleCityPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaGFya29yIiwiYSI6ImNqdXdmd3ZydzBjMjY0ZXBwdmE3NnY2YTUifQ.O39Bv04lF2P8vZiFgU1ElQ");
        setContentView(R.layout.activity_pick_city);
        ButterKnife.bind(this);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singleCityPOJO!=null&&singleCityPOJO.getCity()!=null){
                    Bundle result=new Bundle();
                    result.putString("city_name",singleCityPOJO.getCity());
                    result.putDouble("lon", singleCityPOJO.getLongitude());
                    result.putDouble("lat", singleCityPOJO.getLatitude());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",result);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),R.string.click_on_map_to_choose_city,Toast.LENGTH_SHORT).show();
                }
            }
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        RealmDatabaseController realmDatabaseController=new RealmDatabaseController();
                        List<SingleCityPOJO> favouritesList=realmDatabaseController.getFavourites();
                        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
                        //List<Feature> markerCoordinates=new ArrayList<>();
                        for(int i=0;i<favouritesList.size();i++) {
                            //markerCoordinates.add(Feature.fromGeometry(Point.fromLngLat(favouritesList.get(i).getLongitude(), favouritesList.get(i).getLatitude())));
                            mapboxMap.addMarker(new MarkerOptions().position(new LatLng(favouritesList.get(i).getLatitude(), favouritesList.get(i).getLongitude()))
                                    .title(favouritesList.get(i).getCity()));
                        }

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> addresses;
                                try {
                                    addresses =geocoder.getFromLocation(point.getLatitude(),point.getLongitude(),1);
                                    Toast.makeText(getApplicationContext(),addresses.get(0).getLocality(),Toast.LENGTH_SHORT).show();
                                    singleCityPOJO=new SingleCityPOJO(addresses.get(0).getLocality(),point.getLongitude(),point.getLatitude());
                                }catch (Exception ignored){}
                                return false;
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (markerViewManager != null) {
            markerViewManager.onDestroy();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
