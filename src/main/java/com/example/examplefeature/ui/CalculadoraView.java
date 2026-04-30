package com.example.examplefeature.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("calculadora") // URL de la vista
@PageTitle("Formación | Calculadora")
@Menu(order = 1, icon = "vaadin:calc", title = "Calculadora Estudiantes")
public class CalculadoraView extends VerticalLayout {

    public CalculadoraView() {
        // 1. Configuración del contenedor principal
        setSpacing(true);
        setAlignItems(Alignment.CENTER); // Centra todo horizontalmente

        // 2. Componentes de la interfaz
        H2 titulo = new H2("Calculadora de Ofertas");

        NumberField precioInput = new NumberField("Precio del Producto (€)");
        precioInput.setPrefixComponent(VaadinIcon.EURO.create());

        ComboBox<Integer> descuentoCombo = new ComboBox<>("Selecciona Descuento (%)");
        descuentoCombo.setItems(10, 20, 30, 50);
        descuentoCombo.setValue(10); // Valor por defecto

        Button calcularBtn = new Button("Calcular Precio Final", VaadinIcon.MAGIC.create());
        calcularBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Span resultadoLabel = new Span("Introduce un precio para empezar");
        resultadoLabel.getStyle().set("font-weight", "bold");
        resultadoLabel.getStyle().set("font-size", "1.5em");

        // 3. Lógica (Evento Click)
        calcularBtn.addClickListener(event -> {
            if (precioInput.getValue() != null) {
                double original = precioInput.getValue();
                int descuento = descuentoCombo.getValue();
                double ahorro = original * descuento / 100;
                double precioFinal = original - ahorro;

                resultadoLabel.setText(String.format("¡Precio final: %.2f€! (Ahorraste %.2f€)", precioFinal, ahorro));
                resultadoLabel.getStyle().set("color", "var(--lumo-success-text-color)");

                Notification.show("Cálculo realizado con éxito");
                //cambiar el color del botón usando JavaScript
                //cambiarColorConJS(calcularBtn);
                //cambiar el color del botón usando Vaadin
                calcularBtn.getStyle().set("background-color", "red !important");
                calcularBtn.getStyle().set("background-color", "blue !important");

            } else {
                Notification.show("Por favor, introduce un precio válido", 3000, Notification.Position.MIDDLE);
                precioInput.setInvalid(true);
            }
        });

        // 4. Añadir componentes al layout
        add(titulo, precioInput, descuentoCombo, calcularBtn, resultadoLabel);
    }
    public void cambiarColorConJS(Button calcularBtn) {
        calcularBtn.getElement().executeJs("this.style.backgroundColor = 'blue'");
    }
}