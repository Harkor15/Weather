package harkor.weather.View;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import butterknife.BindView;
import butterknife.ButterKnife;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;

public class MapFragment extends Fragment {
    @BindView(R.id.mapView) MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getContext(), "pk.eyJ1IjoiaGFya29yIiwiYSI6ImNqdXdmd3ZydzBjMjY0ZXBwdmE3NnY2YTUifQ.O39Bv04lF2P8vZiFgU1ElQ");
        View view=inflater.inflate(R.layout.fragment_map,container,false);
        ButterKnife.bind(this,view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        RealmDatabaseController realmDatabaseController=new RealmDatabaseController();
                        SingleCityPOJO singleCityPOJO=realmDatabaseController.getMainCity();

                        if(singleCityPOJO!=null){
                            style.addImage("marker-icon-id",
                                    BitmapFactory.decodeResource(
                                            getActivity().getResources(), R.drawable.mapbox_marker_icon_default));

                            GeoJsonSource geoJsonSource = new GeoJsonSource("source-id", Feature.fromGeometry(
                                    Point.fromLngLat(singleCityPOJO.getLongitude(), singleCityPOJO.getLatitude())));
                            style.addSource(geoJsonSource);

                            SymbolLayer symbolLayer = new SymbolLayer("layer-id", "source-id");
                            symbolLayer.withProperties(
                                    PropertyFactory.iconImage("marker-icon-id")
                            );
                            style.addLayer(symbolLayer);
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(singleCityPOJO.getLatitude(),singleCityPOJO.getLongitude()))
                                    .build();
                            mapboxMap.setCameraPosition(position);

                        }

                    }
                });
            }
        });
        return view;
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

}
