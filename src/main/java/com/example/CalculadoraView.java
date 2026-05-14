package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("calculadora123") // URL de la vista
@PageTitle("Formación | Calculadora")
@Menu(order = 1, icon = "vaadin:calc", title = "Calculadora Estudiantes")

class CalculadoraView extends VerticalLayout {

    public CalculadoraView() {

        // 1. Configuración del contenedor principal
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        // IMAGEN ALINEADA A LA IZQUIERDA
        Image imagenProducto = new Image(
                "https://cdn-icons-png.flaticon.com/512/263/263142.png",
                "Producto"
        );

        imagenProducto.setWidth("120px");

        HorizontalLayout imagenLayout = new HorizontalLayout(imagenProducto);
        imagenLayout.setWidthFull();
        imagenLayout.setJustifyContentMode(JustifyContentMode.START);

        // 2. Componentes de la interfaz
        H2 titulo = new H2("Calculadora de Ofertas y Productos");

        // Campo precio
        NumberField precioInput = new NumberField("Precio del Producto (€)");
        precioInput.setPrefixComponent(VaadinIcon.EURO.create());

        // text box de cantidad de productos
        TextField productoField = new TextField("Cantidad de Productos");
        productoField.setPlaceholder("Ejemplo: 5 productos");

        // Campo cantidad
        IntegerField cantidadInput = new IntegerField("Cantidad");
        cantidadInput.setValue(1);
        cantidadInput.setMin(1);
        cantidadInput.setStepButtonsVisible(true);

        // Combo descuento
        ComboBox<Integer> descuentoCombo = new ComboBox<>("Selecciona Descuento (%)");
        descuentoCombo.setItems(10, 20, 30, 50);
        descuentoCombo.setValue(10);

        // Botón calcular
        Button calcularBtn = new Button(
                "Calcular Precio Final",
                VaadinIcon.MAGIC.create()
        );

        calcularBtn.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS
        );

        // Resultado
        Span resultadoLabel = new Span("Introduce un precio para empezar");

        resultadoLabel.getStyle().set("font-weight", "bold");
        resultadoLabel.getStyle().set("font-size", "1.5em");

        // 3. Lógica (Evento Click)
        calcularBtn.addClickListener(event -> {

            if (precioInput.getValue() != null) {

                double original = precioInput.getValue();
                int cantidad = cantidadInput.getValue();
                int descuento = descuentoCombo.getValue();

                // Multiplicar por cantidad
                double subtotal = original * cantidad;

                // Aplicar descuento
                double ahorro = subtotal * descuento / 100;
                double precioFinal = subtotal - ahorro;

                // Nombre/cantidad escrita
                String textoProducto = productoField.getValue();

                resultadoLabel.setText(
                        String.format(
                                "%s → Total final: %.2f€ (Ahorraste %.2f€)",
                                textoProducto,
                                precioFinal,
                                ahorro
                        )
                );

                resultadoLabel.getStyle().set(
                        "color",
                        "var(--lumo-success-text-color)"
                );

                Notification.show("Cálculo realizado con éxito");

                // Cambiar color botón
                calcularBtn.getStyle().set(
                        "background-color",
                        "blue"
                );

            } else {

                Notification.show(
                        "Por favor, introduce un precio válido",
                        3000,
                        Notification.Position.MIDDLE
                );

                precioInput.setInvalid(true);
            }
        });

        // 4. Añadir componentes al layout
        add(
                imagenLayout,
                titulo,
                productoField,
                precioInput,
                cantidadInput,
                descuentoCombo,
                calcularBtn,
                resultadoLabel
        );
    }


    public void cambiarColorConJS(Button calcularBtn) {
        calcularBtn.getElement()
                .executeJs("this.style.backgroundColor = 'blue'");
    }
}