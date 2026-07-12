package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Laptop;
import Service.LaptopService;

public class LaptopController implements SearchableController {

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
    private Label lblTotalLaptop;

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
    private String searchKeyword = "";

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
        laptopTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadLaptopData() {
        laptopList.clear();
        for (Laptop laptop : laptopService.getAllLaptops()) {
            if (matchesSearch(laptop)) {
                laptopList.add(laptop);
            }
        }
        laptopTable.setItems(laptopList);
        if (lblTotalLaptop != null) {
            lblTotalLaptop.setText(String.valueOf(laptopService.getAllLaptops().size()));
        }
    }

    @Override
    public void setSearchKeyword(String keyword) {
        searchKeyword = keyword != null ? keyword.trim().toLowerCase() : "";
        loadLaptopData();
    }

    private boolean matchesSearch(Laptop laptop) {
        if (searchKeyword.isEmpty()) {
            return true;
        }

        return contains(laptop.getId())
                || contains(laptop.getCustomerId())
                || contains(laptop.getMerk())
                || contains(laptop.getTipe())
                || contains(laptop.getSerialNumber())
                || contains(laptop.getKeluhan());
    }

    private boolean contains(String value) {
        return value != null && value.toLowerCase().contains(searchKeyword);
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
            showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field harus diisi.");
            return null;
        }

        if (!isNumeric(id)) {
            showAlert(Alert.AlertType.ERROR, "Validasi ID", "ID Laptop harus berupa angka.");
            return null;
        }

        if (!isNumeric(customerId)) {
            showAlert(Alert.AlertType.ERROR, "Validasi ID", "ID Customer harus berupa angka.");
            return null;
        }

        return new Laptop(id, customerId, merk, tipe, serialNumber, keluhan);
    }

    private boolean isNumeric(String value) {
        return value != null && value.trim().matches("\\d+");
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

    @FXML
    private void backToDashboard(ActionEvent event) {
        openPage(event, "/view/Dashboard.fxml");
    }

    private void openPage(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1400, 800));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

