package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Service;
import service.ServiceService;
import util.QueueManager;
import util.StackManager;

public class ServiceController {

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
    private TableColumn<Service, Double> colBiaya;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLaptopId;

    @FXML
    private TextField txtTanggalMasuk;

    @FXML
    private TextField txtStatus;

    @FXML
    private TextField txtBiaya;

    private final ServiceService serviceService = new ServiceService();

    // Stub kompatibel dengan modul util
    private final QueueManager queueManager = new QueueManager();
    private final StackManager stackManager = new StackManager();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLaptopId.setCellValueFactory(new PropertyValueFactory<>("laptopId"));
        colTanggalMasuk.setCellValueFactory(new PropertyValueFactory<>("tanggalMasuk"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colBiaya.setCellValueFactory(new PropertyValueFactory<>("biaya"));

        loadTable();

        serviceTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> {
                    if (selected != null) {
                        txtId.setText(selected.getId());
                        txtLaptopId.setText(selected.getLaptopId());
                        txtTanggalMasuk.setText(selected.getTanggalMasuk());
                        txtStatus.setText(selected.getStatus());
                        txtBiaya.setText(String.valueOf(selected.getBiaya()));
                    }
                }
        );
    }

    @FXML
    private void addService() {

        try {

            Service service = createServiceFromInput();

            serviceService.addService(service);

            // Stub antrean
            queueManager.enqueue(service);

            loadTable();
            clearForm();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void updateService() {

        try {

            Service service = createServiceFromInput();

            serviceService.updateService(service);

            // Stub riwayat status
            stackManager.push(service);

            loadTable();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void deleteService() {

        try {

            String id = txtId.getText();

            serviceService.deleteService(id);

            loadTable();
            clearForm();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void clearForm() {

        txtId.clear();
        txtLaptopId.clear();
        txtTanggalMasuk.clear();
        txtStatus.clear();
        txtBiaya.clear();

        serviceTable.getSelectionModel().clearSelection();
    }

    private void loadTable() {

        ObservableList<Service> data =
                FXCollections.observableArrayList(serviceService.getAllServices());

        serviceTable.setItems(data);
    }

    private Service createServiceFromInput() {

        return new Service(
                txtId.getText(),
                txtLaptopId.getText(),
                txtTanggalMasuk.getText(),
                txtStatus.getText(),
                Double.parseDouble(txtBiaya.getText())
        );
    }

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}