package harkor.weather.Services;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import harkor.weather.Interfaces.AddCityToRealmDbInterface;
import harkor.weather.Model.DatabaseObject;
import harkor.weather.Model.MyRealmMigration;
import harkor.weather.Model.SelectedCityRealm;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.Model.TemperatureScale;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmDatabaseController {

    RealmConfiguration config = new RealmConfiguration
            .Builder().schemaVersion(2)
            .migration(new MyRealmMigration())
            .build();

    private void addCityPrivate(final DatabaseObject databaseObject){
        Log.d("weather-test", "addCityprivate");
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(databaseObject);
            }
        });
        Log.d("weather-test", "Close realm");
        //realm.close();
    }

    public void addCity(final SingleCityPOJO singleCityPOJO, final AddCityToRealmDbInterface addCityToRealmDbInterface){
        Log.d("weather-test-lonlat", singleCityPOJO.getLongitude()+" x "+singleCityPOJO.getLatitude());
        final DatabaseObject dbObject=new DatabaseObject();
        dbObject.setCityName(singleCityPOJO.getCity());
        dbObject.setLatitude(singleCityPOJO.getLatitude());
        dbObject.setLongitude(singleCityPOJO.getLongitude());
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        final Boolean[] noDuplicateFlag = new Boolean[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DatabaseObject> results=realm.where(DatabaseObject.class)
                        .equalTo("cityName", singleCityPOJO.getCity().replaceAll("\\s+","")).findAll();
                if(results.size()==0){
                    noDuplicateFlag[0] =true;
                    //addCityPrivate(dbObject);
                    Log.d("weather-test", "size=0");

                }else{
                    noDuplicateFlag[0]=true;
                    for(int i=0;i<results.size();i++){
                        Log.d("weather-test", singleCityPOJO.getLatitude()+"");
                        Log.d("weather-test", singleCityPOJO.getLongitude()+"");
                        Log.d("weather-test", results.get(i).getLatitude()+"");
                        Log.d("weather-test", results.get(i).getLongitude()+"");
                        Log.d("weather-test", "flag:"+noDuplicateFlag[0]);

                        Double difLatitude=singleCityPOJO.getLatitude()-results.get(i).getLatitude();
                        Double difLongitude=singleCityPOJO.getLongitude()-results.get(i).getLongitude();
                        Log.d("weather-test-dif", "difLat: "+difLatitude+"difLon:"+difLongitude);
                        if(((difLatitude<0.05&&difLatitude>-0.05)&&(difLongitude<0.05&&difLongitude>-0.05))){
                            noDuplicateFlag[0] =false;
                            //addCityToRealmDbInterface.cityAdded(singleCityPOJO);
                            Log.d("weather-test", "FOR "+i);
                        }
                    }

                }
            }

        });
        Log.d("weather-test-flag", noDuplicateFlag[0].toString());
        if(noDuplicateFlag[0]){
            addCityPrivate(dbObject);
            addCityToRealmDbInterface.cityAdded(singleCityPOJO);
        }else{
            addCityToRealmDbInterface.duplicate();
        }
        //if(!realm.isClosed()){
            //Log.d("weather-test", "Close realm");
            //realm.close();
        //}
    }

    public void deleteCity(final SingleCityPOJO singleCityPOJO){
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        Log.d("weather-test", "Def inst"+ realm.isClosed());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DatabaseObject> result = realm.where(DatabaseObject.class)
                        .equalTo("cityName", singleCityPOJO.getCity()).findAll();
                for(int i=0;i<result.size();i++){
                    Double difLatitude=singleCityPOJO.getLatitude()-result.get(i).getLatitude();
                    Double difLongitude=singleCityPOJO.getLongitude()-result.get(i).getLongitude();
                    if((difLatitude<0.05&&difLatitude>-0.05)&&(difLongitude<0.05&&difLongitude>-0.05)){
                        Log.d("weather-test", "isClose "+ realm.isClosed());
                        result.deleteFromRealm(i);
                    }
                }
                result.deleteAllFromRealm();
            }
        });
        Log.d("weather-test", "Close realm1");
        //realm.close();
    }

    public List<SingleCityPOJO> getFavourites(){
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        RealmResults<DatabaseObject> results=realm.where(DatabaseObject.class).findAll();
        List<SingleCityPOJO> favoutites=new ArrayList<>();
        for(int i=0;i<results.size();i++){
            favoutites.add(new SingleCityPOJO(results.get(i).getCityName(),results.get(i).getLongitude(),results.get(i).getLatitude()));

        }
        Log.d("weather-test", "Close realm");
        //realm.close();
        return favoutites;
    }

    public void setMainCity(SingleCityPOJO singleCity){
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        final SelectedCityRealm selectedCityRealm = new SelectedCityRealm();
        selectedCityRealm.setCity(singleCity.getCity());
        selectedCityRealm.setLatitude(singleCity.getLatitude());
        selectedCityRealm.setLongitude(singleCity.getLongitude());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(SelectedCityRealm.class);
                realm.insertOrUpdate(selectedCityRealm);
            }
        });
        Log.d("weather-test", "Close realm");
        //realm.close();
    }

    public SingleCityPOJO getMainCity(){
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        RealmResults<SelectedCityRealm> result=realm.where(SelectedCityRealm.class).findAll();

        if(result.isEmpty()){
            //realm.close();
            return null;
        }else{
            SelectedCityRealm selectedCityRealm;
            selectedCityRealm =result.first();
            SingleCityPOJO mainCity=new SingleCityPOJO(selectedCityRealm.getCity(),
                    selectedCityRealm.getLongitude(),selectedCityRealm.getLatitude());
            Log.d("weather-test-getMainCit",selectedCityRealm.getCity()+" Lon "+selectedCityRealm.getLongitude()+" Lat "+selectedCityRealm.getLatitude());
            //realm.close();
            return mainCity;
        }

    }

    public int getTempSign(){
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        RealmResults<TemperatureScale> results=realm.where(TemperatureScale.class).findAll();
        if(results.isEmpty()){
            Log.d("weather-test", "Close realm");
            //realm.close();
            return 1;
        }else{
            int tempSign=results.get(0).getTempMark();
            Log.d("weather-test", "Close realm");
            //realm.close();
            return tempSign;
        }
    }

    public void setTempSign(final int tempSign){
        Log.d("weather-test", "Open realm");
        Realm realm=Realm.getInstance(config);
        final TemperatureScale temperatureScale=new TemperatureScale();
        temperatureScale.setTempMark(tempSign);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(TemperatureScale.class);
                realm.insertOrUpdate(temperatureScale);
            }
        });
        Log.d("weather-test", "Close realm");
        //realm.close();
    }
}
