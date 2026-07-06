package controller;

import Service.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtNoHP;

    @FXML
    private TextField txtAlamat;

    @FXML
    private Button btnTambah;

    @FXML
    private Button btnUbah;

    @FXML
    private Button btnHapus;

    @FXML
    private Button btnClear;

    @FXML
    private TableView<Customer> tableCustomer;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    private TableColumn<Customer, String> colNama;

    @FXML
    private TableColumn<Customer, String> colNoHP;

    @FXML
    private TableColumn<Customer, String> colAlamat;

    private CustomerService customerService;
    private ObservableList<Customer> customerList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customerService = new CustomerService();

        customerList = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNoHP.setCellValueFactory(new PropertyValueFactory<>("noHP"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));

        loadCustomerData();

        tableCustomer.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedCustomer) -> {

                    if (selectedCustomer != null) {

                        txtId.setText(selectedCustomer.getId());
                        txtNama.setText(selectedCustomer.getNama());
                        txtNoHP.setText(selectedCustomer.getNoHP());
                        txtAlamat.setText(selectedCustomer.getAlamat());

                    }

                });
    }

    private void loadCustomerData() {

        customerList.clear();

        customerList.addAll(customerService.getAllCustomers());

        tableCustomer.setItems(customerList);

    }

    @FXML
    private void handleTambah() {

        Customer customer = new Customer(
                txtId.getText(),
                txtNama.getText(),
                txtNoHP.getText(),
                txtAlamat.getText()
        );

        customerService.addCustomer(customer);

        loadCustomerData();

        clearForm();

    }

    @FXML
    private void handleUbah() {

        Customer customer = new Customer(
                txtId.getText(),
                txtNama.getText(),
                txtNoHP.getText(),
                txtAlamat.getText()
        );

        customerService.updateCustomer(customer);

        loadCustomerData();

        clearForm();

    }

    @FXML
    private void handleHapus() {

        if (txtId.getText().isEmpty()) {
            return;
        }

        customerService.deleteCustomer(txtId.getText());

        loadCustomerData();

        clearForm();

    }

    @FXML
    private void handleClear() {

        clearForm();

    }

    private void clearForm() {

        txtId.clear();
        txtNama.clear();
        txtNoHP.clear();
        txtAlamat.clear();

        tableCustomer.getSelectionModel().clearSelection();

    }

}