package com.tuempresa.serviciosweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuempresa.serviciosweb.model.Customer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private static final String URL = "https://sqpvqkkdfgtzdgrjrgxs.supabase.co/rest/v1/customers";
    public List<Customer> obtenerCustomer() throws IOException, InterruptedException {

        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", "sb_publishable_vIJdi0L7Kd2XtbzaTaFSgg_xvbo3eAf")
                .GET()
                .build();

        HttpResponse<String> response = cliente.send(
                request, HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Error al obtener el usuario");

        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode datos = mapper.readTree(response.body());

        if(!datos.isArray()){
            throw new IOException ("La respuesta no contiene un formato JSON");
        }

        List<Customer> customers = new ArrayList<>();
        for (JsonNode nodo : datos) {
            Customer customer = new Customer(
                    nodo.path("id").asInt(),
                    nodo.path("first_name").asText(),
                    nodo.path("last_name").asText(),
                    nodo.path("email").asText(),
                    nodo.path("phone").asText(),
                    nodo.path("date_of_birth").asText(),
                    nodo.path("country").asText(),
                    nodo.path("city").asText()
            );
            customers.add(customer);
        }
        return customers;
    }

}
