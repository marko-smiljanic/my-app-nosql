package com.example.application.views.about;

import com.example.application.entity.Role;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("About")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {
    private AuthenticatedUser authenticatedUser;

    public AboutView(AuthenticatedUser authenticatedUser) {

        this.authenticatedUser = authenticatedUser;
        //ovo je primer kako se moze raditi provera prava pristupa za odredjene delove komponente
        if (authenticatedUser.hasRole(Role.USER) && authenticatedUser.hasRole(Role.ADMIN)){
            add(new H2("Ti si i --USER i --ADMIN."));
        }else {
            if (authenticatedUser.hasRole(Role.USER)) {
                add(new H2("Ti si --USER. Za Dodatne funkcionalnosti uloguj se kao ADMIN."));
            } else if (authenticatedUser.hasRole(Role.ADMIN)) {
                add(new H2("Ti si --ADMIN."));
            }
            else {
                add(new H2("Nisi ulogovan. Za Dodatne funkcionalnosti uloguj se."));
            }
        }


        setSpacing(false);
        //Image img = new Image("images/empty-plant.png", "placeholder plant");
        //img.setWidth("200px");
        //add(img);

       // H2 header = new H2("This place intentionally left empty");
        //header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        //add(header);
        //add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
