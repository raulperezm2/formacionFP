package com.example.examplefeature.ui;

import com.example.examplefeature.Entity.EmpleadoEig;
import com.example.examplefeature.Entity.NivelEducativo;
import com.example.examplefeature.Entity.NivelIdioma;
import com.example.examplefeature.Repository.EmpleadoEigRepository;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("eig-registro")
@PageTitle("eig-registro")
@Menu(order = 7, icon = "vaadin:specialist", title = "EIG Registro")
public class EmpleadosEigView extends VerticalLayout {

    private final transient EmpleadoEigRepository repository;
    private final Binder<EmpleadoEig> binder = new Binder<>(EmpleadoEig.class);
    private final Grid<EmpleadoEig> grid = new Grid<>(EmpleadoEig.class);

    public EmpleadosEigView(EmpleadoEigRepository repository) {
        this.repository = repository;
        addClassName("eig");
        setSizeFull();

        TextField nombre = new TextField("Nombre");
        TextField email = new TextField("Email");
        IntegerField edad = new IntegerField("Edad");

        TextField primerIdioma = new TextField("Primer Idioma");
        ComboBox<NivelIdioma> nivelPrimerIdioma = new ComboBox<>("Nivel primer Idioma");
        nivelPrimerIdioma.setItems(NivelIdioma.values());

        TextField segundoIdioma = new TextField("Segundo Idioma");
        ComboBox<NivelIdioma> nivelSegundoIdioma = new ComboBox<>("Nivel segundo Idioma");
        nivelSegundoIdioma.setItems(NivelIdioma.values());

        TextField titulacionSuperior = new TextField("Titulación Superior");
        ComboBox<NivelEducativo> nivelEducativo = new ComboBox<>("Nivel educativo");
        nivelEducativo.setItems(NivelEducativo.values());

        Button btnGuardar = new Button("Registrar", e -> saveRow());

        FormLayout form = new FormLayout(nombre, email, edad, primerIdioma, nivelPrimerIdioma, segundoIdioma, nivelSegundoIdioma, titulacionSuperior, nivelEducativo, btnGuardar);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        binder.forField(nombre).bind("nombre");
        binder.forField(email).bind("email");
        binder.forField(edad).bind("edad");
        binder.forField(primerIdioma).bind("primerIdioma");
        binder.forField(nivelPrimerIdioma).bind("nivelPrimerIdioma");
        binder.forField(segundoIdioma).bind("segundoIdioma");
        binder.forField(nivelSegundoIdioma).bind("nivelSegundoIdioma");
        binder.forField(titulacionSuperior).bind("titulacionSuperior");
        binder.forField(nivelEducativo).bind("nivelEducativo");

        grid.setColumns("id", "nombre", "email", "edad", "primerIdioma", "nivelPrimerIdioma", "segundoIdioma", "nivelSegundoIdioma", "titulacionSuperior", "nivelEducativo");
        refreshTable();
        add(new H2("Sistema de Registro EIG"), form, new Hr(), new H3("Listado Actual"), grid);
    }

    private void refreshTable() {
        grid.setItems(repository.findAll());
    }

    private void saveRow() {
        EmpleadoEig empleado = new EmpleadoEig();
        try {
            binder.writeBean(empleado);
            repository.save(empleado);
            Notification.show("Registro guardado exitosamente", 3000, Notification.Position.BOTTOM_END).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            binder.readBean(new EmpleadoEig());
            refreshTable();
        } catch (ValidationException e) {
            // Los mensajes de error aparecen automáticamente en los campos
        }
    }
}
