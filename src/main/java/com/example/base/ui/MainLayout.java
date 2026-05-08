package com.example.base.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        addToDrawer(createHeader(), new Scroller(createSideNav()));
        this.addClassName("color-orange-text");
    }

    private Component createHeader() {
        String imageUrl = fetchUserImageUrl("e043394");

        Component appLogo;
        if (!imageUrl.isBlank()) {
            var image = new Image(imageUrl, "App logo");
            image.setWidth("48px");
            appLogo = image;
        } else {
            var icon = VaadinIcon.CUBES.create();
            icon.setSize("48px");
            icon.setColor("green");
            appLogo = icon;
        }

        var appName = new Span("My Application");
        appName.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var header = new VerticalLayout(appLogo, appName);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(LumoUtility.Margin.Horizontal.MEDIUM);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }

    private String fetchUserImageUrl(String idUser) {
        String token = fetchToken();
        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
            String encoded = URLEncoder.encode(idUser, StandardCharsets.UTF_8);
            var uri = URI.create("https://svc-0023-00-microservicios-des.apps.infraprev.igrupobbva/image?iduser=" + encoded);
            var builder = HttpRequest.newBuilder().uri(uri).GET().timeout(Duration.ofSeconds(5));
            if (!token.isBlank()) {
                builder.header("Authorization", "Bearer " + token);
            }
            var request = builder.build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                var body = response.body().trim();
                if (!body.isEmpty()) {
                    ImageResponseDTO imageResponse = deserializeImageResponse(body);
                    if (imageResponse.getImagePah() != null && !imageResponse.getImagePah().isBlank()) {
                        return imageResponse.getImagePah();
                    }
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception ignored) {
            // Silenciar errores y usar fallback
        }
        return "";
    }

    private String fetchToken() {
        String url = "https://sso-picasso-des.apps.infraprev.igrupobbva/auth/realms/Opplus/protocol/openid-connect/token";
        String form = "grant_type=password&client_id=svc-0023-00&client_secret=a8d376ac-5ba1-42b4-94d6-5a3ba5461e34&username=svc-0023-00-writer&password=";

        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
            var uri = URI.create(url);
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body();
                TokenResponseDTO tokenResponse = deserializeTokenResponse(body);
                if (tokenResponse.getAccessToken() != null && !tokenResponse.getAccessToken().isBlank()) {
                    return tokenResponse.getAccessToken();
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception ignored) {
            // Silenciar errores y usar fallback
        }
        return "";
    }

    private static class ImageResponseDTO {
        private final String imagePah;

        ImageResponseDTO(String imagePah) {
            this.imagePah = imagePah;
        }

        String getImagePah() {
            return imagePah != null ? imagePah : "";
        }
    }

    private static class TokenResponseDTO {
        private final String accessToken;

        TokenResponseDTO(String accessToken) {
            this.accessToken = accessToken;
        }

        String getAccessToken() {
            return accessToken != null ? accessToken : "";
        }
    }

    private ImageResponseDTO deserializeImageResponse(String json) {
        String imagePah = extractValue(json, "imagePah");
        return new ImageResponseDTO(imagePah);
    }

    private TokenResponseDTO deserializeTokenResponse(String json) {
        String accessToken = extractValue(json, "access_token");
        return new TokenResponseDTO(accessToken);
    }

    private String extractValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
