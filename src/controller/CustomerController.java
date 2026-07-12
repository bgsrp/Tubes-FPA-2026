package controller;

import Service.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable, SearchableController {

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
    private Label lblTotalCustomer;

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
    private String searchKeyword = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customerService = new CustomerService();

        customerList = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNoHP.setCellValueFactory(new PropertyValueFactory<>("noHP"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        tableCustomer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        for (Customer customer : customerService.getAllCustomers()) {
            if (matchesSearch(customer)) {
                customerList.add(customer);
            }
        }

        tableCustomer.setItems(customerList);

        if (lblTotalCustomer != null) {
            lblTotalCustomer.setText(String.valueOf(customerService.getAllCustomers().size()));
        }

    }

    @Override
    public void setSearchKeyword(String keyword) {

        searchKeyword = keyword != null ? keyword.trim().toLowerCase() : "";

        loadCustomerData();

    }

    private boolean matchesSearch(Customer customer) {

        if (searchKeyword.isEmpty()) {
            return true;
        }

        return contains(customer.getId())
                || contains(customer.getNama())
                || contains(customer.getNoHP())
                || contains(customer.getAlamat());

    }

    private boolean contains(String value) {

        return value != null && value.toLowerCase().contains(searchKeyword);

    }

    @FXML
    private void handleTambah() {

        try {

            if (!isNumeric(txtId.getText())) {
                showAlert(Alert.AlertType.ERROR, "Validasi ID", "ID Customer harus berupa angka.");
                return;
            }

            Customer customer = new Customer(
                    txtId.getText().trim(),
                    txtNama.getText().trim(),
                    txtNoHP.getText().trim(),
                    txtAlamat.getText().trim()
            );

            customerService.addCustomer(customer);

            loadCustomerData();

            clearForm();

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data customer berhasil ditambahkan.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal", e.getMessage());
        }

    }

    @FXML
    private void handleUbah() {

        try {

            if (!isNumeric(txtId.getText())) {
                showAlert(Alert.AlertType.ERROR, "Validasi ID", "ID Customer harus berupa angka.");
                return;
            }

            Customer customer = new Customer(
                    txtId.getText().trim(),
                    txtNama.getText().trim(),
                    txtNoHP.getText().trim(),
                    txtAlamat.getText().trim()
            );

            customerService.updateCustomer(customer);

            loadCustomerData();

            clearForm();

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data customer berhasil diperbarui.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal", e.getMessage());
        }

    }

    @FXML
    private void handleHapus() {

        try {

            if (txtId.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validasi", "Pilih atau masukkan ID customer yang ingin dihapus.");
                return;
            }

            if (!isNumeric(txtId.getText())) {
                showAlert(Alert.AlertType.ERROR, "Validasi ID", "ID Customer harus berupa angka.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText(null);
            confirm.setContentText("Apakah Anda yakin ingin menghapus data customer dengan ID " + txtId.getText() + "?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                customerService.deleteCustomer(txtId.getText().trim());

                loadCustomerData();

                clearForm();

                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data customer berhasil dihapus.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gagal", e.getMessage());
        }

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

    private boolean isNumeric(String value) {

        return value != null && value.trim().matches("\\d+");

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    @FXML
    private void backToDashboard(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1400, 800));
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
