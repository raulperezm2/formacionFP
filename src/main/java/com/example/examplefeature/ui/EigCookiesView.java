package com.example.examplefeature.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.time.Duration;


@Route("eig-cookies")
@PageTitle("eig-cookies")
@Menu(order = 4, icon = "vaadin:disc", title = "eig cookies")
public class EigCookiesView  extends VerticalLayout {
    /** Nombres de las cookies*/
    private static final String[] COOKIE_NAMES = {"user-name", "user-email", "user-password"};
    /** Duración en días de las cookies */
    private static final int COOKIE_DAYS = 7;

    public EigCookiesView()
    {
        // 1. Configuración del contenedor principal
        setSpacing(true);
        setClassName("eig-cookies");
        setAlignItems(Alignment.CENTER); // Centra todo horizontalmente
        H2 title = new H2("Multiples Cookies");

        var nameInput = new TextField("Nombre: ");
        nameInput.setPrefixComponent(VaadinIcon.SPECIALIST.create());
        var emailInput = new EmailField("Email: ");
        emailInput.setPrefixComponent(VaadinIcon.MAILBOX.create());
        var passwordInput = new PasswordField("Contraseña: ");
        passwordInput.setPrefixComponent(VaadinIcon.PASSWORD.create());

        var spacing = new Hr();
        spacing.getStyle().setWidth("25%");

        var saveButton = new Button("Guardar");
        saveButton.setPrefixComponent(VaadinIcon.ADD_DOCK.create());
        var greetCointainer = new Span();

        var savedData = readCookies();
        if (savedData != null && savedData[0] != null  && !savedData[0].isBlank()) {
            var greeting = String.format("Hola de nuevo, \"%s\"\nTu email es: \"%s\"\n Tu contraseña tiene %d caracteres.", savedData[0], savedData[1], savedData[2].length());
            greetCointainer.setText(greeting);
        }
        else {
            greetCointainer.setText("Bienvenido");
        }

        saveButton.addClickListener(e -> {
            var nameValue = nameInput.getValue();
            var emailValue = emailInput.getValue();
            var passwordValue = passwordInput.getValue();
            saveCookies(new String[]{nameValue, emailValue, passwordValue});
            Notification.show("Cookies guardadas. Refresca la página.");
        });

        add(title, greetCointainer, nameInput, emailInput, passwordInput, spacing, saveButton);
    }

    private String[]  readCookies()
    {
        var cookies = VaadinService.getCurrentRequest().getCookies();
        var savedData = new String[COOKIE_NAMES.length];

        if (cookies != null)
            for (Cookie c : cookies) {
                for (int i = 0; i < COOKIE_NAMES.length; i++) {
                    if (c.getName().equals(COOKIE_NAMES[i])) {
                        savedData[i] = c.getValue();
                    }
                }
            }

        return savedData;
    }

    private void saveCookies(String[] values)
    {
        var nameCookie = new Cookie(COOKIE_NAMES[0], values[0]);
        var emailCookie = new Cookie(COOKIE_NAMES[1], values[1]);
        var passwordCookie = new Cookie(COOKIE_NAMES[2], values[2]);
        nameCookie.setPath("/");
        emailCookie.setPath("/");
        passwordCookie.setPath("/");

        int maxAgeSeconds = Math.toIntExact(Duration.ofDays(COOKIE_DAYS).getSeconds());

        nameCookie.setMaxAge(maxAgeSeconds);
        emailCookie.setMaxAge(maxAgeSeconds);
        passwordCookie.setMaxAge(maxAgeSeconds);

        VaadinService.getCurrentResponse().addCookie(nameCookie);
        VaadinService.getCurrentResponse().addCookie(emailCookie);
        VaadinService.getCurrentResponse().addCookie(passwordCookie);
    }
}
