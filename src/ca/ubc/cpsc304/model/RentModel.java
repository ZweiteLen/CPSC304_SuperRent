package ca.ubc.cpsc304.model;

public class RentModel {
    private final String rid;
    private final String vid;
    private final int cellphone;
    private final String fromDateTime;
    private final String toDateTime;
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
        this.fromDateTime = fromDate + " " + fromTime;
        this.toDateTime = toDate + " " + toTime;
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

    public String getFromDateTime() {
        return fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
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
