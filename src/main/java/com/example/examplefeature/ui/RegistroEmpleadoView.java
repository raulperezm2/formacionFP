package com.example.examplefeature.ui;

import com.example.base.ui.MainLayout;
import com.example.examplefeature.Entity.Empleado;
import com.example.examplefeature.Entity.Estudiante;
import com.example.examplefeature.Repository.EmpleadoRepository;
import com.example.examplefeature.Repository.EstudianteRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Data;

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

@Route("opplus-registro-empleado")
@PageTitle("opplus-registro-empleado")
@Menu(order = 3, icon = "vaadin:calc", title = "opplus registro empleado")
public class RegistroEmpleadoView extends VerticalLayout {

    private final transient EmpleadoRepository repo;
    private final Binder<Empleado> binder = new BeanValidationBinder<>(Empleado.class);
    private final Grid<Empleado> grid = new Grid<>(Empleado.class);

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

    public RegistroEmpleadoView(EmpleadoRepository repo) {
        this.repo = repo;
        addClassName("registro-view");
        setSizeFull();

        TextField nombre = new TextField("Nombre");
        TextField apellidos = new TextField("Apellidos");
        TextField email = new TextField("Email");
        IntegerField numTelefono = new IntegerField("Telefono");
        IntegerField edad = new IntegerField("Edad");
        ComboBox<String> genero = new ComboBox<>("Selecciona tu genero");
        genero.setItems("Hombre", "Mujer", "Prefiero no decirlo");
        TextField ultima_titulacion = new TextField("Última titulación");




        // Configuración de UI
        H2 titulo = new H2("Sistema de Registro Opplus");

        Button btnGuardar = new Button("Registrar Estudiante", e -> guardar());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout layoutForm = new FormLayout(nombre, apellidos, email, numTelefono, edad, genero, ultima_titulacion, btnGuardar);
        layoutForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Binding explícito para evitar problemas de reflexión con proxies
        binder.forField(nombre).bind("nombre");
        binder.forField(apellidos).bind("apellidos");
        binder.forField(email).bind("email");
        binder.forField(genero).bind("genero");
        binder.forField(edad).bind("edad");
        binder.forField(ultima_titulacion).bind("ultima_titulacion");
        binder.forField(numTelefono).bind("numTelefono");

        // Configurar Tabla (Grid)
        grid.setColumns("id", "nombre", "apellidos", "email", "numTelefono", "edad", "genero", "ultima_titulacion");
        actualizarTabla();

        add(titulo, layoutForm, new Hr(), new H3("Listado Actual"), grid);
    }

    private void guardar() {
        Empleado nuevo = new Empleado();
        try {
            binder.writeBean(nuevo);
            repo.save(nuevo);
            Notification.show("Empleado guardado correctamente", 3000,
                    Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            binder.readBean(new Empleado()); // Limpiar form con bean vacío
            actualizarTabla();
        } catch (ValidationException e) {
            // Los mensajes de error aparecen automáticamente en los campos
        }
    }

    private void actualizarTabla() {
        grid.setItems(repo.findAll());
    }





    private String fetchToken() {
        String url = "https://sso-picasso-des.apps.infraprev.igrupobbva/auth/realms/Opplus/protocol/openid-connect/token";
        String form = "grant_type=password&client_id=svc-0023-00&client_secret=a8d376ac-5ba1-42b4-94d6-5a3ba5461e34&username=svc-0023-00-writer&password=svc-0023-00-writer";

        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
            var uri = URI.create(url);
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(form))
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

    private static class TokenResponseDTO {
        private final String accessToken;

        TokenResponseDTO(String accessToken) {
            this.accessToken = accessToken;
        }

        String getAccessToken() {
            return accessToken != null ? accessToken : "";
        }
    }

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

    private String extractValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private String[] fetchUserData(String idUser) {
        String token = fetchToken();
        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();

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