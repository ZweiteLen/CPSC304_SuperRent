package ca.ubc.cpsc304.model;

public class CustomerModel {
    private final String cellphone;
    private final String name;
    private final String address;
    private final String dLicemse;

    public CustomerModel(String cellphone, String name, String address, String dLicemse) {
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dLicemse = dLicemse;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getAddress() {
        return address;
    }

    public String getdLicemse() {
        return dLicemse;
    }

    public String getName() {
        return name;
    }
}
