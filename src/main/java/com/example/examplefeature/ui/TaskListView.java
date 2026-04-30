package com.example.examplefeature.ui;

import com.example.base.ui.ViewToolbar;
import com.example.examplefeature.Task;
import com.example.examplefeature.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Task List")
class TaskListView extends VerticalLayout {

    private final TaskService taskService;

    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<Task> taskGrid;

    TaskListView(TaskService taskService) {
        this.taskService = taskService;

        //añado la clase personalizada
        this.addClassName("container-padding");


        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(Task.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");
        description.setRequiredIndicatorVisible(true);

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");
        dueDate.setMin(LocalDate.now()); // No permitir fechas pasadas

        createBtn = new Button("Create", event -> createTask());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        taskGrid = new Grid<>();
        taskGrid.setItems(query -> taskService.list(toSpringPageRequest(query)).stream());
        taskGrid.addColumn(Task::getDescription).setHeader("Description");
        taskGrid.addColumn(task -> Optional.ofNullable(task.getDueDate()).map(dateFormatter::format).orElse("Never"))
                .setHeader("Due Date");
        taskGrid.addColumn(task -> dateTimeFormatter.format(task.getCreationDate())).setHeader("Creation Date");
        taskGrid.setEmptyStateText("You have no tasks to complete");
        taskGrid.setSizeFull();
        taskGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        taskGrid.addClassName("color-azul");
// ... después de taskGrid.addColumn(...)



        taskGrid.addItemClickListener(event -> {
            // Obtenemos la tarea sobre la que se hizo clic
            Task tareaSeleccionada = event.getItem();
            // Opcional: Mostrar un aviso antes de irse
            Notification.show("Navegando a la calculadora para: " + tareaSeleccionada.getDescription());

            // Navegamos a la ruta definida en CalculadoraView (@Route("calculadora"))
            getUI().ifPresent(ui -> ui.navigate(CalculadoraView.class));
        });
        // Dentro del constructor de TaskListView, donde configuras el grid:

        taskGrid.addColumn(Task::getDescription)
                .setHeader("Description")
                .setPartNameGenerator(task -> "task-link"); // Asigna un nombre de "parte" para CSS

// Añade el evento de clic que ya teníamos
        taskGrid.addItemClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(CalculadoraView.class));
        });

//SIN USAR CSS
//        taskGrid.addComponentColumn(task -> {
//            Button link = new Button(task.getDescription());
//            link.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE); // Lo hace parecer un link de texto
//            link.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(CalculadoraView.class)));
//            return link;
//        }).setHeader("Description");

// Opcional: Hacer que el cursor cambie a "puntero" para que parezca un enlace
        taskGrid.getElement().getStyle().set("cursor", "pointer");
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(new ViewToolbar("Task List", ViewToolbar.group(description, dueDate, createBtn)));
        add(taskGrid);
    }

    private void createTask() {
        taskService.createTask(description.getValue(), dueDate.getValue());
        taskGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        Notification.show("Task added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
