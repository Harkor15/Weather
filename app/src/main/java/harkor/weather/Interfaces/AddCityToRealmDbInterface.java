package harkor.weather.Interfaces;



import harkor.weather.Model.SingleCityPOJO;

public interface AddCityToRealmDbInterface {
    void cityAdded(SingleCityPOJO singleCityPOJO);
    void duplicate();
}
