package ca.ubc.cpsc304.model;

public class CustomerModel {
    private final String dLicense;
    private final String name;
    private final String address;
    private final String dlicense;

    public CustomerModel(String dLicense, String name, String address, String dlicense) {
        this.dLicense = dLicense;
        this.name = name;
        this.address = address;
        this.dlicense = dlicense;
    }

    public String getDLicense() {
        return dLicense;
    }

    public String getAddress() {
        return address;
    }

    public String getDlicense() {
        return dlicense;
    }

    public String getName() {
        return name;
    }
}
