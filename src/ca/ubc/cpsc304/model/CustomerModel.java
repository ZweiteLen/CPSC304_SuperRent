package ca.ubc.cpsc304.model;

public class CustomerModel {
    private final int cellphone;
    private final String name;
    private final String address;
    private final String dlicense;

    public CustomerModel(int cellphone, String name, String address, String dlicense) {
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dlicense = dlicense;
    }

    public int getCellphone() {
        return cellphone;
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
