package harkor.weather.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import harkor.weather.Adapters.FavouritesListAdapter;
import harkor.weather.Interfaces.SettingsViewModelInterface;
import harkor.weather.Model.NominatimObject;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;
import harkor.weather.ViewModel.SettingsViewModel;
import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity implements FavouritesListAdapter.ItemClickListener, SettingsViewModelInterface {
    FavouritesListAdapter listViewFavouritesAdapter;
    List<SingleCityPOJO> favouriteCitys=new ArrayList<>();
    FavouritesListAdapter favouritesListAdapter;
    SettingsViewModel settingsViewModel;
    SingleCityPOJO startMainCity;
    @BindView(R.id.favourites_list) RecyclerView favouritesList;
    @BindView(R.id.image_arrow_back) ImageView backButton;
    @BindView(R.id.add_from_map_btn)Button addFromMap;
    @BindView(R.id.temperature_unit_group)RadioGroup radioGroup;
    @BindView(R.id.add_favourites_button) ImageView addCityButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        final RadioButton celsius=radioGroup.findViewById(R.id.unit_celsius);
        final RadioButton kelvin=radioGroup.findViewById(R.id.unit_kelvin);
        final RadioButton fahrenheit=radioGroup.findViewById(R.id.unit_fahrenheit);
        settingsViewModel=new SettingsViewModel(this);
        addFromMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PickCityActivity.class);
                startActivityForResult(intent,1);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.unit_celsius:
                        settingsViewModel.setTempSign(1);
                        break;
                    case R.id.unit_kelvin:
                        settingsViewModel.setTempSign(2);
                        break;
                    case R.id.unit_fahrenheit:
                       settingsViewModel.setTempSign(3);
                        break;
                    default:
                        break;
                }
            }
        });
        int tempUnit;
        try{
            tempUnit=settingsViewModel.getTempSign();
        }catch (Throwable t){
            Realm.init(getApplicationContext());
            tempUnit=settingsViewModel.getTempSign();
            Log.v("Error realm",t.toString());
        }

        switch(tempUnit){
            case 1:
                celsius.setChecked(true);
                break;
            case 2:
                kelvin.setChecked(true);
                break;
            case 3:
                fahrenheit.setChecked(true);
                break;
            default:
                break;
        }


        favouriteCitys=settingsViewModel.getListOfFavourites();
        listViewFavouritesAdapter=new FavouritesListAdapter(getApplicationContext(),favouriteCitys);
        favouritesList.setLayoutManager(new LinearLayoutManager(this));
        favouritesListAdapter=new FavouritesListAdapter(this,favouriteCitys);
        favouritesListAdapter.setItemClickListener(this);
        RealmDatabaseController realmDatabaseController=new RealmDatabaseController();
        startMainCity=realmDatabaseController.getMainCity();
        if(startMainCity!= null){
            favouritesListAdapter.setMainColor(startMainCity.getCity());
        }
        favouritesList.setAdapter(favouritesListAdapter);


        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cityToAdd=findViewById(R.id.favourites_plain_text);
                String addedCity=cityToAdd.getText().toString();
                cityToAdd.setText("");
                settingsViewModel.addCityToFavourites(addedCity);
            }
        });
    }

    @Override
    public void OnDeleteClick(View view, int position) {
        settingsViewModel.removeCity(favouriteCitys.get(position));
        favouriteCitys.remove(position);
        favouritesListAdapter.setFavourites(favouriteCitys);
        favouritesListAdapter.notifyItemRemoved(position);
    }

    @Override
    public void noRespond() {
        Toast.makeText(getApplicationContext(),R.string.city_not_found,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void oneRespond(SingleCityPOJO singleCityPOJO) {
        favouriteCitys.add(singleCityPOJO);
        favouritesListAdapter.setFavourites(favouriteCitys);
        favouritesListAdapter.notifyItemInserted(favouriteCitys.size()-1);
    }

    @Override
    public void manyResponds(final ArrayList<NominatimObject> nominatimObjects, final String cityName) {
        AlertDialog.Builder builder= new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(R.string.choose_city);

        String [] citys= new String[nominatimObjects.size()];
        for(int i=0;i<nominatimObjects.size();i++){
            citys[i]=nominatimObjects.get(i).getDisplay_name();
            Log.d("weather-test", citys[i]);
        }
        Log.d("weather-test", citys.length+"");
        final int[] checkedItem = {0};
        builder.setSingleChoiceItems(citys, checkedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem[0] =which;
                Log.d("weather-test", "which "+which);
            }
        });
        builder.setNegativeButton(R.string.cancel,null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("weather-test", "Clicked ok");
                settingsViewModel.addCity(new SingleCityPOJO(cityName,
                        nominatimObjects.get(checkedItem[0]).getLon(),
                        nominatimObjects.get(checkedItem[0]).getLat()));
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                Bundle bundle=data.getBundleExtra("result");
                SingleCityPOJO singleCityPOJO=new SingleCityPOJO(bundle.getString("city_name"),bundle.getDouble("lon"),bundle.getDouble("lat"));
                settingsViewModel.addCity(singleCityPOJO);
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent=new Intent();
        RealmDatabaseController realmDatabaseController=new RealmDatabaseController();
        if(realmDatabaseController.getMainCity()!=startMainCity){
            returnIntent.putExtra("change_flag", true);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }else{
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }
}
