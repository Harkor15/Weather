package harkor.weather.ViewModel;

import java.util.ArrayList;
import java.util.List;
import harkor.weather.API.NominatimResponseMenager;
import harkor.weather.Interfaces.AddCityToRealmDbInterface;
import harkor.weather.Interfaces.NominatimAfterResponseInterface;
import harkor.weather.Interfaces.SettingsViewModelInterface;
import harkor.weather.Model.NominatimObject;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.Services.RealmDatabaseController;

public class SettingsViewModel implements NominatimAfterResponseInterface, AddCityToRealmDbInterface {
    RealmDatabaseController realmDatabaseController=new RealmDatabaseController();
    SettingsViewModelInterface settingsViewModelInterface;

    public SettingsViewModel(SettingsViewModelInterface settingsViewModelInterface) {
        this.settingsViewModelInterface = settingsViewModelInterface;
    }

    public List<SingleCityPOJO> getListOfFavourites(){
        return realmDatabaseController.getFavourites();
    }

    public void addCity(SingleCityPOJO city){
        realmDatabaseController.addCity(city,this);
    }

    public void setTempSign(int tempSign){
        realmDatabaseController.setTempSign(tempSign);
    }

    public int getTempSign(){
        return realmDatabaseController.getTempSign();
    }

    public void removeCity(SingleCityPOJO city){
        realmDatabaseController.deleteCity(city);
    }

    public void addCityToFavourites(String cityName){
        NominatimResponseMenager nominatinResponseMenager=new NominatimResponseMenager();
        nominatinResponseMenager.getNominatinResponse(cityName, this);
    }

    @Override
    public void nominatimList(ArrayList<NominatimObject> nominatimObjects, String cityName) {
        if(nominatimObjects.size()==0){
            settingsViewModelInterface.noRespond();
        }else if(nominatimObjects.size()==1){
            realmDatabaseController.addCity(new SingleCityPOJO(cityName,nominatimObjects.get(0).getLon(),nominatimObjects.get(0).getLat()),this);
        }else{
            settingsViewModelInterface.manyResponds(nominatimObjects,cityName);
        }
    }

    @Override
    public void cityAdded(SingleCityPOJO singleCityPOJO) {
        settingsViewModelInterface.oneRespond(singleCityPOJO);

    }

    @Override
    public void duplicate() { }
}

