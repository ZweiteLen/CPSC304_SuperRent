package ca.ubc.cpsc304.model;

public class VehicleTypeModel {
    private final String vtname;
    private final String features;
    private final int wrate;
    private final int drate;
    private final int hrate;
    private final int wirate;
    private final int hirate;
    private final int dirate;
    private final int krate;

    public VehicleTypeModel (String vtname, String features, int wrate, int drate, int hrate, int wirate, int dirate,
                             int hirate, int krate) {
        this.vtname = vtname;
        this.features = features;
        this.wrate = wrate;
        this.hrate = hrate;
        this.drate = drate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.hirate = hirate;
        this.krate = krate;
    }

    public String getVtname() {
        return vtname;
    }

    public String getFeatures() {
        return features;
    }

    public int getWrate() {
        return wrate;
    }

    public int getDrate() {
        return drate;
    }

    public int getHrate() {
        return hrate;
    }

    public int getWirate() {
        return wirate;
    }

    public int getDirate() {
        return dirate;
    }

    public int getHirate() {
        return hirate;
    }

    public int getKrate() {
        return krate;
    }
}
