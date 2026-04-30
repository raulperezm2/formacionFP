package com.example.examplefeature.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.time.Duration;

@Route("opplus-cookies")
@PageTitle("opplus-cookies")
@Menu(order = 4, icon = "vaadin:calc", title = "opplus cookies")
public class CookieView extends VerticalLayout {

    private static final String COOKIE_NAME = "usuario-nombre";
    private static final int COOKIE_DAYS = 7;

    public CookieView() {
        TextField nombreInput = new TextField("Tu nombre");
        Button guardarBtn = new Button("Recordarme");
        Span saludo = new Span();

        // 1. Intentar leer la cookie al cargar la vista
        String valorGuardado = leerCookie();
        if (valorGuardado != null) {
            saludo.setText("¡Hola de nuevo, " + valorGuardado + "!");
        }

        // 2. Lógica para guardar
        guardarBtn.addClickListener(e -> {
            String valor = nombreInput.getValue();
            guardarCookie(valor);
            Notification.show("Cookie guardada. Refresca la página.");
        });

        add(nombreInput, guardarBtn, saludo);
    }

    private void guardarCookie(String valor) {
        Cookie cookie = new Cookie(COOKIE_NAME, valor);
        cookie.setPath("/");
        int maxAgeSeconds = Math.toIntExact(Duration.ofDays(COOKIE_DAYS).getSeconds());
        cookie.setMaxAge(maxAgeSeconds);
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    private String leerCookie() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}