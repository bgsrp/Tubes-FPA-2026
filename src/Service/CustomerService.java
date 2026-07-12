package Service;

import java.util.List;

import Repository.CustomerRepository;
import model.Customer;

public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService() {
        repository = new CustomerRepository();
    }

    /**
     * Mengambil seluruh data customer.
     */
    public List<Customer> getAllCustomers() {
        return repository.getAllCustomers();
    }

    /**
     * Menambahkan customer baru.
     */
    public void addCustomer(Customer customer) {
        validateCustomer(customer);

        if (repository.getCustomerById(customer.getId()) != null) {
            throw new IllegalArgumentException("ID Customer sudah digunakan.");
        }

        repository.addCustomer(customer);
    }

    /**
     * Mengupdate data customer.
     */
    public void updateCustomer(Customer customer) {
        validateCustomer(customer);

        if (repository.getCustomerById(customer.getId()) == null) {
            throw new IllegalArgumentException("Data Customer tidak ditemukan.");
        }

        repository.updateCustomer(customer);
    }

    /**
     * Menghapus customer berdasarkan ID.
     */
    public void deleteCustomer(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Customer wajib diisi.");
        }

        if (repository.getCustomerById(id.trim()) == null) {
            throw new IllegalArgumentException("Data Customer tidak ditemukan.");
        }

        repository.deleteCustomer(id);
    }

    /**
     * Mencari customer berdasarkan ID.
     */
    public Customer getCustomerById(String id) {
        return repository.getCustomerById(id);
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Data Customer tidak boleh null.");
        }

        if (isBlank(customer.getId())) {
            throw new IllegalArgumentException("ID Customer wajib diisi.");
        }

        if (isBlank(customer.getNama())) {
            throw new IllegalArgumentException("Nama Customer wajib diisi.");
        }

        if (isBlank(customer.getNoHP())) {
            throw new IllegalArgumentException("Nomor HP wajib diisi.");
        }

        if (isBlank(customer.getAlamat())) {
            throw new IllegalArgumentException("Alamat wajib diisi.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
