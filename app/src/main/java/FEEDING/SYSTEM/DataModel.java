package FEEDING.SYSTEM;

public class DataModel {

    String Ammonia, Date, PhLevel,Temperature, Time, Uid;

    public DataModel(){

}
    public DataModel(String ammonia, String date, String phLevel, String temperature, String time, String uid) {
        Ammonia = ammonia;
        Date = date;
        PhLevel = phLevel;
        Temperature = temperature;
        Time = time;
        Uid = uid;
    }

    public String getAmmonia() {
        return Ammonia;
    }

    public void setAmmonia(String ammonia) {
        Ammonia = ammonia;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPhLevel() {
        return PhLevel;
    }

    public void setPhLevel(String phLevel) {
        PhLevel = phLevel;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
