package ca.ubc.cpsc304.model;

public class ReservationModel {
    private final String confNo;
    private final String vtname;
    private final int cellphone;
    // TODO: figure out how we want to represent date/time
    private final String fromDate;
    private final String fromTime;
    private final String toDate;
    private final String toTime;

    public ReservationModel (String confNo, String vtname, int cellphone, String fromDate, String fromTime,
                             String toDate, String toTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public int getCellphone() {
        return cellphone;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }
}
