package repository;

import model.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk akses data Service ke XML.
 * Implementasi XML akan menggunakan XMLHelper
 * yang disediakan pada modul integrasi.
 */
public class ServiceRepository {

    private final List<Service> serviceList;

    public ServiceRepository() {
        serviceList = new ArrayList<>();
        loadData();
    }

    /**
     * Mengambil seluruh data service.
     */
    public List<Service> getAllServices() {
        return new ArrayList<>(serviceList);
    }

    /**
     * Menambahkan data service.
     */
    public void addService(Service service) {
        serviceList.add(service);
        saveData();
    }

    /**
     * Mengubah data service berdasarkan id.
     */
    public void updateService(Service updatedService) {
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).getId().equals(updatedService.getId())) {
                serviceList.set(i, updatedService);
                break;
            }
        }
        saveData();
    }

    /**
     * Menghapus data service berdasarkan id.
     */
    public void deleteService(String id) {
        serviceList.removeIf(service -> service.getId().equals(id));
        saveData();
    }

    /**
     * Mencari service berdasarkan id.
     */
    public Service findById(String id) {
        for (Service service : serviceList) {
            if (service.getId().equals(id)) {
                return service;
            }
        }
        return null;
    }

    /**
     * Stub untuk membaca data dari XML.
     *
     * XMLHelper akan dibuat oleh anggota yang menangani
     * integrasi sehingga method ini sengaja dibiarkan
     * kompatibel untuk tahap integrasi.
     */
    private void loadData() {
        // TODO:
        // serviceList.clear();
        // serviceList.addAll(XMLHelper.loadServices("xml/service.xml"));
    }

    /**
     * Stub untuk menyimpan data ke XML.
     *
     * Akan dihubungkan dengan XMLHelper pada saat integrasi.
     */
    private void saveData() {
        // TODO:
        // XMLHelper.saveServices("xml/service.xml", serviceList);
    }

}