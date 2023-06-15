
package com.application.views;

        import com.vaadin.flow.component.login.LoginOverlay;
        import com.vaadin.flow.component.html.Div;
        import com.vaadin.flow.router.Route;

@Route("login-overlay-header")
public class LoginOverlayHeader extends Div {

    public LoginOverlayHeader() {
        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setTitle("ErrorAnnihilator");
        loginOverlay.setDescription("♥ We will get rid of your bugs ♥");
        add(loginOverlay);
        loginOverlay.setOpened(true);
        loginOverlay.getElement().setAttribute("no-autofocus", "");
    }

}


