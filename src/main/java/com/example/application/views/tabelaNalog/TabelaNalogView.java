package com.example.application.views.tabelaNalog;

import com.example.application.DTO.*;
import com.example.application.entity.Firma;
import com.example.application.entity.Nalog;
import com.example.application.entity.Roba;
import com.example.application.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.FirmaService;
import com.example.application.services.NalogService;
import com.example.application.services.RobaService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RolesAllowed("ADMIN")
@PageTitle("Nalozi")
@Route(value = "nalozi", layout = MainLayout.class)
public class TabelaNalogView extends Div {
    @Autowired
    private NalogService nalogService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirmaService firmaService;
    private Grid<NalogDTO> grid;
    private AuthenticatedUser authenticatedUser;

    public TabelaNalogView(NalogService nalogService, AuthenticatedUser authenticatedUser, UserService userService, FirmaService firmaService) {
        this.nalogService = nalogService;
        this.firmaService = firmaService;
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        iscrtajDugmeDodaj();
        iscrtajGrid();
    }
    
    private void iscrtajGrid() {
        //Kreiranje grida
        grid = new Grid<>(NalogDTO.class, false);
        grid.addColumn(NalogDTO::getVremeKreiranja).setHeader("Vreme kreiranja");
        // Kolona za User (učitavanje korisnika po ID-u)
        grid.addColumn(nalog -> {
            if (nalog.getUserId() != null) {
                return userService.findById(nalog.getUserId())
                        .map(user -> user.getUsername() + " (" + user.getIme() + " " + user.getPrezime() + ")")
                        .orElse("N/A");
            }
            return "N/A";
        }).setHeader("User");

        // Kolona za Firmu (učitavanje firme po ID-u)
        grid.addColumn(nalog -> {
            if (nalog.getFirmaId() != null) {
                return firmaService.findById(nalog.getFirmaId())
                        .map(Firma::getNaziv)
                        .orElse("N/A");
            }
            return "N/A";
        }).setHeader("Firma");

        grid.setSizeFull();

        grid.addColumn(new ComponentRenderer<>(nalogDTO -> {
            HorizontalLayout layout = new HorizontalLayout();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> editNalog(nalogDTO));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addClickListener(e -> deleteNalog(nalogDTO.getId()));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            layout.add(editButton, deleteButton);
            return layout;
        })).setHeader("Actions");

        grid.setItems(query -> nalogService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
        add(grid);
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

    private void deleteNalog(String id){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA Brisanje naloga sa ID: " + id);
        nalogService.deleteById(String.valueOf(id));
        //nalogService.deleteById(id);
        this.osveziPrikaz();
    }

    private void osveziPrikaz(){
        grid.setItems(query -> nalogService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
    }


    private void addNalog(){
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        // Kreiraj Binder za UserDTO
        Binder<Nalog> binder = new Binder<>(Nalog.class);

        // Kreiraj polja za unos
        DateTimePicker vremeField = new DateTimePicker("Datum i vreme kreiranja naloga");
        vremeField.setRequiredIndicatorVisible(true);

        TextField usernameField = new TextField("Korisničko ime usera");
        usernameField.setRequiredIndicatorVisible(true);

        TextField firmaField = new TextField("Firma pib");
        firmaField.setRequiredIndicatorVisible(true);

        //Postavka polja za binder
        binder.forField(vremeField)
                .asRequired("Field is required")
                .bind(Nalog::getVremeKreiranja, Nalog::setVremeKreiranja);

        binder.forField(usernameField)
                .asRequired("Field is required")
                .bind(
                        nalog -> nalog.getUserId() != null ? userService.findById(nalog.getUserId()).map(UserDTO::getUsername).orElse("") : "",
                        (nalog, username) -> {
                            if (username != null) {
                                User user = userService.findByUsername(username); // NE KORISTIMO Optional
                                nalog.setUserId(user != null ? user.getId() : null);
                            } else {
                                nalog.setUserId(null);
                            }
                        }
                );

        binder.forField(firmaField)
                .asRequired("Field is required")
                .bind(
                        nalog -> nalog.getFirmaId() != null ? firmaService.findById(nalog.getFirmaId()).map(Firma::getPib).orElse("") : "",
                        (nalog, firmaPib) -> {
                            if (firmaPib != null) {
                                Firma firma = firmaService.findByPib(firmaPib);
                                nalog.setFirmaId(firma != null ? firma.getId() : null); // Čuvamo samo ID firme
                            } else {
                                nalog.setFirmaId(null);
                            }
                        }
                );

        Button saveButton = new Button("Save", event -> {
            Nalog nalog = new Nalog();
            if (binder.writeBeanIfValid(nalog)) {
                //Nalog newNalog = Konverzija.konvertujUEntitet(nalogDTO, Nalog.class);
                nalogService.create(nalog);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(vremeField, usernameField, firmaField, saveButton, cancelButton));
        dialog.open();
    }


    private void editNalog(NalogDTO dto){
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        // Kreiraj Binder za UserDTO
        Binder<Nalog> binder = new Binder<>(Nalog.class);

        // Kreiraj polja za unos
        DateTimePicker vremeField = new DateTimePicker("Datum i vreme kreiranja naloga");
        vremeField.setRequiredIndicatorVisible(true);

        TextField usernameField = new TextField("Korisničko ime usera");
        //usernameField.setReadOnly(true); // Polje je samo za čitanje
        usernameField.setRequiredIndicatorVisible(true);

        TextField firmaField = new TextField("Firma pib");
        firmaField.setRequiredIndicatorVisible(true);

        //Postavka polja za binder
        binder.forField(vremeField)
                .asRequired("Field is required")
                .bind(Nalog::getVremeKreiranja, Nalog::setVremeKreiranja);

        binder.forField(usernameField)
                .asRequired("Field is required")
                .bind(
                        nalogDTO -> nalogDTO.getUserId() != null ?
                                userService.findById(nalogDTO.getUserId()).map(UserDTO::getUsername).orElse("") : "",
                        (nalogDTO, username) -> {
                            if (username != null) {
                                User user = userService.findByUsername(username);
                                nalogDTO.setUserId(user != null ? user.getId() : null); // Postavljamo samo ID
                            } else {
                                nalogDTO.setUserId(null);
                            }
                        }
                );

        binder.forField(firmaField)
                .asRequired("Field is required")
                .bind(
                        nalogDTO -> nalogDTO.getFirmaId() != null ?
                                firmaService.findById(nalogDTO.getFirmaId()).map(Firma::getPib).orElse("") : "",
                        (nalogDTO, firmaPib) -> {
                            if (firmaPib != null) {
                                Firma firma = firmaService.findByPib(firmaPib);
                                nalogDTO.setFirmaId(firma != null ? firma.getId() : null); // Postavljamo samo ID
                            } else {
                                nalogDTO.setFirmaId(null);
                            }
                        }
                );


        //da popuni polja inicijalno u formu
        Nalog nn = Konverzija.konvertujUEntitet(dto, Nalog.class);
        binder.readBean(nn);

        Button saveButton = new Button("Save", event -> {
            Nalog nalog = new Nalog();
            if (binder.writeBeanIfValid(nalog)) {
                //Nalog nalog = Konverzija.konvertujUEntitet(nalogDTO, Nalog.class);
                nalog.setId(nn.getId());
                nalogService.update(nalog);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(vremeField, usernameField, firmaField,saveButton, cancelButton));
        dialog.open();
    }




















//    private void addNalog(){
//        Dialog dialog = new Dialog();
//        dialog.setWidth("400px");
//
//        // Kreiraj Binder za UserDTO
//        Binder<NalogDTO> binder = new Binder<>(NalogDTO.class);
//
//        // Kreiraj polja za unos
//        DateTimePicker vremeField = new DateTimePicker("Datum i vreme kreiranja naloga");
//        vremeField.setRequiredIndicatorVisible(true);
//        vremeField.setErrorMessage("Field is required!!!");
//
//        TextField usernameField = new TextField("Korisničko ime usera");
//        //usernameField.setReadOnly(true); // Polje je samo za čitanje
//
//        TextField firmaField = new TextField("Firma pib");
//        //firmaField.setRequired(true);
//        //firmaField.setRequiredIndicatorVisible(true);
//        //firmaField.setErrorMessage("Field is required!!!");
//
//
////        TextField sifraField = new TextField("Sifra");
////        sifraField.setRequired(true);
////        sifraField.setRequiredIndicatorVisible(true);
////        sifraField.setErrorMessage("Field is required!!!");
//
//        //Postavka polja za binder
//        binder.forField(vremeField)
//                .asRequired("Field is required")
//                .bind(NalogDTO::getVremeKreiranja, NalogDTO::setVremeKreiranja);
//
////        binder.forField(sifraField)
////                .asRequired("Field is required")
////                .bind(NalogDTO::getSifra, NalogDTO::setSifra);
//
//        binder.forField(usernameField)
//                .bind(
//                    nalogDTO -> nalogDTO.getUser() != null ? nalogDTO.getUser().getUsername() : "",
//                    (nalogDTO, username) -> {
//                        if (username != null) {
//                            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   " + username);
//                            User user = userService.findByUsername(username);
//                            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   " + user.getUsername());
//                            UserDTO userDTO = Konverzija.konvertujUDto(user, UserDTO.class);    //ovde nastaje problem, kontam zbog previse konvertovanja
//                            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA   " + userDTO.getUsername());
//                            nalogDTO.setUser(userDTO);              // Postavi UserDTO u NalogDTO
//                        } else {
//                            nalogDTO.setUser(null);                  // Ako je username prazan, setuj null
//                        }
//                    }
//                );
//
//        binder.forField(firmaField)
//                .bind(
//                    nalogDTO -> nalogDTO.getFirma() != null ? nalogDTO.getFirma().getPib() : "",
//                    (nalogDTO, firmaPib) -> {
//                        if (firmaPib != null) {
////                            Firma firma = firmaService.findByPib(firmaPib);
////                            FirmaDTO firmaDTO = Konverzija.konvertujUDto(firma, FirmaDTO.class);
//                            nalogDTO.setFirma(null);
//                        } else {
//                            nalogDTO.setFirma(null); // Ako je naziv firme prazan, postavi na null
//                        }
//                    }
//                );
//
//        Button saveButton = new Button("Save", event -> {
//            NalogDTO nalogDTO = new NalogDTO();
//            if (binder.writeBeanIfValid(nalogDTO)) {
//                Nalog newNalog = Konverzija.konvertujUEntitet(nalogDTO, Nalog.class);
//                nalogService.create(newNalog);
//
//                this.osveziPrikaz();
//                dialog.close();
//            } else {
//                Notification.show("Please fill all required fields.");
//            }
//        });
//
//        Button cancelButton = new Button("Cancel", event -> dialog.close());
//
//        dialog.add(new VerticalLayout(vremeField, usernameField, firmaField,saveButton, cancelButton));
//        dialog.open();
//    }




}
