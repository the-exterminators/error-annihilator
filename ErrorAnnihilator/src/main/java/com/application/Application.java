package com.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
@Theme(value="ErrorAnnihilator")
@PWA(
        name="ErrorAnnihilator",
        shortName = "E.A.",
        offlinePath = "offline.html",
        offlineResources = {"images/logo.png", "images/offline.png"}
)
public class Application implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Hello world!");
    }
}