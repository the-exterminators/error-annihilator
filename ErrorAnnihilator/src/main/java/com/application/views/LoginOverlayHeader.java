
package com.application.views;

import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | ErrorAnnihilator")
@AnonymousAllowed
public class LoginOverlayHeader extends VerticalLayout implements BeforeEnterObserver {



	public LoginOverlayHeader(){
            addClassName("login-view");
            setSizeFull();
            LoginOverlay loginOverlay = new LoginOverlay();
            loginOverlay.setTitle("ErrorAnnihilator");
            loginOverlay.setDescription("♥ We will get rid of your bugs ♥");

            add(loginOverlay);
            loginOverlay.setOpened(true);
            loginOverlay.getElement().setAttribute("no-autofocus", "");
            setJustifyContentMode(JustifyContentMode.CENTER);
            loginOverlay.setAction("login");

            //Forget password Button set to not visible for the moment
            loginOverlay.setForgotPasswordButtonVisible(false);

        }

        @Override
        public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
            // inform the user about an authentication error
            if(beforeEnterEvent.getLocation()
                    .getQueryParameters()
                    .getParameters()
                    .containsKey("error")) {
                        Notification notification = new Notification(
                            "Username or password incorrect!",
                            3000,
                            Notification.Position.MIDDLE);
                        notification.open();
            };
        }

    }


