package com.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootConfiguration
@Theme(value="ErrorAnnihilator")
@PWA(
        name="ErrorAnnihilator",
        shortName = "E.A.",
        offlinePath = "offline.html",
        offlineResources = {"images/logo.png", "images/offline.png"}
)
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}