package ca.ubc.cpsc304.model;

public class CustomerModel {
    private final String cellphone;
    private final String name;
    private final String address;
    private final String dLicense;

    public CustomerModel(String cellphone, String name, String address, String dLicense) {
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dLicense = dLicense;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getAddress() {
        return address;
    }

    public String getdLicense() {
        return dLicense;
    }

    public String getName() {
        return name;
    }
}
