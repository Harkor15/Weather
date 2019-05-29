package harkor.weather.Model;

import io.realm.RealmObject;

public class TemperatureScale extends RealmObject {
    int tempMark; //1-C 2-K 3-F

    public void setTempMark(int tempMark) {
        this.tempMark = tempMark;
    }

    public int getTempMark() {
        return tempMark;
    }
}
