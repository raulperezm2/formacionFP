package com.example.examplefeature.service;

import com.example.examplefeature.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class UserService {

    public List<UserDTO> getUserById(String idUser) {

        try {
            String url = "https://svc-0023-00-microservicios-des.apps.infraprev.igrupobbva/users?iduser=" + idUser;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(
                    response.body(),
                    new TypeReference<List<UserDTO>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("Error llamando al endpoint", e);
        }
    }
}