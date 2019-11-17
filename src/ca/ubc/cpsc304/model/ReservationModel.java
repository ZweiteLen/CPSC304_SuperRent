package ca.ubc.cpsc304.model;

public class ReservationModel {
    private final String confNo;
    private final String vtname;
    private final int cellphone;
    private final String fromDateTime;
    private final String toDateTime;

    public ReservationModel (String confNo, String vtname, int cellphone, String fromDate, String fromTime,
                             String toDate, String toTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDateTime = fromDate + " " + fromTime;
        this.toDateTime = toDate + " " + toTime;
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

    public String getFromDateTime() {
        return fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }
}
