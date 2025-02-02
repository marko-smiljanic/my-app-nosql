package com.example.application.views.kreiranjeNalogaView;


import com.example.application.DTO.FirmaDTO;
import com.example.application.DTO.Konverzija;
import com.example.application.DTO.NalogDTO;
import com.example.application.DTO.UserDTO;
import com.example.application.entity.Firma;
import com.example.application.entity.Nalog;
import com.example.application.entity.StavkaNaloga;
import com.example.application.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.FirmaService;
import com.example.application.services.NalogService;
import com.example.application.services.StavkaNalogaService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@RolesAllowed({"ADMIN", "USER"})
@PageTitle("Kreiranje naloga")
@Route(value = "kreiranjeNaloga", layout = MainLayout.class)
public class KreiranjeNalogaView extends Div {
    @Autowired
    private NalogService nalogService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirmaService firmaService;
    @Autowired
    private StavkaNalogaService stavkaNalogaService;
    private Grid<NalogDTO> grid;
    private AuthenticatedUser authenticatedUser;
    private User ulogovani;
    private ListDataProvider<NalogDTO> dataProvider;

    public KreiranjeNalogaView(NalogService nalogService, AuthenticatedUser authenticatedUser, UserService userService, FirmaService firmaService, StavkaNalogaService stavkaNalogaService) {
        this.nalogService = nalogService;
        this.firmaService = firmaService;
        this.userService = userService;
        this.stavkaNalogaService = stavkaNalogaService;
        this.authenticatedUser = authenticatedUser;
        ulogovani = authenticatedUser.get().orElse(null);


        setSizeFull();
        iscrtajUlogovanogKorisnka();
        iscrtajDugmeDodaj();
        iscrtajGrid();
    }

    private void iscrtajGrid() {
        //Kreiranje grida
        grid = new Grid<>(NalogDTO.class, false);
        grid.addColumn(NalogDTO::getVremeKreiranja).setHeader("Vreme kreiranja");
        //grid.addColumn(NalogDTO::getUser).setHeader("User");
        grid.addColumn(nalog -> {
            UserDTO userDTO = nalog.getUser();
            if (userDTO != null) {
                return userDTO.getUsername() + " (" + userDTO.getIme() + " " + userDTO.getPrezime() + ")";
            } else {
                return "N/A";
            }
        }).setHeader("User");
        //grid.addColumn(NalogDTO::getFirma).setHeader("Firma");
        grid.addColumn(nalog -> {
            FirmaDTO firmaDTO = nalog.getFirma();
            if (firmaDTO != null) {
                return firmaDTO.getNaziv();
            } else {
                return "N/A";
            }
        }).setHeader("Firma");
        grid.setSizeFull();

//        grid.addColumn(new ComponentRenderer<>(nalogDTO -> {
//            HorizontalLayout layout = new HorizontalLayout();
//            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
//            editButton.addClickListener(e -> editNalog(nalogDTO));
//            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
//
//            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
//            deleteButton.addClickListener(e -> deleteNalog(nalogDTO.getId()));
//            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
//
//            layout.add(editButton, deleteButton);
//            return layout;
//        })).setHeader("Actions");

        //grid.setItems(query -> nalogService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
//        List<UserDTO> users = userService.findAll2();
//        grid.setItems(users);


        dataProvider = new ListDataProvider<>(nalogService.findByUserId(ulogovani.getId()));
        grid.setDataProvider(dataProvider);


        add(grid);
    }

    private void iscrtajUlogovanogKorisnka(){
        H2 h2 = new H2("Trenutno ulogovani korisnik:  " + ulogovani.getUsername());
        H4 h4 = new H4("Uloge:  " + ulogovani.getRoles());

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("100%");
        //verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(h2);
        verticalLayout.add(h4);
        add(verticalLayout);
    }

    private void iscrtajDugmeDodaj(){
        Button addButton = new Button("Dodaj", event -> addNalog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.add(addButton);
        add(horizontalLayout);
    }

    private void deleteNalog(Long id){
        nalogService.deleteById(id);
        this.osveziPrikaz();
    }

    private void osveziPrikaz(){
        dataProvider = new ListDataProvider<>(nalogService.findByUserId(ulogovani.getId()));
        grid.setDataProvider(dataProvider);
    }


    private void addNalog(){
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        Binder<Nalog> binder = new Binder<>(Nalog.class);

        DateTimePicker vremeField = new DateTimePicker("Datum i vreme kreiranja naloga");
        vremeField.setReadOnly(true);
        vremeField.setValue(LocalDateTime.now());

        TextField usernameField = new TextField("Korisničko ime usera");
        usernameField.setReadOnly(true);
        usernameField.setValue(ulogovani.getUsername());


        ComboBox<Firma> firmaComboBox = new ComboBox<>("Firma");
        firmaComboBox.setItems(firmaService.findAll());
        firmaComboBox.setItemLabelGenerator(firma ->
                firma.getNaziv() + " (" + firma.getPib() + ")"
        );
        firmaComboBox.setRequiredIndicatorVisible(true);

        //multi select za stavke naloga
        MultiSelectComboBox<StavkaNaloga> stavkaMultiSelectComboBox = new MultiSelectComboBox<>("Stavka naloga");
        stavkaMultiSelectComboBox.setItems(stavkaNalogaService.findAll());
        stavkaMultiSelectComboBox.setItemLabelGenerator(stavka -> "(" + stavka.getId() + ") " + "Roba: " + stavka.getRoba().getNaziv() + ", kolicina: " + stavka.getKolicina());

        binder.forField(vremeField)
                .asRequired("Field is required")
                .bind(Nalog::getVremeKreiranja, Nalog::setVremeKreiranja);

        binder.forField(usernameField)
                .asRequired("Field is required")
                .bind(
                        nalog -> nalog.getUser() != null ? nalog.getUser().getUsername() : "",
                        (nalog, username) -> {} //ne treba postaviti, korisnik je dohvacen globalno
                );

        binder.forField(firmaComboBox)
                .asRequired("Field is required")
                .bind(
                        Nalog::getFirma,
                        Nalog::setFirma
                );

        binder.forField(stavkaMultiSelectComboBox)
                .asRequired("Field is required")
                .bind(Nalog::getStavkeNaloga, Nalog::setStavkeNaloga);


        Button saveButton = new Button("Save", event -> {
            Nalog nalog = new Nalog();
            //nalog.setVremeKreiranja(LocalDateTime.now());   //ipak cu da setujem u formi i da bindujem odande
            nalog.setUser(ulogovani);

            if (binder.writeBeanIfValid(nalog)) {
                nalogService.create(nalog);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill in all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new VerticalLayout(vremeField, usernameField, firmaComboBox, stavkaMultiSelectComboBox, saveButton, cancelButton));

        dialog.open();
    }



}

