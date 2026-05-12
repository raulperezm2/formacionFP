package com.example.examplefeature.ui;

import com.example.examplefeature.Entity.Estudiante;
import com.example.examplefeature.Repository.EstudianteRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("opplus-registro")
@PageTitle("opplus-registro")
@Menu(order = 3, icon = "vaadin:pencil", title = "opplus registro")
public class RegistroView extends VerticalLayout {

    private final transient EstudianteRepository repo;
    private final Binder<Estudiante> binder = new BeanValidationBinder<>(Estudiante.class);
    private final Grid<Estudiante> grid = new Grid<>(Estudiante.class);

    public RegistroView(EstudianteRepository repo) {
        this.repo = repo;
        addClassName("registro-view");
        setSizeFull();

        TextField nombre = new TextField("Nombre");
        TextField email = new TextField("Email");
        IntegerField edad = new IntegerField("Edad");


        // Configuración de UI
        H2 titulo = new H2("Sistema de Registro Opplus");

        Button btnGuardar = new Button("Registrar Estudiante", e -> guardar());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout layoutForm = new FormLayout(nombre, email, edad, btnGuardar);
        layoutForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Binding explícito para evitar problemas de reflexión con proxies
        binder.forField(nombre).bind("nombre");
        binder.forField(email).bind("email");
        binder.forField(edad).bind("edad");

        // Configurar Tabla (Grid)
        grid.setColumns("id", "nombre", "email", "edad");
        actualizarTabla();

        add(titulo, layoutForm, new Hr(), new H3("Listado Actual"), grid);
    }

    private void guardar() {
        Estudiante nuevo = new Estudiante();
        try {
            binder.writeBean(nuevo);
            repo.save(nuevo);
            Notification.show("Estudiante guardado correctamente", 3000,
                    Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            binder.readBean(new Estudiante()); // Limpiar form con bean vacío
            actualizarTabla();
        } catch (ValidationException e) {
            // Los mensajes de error aparecen automáticamente en los campos
        }
    }

    private void actualizarTabla() {
        grid.setItems(repo.findAll());
    }
}