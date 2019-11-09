package ca.ubc.cpsc304.model;

public class RentModel {
    private final String rid;
    private final String vid;
    private final int cellphone;
    // TODO: figure out how we want to represent date/time
    private final String fromDate;
    private final String fromTime;
    private final String toDate;
    private final String toTime;
    private final int odometer;
    private final String cardName;
    private final String cardNo;
    private final String expDate;
    private final String confNo;

    public RentModel (String rid, String vid, int cellphone, String fromDate, String fromTime, String toDate, String toTime,
                      int odometer, String cardName, String cardNo, String expDate, String confNo) {
        this.rid = rid;
        this.vid= vid;
        this.cellphone = cellphone;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    public String getRid() {
        return rid;
    }

    public String getVid() {
        return vid;
    }

    public int getCellphone() {
        return cellphone;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getConfNo() {
        return confNo;
    }
}
