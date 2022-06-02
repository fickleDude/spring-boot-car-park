package com.example.vaadin_app.view.user;

import com.example.vaadin_app.security.AuthenticatedUser;
import com.example.vaadin_app.view.admin.AutoRequest;
import com.example.vaadin_app.view.admin.PersonnelRequest;
import com.example.vaadin_app.view.admin.RouteRequest;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;

@RolesAllowed({"USER", "ADMIN"})
public class MainLayoutForUsers extends AppLayout implements HasDynamicTitle {
    //A simple navigation item component, based on ListItem element.
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames("font-medium", "text-s");

            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }
    }

    private H1 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayoutForUsers(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");


        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        header.setHeight(header.getHeight());
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("Car park tables");
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        for (MainLayoutForUsers.MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }

        }
        return nav;
    }

    private MainLayoutForUsers.MenuItemInfo[] createMenuItems() {
        return new MainLayoutForUsers.MenuItemInfo[]{
                new MainLayoutForUsers.MenuItemInfo("Personnel", PersonnelRequest.class),
                new MainLayoutForUsers.MenuItemInfo("Auto",  AutoRequest.class),
                new MainLayoutForUsers.MenuItemInfo("Route",  RouteRequest.class)
        };
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        Button logout = new Button("Logout", e -> authenticatedUser.logout());
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        layout.add(logout);
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getPageTitle());
    }

    @Override
    public String getPageTitle() {
        String title = "TODAY   IS   " + LocalDate.now().getDayOfMonth() + "th   OF   "
                + LocalDate.now().getMonth().toString();
        return title;
    }
}
