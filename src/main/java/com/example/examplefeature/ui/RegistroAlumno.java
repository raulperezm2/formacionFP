package com.example.examplefeature.ui;

import com.example.examplefeature.Entity.Cargo;
import com.example.examplefeature.Entity.Estudiante;
import com.example.examplefeature.Entity.alumnos;
import com.example.examplefeature.Repository.AlumnosRepository;
import com.example.examplefeature.Repository.EstudianteRepository;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route("Alumnos-registro")
@PageTitle("Alumnos-registro")
@Menu(order = 5, icon = "vaadin:calc", title = "Alumnos registro")
public class RegistroAlumno extends VerticalLayout {

    private final transient AlumnosRepository repo;
    private final Binder<alumnos> binder = new BeanValidationBinder<>(alumnos.class);
    private final Grid<alumnos> grid = new Grid<>(alumnos.class);


    public RegistroAlumno(AlumnosRepository repo) {
        this.repo = repo;
        addClassName("registro-view");
        setSizeFull();

        // CAMPOS
        TextField nombre = new TextField("Nombre");

        TextField apellidos = new TextField("Apellidos");

        TextField email = new TextField("Email");

        IntegerField edad = new IntegerField("Edad");

        IntegerField telefono = new IntegerField("Telefono");

        ComboBox<Cargo> cargos = new ComboBox<>("Cargo");
        cargos.setItems(Cargo.values());

        // TITULO
        H2 titulo = new H2("Sistema de Registro de Alumnos");

        // BOTON
        Button btnGuardar = new Button("Registrar Alumno",
                e -> guardar());

        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // FORMULARIO
        FormLayout layoutForm = new FormLayout(
                nombre,
                apellidos,
                email,
                edad,
                telefono,
                cargos,
                btnGuardar
        );

        layoutForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        // BINDINGS
        binder.forField(nombre)
                .bind("nombre");

        binder.forField(apellidos)
                .bind("apellidos");

        binder.forField(email)
                .bind("email");

        binder.forField(edad)
                .bind("edad");

        binder.forField(telefono)
                .bind("telefono");

        binder.forField(cargos)
                .bind("cargos");

        // GRID
        grid.setColumns(
                "id",
                "nombre",
                "apellidos",
                "email",
                "edad",
                "telefono",
                "cargos"
        );
        actualizarTabla();

        // AGREGAR COMPONENTES
        add(
                titulo,
                layoutForm,
                new Hr(),
                new H3("Listado Actual"),
                grid
        );

    }

    private void actualizarTabla() {
        grid.setItems(repo.findAll());
    }

    private void guardar() {
        alumnos nuevo = new alumnos();

        try {

            binder.writeBean(nuevo);

            repo.save(nuevo);

            Notification.show(
                    "Alumno guardado correctamente",
                    3000,
                    Notification.Position.BOTTOM_END
            ).addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // LIMPIAR FORMULARIO
            binder.readBean(new alumnos());

            actualizarTabla();
        } catch (ValidationException e) {

            Notification.show(
                    "Error al guardar",
                    3000,
                    Notification.Position.BOTTOM_END
            ).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }


}
