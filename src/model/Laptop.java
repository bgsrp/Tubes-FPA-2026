package model;

public class Laptop {
    private String id;
    private String customerId;
    private String merk;
    private String tipe;
    private String serialNumber;
    private String keluhan;

    public Laptop() {
    }

    public Laptop(String id, String customerId, String merk, String tipe, String serialNumber, String keluhan) {
        this.id = id;
        this.customerId = customerId;
        this.merk = merk;
        this.tipe = tipe;
        this.serialNumber = serialNumber;
        this.keluhan = keluhan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", merk='" + merk + '\'' +
                ", tipe='" + tipe + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", keluhan='" + keluhan + '\'' +
                '}';
    }
}
```
