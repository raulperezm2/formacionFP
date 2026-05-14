package com.example.examplefeature.ui;

import com.example.examplefeature.dto.UserDTO;
import com.example.examplefeature.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("usuarios-api")
@PageTitle("Usuarios API")
public class UsuarioView extends VerticalLayout {

    private final UserService userService;

    private final TextField idField = new TextField("ID Usuario");
    private final Button btnBuscar = new Button("Buscar");
    private final Grid<UserDTO> grid = new Grid<>(UserDTO.class);

    public UsuarioView(UserService userService) {
        this.userService = userService;

        idField.setPlaceholder("Ej: E043394");

        grid.setSizeFull();

        btnBuscar.addClickListener(e -> buscar());

        add(idField, btnBuscar, grid);
    }

    private void buscar() {

        String id = idField.getValue();

        // 🔴 VALIDACIÓN
        if (id == null || id.trim().isEmpty()) {
            Notification.show("Introduce un ID de usuario");
            return;
        }

        try {
            List<UserDTO> lista = userService.getUserById(id);
            grid.setItems(lista);

        } catch (Exception e) {
            Notification.show("Error al consultar el servicio");
        }
    }
}