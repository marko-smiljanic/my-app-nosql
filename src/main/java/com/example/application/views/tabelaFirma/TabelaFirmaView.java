package com.example.application.views.tabelaFirma;


import com.example.application.DTO.FirmaDTO;
import com.example.application.DTO.Konverzija;
import com.example.application.DTO.RobaDTO;
import com.example.application.entity.Firma;
import com.example.application.entity.Nalog;
import com.example.application.entity.Roba;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.FirmaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed("ADMIN")
@PageTitle("Firme")
@Route(value = "firme", layout = MainLayout.class)
public class TabelaFirmaView extends Div {
    @Autowired
    private FirmaService firmaService;
    private Grid<FirmaDTO> grid;
    private AuthenticatedUser authenticatedUser;


    public TabelaFirmaView(FirmaService firmaService, AuthenticatedUser authenticatedUser) {
        this.firmaService = firmaService;
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        iscrtajDugmeDodaj();
        iscrtajGrid();
    }

    private void iscrtajGrid() {
        //Kreiranje grida
        grid = new Grid<>(FirmaDTO.class, false);
        grid.addColumn(FirmaDTO::getNaziv).setHeader("Naziv");
        grid.addColumn(FirmaDTO::getPib).setHeader("PIB");
        grid.setSizeFull();

        grid.addColumn(new ComponentRenderer<>(firmaDTO -> {
            HorizontalLayout layout = new HorizontalLayout();

            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> editFirma(firmaDTO));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addClickListener(e -> deleteFirma(firmaDTO.getId()));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            layout.add(editButton, deleteButton);
            return layout;
        })).setHeader("Actions");

        grid.setItems(query -> firmaService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
        add(grid);
    }

    private void iscrtajDugmeDodaj(){
        Button addButton = new Button("Dodaj", event -> addFirma());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.add(addButton);
        add(horizontalLayout);
    }

    private void addFirma(){
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Kreiraj Binder zza entitet
        Binder<Firma> binder = new Binder<>(Firma.class);

        // Kreiraj polja za unos
        TextField nazivField = new TextField("Naziv");
        nazivField.setRequiredIndicatorVisible(true);

        TextField pibField = new TextField("PIB");
        pibField.setRequiredIndicatorVisible(true);

        //Postavka polja za binder
        binder.forField(nazivField)
                .asRequired("Field is required")
                .bind(Firma::getNaziv, Firma::setNaziv);

        binder.forField(pibField)
                .withValidator(new StringLengthValidator("Pib number must contain exactly 9 numbers", 9, 9))
                .withValidator(new RegexpValidator("Phone number must contain only numbers", "\\d{9}"))
                .asRequired("Field is required")
                .bind(Firma::getPib, Firma::setPib);

        Button saveButton = new Button("Save", event -> {
            Firma firma = new Firma();
            if (binder.writeBeanIfValid(firma)) {
                firmaService.create(firma);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(nazivField, pibField, saveButton, cancelButton));
        dialog.open();
    }

    private void editFirma(FirmaDTO dto){
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Kreiraj Binder zza entitet
        Binder<Firma> binder = new Binder<>(Firma.class);

        // Kreiraj polja za unos
        TextField nazivField = new TextField("Naziv");
        nazivField.setRequiredIndicatorVisible(true);

        TextField pibField = new TextField("PIB");
        pibField.setRequiredIndicatorVisible(true);

        //Postavka polja za binder
        binder.forField(nazivField)
                .asRequired("Field is required")
                .bind(Firma::getNaziv, Firma::setNaziv);

        binder.forField(pibField)
                .withValidator(new StringLengthValidator("Pib number must contain exactly 9 numbers", 9, 9))
                .withValidator(new RegexpValidator("Phone number must contain only numbers", "\\d{9}"))
                .asRequired("Field is required")
                .bind(Firma::getPib, Firma::setPib);


        //da popuni polja inicijalno u formu
        Firma nn = Konverzija.konvertujUEntitet(dto, Firma.class);
        binder.readBean(nn);


        Button saveButton = new Button("Save", event -> {
            Firma firma = new Firma();
            if (binder.writeBeanIfValid(firma)) {
                firma.setId(nn.getId());
                firmaService.update(firma);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(nazivField, pibField, saveButton, cancelButton));
        dialog.open();
    }

    private void deleteFirma(Long id){
        firmaService.deleteById(id);
        osveziPrikaz();
    }
    private void osveziPrikaz(){
        grid.setItems(query -> firmaService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
    }










}
