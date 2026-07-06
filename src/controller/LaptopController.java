package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Laptop;
import service.LaptopService;

public class LaptopController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtMerk;

    @FXML
    private TextField txtTipe;

    @FXML
    private TextField txtSerialNumber;

    @FXML
    private TextArea txtKeluhan;

    @FXML
    private TableView<Laptop> laptopTable;

    @FXML
    private TableColumn<Laptop, String> colId;

    @FXML
    private TableColumn<Laptop, String> colCustomerId;

    @FXML
    private TableColumn<Laptop, String> colMerk;

    @FXML
    private TableColumn<Laptop, String> colTipe;

    @FXML
    private TableColumn<Laptop, String> colSerialNumber;

    @FXML
    private TableColumn<Laptop, String> colKeluhan;

    private final LaptopService laptopService = new LaptopService();
    private final ObservableList<Laptop> laptopList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadLaptopData();
        setupTableSelection();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colCustomerId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCustomerId()));
        colMerk.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMerk()));
        colTipe.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipe()));
        colSerialNumber.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSerialNumber()));
        colKeluhan.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKeluhan()));
    }

    private void loadLaptopData() {
        laptopList.clear();
        laptopList.addAll(laptopService.getAllLaptops());
        laptopTable.setItems(laptopList);
    }

    private void setupTableSelection() {
        laptopTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedLaptop) -> {
            if (selectedLaptop != null) {
                txtId.setText(selectedLaptop.getId());
                txtCustomerId.setText(selectedLaptop.getCustomerId());
                txtMerk.setText(selectedLaptop.getMerk());
                txtTipe.setText(selectedLaptop.getTipe());
                txtSerialNumber.setText(selectedLaptop.getSerialNumber());
                txtKeluhan.setText(selectedLaptop.getKeluhan());
            }
        });
    }

    @FXML
    private void handleAddLaptop() {
        Laptop laptop = getLaptopFromForm();

        if (laptop == null) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field harus diisi.");
            return;
        }

        boolean success = laptopService.addLaptop(laptop);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data laptop berhasil ditambahkan.");
            loadLaptopData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Data laptop gagal ditambahkan. Pastikan ID belum digunakan dan semua field terisi.");
        }
    }

    @FXML
    private void handleUpdateLaptop() {
        Laptop laptop = getLaptopFromForm();

        if (laptop == null) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field harus diisi.");
            return;
        }

        boolean success = laptopService.updateLaptop(laptop);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data laptop berhasil diperbarui.");
            loadLaptopData();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Data laptop gagal diperbarui. Pastikan ID laptop sudah ada.");
        }
    }

    @FXML
    private void handleDeleteLaptop() {
        String id = txtId.getText();

        if (id == null || id.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Pilih atau masukkan ID laptop yang ingin dihapus.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Apakah Anda yakin ingin menghapus data laptop dengan ID " + id + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean success = laptopService.deleteLaptop(id);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data laptop berhasil dihapus.");
                loadLaptopData();
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Data laptop tidak ditemukan atau gagal dihapus.");
            }
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
        laptopTable.getSelectionModel().clearSelection();
    }

    private Laptop getLaptopFromForm() {
        String id = txtId.getText() != null ? txtId.getText().trim() : "";
        String customerId = txtCustomerId.getText() != null ? txtCustomerId.getText().trim() : "";
        String merk = txtMerk.getText() != null ? txtMerk.getText().trim() : "";
        String tipe = txtTipe.getText() != null ? txtTipe.getText().trim() : "";
        String serialNumber = txtSerialNumber.getText() != null ? txtSerialNumber.getText().trim() : "";
        String keluhan = txtKeluhan.getText() != null ? txtKeluhan.getText().trim() : "";

        if (id.isEmpty() || customerId.isEmpty() || merk.isEmpty() || tipe.isEmpty()
                || serialNumber.isEmpty() || keluhan.isEmpty()) {
            return null;
        }

        return new Laptop(id, customerId, merk, tipe, serialNumber, keluhan);
    }

    private void clearForm() {
        txtId.clear();
        txtCustomerId.clear();
        txtMerk.clear();
        txtTipe.clear();
        txtSerialNumber.clear();
        txtKeluhan.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
```
