package com.example.vaadin_app.view.anonymous;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
    }

    private Component createHeaderContent() {
        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10",
                "box-border", "flex", "h-xl", "items-center",
                "w-full");
        header.setHeight(header.getHeight());
        header.add(createFooter());
        return header;
    }

    private Component createFooter() {
        HorizontalLayout layout = new HorizontalLayout();

        Anchor signupLink = new Anchor("signup", "Sign up");
        Anchor loginLink = new Anchor("login", "Log in");
        layout.add(signupLink, loginLink);
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
