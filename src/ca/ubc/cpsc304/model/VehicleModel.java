package ca.ubc.cpsc304.model;

public class VehicleModel {
    private final String vid;
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

    public VehicleModel(String vid, String vlicense, String make, String model, int year, String color, int odometer,
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

    public String getVid() {
        return vid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColour() {
        return colour;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getStatus() {
        return status;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}
