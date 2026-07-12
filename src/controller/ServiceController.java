package controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import model.Service;
import Service.ServiceService;

public class ServiceController implements SearchableController {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    private static final Map<String, String> STATUS_LABELS = new LinkedHashMap<>();

    static {
        CURRENCY_FORMAT.setMaximumFractionDigits(0);

        STATUS_LABELS.put(ServiceService.STATUS_WAITING, "Menunggu");
        STATUS_LABELS.put(ServiceService.STATUS_IN_PROGRESS, "Dalam Proses");
        STATUS_LABELS.put(ServiceService.STATUS_COMPLETED, "Selesai");
    }

    @FXML
    private TableView<Service> serviceTable;

    @FXML
    private TableColumn<Service, String> colId;

    @FXML
    private TableColumn<Service, String> colLaptopId;

    @FXML
    private TableColumn<Service, String> colTanggalMasuk;

    @FXML
    private TableColumn<Service, String> colStatus;

    @FXML
    private TableColumn<Service, String> colKeterangan;

    @FXML
    private TableColumn<Service, Double> colBiaya;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLaptopId;

    @FXML
    private DatePicker dateTanggalMasuk;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TextField txtBiaya;

    @FXML
    private TextField txtKeterangan;

    @FXML
    private Button btnFilterAll;

    @FXML
    private Button btnFilterWaiting;

    @FXML
    private Button btnFilterInProgress;

    @FXML
    private Button btnFilterCompleted;

    @FXML
    private Label lblStatSemua;

    @FXML
    private Label lblStatMenunggu;

    @FXML
    private Label lblStatDalamProses;

    @FXML
    private Label lblStatSelesai;

    @FXML
    private Label lblPaginationInfo;

    private final ServiceService serviceService = new ServiceService();
    private final ObservableList<Service> serviceList = FXCollections.observableArrayList();
    private String searchKeyword = "";
    private String statusFilter = null;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLaptopId.setCellValueFactory(new PropertyValueFactory<>("laptopId"));
        colTanggalMasuk.setCellValueFactory(new PropertyValueFactory<>("tanggalMasuk"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colBiaya.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        serviceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colStatus.setCellFactory(column -> new StatusBadgeCell());
        colBiaya.setCellFactory(column -> new CurrencyCell());

        cmbStatus.setItems(FXCollections.observableArrayList(STATUS_LABELS.values()));

        loadTable();
        updateFilterButtonStyles();

        serviceTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> {
                    if (selected != null) {
                        txtId.setText(selected.getId());
                        txtLaptopId.setText(selected.getLaptopId());
                        dateTanggalMasuk.setValue(parseDate(selected.getTanggalMasuk()));
                        cmbStatus.setValue(toDisplayStatus(selected.getStatus()));
                        txtBiaya.setText(String.valueOf(selected.getBiaya()));
                        txtKeterangan.setText(selected.getKeterangan());
                    }
                }
        );
    }

    @FXML
    private void addService() {

        try {

            Service service = createServiceFromInput();

            serviceService.addService(service);

            loadTable();
            clearForm();
            showInfo("Sukses", "Data service berhasil ditambahkan.");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateService() {

        try {

            Service service = createServiceFromInput();

            serviceService.updateService(service);

            loadTable();
            selectService(service.getId());
            showInfo("Sukses", "Data service berhasil diperbarui.");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void deleteService() {

        try {

            String id = txtId.getText();

            if (id == null || id.trim().isEmpty()) {
                showError("Pilih atau masukkan ID service yang ingin dihapus.");
                return;
            }

            if (!isNumeric(id)) {
                showError("ID Service harus berupa angka.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText(null);
            confirm.setContentText("Apakah Anda yakin ingin menghapus data service dengan ID " + id + "?");

            if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }

            serviceService.deleteService(id);

            loadTable();
            clearForm();
            showInfo("Sukses", "Data service berhasil dihapus.");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void clearForm() {

        txtId.clear();
        txtLaptopId.clear();
        dateTanggalMasuk.setValue(null);
        cmbStatus.setValue(null);
        txtBiaya.clear();
        txtKeterangan.clear();

        serviceTable.getSelectionModel().clearSelection();
    }

    private void loadTable() {

        List<Service> allServices = serviceService.getAllServices();

        List<Service> filteredServices = allServices.stream()
                .filter(this::matchesSearch)
                .filter(this::matchesStatusFilter)
                .collect(Collectors.toList());

        serviceList.setAll(filteredServices);
        serviceTable.setItems(serviceList);

        if (isFilterActive()) {
            lblPaginationInfo.setText("Menampilkan " + serviceList.size() + " dari " + allServices.size() + " entri");
        } else {
            lblPaginationInfo.setText("Menampilkan " + serviceList.size() + " entri");
        }

        refreshStats();
    }

    private boolean isFilterActive() {
        return statusFilter != null || !searchKeyword.isEmpty();
    }

    @Override
    public void setSearchKeyword(String keyword) {

        searchKeyword = keyword != null ? keyword.trim().toLowerCase() : "";

        loadTable();

    }

    private boolean matchesSearch(Service service) {

        if (searchKeyword.isEmpty()) {
            return true;
        }

        return contains(service.getId())
                || contains(service.getLaptopId())
                || contains(service.getTanggalMasuk())
                || contains(service.getStatus())
                || contains(String.valueOf(service.getBiaya()))
                || contains(service.getKeterangan());

    }

    private boolean contains(String value) {

        return value != null && value.toLowerCase().contains(searchKeyword);

    }

    private boolean matchesStatusFilter(Service service) {

        return statusFilter == null || statusFilter.equals(service.getStatus());

    }

    @FXML
    private void filterWaiting() {
        toggleStatusFilter("waiting");
    }

    @FXML
    private void filterInProgress() {
        toggleStatusFilter("in progress");
    }

    @FXML
    private void filterCompleted() {
        toggleStatusFilter("completed");
    }

    private void toggleStatusFilter(String status) {

        statusFilter = status.equals(statusFilter) ? null : status;

        loadTable();
        updateFilterButtonStyles();

    }

    private void updateFilterButtonStyles() {

        setFilterButtonActive(btnFilterAll, statusFilter == null);
        setFilterButtonActive(btnFilterWaiting, "waiting".equals(statusFilter));
        setFilterButtonActive(btnFilterInProgress, "in progress".equals(statusFilter));
        setFilterButtonActive(btnFilterCompleted, "completed".equals(statusFilter));

    }

    private void setFilterButtonActive(Button button, boolean active) {

        button.getStyleClass().remove("filter-tab-active");

        if (active) {
            button.getStyleClass().add("filter-tab-active");
        }

    }

    private Service createServiceFromInput() {

        if (!isNumeric(txtId.getText())) {
            throw new IllegalArgumentException("ID Service harus berupa angka.");
        }

        if (!isNumeric(txtLaptopId.getText())) {
            throw new IllegalArgumentException("ID Laptop harus berupa angka.");
        }

        if (txtBiaya.getText() == null || !txtBiaya.getText().trim().matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Biaya harus berupa angka.");
        }

        return new Service(
                txtId.getText().trim(),
                txtLaptopId.getText().trim(),
                getSelectedDate(),
                toStoredStatus(cmbStatus.getValue()),
                Double.parseDouble(txtBiaya.getText().trim()),
                txtKeterangan.getText() != null ? txtKeterangan.getText().trim() : ""
        );
    }

    // =========================
    // STATUS LABEL MAPPING
    // =========================

    private String toDisplayStatus(String status) {

        if (status == null) {
            return null;
        }

        return STATUS_LABELS.getOrDefault(status.trim().toLowerCase(Locale.ROOT), status);

    }

    private String toStoredStatus(String displayStatus) {

        if (displayStatus == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : STATUS_LABELS.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(displayStatus.trim())) {
                return entry.getKey();
            }
        }

        return displayStatus;

    }

    @FXML
    private void filterAll() {
        statusFilter = null;
        loadTable();
        updateFilterButtonStyles();
    }

    // =========================
    // STATS
    // =========================

    private void refreshStats() {

        lblStatSemua.setText(String.valueOf(serviceService.getAllServices().size()));
        lblStatMenunggu.setText(String.valueOf(serviceService.getWaitingService()));
        lblStatDalamProses.setText(String.valueOf(serviceService.getProgressService()));
        lblStatSelesai.setText(String.valueOf(serviceService.getCompletedService()));

    }

    private void selectService(String id) {

        for (Service service : serviceList) {
            if (service.getId() != null && service.getId().equals(id)) {
                serviceTable.getSelectionModel().select(service);
                serviceTable.scrollTo(service);
                break;
            }
        }
    }

    private boolean isNumeric(String value) {

        return value != null && value.trim().matches("\\d+");

    }

    private String getSelectedDate() {
        LocalDate selectedDate = dateTanggalMasuk.getValue();

        if (selectedDate == null) {
            return "";
        }

        return selectedDate.toString();
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

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    private void showInfo(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

    // =========================
    // CUSTOM CELLS
    // =========================

    private static class StatusBadgeCell extends TableCell<Service, String> {

        @Override
        protected void updateItem(String status, boolean empty) {

            super.updateItem(status, empty);

            if (empty || status == null || status.trim().isEmpty()) {
                setGraphic(null);
                setText(null);
                return;
            }

            String normalized = status.trim().toLowerCase(Locale.ROOT);
            String label = STATUS_LABELS.getOrDefault(normalized, status).toUpperCase(Locale.ROOT);

            Label badge = new Label(label);
            badge.getStyleClass().add("status-badge");

            Circle dot = new Circle(4);

            if (normalized.equals(ServiceService.STATUS_WAITING)) {
                badge.getStyleClass().add("badge-waiting");
                dot.getStyleClass().add("dot-waiting");
            } else if (normalized.equals(ServiceService.STATUS_IN_PROGRESS)) {
                badge.getStyleClass().add("badge-progress");
                dot.getStyleClass().add("dot-progress");
            } else if (normalized.equals(ServiceService.STATUS_COMPLETED)) {
                badge.getStyleClass().add("badge-completed");
                dot.getStyleClass().add("dot-completed");
            }

            badge.setGraphic(dot);
            badge.setContentDisplay(ContentDisplay.LEFT);
            badge.setGraphicTextGap(6);

            setText(null);
            setGraphic(badge);
            setAlignment(Pos.CENTER);

        }

    }

    private static class CurrencyCell extends TableCell<Service, Double> {

        @Override
        protected void updateItem(Double biaya, boolean empty) {

            super.updateItem(biaya, empty);

            if (empty || biaya == null) {
                setText(null);
            } else {
                setText("Rp " + CURRENCY_FORMAT.format(biaya));
            }

            setAlignment(Pos.CENTER_RIGHT);

        }

    }

}
