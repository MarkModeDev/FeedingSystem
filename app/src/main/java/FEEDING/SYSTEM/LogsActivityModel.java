package FEEDING.SYSTEM;

public class LogsActivityModel {

    public LogsActivityModel(){

    }
    public LogsActivityModel(String date, String time, String uid, String status) {
        Date = date;
        Time = time;
        Uid = uid;
        Status = status;
    }

    String Date, Time, Uid, Status;


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
