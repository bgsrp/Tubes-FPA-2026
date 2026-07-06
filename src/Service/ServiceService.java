package Service;

import model.Service;
import Repository.ServiceRepository;

import java.util.List;

public class ServiceService {

    private final ServiceRepository repository;

    public ServiceService() {
        repository = new ServiceRepository();
    }

    /**
     * Mengambil seluruh data service.
     */
    public List<Service> getAllServices() {
        return repository.getAllServices();
    }

    /**
     * Menambahkan data service.
     */
    public void addService(Service service) {

        validateService(service);

        if (repository.findById(service.getId()) != null) {
            throw new IllegalArgumentException("ID Service sudah digunakan.");
        }

        repository.addService(service);
    }

    /**
     * Memperbarui data service.
     */
    public void updateService(Service service) {

        validateService(service);

        if (repository.findById(service.getId()) == null) {
            throw new IllegalArgumentException("Data Service tidak ditemukan.");
        }

        repository.updateService(service);
    }

    /**
     * Menghapus data service.
     */
    public void deleteService(String id) {

        if (repository.findById(id) == null) {
            throw new IllegalArgumentException("Data Service tidak ditemukan.");
        }

        repository.deleteService(id);
    }

    /**
     * Mencari service berdasarkan id.
     */
    public Service findById(String id) {
        return repository.findById(id);
    }

    public double getTotalRevenue() {
        return repository.getAllServices().stream()
                .mapToDouble(Service::getBiaya)
                .sum();
    }

    public int getMonthlyService(String month) {
        if (month == null || month.trim().isEmpty()) {
            return 0;
        }
        String monthLower = month.toLowerCase();
        return (int) repository.getAllServices().stream()
                .filter(service -> {
                    String tanggal = service.getTanggalMasuk();
                    return tanggal != null && tanggal.toLowerCase().contains(monthLower);
                })
                .count();
    }

    public int getCompletedService() {
        return (int) repository.getAllServices().stream()
                .filter(service -> service.getStatus() != null
                        && service.getStatus().equalsIgnoreCase("completed"))
                .count();
    }

    public int getProgressService() {
        return (int) repository.getAllServices().stream()
                .filter(service -> service.getStatus() != null
                        && service.getStatus().toLowerCase().contains("progress"))
                .count();
    }

    public int getWaitingService() {
        return (int) repository.getAllServices().stream()
                .filter(service -> service.getStatus() != null
                        && service.getStatus().equalsIgnoreCase("waiting"))
                .count();
    }

    public List<Service> getRecentServices() {
        return repository.getAllServices();
    }

    /**
     * Validasi business rule sederhana.
     */
    private void validateService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException("Data Service tidak boleh null.");
        }

        if (service.getId() == null || service.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("ID Service wajib diisi.");
        }

        if (service.getLaptopId() == null || service.getLaptopId().trim().isEmpty()) {
            throw new IllegalArgumentException("ID Laptop wajib diisi.");
        }

        if (service.getTanggalMasuk() == null || service.getTanggalMasuk().trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal Masuk wajib diisi.");
        }

        if (service.getStatus() == null || service.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status wajib diisi.");
        }

        if (service.getBiaya() < 0) {
            throw new IllegalArgumentException("Biaya tidak boleh bernilai negatif.");
        }
    }

}