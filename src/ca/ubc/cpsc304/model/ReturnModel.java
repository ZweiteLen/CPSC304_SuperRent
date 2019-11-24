package ca.ubc.cpsc304.model;

import java.sql.*;

public class ReturnModel {
    private final String rid;
    private final Timestamp datetime;
    private final int odometer;
    private final int fulltank;
    private final int value;

    public ReturnModel(String rid, Timestamp datetime, int odometer, int fulltank, int value) {
        this.rid = rid;
        this.datetime = datetime;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public String getRid() {
        return rid;
    }

    public Timestamp getDateTime() {
        return datetime;
    }

    public int getOdometer() {
        return odometer;
    }

    public int isFulltank() { return fulltank; }

    public int getValue() {
        return value;
    }
}
