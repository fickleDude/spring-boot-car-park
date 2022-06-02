package com.example.vaadin_app.view.anonymous;

import com.example.vaadin_app.service.UserDetailsServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("signup")
@AnonymousAllowed
public class SignUpView extends FormLayout {
    TextField username;
    PasswordField password;

    @Autowired
    UserDetailsServiceImpl service;

    public SignUpView() {
        this.username = new TextField("Username");
        this.password = new PasswordField("Password");

        add(username, password);

        setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("500px", 2)
        );

        add(new Button("Sing in", e -> service.signup(username.getValue(), password.getValue())));
    }
}
