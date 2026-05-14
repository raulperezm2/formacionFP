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
}