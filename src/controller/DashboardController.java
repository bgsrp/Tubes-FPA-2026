package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Service;

import Service.CustomerService;
import Service.LaptopService;
import Service.ServiceService;

public class DashboardController implements Initializable {

    // =========================
    // SIDEBAR
    // =========================

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnLaptop;

    @FXML
    private Button btnService;

    @FXML
    private Button btnLogout;

    // =========================
    // HEADER
    // =========================

    @FXML
    private TextField txtSearch;

    // =========================
    // STATISTIC LABEL
    // =========================

    @FXML
    private Label lblTotalCustomer;

    @FXML
    private Label lblTotalLaptop;

    @FXML
    private Label lblActiveService;

    @FXML
    private Label lblTotalRevenue;

    @FXML
    private Label lblCompleted;

    @FXML
    private Label lblProgress;

    @FXML
    private Label lblWaiting;

    // =========================
    // CHART
    // =========================

    @FXML
    private BarChart<String, Number> barChartService;

    @FXML
    private PieChart pieChartStatus;

    // =========================
    // TABLE
    // =========================

    @FXML
    private TableView<Service> tableRecentService;

    @FXML
    private TableColumn<Service, String> colServiceId;

    @FXML
    private TableColumn<Service, String> colCustomer;

    @FXML
    private TableColumn<Service, String> colLaptop;

    @FXML
    private TableColumn<Service, String> colStatus;

    @FXML
    private TableColumn<Service, Double> colCost;

    // =========================
    // SERVICES
    // =========================

    private CustomerService customerService;
    private LaptopService laptopService;
    private ServiceService serviceService;

    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerService = new CustomerService();
        laptopService = new LaptopService();
        serviceService = new ServiceService();

        initializeTable();

        loadStatistics();

        loadBarChart();

        loadPieChart();

        loadRecentService();

    }
        // =========================
    // NAVIGATION
    // =========================

    @FXML
    private void openDashboard() {

        // Sudah berada di halaman Dashboard,
        // sehingga tidak perlu melakukan perpindahan scene.

    }

    @FXML
    private void openCustomer() {

        openPage("/view/Customer.fxml");

    }

    @FXML
    private void openLaptop() {

        openPage("/view/Laptop.fxml");

    }

    @FXML
    private void openService() {

        openPage("/view/Service.fxml");

    }

    @FXML
    private void logout() {

        System.out.println("Logout berhasil.");

        System.exit(0);

    }

    // =========================
    // PAGE LOADER
    // =========================

    private void openPage(String fxmlPath) {

        try {

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath));

            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage =
                    (javafx.stage.Stage) btnDashboard.getScene().getWindow();

            javafx.scene.Scene scene =
                    new javafx.scene.Scene(root);

            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
        // =========================
    // TABLE INITIALIZATION
    // =========================

    private void initializeTable() {

        colServiceId.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("id"));

        colCustomer.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("customer"));

        colLaptop.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("laptop"));

        colStatus.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("status"));

        colCost.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("biaya"));

    }

    // =========================
    // LOAD DASHBOARD STATISTICS
    // =========================

    private void loadStatistics() {

        try {

            int totalCustomer = customerService.getAllCustomers().size();

            int totalLaptop = laptopService.getAllLaptops().size();

            int totalService = serviceService.getAllServices().size();

            double totalRevenue = serviceService.getTotalRevenue();

            lblTotalCustomer.setText(String.valueOf(totalCustomer));

            lblTotalLaptop.setText(String.valueOf(totalLaptop));

            lblActiveService.setText(String.valueOf(totalService));

            lblTotalRevenue.setText(
                    String.format("Rp %,.0f", totalRevenue));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // =========================
    // LOAD BAR CHART
    // =========================

    private void loadBarChart() {

        barChartService.getData().clear();

        javafx.scene.chart.XYChart.Series<String, Number> series =
                new javafx.scene.chart.XYChart.Series<>();

        try {

            series.getData().add(
                    new javafx.scene.chart.XYChart.Data<>("Jan",
                            serviceService.getMonthlyService("Jan")));

            series.getData().add(
                    new javafx.scene.chart.XYChart.Data<>("Feb",
                            serviceService.getMonthlyService("Feb")));

            series.getData().add(
                    new javafx.scene.chart.XYChart.Data<>("Mar",
                            serviceService.getMonthlyService("Mar")));

            series.getData().add(
                    new javafx.scene.chart.XYChart.Data<>("Apr",
                            serviceService.getMonthlyService("Apr")));

            series.getData().add(
                    new javafx.scene.chart.XYChart.Data<>("Mei",
                            serviceService.getMonthlyService("Mei")));

            series.getData().add(
                    new javafx.scene.chart.XYChart.Data<>("Jun",
                            serviceService.getMonthlyService("Jun")));

        } catch (Exception e) {

            e.printStackTrace();

        }

        barChartService.getData().add(series);

    }

    // =========================
    // LOAD PIE CHART
    // =========================

    private void loadPieChart() {

        pieChartStatus.getData().clear();

        try {

            int completed = serviceService.getCompletedService();

            int progress = serviceService.getProgressService();

            int waiting = serviceService.getWaitingService();

            lblCompleted.setText(String.valueOf(completed));

            lblProgress.setText(String.valueOf(progress));

            lblWaiting.setText(String.valueOf(waiting));

            pieChartStatus.getData().add(
                    new PieChart.Data("Completed", completed));

            pieChartStatus.getData().add(
                    new PieChart.Data("In Progress", progress));

            pieChartStatus.getData().add(
                    new PieChart.Data("Waiting", waiting));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // =========================
    // LOAD RECENT SERVICE
    // =========================

    private void loadRecentService() {

        try {

            tableRecentService.getItems().clear();

            tableRecentService.getItems().addAll(
                    serviceService.getRecentServices());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
        // =========================
    // REFRESH DASHBOARD
    // =========================

    @FXML
    private void refreshDashboard() {

        loadStatistics();

        loadBarChart();

        loadPieChart();

        loadRecentService();

    }

    // =========================
    // RESET SEARCH
    // =========================

    private void clearSearch() {

        if (txtSearch != null) {

            txtSearch.clear();

        }

    }

    // =========================
    // REFRESH ALL COMPONENT
    // =========================

    private void refreshAll() {

        clearSearch();

        refreshDashboard();

    }

}