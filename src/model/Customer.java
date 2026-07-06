package model;

public class Customer {

    private String id;
    private String nama;
    private String noHP;
    private String alamat;

    // Constructor kosong
    public Customer() {
    }

    // Constructor dengan parameter
    public Customer(String id, String nama, String noHP, String alamat) {
        this.id = id;
        this.nama = nama;
        this.noHP = noHP;
        this.alamat = alamat;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        return id + " - " + nama;
    }
}