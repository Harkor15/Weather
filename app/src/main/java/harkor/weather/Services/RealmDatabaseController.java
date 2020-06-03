package harkor.weather.Services;

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
        Realm realm=Realm.getInstance(config);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(databaseObject);
            }
        });
    }

    public void addCity(final SingleCityPOJO singleCityPOJO, final AddCityToRealmDbInterface addCityToRealmDbInterface){
        final DatabaseObject dbObject=new DatabaseObject();
        dbObject.setCityName(singleCityPOJO.getCity());
        dbObject.setLatitude(singleCityPOJO.getLatitude());
        dbObject.setLongitude(singleCityPOJO.getLongitude());
        Realm realm=Realm.getInstance(config);
        final Boolean[] noDuplicateFlag = new Boolean[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DatabaseObject> results=realm.where(DatabaseObject.class)
                        .equalTo("cityName", singleCityPOJO.getCity().replaceAll("\\s+","")).findAll();
                if(results.size()==0){
                    noDuplicateFlag[0] =true;
                }else{
                    noDuplicateFlag[0]=true;
                    for(int i=0;i<results.size();i++){
                        Double difLatitude=singleCityPOJO.getLatitude()-results.get(i).getLatitude();
                        Double difLongitude=singleCityPOJO.getLongitude()-results.get(i).getLongitude();
                        if(((difLatitude<0.05&&difLatitude>-0.05)&&(difLongitude<0.05&&difLongitude>-0.05))){
                            noDuplicateFlag[0] =false;
                        }
                    }
                }
            }
        });
        if(noDuplicateFlag[0]){
            addCityPrivate(dbObject);
            addCityToRealmDbInterface.cityAdded(singleCityPOJO);
        }else{
            addCityToRealmDbInterface.duplicate();
        }
    }

    public void deleteCity(final SingleCityPOJO singleCityPOJO){
        Realm realm=Realm.getInstance(config);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DatabaseObject> result = realm.where(DatabaseObject.class)
                        .equalTo("cityName", singleCityPOJO.getCity()).findAll();
                for(int i=0;i<result.size();i++){
                    Double difLatitude=singleCityPOJO.getLatitude()-result.get(i).getLatitude();
                    Double difLongitude=singleCityPOJO.getLongitude()-result.get(i).getLongitude();
                    if((difLatitude<0.05&&difLatitude>-0.05)&&(difLongitude<0.05&&difLongitude>-0.05)){
                        result.deleteFromRealm(i);
                    }
                }
                result.deleteAllFromRealm();
            }
        });
    }

    public List<SingleCityPOJO> getFavourites(){
        Realm realm=Realm.getInstance(config);
        RealmResults<DatabaseObject> results=realm.where(DatabaseObject.class).findAll();
        List<SingleCityPOJO> favoutites=new ArrayList<>();
        for(int i=0;i<results.size();i++){
            favoutites.add(new SingleCityPOJO(results.get(i).getCityName(),results.get(i).getLongitude(),results.get(i).getLatitude()));
        }
        return favoutites;
    }

    public void setMainCity(SingleCityPOJO singleCity){
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
    }

    public SingleCityPOJO getMainCity(){
        Realm realm=Realm.getInstance(config);
        RealmResults<SelectedCityRealm> result=realm.where(SelectedCityRealm.class).findAll();

        if(result.isEmpty()){

            return null;
        }else{
            SelectedCityRealm selectedCityRealm;
            selectedCityRealm =result.first();
            SingleCityPOJO mainCity=new SingleCityPOJO(selectedCityRealm.getCity(),
                    selectedCityRealm.getLongitude(),selectedCityRealm.getLatitude());
            return mainCity;
        }

    }

    public int getTempSign(){
        Realm realm=Realm.getInstance(config);
        RealmResults<TemperatureScale> results=realm.where(TemperatureScale.class).findAll();
        if(results.isEmpty()){
            return 1;
        }else{
            int tempSign=results.get(0).getTempMark();
            return tempSign;
        }
    }

    public void setTempSign(final int tempSign){
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
    }
}
