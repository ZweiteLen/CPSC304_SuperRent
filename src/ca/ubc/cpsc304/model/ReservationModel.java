package ca.ubc.cpsc304.model;

import java.sql.Timestamp;

public class ReservationModel {
    private final String confNo;
    private final String vtname;
    private final String dLicense;
    private final String fromDateTime;
    private final String toDateTime;

    public ReservationModel (String confNo, String vtname, String dLicense, String fromDateTime,
                             String toDateTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.dLicense = dLicense;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    public String getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public String getDLicense() {
        return dLicense;
    }

    public String getFromDateTime() {
        return fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }
}
