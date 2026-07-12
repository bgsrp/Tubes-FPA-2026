package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import model.Service;

import Service.CustomerService;
import Service.LaptopService;
import Service.ServiceService;

public class DashboardController implements Initializable {

    private static final Map<String, String> STATUS_LABELS = new LinkedHashMap<>();

    static {
        STATUS_LABELS.put(ServiceService.STATUS_WAITING, "Menunggu");
        STATUS_LABELS.put(ServiceService.STATUS_IN_PROGRESS, "Dalam Proses");
        STATUS_LABELS.put(ServiceService.STATUS_COMPLETED, "Selesai");
    }

    @FXML
    private BorderPane rootPane;

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

    @FXML
    private Label lblHeaderTitle;

    @FXML
    private VBox dashboardContent;

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
    private NumberAxis numberAxis;

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
    private Node dashboardView;
    private SearchableController activeSearchController;
    private boolean showingDashboard;

    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerService = new CustomerService();
        laptopService = new LaptopService();
        serviceService = new ServiceService();

        dashboardView = dashboardContent;
        showingDashboard = true;

        initializeTable();

        initializeSearch();

        loadStatistics();

        loadBarChart();

        loadPieChart();

        loadRecentService();

        setActiveMenuButton(btnDashboard);

    }
        // =========================
    // NAVIGATION
    // =========================

    @FXML
    private void openDashboard() {

        lblHeaderTitle.setText("Dashboard Overview");

        rootPane.setCenter(dashboardView);
        activeSearchController = null;
        showingDashboard = true;

        refreshAll();

        setActiveMenuButton(btnDashboard);

    }

    @FXML
    private void openCustomer() {

        openPage("/view/Customer.fxml", "Data Customer");
        setActiveMenuButton(btnCustomer);

    }

    @FXML
    private void openLaptop() {

        openPage("/view/Laptop.fxml", "Manajemen Data Laptop");
        setActiveMenuButton(btnLaptop);

    }

    @FXML
    private void openService() {

        openPage("/view/Service.fxml", "Manajemen Service Laptop");
        setActiveMenuButton(btnService);

    }

    // =========================
    // ACTIVE MENU INDICATOR
    // =========================

    private void setActiveMenuButton(Button active) {

        for (Button button : new Button[] { btnDashboard, btnCustomer, btnLaptop, btnService }) {
            button.getStyleClass().remove("menu-button-active");
        }

        active.getStyleClass().add("menu-button-active");

    }

    @FXML
    private void logout() {

        System.out.println("Logout berhasil.");

        System.exit(0);

    }

    // =========================
    // PAGE LOADER
    // =========================

    private void openPage(String fxmlPath, String title) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(fxmlPath));

            Parent page = loader.load();
            Object controller = loader.getController();

            lblHeaderTitle.setText(title);

            rootPane.setCenter(page);
            showingDashboard = false;
            activeSearchController = controller instanceof SearchableController
                    ? (SearchableController) controller
                    : null;

            if (txtSearch != null) {
                txtSearch.clear();
            }

            applySearch("");

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

        colStatus.setCellFactory(column -> new StatusBadgeCell());

        colCost.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("biaya"));
        tableRecentService.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    // =========================
    // LOAD DASHBOARD STATISTICS
    // =========================

    private void loadStatistics() {

        try {

            int totalCustomer = customerService.getAllCustomers().size();

            int totalLaptop = laptopService.getAllLaptops().size();

            int activeService = serviceService.getWaitingService() + serviceService.getProgressService();

            double totalRevenue = serviceService.getTotalRevenue();

            lblTotalCustomer.setText(String.valueOf(totalCustomer));

            lblTotalLaptop.setText(String.valueOf(totalLaptop));

            lblActiveService.setText(String.valueOf(activeService));

            lblTotalRevenue.setText(
                    String.format("Rp %,.0f", totalRevenue));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // =========================
    // LOAD LINE CHART
    // =========================

    private void loadBarChart() {

        barChartService.getData().clear();

        javafx.scene.chart.XYChart.Series<String, Number> series =
                new javafx.scene.chart.XYChart.Series<>();

        try {

            addMonthlyData(series, "Jan");
            addMonthlyData(series, "Feb");
            addMonthlyData(series, "Mar");
            addMonthlyData(series, "Apr");
            addMonthlyData(series, "Mei");
            addMonthlyData(series, "Jun");
            addMonthlyData(series, "Jul");
            addMonthlyData(series, "Agu");
            addMonthlyData(series, "Sep");
            addMonthlyData(series, "Okt");
            addMonthlyData(series, "Nov");
            addMonthlyData(series, "Des");

        } catch (Exception e) {

            e.printStackTrace();

        }

        barChartService.getData().add(series);

        int maxCount = 1;
        for (javafx.scene.chart.XYChart.Data<String, Number> data : series.getData()) {
            maxCount = Math.max(maxCount, data.getYValue().intValue());
        }

        numberAxis.setAutoRanging(false);
        numberAxis.setLowerBound(0);
        numberAxis.setUpperBound(maxCount);
        numberAxis.setTickUnit(1);

    }

    private void addMonthlyData(javafx.scene.chart.XYChart.Series<String, Number> series, String month) {

        series.getData().add(
                new javafx.scene.chart.XYChart.Data<>(
                        month,
                        serviceService.getMonthlyService(month)
                )
        );

    }

    private void initializeSearch() {

        txtSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> applySearch(newValue));

    }

    private void applySearch(String keyword) {

        if (showingDashboard) {
            filterRecentService(keyword);
            return;
        }

        if (activeSearchController != null) {
            activeSearchController.setSearchKeyword(keyword);
        }

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
                    new PieChart.Data("Selesai", completed));

            pieChartStatus.getData().add(
                    new PieChart.Data("Dalam Proses", progress));

            pieChartStatus.getData().add(
                    new PieChart.Data("Menunggu", waiting));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // =========================
    // LOAD RECENT SERVICE
    // =========================

    private void loadRecentService() {

        try {

            filterRecentService(txtSearch.getText());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void filterRecentService(String keyword) {

        String search = keyword != null ? keyword.trim().toLowerCase() : "";

        tableRecentService.setItems(FXCollections.observableArrayList(
                serviceService.getRecentServices().stream()
                        .filter(service -> matchesRecentService(service, search))
                        .collect(java.util.stream.Collectors.toList())
        ));

    }

    private boolean matchesRecentService(Service service, String search) {

        if (search.isEmpty()) {
            return true;
        }

        return contains(service.getId(), search)
                || contains(service.getCustomer(), search)
                || contains(service.getLaptop(), search)
                || contains(service.getStatus(), search)
                || contains(String.valueOf(service.getBiaya()), search);

    }

    private boolean contains(String value, String search) {

        return value != null && value.toLowerCase().contains(search);

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

    // =========================
    // STATUS BADGE CELL
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

}
