package ca.ubc.cpsc304.model;

public class VehicleModel {
    private final int vid;
    private final String vlicense;
    private final String make;
    private final String model;
    private final int year;
    private final String colour;
    private final int odometer;
    private final String status;
    private final String vtname;
    private final String location;
    private final String city;

    public VehicleModel(int vid, String vlicense, String make, String model, int year, String color, int odometer,
                        String status, String vtname, String location, String city) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.model = model;
        this.year = year;
        this.colour = color;
        this.odometer = odometer;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}
