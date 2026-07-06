package Service;

import java.util.List;

import Repository.CustomerRepository;
import model.Customer;

public class CustomerService {

    private CustomerRepository repository;

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
        repository.addCustomer(customer);
    }

    /**
     * Mengupdate data customer.
     */
    public void updateCustomer(Customer customer) {
        repository.updateCustomer(customer);
    }

    /**
     * Menghapus customer berdasarkan ID.
     */
    public void deleteCustomer(String id) {
        repository.deleteCustomer(id);
    }

    /**
     * Mencari customer berdasarkan ID.
     */
    public Customer getCustomerById(String id) {
        return repository.getCustomerById(id);
    }

}