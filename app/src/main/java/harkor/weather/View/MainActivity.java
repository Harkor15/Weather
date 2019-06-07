package harkor.weather.View;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import harkor.weather.Adapters.PagerAdapter;
import harkor.weather.Interfaces.GpsResultInterface;
import harkor.weather.Model.SingleCityPOJO;

import harkor.weather.R;
import harkor.weather.Services.MyLocationListener;
import harkor.weather.Services.RealmDatabaseController;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity implements GpsResultInterface {
    @BindView(R.id.main_city) TextView cityNameTv;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.simple_view_pager) ViewPager viewPager;

    PagerAdapter pagerAdapter;
    final GpsResultInterface gpsResultInterface = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTabLayout();
        reamlConfiguraton();

        ImageView settingsButton = findViewById(R.id.imege_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume", "test");
        setMainCity();
    }

    void setTabLayout(){
        //tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_weather));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.forecast));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.map));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pagerAdapter = new PagerAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    AlertDialog.Builder getAlertBuilder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.checkFromGps)
                .setTitle(R.string.localisation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkLocalisation();
                    }
                })
                .setNegativeButton(R.string.no,null);
        return builder;
    }

    void reamlConfiguraton(){
        Realm.init(getApplicationContext());

       // RealmConfiguration config = new RealmConfiguration
        //        .Builder()
        //        .deleteRealmIfMigrationNeeded()
        //        .build();
    }

    void setMainCity(){
        RealmDatabaseController realmDatabaseController = new RealmDatabaseController();
        SingleCityPOJO mainCity = realmDatabaseController.getMainCity();

        if(mainCity==null){
            AlertDialog dialog=getAlertBuilder().create();
            dialog.show();
        }else{
            cityNameTv.setText(mainCity.getCity());
        }
    }

    void checkLocalisation(){
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener(getApplicationContext(), gpsResultInterface);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final String[] GPS_PERMS={
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
                requestPermissions(GPS_PERMS,1337);
            }
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100000, locationListener);
        }
    }

    @Override
    public void setMainLocation(String cityName,Double lon,Double lat) {
        cityNameTv.setText(cityName);
        new RealmDatabaseController().setMainCity(new SingleCityPOJO(cityName,lon,lat));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(resultCode== Activity.RESULT_OK){
                //TODO:Refresh fragments
                viewPager.setAdapter(pagerAdapter);

                Log.d("weather-test-refresh","true");
            }
        }
    }
}
