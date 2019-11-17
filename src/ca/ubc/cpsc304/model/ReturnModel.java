package ca.ubc.cpsc304.model;

public class ReturnModel {
    private final String rid;
    private final String datetime;
    private final int odometer;
    private final boolean fulltank;
    private final int value;

    public ReturnModel(String rid, String date, String time, int odometer, boolean fulltank, int value) {
        this.rid = rid;
        this.datetime = date + " " + time;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public String getRid() {
        return rid;
    }

    public String getDate() {
        return datetime;
    }

    public int getOdometer() {
        return odometer;
    }

    public boolean isFulltank() {
        return fulltank;
    }

    public int getValue() {
        return value;
    }
}
