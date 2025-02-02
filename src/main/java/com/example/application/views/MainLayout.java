package com.example.application.views;

import com.example.application.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.about.AboutView;
import com.example.application.views.kreiranjeNalogaView.KreiranjeNalogaView;
import com.example.application.views.tabelaFirma.TabelaFirmaView;
import com.example.application.views.tabelaNalog.TabelaNalogView;
import com.example.application.views.tabelaRoba.TabelaRobaView;
import com.example.application.views.tabelaKorisnika.TabelaKorisnikaView;
import com.example.application.views.tabelaStavkaNaloga.TabelaStavkaNalogaView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    //dodavanje dugmeta za zatvaranje i otvaranje bocnog menija i naslov stranice u zaglalvju
    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    //dodaje naslov aplikacije u bicno meni i navigacije koristeci scroller za pomeranje
    private void addDrawerContent() {
        Span appName = new Span("My App");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    //kreira bocni meni sidenav i ovde ukljucujem moje view-e
    private SideNav createNavigation() {
        SideNav nav = new SideNav();


        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.FILE.create()));

        }
//        if (accessChecker.hasAccess(PersonFormView.class)) {
//            nav.addItem(new SideNavItem("Person Form", PersonFormView.class, LineAwesomeIcon.USER.create()));
//
//        }
//        if (accessChecker.hasAccess(CheckoutFormView.class)) {
//            nav.addItem(new SideNavItem("Checkout Form", CheckoutFormView.class, LineAwesomeIcon.CREDIT_CARD.create()));
//
//        }

        if (accessChecker.hasAccess(TabelaKorisnikaView.class)) {
            nav.addItem(new SideNavItem("Korisnici", TabelaKorisnikaView.class, LineAwesomeIcon.USER.create()));
        }
        if (accessChecker.hasAccess(TabelaRobaView.class)) {
            nav.addItem(new SideNavItem("Roba", TabelaRobaView.class, LineAwesomeIcon.BOX_OPEN_SOLID.create()));
        }
        if (accessChecker.hasAccess(TabelaNalogView.class)) {
            nav.addItem(new SideNavItem("Nalozi", TabelaNalogView.class, LineAwesomeIcon.FILE_ALT_SOLID.create()));
        }
        if (accessChecker.hasAccess(TabelaFirmaView.class)) {
            nav.addItem(new SideNavItem("Firme", TabelaFirmaView.class, LineAwesomeIcon.BUILDING.create()));
        }
        if (accessChecker.hasAccess(TabelaStavkaNalogaView.class)) {
            nav.addItem(new SideNavItem("Stavke naloga", TabelaStavkaNalogaView.class, LineAwesomeIcon.FOLDER.create()));
        }
        if (accessChecker.hasAccess(KreiranjeNalogaView.class)) {
            nav.addItem(new SideNavItem("Kreiraj nalog", KreiranjeNalogaView.class, LineAwesomeIcon.PLUS_SOLID.create()));
        }


        return nav;
    }

    //donji deo sa avatarom korisnika i opcijom za odjavu ako je korisnik prijavljen, ako nije prikazuje signin link
    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getIme());
            if(user.getProfilePicture() != null) {
                StreamResource resource = new StreamResource("profile-pic",
                        () -> new ByteArrayInputStream(user.getProfilePicture()));
                avatar.setImageResource(resource);
                avatar.setThemeName("xsmall");
                avatar.getElement().setAttribute("tabindex", "-1");
            }

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getIme());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);


        } else {
            //anchor je vaadin komponenta koja predstalvja link <a>, prvi argument je putanja drugi tekst linka
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }


    //ovo se poziva nakon sto se izvrsi navigaicja i postalvja naslov stranice prema trenutnom prikazu
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    //dobija naslov trenutne stranice i koristi anotaciju page title u mojim view klasama
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


}
