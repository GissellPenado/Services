package com.tuempresa.serviciosweb.controller;

import com.tuempresa.serviciosweb.model.Customer;
import com.tuempresa.serviciosweb.service.CustomerService;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CustomerController {
    @FXML
    private TableView<Customer> tblCustomers;
    @FXML
    private TableColumn<Customer,Integer> colId;
    @FXML
    private TableColumn<Customer,String> colNombre;
    @FXML
    private TableColumn<Customer,String> colApellido;
    @FXML
    private TableColumn<Customer,String> colFechaNacimiento;
    @FXML
    private TableColumn<Customer,String> colCorreo;
    @FXML
    private TableColumn<Customer,String> colTelefono;
    @FXML
    private TableColumn<Customer,String> colCiudad;
    @FXML
    private TableColumn<Customer,String> colPais;
    @FXML
    private Button btnConsultarCustomers;
    @FXML
    private Label lblEstadoCustomers;

    private final CustomerService service =  new CustomerService();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );
        colApellido.setCellValueFactory(
                new PropertyValueFactory<>("apellido")
        );
        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );
        colTelefono.setCellValueFactory(
                new PropertyValueFactory<>("telefono")
        );
        colCiudad.setCellValueFactory(
                new PropertyValueFactory<>("ciudad")
        );
        colPais.setCellValueFactory(
                new PropertyValueFactory<>("pais")
        );
        colFechaNacimiento.setCellValueFactory(
                new PropertyValueFactory<>("fechaNacimiento")
        );
    }

    @FXML
    private void consultarCustomers() {
        Task<List<Customer>> task = new Task<List<Customer>>() {
            @Override
            protected List<Customer> call() throws Exception {
                return service.obtenerCustomer();
            }
        };

        btnConsultarCustomers.disableProperty().bind(
                task.runningProperty()
        );
        lblEstadoCustomers.setText("Consultando API REST...");
        task.setOnSucceeded(event -> {
            List<Customer> customers = task.getValue();
            tblCustomers.setItems(FXCollections.observableArrayList(customers));
            lblEstadoCustomers.setText("Customers encontrados");
        });

        //En caso de error
        task.setOnFailed(event -> {
            Throwable error = task.getException();
            lblEstadoCustomers.setText(error.getMessage());
        });
        Thread hilo = new Thread(task);
        hilo.setDaemon(true);
        hilo.start();

    }

}
