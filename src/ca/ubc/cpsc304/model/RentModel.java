package ca.ubc.cpsc304.model;

public class RentModel {
    private final String rid;
    private final String vlicense;
    private final String cellphone;
    private final String dlicense;
    private final String fromDateTime;
    private final String toDateTime;
    private final int odometer;
    private final String cardName;
    private final String cardNo;
    private final String expDate;
    private final String confNo;

    public RentModel (String rid, String vlicense, String cellphone, String dlicense, String fromDateTime, String toDateTime,
                      int odometer, String cardName, String cardNo, String expDate, String confNo) {
        this.rid = rid;
        this.vlicense = vlicense;
        this.cellphone = cellphone;
        this.dlicense = dlicense;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    public String getRid() {
        return rid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getDlicense() {
        return dlicense;
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
