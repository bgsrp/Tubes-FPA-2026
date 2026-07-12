package model;

public class Service {

    private String id;
    private String laptopId;
    private String tanggalMasuk;
    private String status;
    private double biaya;
    private String keterangan;
    private String customer;
    private String laptop;

    public Service() {
    }

    public Service(String id, String laptopId, String tanggalMasuk, String status, double biaya) {
        this(id, laptopId, tanggalMasuk, status, biaya, "");
    }

    public Service(String id, String laptopId, String tanggalMasuk, String status, double biaya, String keterangan) {
        this.id = id;
        this.laptopId = laptopId;
        this.tanggalMasuk = tanggalMasuk;
        this.status = status;
        this.biaya = biaya;
        this.keterangan = keterangan;
    }

    public Service(String id, String laptopId, String tanggalMasuk, String status, double biaya, String customer, String laptop) {
        this(id, laptopId, tanggalMasuk, status, biaya, "");
        this.customer = customer;
        this.laptop = laptop;
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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getLaptop() {
        return laptop;
    }

    public void setLaptop(String laptop) {
        this.laptop = laptop;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", laptopId='" + laptopId + '\'' +
                ", tanggalMasuk='" + tanggalMasuk + '\'' +
                ", status='" + status + '\'' +
                ", biaya=" + biaya +
                ", keterangan='" + keterangan + '\'' +
                ", customer='" + customer + '\'' +
                ", laptop='" + laptop + '\'' +
                '}';
    }
}