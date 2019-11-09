package ca.ubc.cpsc304.model;

public class ReturnModel {
    private final String rid;
    // TODO: figure out how we want to represent date/time
    private final String date;
    private final String time;
    private final int odometer;
    private final boolean fulltank;
    private final int value;

    public ReturnModel(String rid, String date, String time, int odometer, boolean fulltank, int value) {
        this.rid = rid;
        this.date = date;
        this.time = time;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public String getRid() {
        return rid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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
