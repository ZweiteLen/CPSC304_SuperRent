package ca.ubc.cpsc304.model;

public class ReservationModel {
    private final int confNo;
    private final String vtname;
    private final String cellphone;
    private final String fromDate;
    private final int fromTime;
    private final String toDate;
    private final int toTime;

    public ReservationModel (int confNo, String vtname, String cellphone, String fromDate, int fromTime,
                             String toDate, int toTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public int getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public String getCellphone() {
        return cellphone;
    }

    public int getFromTime() {
        return fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }
}
