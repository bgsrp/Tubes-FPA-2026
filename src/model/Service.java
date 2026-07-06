package model;

public class Service {

    private String id;
    private String laptopId;
    private String tanggalMasuk;
    private String status;
    private double biaya;

    public Service() {
    }

    public Service(String id, String laptopId, String tanggalMasuk, String status, double biaya) {
        this.id = id;
        this.laptopId = laptopId;
        this.tanggalMasuk = tanggalMasuk;
        this.status = status;
        this.biaya = biaya;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaptopId() {
        return laptopId;
    }

    public void setLaptopId(String laptopId) {
        this.laptopId = laptopId;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(String tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", laptopId='" + laptopId + '\'' +
                ", tanggalMasuk='" + tanggalMasuk + '\'' +
                ", status='" + status + '\'' +
                ", biaya=" + biaya +
                '}';
    }
}