package com.example.application.views.tabelaRoba;

import com.example.application.DTO.RobaDTO;
import com.example.application.entity.Roba;
import com.example.application.services.RobaService;
import com.vaadin.flow.component.html.Div;
import com.example.application.DTO.Konverzija;

import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed("ADMIN")
@PageTitle("Roba")
@Route(value = "roba", layout = MainLayout.class)
public class TabelaRobaView extends Div {
    @Autowired
    private RobaService robaService;
    private Grid<RobaDTO> grid;
    private AuthenticatedUser authenticatedUser;


    public TabelaRobaView(RobaService robaService, AuthenticatedUser authenticatedUser) {
        this.robaService = robaService;
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        iscrtajDugmeDodaj();
        iscrtajGrid();
    }

    private void iscrtajGrid() {
        //Kreiranje grida
        grid = new Grid<>(RobaDTO.class, false);
        grid.addColumn(RobaDTO::getNaziv).setHeader("Naziv");
        grid.addColumn(RobaDTO::getSifra).setHeader("Sifra");
        grid.setSizeFull();

        grid.addColumn(new ComponentRenderer<>(robaDto -> {
            HorizontalLayout layout = new HorizontalLayout();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> editRoba(robaDto));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addClickListener(e -> deleteRoba(robaDto.getId()));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            layout.add(editButton, deleteButton);
            return layout;
        })).setHeader("Actions");

        grid.setItems(query -> robaService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
        add(grid);
    }

    private void iscrtajDugmeDodaj(){
        Button addButton = new Button("Dodaj", event -> addRoba());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.add(addButton);
        add(horizontalLayout);
    }

    private void addRoba() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Kreiraj Binder za UserDTO
        Binder<RobaDTO> binder = new Binder<>(RobaDTO.class);

        // Kreiraj polja za unos
        TextField nazivField = new TextField("Naziv");
        nazivField.setRequired(true);
        nazivField.setRequiredIndicatorVisible(true);
        nazivField.setErrorMessage("Field is required!!!");

        TextField sifraField = new TextField("Sifra");
        sifraField.setRequired(true);
        sifraField.setRequiredIndicatorVisible(true);
        sifraField.setErrorMessage("Field is required!!!");

        //Postavka polja za binder
        binder.forField(nazivField)
                .asRequired("Field is required")
                .bind(RobaDTO::getNaziv, RobaDTO::setNaziv);

        binder.forField(sifraField)
                .asRequired("Field is required")
                .bind(RobaDTO::getSifra, RobaDTO::setSifra);

        Button saveButton = new Button("Save", event -> {
            RobaDTO robaDTO = new RobaDTO();
            if (binder.writeBeanIfValid(robaDTO)) {
                Roba newRoba = Konverzija.konvertujUEntitet(robaDTO, Roba.class);
                robaService.create(newRoba);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(nazivField, sifraField, saveButton, cancelButton));
        dialog.open();
    }

    private void deleteRoba(Long id){
        this.robaService.deleteById(id);
        this.osveziPrikaz();
    }

    private void editRoba(RobaDTO dto){
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Kreiraj Binder za UserDTO
        Binder<RobaDTO> binder = new Binder<>(RobaDTO.class);

        // Kreiraj polja za unos
        TextField nazivField = new TextField("Naziv");
        nazivField.setRequired(true);
        nazivField.setRequiredIndicatorVisible(true);
        nazivField.setErrorMessage("Field is required!!!");

        TextField sifraField = new TextField("Sifra");
        sifraField.setRequired(true);
        sifraField.setRequiredIndicatorVisible(true);
        sifraField.setErrorMessage("Field is required!!!");

        //Postavka polja za binder
        binder.forField(nazivField)
                .asRequired("Field is required")
                .bind(RobaDTO::getNaziv, RobaDTO::setNaziv);

        binder.forField(sifraField)
                .asRequired("Field is required")
                .bind(RobaDTO::getSifra, RobaDTO::setSifra);

        //Postavlja inicijalnu vrednost forme odnosno postavlja trenutne vrednosti iz dto objekta
        binder.readBean(dto);

        Button saveButton = new Button("Save", event -> {
            RobaDTO robaDTO = new RobaDTO();
            if (binder.writeBeanIfValid(robaDTO)) {
                Roba n = Konverzija.konvertujUEntitet(robaDTO, Roba.class);
                n.setId(dto.getId());
                robaService.update(n);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(nazivField, sifraField, saveButton, cancelButton));
        dialog.open();
    }

    private void osveziPrikaz(){
        grid.setItems(query -> robaService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
    }

}
