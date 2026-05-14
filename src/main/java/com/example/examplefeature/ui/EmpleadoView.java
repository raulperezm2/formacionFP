package com.example.examplefeature.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Route("empleado")
@PageTitle("empleado")
@Menu(order = 8, icon = "vaadin:specialist", title = "Empleado")
@StyleSheet("pabloIR.css")
public class EmpleadoView extends VerticalLayout {

    /**
     * ID de usuario de Raul
     */
    private final String USER_RAUL = "e043394";
    /**
     * ID de usuario de Monica
     */
    private final String USER_MONICA = "O018699";
    /**
     * Object Mapper para deserializar JSON
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmpleadoView() {
        addClassName("eig");
        setSizeFull();

        showUser(USER_RAUL);
        showUser(USER_MONICA);
        showUser("T067722");
    }

    /**
     * Añadir al usuario recibido a la vista
     *
     * @param user ID de usuario a mostrar
     */
    private void showUser(String user) {
        Component logo = getAvatar(user);
        String[] userData = fetchUserData(user);
        if (userData.length > 0) {
            var userSpan = new Span(userData[0]);
            var userUnit = new Span(userData[1]);
            var userBoss = new Span(userData[2]);
            var userOffice = new Span(userData[3]);
            add(logo, userSpan, userUnit, userBoss, userOffice);
        } else {
            add(logo, new Span("No se ha encontrado al usuario"));
        }
    }

    /**
     * Obtener avatar del usuario
     *
     * @param user ID de Usuario
     * @return Componente del avatar de usuario
     */
    private Component getAvatar(String user) {
        var url = fetchUserImageUrl(user);
        if (url.isBlank()) {
            var icon = VaadinIcon.MALE.create();
            icon.setColor("Blue");
            icon.setSize("128px");
            return icon;
        } else {
            var image = new Image(url, "Raúl");
            image.setWidth("128px");
            return image;
        }
    }

    /**
     * @return Token de acceso a los servicios
     */
    private String fetchToken() {
        String tokenUrl = "https://sso-picasso-des.apps.infraprev.igrupobbva/auth/realms/Opplus/protocol/openid-connect/token";
        String tokenForm = "grant_type=password&client_id=svc-0023-00&client_secret=a8d376ac-5ba1-42b4-94d6-5a3ba5461e34&username=svc-0023-00-writer&password=svc-0023-00-writer";

        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
            var uri = URI.create(tokenUrl);
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(tokenForm))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body();
                TokenResponseDTO tokenResponse = deserializeTokenResponse(body);
                if (tokenResponse.getAccessToken() != null && !tokenResponse.getAccessToken().isBlank()) {
                    return tokenResponse.getAccessToken();
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception ignored) {
            // Silenciar errores y usar fallback
        }
        return "";
    }

    /**
     * DTO para deserializar Token
     */
    private static class TokenResponseDTO {
        private final String accessToken;

        TokenResponseDTO(String accessToken) {
            this.accessToken = accessToken;
        }

        String getAccessToken() {
            return accessToken != null ? accessToken : "";
        }
    }

    /**
     * DTO para deserializar URL de imagenes
     */
    private static class ImageResponseDTO {
        private final String imagePath;

        // Usamos @JsonProperty porque el campo es final y el nombre del JSON debe coincidir
        public ImageResponseDTO(@JsonProperty("imagePah") String imagePah) {
            this.imagePath = imagePah;
        }

        public String getImagePath() {
            return imagePath != null ? imagePath : "";
        }
    }

    /**
     * Deserializar JSON de tokens
     */
    private TokenResponseDTO deserializeTokenResponse(String json) {
        String accessToken = extractValue(json, "access_token");
        return new TokenResponseDTO(accessToken);
    }

    /**
     * DTO para deserializar Usuarios
     */
    @Data
    private static class UserResponseDTO {
        @JsonProperty("idEmpresa")
        long idEmpresa;
        @JsonProperty("id")
        String id;
        @JsonProperty("correoPersonal")
        String email;
        @JsonProperty("nombre")
        String nombre;
        @JsonProperty("primerApellido")
        String primerApellido;
        @JsonProperty("segundoApellido")
        String segundoApellido;
        @JsonProperty("nombreCompleto")
        String nombreCompleto;
        @JsonProperty("matricula")
        long matricula;
        @JsonProperty("posicionTrabajador")
        long posicionTrabajador;
        @JsonProperty("posicionJefe")
        long posicionJefe;
        @JsonProperty("idUsuarioJefe")
        String idUsuarioJefe;
        @JsonProperty("nombreCompletoJefe")
        String nombreCompletoJefe;
        @JsonProperty("idUnidad")
        long idUnidad;
        @JsonProperty("nombreUnidad")
        String nombreUnidad;
        @JsonProperty("nivel1")
        long nivel1;
        @JsonProperty("nivel2")
        long nivel2;
        @JsonProperty("nivel3")
        long nivel3;
        @JsonProperty("nivel4")
        long nivel4;
        @JsonProperty("nivel5")
        long nivel5;
        @JsonProperty("nivel6")
        long nivel6;
        @JsonProperty("nivel7")
        long nivel7;
        @JsonProperty("nivel1Nombre")
        String nivel1Nombre;
        @JsonProperty("nivel2Nombre")
        String nivel2Nombre;
        @JsonProperty("nivel3Nombre")
        String nivel3Nombre;
        @JsonProperty("nivel4Nombre")
        String nivel4Nombre;
        @JsonProperty("nivel5Nombre")
        String nivel5Nombre;
        @JsonProperty("nivel6Nombre")
        String nivel6Nombre;
        @JsonProperty("nivel7Nombre")
        String nivel7Nombre;
        @JsonProperty("idOrganizacion")
        long idOrganizacion;
        @JsonProperty("nombreOrganizacion")
        String nombreOrganizacion;
        @JsonProperty("idEdificio")
        long idEdificio;
        @JsonProperty("nombreEdificio")
        String nombreEdificio;
        @JsonProperty("idPlanta")
        long idPlanta;
        @JsonProperty("idMesa")
        String idMesa;
        @JsonProperty("telefono")
        String telefono;
        @JsonProperty("telefonoLargo")
        String telefonoLargo;
        @JsonProperty("idOrdenador")
        String idOrdenador;
        @JsonProperty("cargoFuncional")
        String cargoFuncional;
        @JsonProperty("nombreCargo")
        String nombreCargo;
        @JsonProperty("idEstado")
        long idEstado;
        @JsonProperty("fechaMod")
        String fechaMod;
        @JsonProperty("fechaAlta")
        String fechaAlta;
        @JsonProperty("fechaBaja")
        String fechaBaja;
    }

    /**
     * Axiliar para extraer el token
     */
    private String extractValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * Obtener URL de Usuario
     */
    private String fetchUserImageUrl(String idUser) {
        String token = fetchToken();
        try {
            var client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();

            String encoded = URLEncoder.encode(idUser, StandardCharsets.UTF_8);
            var uri = URI.create("https://svc-0023-00-microservicios-des.apps.infraprev.igrupobbva/image?iduser=" + encoded);

            var builder = HttpRequest.newBuilder().uri(uri).GET().timeout(Duration.ofSeconds(5));
            if (token != null && !token.isBlank()) {
                builder.header("Authorization", "Bearer " + token);
            }

            var request = builder.build();
            // Corregido: HttpResponse.BodyHandlers.ofString()
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();
                if (body != null && !body.isBlank()) {
                    // Deserialización aquí
                    ImageResponseDTO imageResponse = objectMapper.readValue(body, ImageResponseDTO.class);

                    if (!imageResponse.getImagePath().isBlank()) {
                        return imageResponse.getImagePath();
                    }
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Loggear el error es buena práctica antes de retornar vacío
            // System.err.println("Error fetching image: " + e.getMessage());
        }
        return "";
    }

    /**
     * Obtener datos de Usuario
     */
    private String[] fetchUserData(String idUser) {
        String token = fetchToken();
        try {
            var client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();

            String encoded = URLEncoder.encode(idUser, StandardCharsets.UTF_8);
            var uri = URI.create("https://svc-0023-00-microservicios-des.apps.infraprev.igrupobbva/users?iduser=" + encoded);

            var builder = HttpRequest.newBuilder().uri(uri).GET().timeout(Duration.ofSeconds(5));
            if (token != null && !token.isBlank()) {
                builder.header("Authorization", "Bearer " + token);
            }

            var request = builder.build();
            // Corregido: HttpResponse.BodyHandlers.ofString()
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();
                if (body != null && !body.isBlank()) {
                    //UserResponseDTO userResponse = objectMapper.readValue(body.replace("[","").replace("]",""), UserResponseDTO.class);
                    UserResponseDTO[] empleados = objectMapper.readValue(body, UserResponseDTO[].class);
                    List<UserResponseDTO> lista = Arrays.asList(empleados);
                    var userResponse = lista.getFirst();
                    var user = userResponse.getNombreCompleto();
                    var unit = userResponse.getNombreUnidad();
                    var boss = userResponse.getNombreCompletoJefe();
                    var office = userResponse.getNombreEdificio();
                    if (!user.isBlank() && !unit.isBlank() && !boss.isBlank() && !office.isBlank()) {
                        return new String[]{"Usuario: " + user, "Unidad: " + unit, "Superior: " + boss, "Oficina: " + office};
                    }
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Loggear el error es buena práctica antes de retornar vacío
            // System.err.println("Error fetching image: " + e.getMessage());
        }
        return new String[0];
    }
}
