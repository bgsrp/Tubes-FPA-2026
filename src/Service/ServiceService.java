package Service;

import model.Service;
import Repository.ServiceRepository;
import Repository.LaptopRepository;
import Repository.CustomerRepository;
import model.Laptop;
import model.Customer;
import util.QueueManager;
import util.StackManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceService {

    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_IN_PROGRESS = "in progress";
    public static final String STATUS_COMPLETED = "completed";

    private static final List<String> VALID_STATUSES = Arrays.asList(
            STATUS_WAITING,
            STATUS_IN_PROGRESS,
            STATUS_COMPLETED
    );

    private static final QueueManager queueManager = new QueueManager();
    private static final StackManager stackManager = new StackManager();

    private final ServiceRepository repository;
    private final LaptopRepository laptopRepository;
    private final CustomerRepository customerRepository;

    public ServiceService() {
        repository = new ServiceRepository();
        laptopRepository = new LaptopRepository();
        customerRepository = new CustomerRepository();
        refreshQueue();
    }

    /**
     * Mengambil seluruh data service.
     */
    public List<Service> getAllServices() {
        return repository.getAllServices();
    }

    /**
     * Mengambil seluruh data service dengan informasi customer dan laptop.
     */
    public List<Service> getAllServicesWithDetails() {
        return repository.getAllServices().stream()
                .map(service -> enrichServiceWithDetails(copyOf(service)))
                .collect(Collectors.toList());
    }

    /**
     * Enrich service data dengan customer dan laptop details
     */
    private Service enrichServiceWithDetails(Service service) {
        if (service == null) {
            return service;
        }

        Laptop laptop = laptopRepository.getLaptopById(service.getLaptopId());
        String customerName = "";
        String laptopInfo = "";

        if (laptop != null) {
            Customer customer = customerRepository.getCustomerById(laptop.getCustomerId());
            if (customer != null) {
                customerName = customer.getNama();
            }
            laptopInfo = laptop.getMerk() + " " + laptop.getTipe();
        }

        service.setCustomer(customerName);
        service.setLaptop(laptopInfo);

        return service;
    }

    /**
     * Menambahkan data service.
     */
    public void addService(Service service) {

        service = normalizeService(service);

        validateService(service);

        if (repository.findById(service.getId()) != null) {
            throw new IllegalArgumentException("ID Service sudah digunakan.");
        }

        repository.addService(service);
        refreshQueue();
    }

    /**
     * Memperbarui data service.
     */
    public void updateService(Service service) {

        service = normalizeService(service);

        validateService(service);

        Service existingService = repository.findById(service.getId());

        if (existingService == null) {
            throw new IllegalArgumentException("Data Service tidak ditemukan.");
        }

        if (isStatusChanged(existingService, service)) {
            stackManager.push(copyOf(existingService));
        }

        repository.updateService(service);
        refreshQueue();
    }

    /**
     * Menghapus data service.
     */
    public void deleteService(String id) {

        id = clean(id);

        if (id.isEmpty()) {
            throw new IllegalArgumentException("ID Service wajib diisi.");
        }

        if (repository.findById(id) == null) {
            throw new IllegalArgumentException("Data Service tidak ditemukan.");
        }

        repository.deleteService(id);
        refreshQueue();
    }

    /**
     * Mencari service berdasarkan id.
     */
    public Service findById(String id) {
        return repository.findById(clean(id));
    }

    /**
     * Mengambil snapshot antrean servis berstatus waiting.
     */
    public List<Service> getQueueSnapshot() {
        refreshQueue();

        return queueManager.getQueue().stream()
                .map(service -> enrichServiceWithDetails(copyOf(service)))
                .collect(Collectors.toList());
    }

    /**
     * Mengambil snapshot riwayat perubahan status.
     */
    public List<Service> getStatusHistorySnapshot() {
        return stackManager.getStack().stream()
                .map(service -> enrichServiceWithDetails(copyOf(service)))
                .collect(Collectors.toList());
    }

    /**
     * Mengubah service pertama dalam antrean menjadi in progress.
     */
    public Service processNextService() {
        refreshQueue();

        Service nextService = queueManager.dequeue();

        if (nextService == null) {
            throw new IllegalArgumentException("Tidak ada antrean service yang menunggu.");
        }

        Service updatedService = copyOf(nextService);
        updatedService.setStatus(STATUS_IN_PROGRESS);
        updateService(updatedService);

        return enrichServiceWithDetails(copyOf(updatedService));
    }

    /**
     * Menandai service tertentu sebagai completed.
     */
    public Service completeService(String id) {
        Service service = repository.findById(clean(id));

        if (service == null) {
            throw new IllegalArgumentException("Data Service tidak ditemukan.");
        }

        Service updatedService = copyOf(service);
        updatedService.setStatus(STATUS_COMPLETED);
        updateService(updatedService);

        return enrichServiceWithDetails(copyOf(updatedService));
    }

    /**
     * Membatalkan perubahan status terakhir dari stack.
     */
    public Service undoLastStatusChange() {
        Service previousService = stackManager.pop();

        if (previousService == null) {
            throw new IllegalArgumentException("Belum ada riwayat status untuk di-undo.");
        }

        if (repository.findById(previousService.getId()) == null) {
            throw new IllegalArgumentException("Service pada riwayat sudah tidak ada di XML.");
        }

        repository.updateService(previousService);
        refreshQueue();

        return enrichServiceWithDetails(copyOf(previousService));
    }

    public double getTotalRevenue() {
        return repository.getAllServices().stream()
                .filter(service -> STATUS_COMPLETED.equalsIgnoreCase(clean(service.getStatus())))
                .mapToDouble(Service::getBiaya)
                .sum();
    }

    public int getMonthlyService(String month) {
        if (month == null || month.trim().isEmpty()) {
            return 0;
        }

        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("jan", 1); monthMap.put("january", 1); monthMap.put("01", 1);
        monthMap.put("feb", 2); monthMap.put("february", 2); monthMap.put("02", 2);
        monthMap.put("mar", 3); monthMap.put("march", 3); monthMap.put("03", 3);
        monthMap.put("apr", 4); monthMap.put("april", 4); monthMap.put("04", 4);
        monthMap.put("mei", 5); monthMap.put("may", 5); monthMap.put("05", 5);
        monthMap.put("jun", 6); monthMap.put("june", 6); monthMap.put("06", 6);
        monthMap.put("jul", 7); monthMap.put("july", 7); monthMap.put("07", 7);
        monthMap.put("agu", 8); monthMap.put("aug", 8); monthMap.put("august", 8); monthMap.put("08", 8);
        monthMap.put("sep", 9); monthMap.put("september", 9); monthMap.put("09", 9);
        monthMap.put("okt", 10); monthMap.put("oct", 10); monthMap.put("october", 10); monthMap.put("10", 10);
        monthMap.put("nov", 11); monthMap.put("november", 11); monthMap.put("11", 11);
        monthMap.put("des", 12); monthMap.put("dec", 12); monthMap.put("december", 12); monthMap.put("12", 12);

        Integer targetMonth = monthMap.get(month.trim().toLowerCase(Locale.ROOT));
        if (targetMonth == null) {
            return 0;
        }

        return (int) repository.getAllServices().stream()
                .filter(service -> parseMonth(service.getTanggalMasuk()) == targetMonth)
                .count();
    }

    private int parseMonth(String tanggal) {
        if (tanggal == null || tanggal.trim().isEmpty()) {
            return -1;
        }

        String[] parts = tanggal.trim().split("[-/]");
        if (parts.length != 3) {
            return -1;
        }

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int getCompletedService() {
        return (int) repository.getAllServices().stream()
                .filter(service -> service.getStatus() != null
                        && STATUS_COMPLETED.equalsIgnoreCase(clean(service.getStatus())))
                .count();
    }

    public int getProgressService() {
        return (int) repository.getAllServices().stream()
                .filter(service -> service.getStatus() != null
                        && STATUS_IN_PROGRESS.equalsIgnoreCase(clean(service.getStatus())))
                .count();
    }

    public int getWaitingService() {
        return (int) repository.getAllServices().stream()
                .filter(service -> service.getStatus() != null
                        && STATUS_WAITING.equalsIgnoreCase(clean(service.getStatus())))
                .count();
    }

    public List<Service> getRecentServices() {
        return getAllServicesWithDetails().stream()
                .sorted(Comparator
                        .comparing((Service service) -> parseDate(service.getTanggalMasuk()),
                                Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(service -> parseInteger(service.getId()), Comparator.reverseOrder()))
                .collect(Collectors.toList());
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

        if (!isInteger(service.getId())) {
            throw new IllegalArgumentException("ID Service harus berupa angka.");
        }

        if (service.getLaptopId() == null || service.getLaptopId().trim().isEmpty()) {
            throw new IllegalArgumentException("ID Laptop wajib diisi.");
        }

        if (!isInteger(service.getLaptopId())) {
            throw new IllegalArgumentException("ID Laptop harus berupa angka.");
        }

        if (laptopRepository.getLaptopById(service.getLaptopId()) == null) {
            throw new IllegalArgumentException("ID Laptop tidak ditemukan.");
        }

        if (service.getTanggalMasuk() == null || service.getTanggalMasuk().trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal Masuk wajib diisi.");
        }

        if (parseDate(service.getTanggalMasuk()) == null) {
            throw new IllegalArgumentException("Tanggal Masuk tidak valid.");
        }

        if (service.getStatus() == null || service.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status wajib diisi.");
        }

        if (!VALID_STATUSES.contains(clean(service.getStatus()).toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Status harus waiting, in progress, atau completed.");
        }

        if (service.getBiaya() < 0) {
            throw new IllegalArgumentException("Biaya tidak boleh bernilai negatif.");
        }
    }

    private void refreshQueue() {
        List<Service> waitingServices = repository.getAllServices().stream()
                .filter(service -> STATUS_WAITING.equalsIgnoreCase(clean(service.getStatus())))
                .sorted(Comparator
                        .comparing((Service service) -> parseDate(service.getTanggalMasuk()),
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(service -> parseInteger(service.getId())))
                .map(this::copyOf)
                .collect(Collectors.toList());

        queueManager.load(waitingServices);
    }

    private boolean isStatusChanged(Service oldService, Service newService) {
        return !clean(oldService.getStatus()).equalsIgnoreCase(clean(newService.getStatus()));
    }

    private Service normalizeService(Service service) {
        if (service == null) {
            return null;
        }

        Service normalizedService = copyOf(service);
        normalizedService.setId(clean(service.getId()));
        normalizedService.setLaptopId(clean(service.getLaptopId()));
        normalizedService.setTanggalMasuk(clean(service.getTanggalMasuk()));
        normalizedService.setStatus(normalizeStatus(service.getStatus()));
        normalizedService.setKeterangan(clean(service.getKeterangan()));

        return normalizedService;
    }

    private String normalizeStatus(String status) {
        String value = clean(status).toLowerCase(Locale.ROOT);

        if ("menunggu".equals(value)) {
            return STATUS_WAITING;
        }

        if ("diproses".equals(value)) {
            return STATUS_IN_PROGRESS;
        }

        if ("selesai".equals(value)) {
            return STATUS_COMPLETED;
        }

        return value;
    }

    private Service copyOf(Service service) {
        if (service == null) {
            return null;
        }

        Service copy = new Service(
                service.getId(),
                service.getLaptopId(),
                service.getTanggalMasuk(),
                service.getStatus(),
                service.getBiaya(),
                service.getKeterangan()
        );
        copy.setCustomer(service.getCustomer());
        copy.setLaptop(service.getLaptop());

        return copy;
    }

    private LocalDate parseDate(String tanggal) {
        if (tanggal == null || tanggal.trim().isEmpty()) {
            return null;
        }

        String[] parts = tanggal.trim().split("[-/]");
        if (parts.length != 3) {
            return null;
        }

        try {
            if (parts[0].length() == 4) {
                return LocalDate.of(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                );
            }

            return LocalDate.of(
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[0])
            );
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(clean(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(clean(value));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String clean(String value) {
        return value != null ? value.trim() : "";
    }

}
