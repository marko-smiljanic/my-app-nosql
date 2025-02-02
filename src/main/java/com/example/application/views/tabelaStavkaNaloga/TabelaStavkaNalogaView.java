package com.example.application.views.tabelaStavkaNaloga;

import com.example.application.DTO.Konverzija;
import com.example.application.DTO.NalogDTO;
import com.example.application.DTO.RobaDTO;
import com.example.application.DTO.StavkaNalogaDTO;
import com.example.application.entity.Nalog;
import com.example.application.entity.Roba;
import com.example.application.entity.StavkaNaloga;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.NalogService;
import com.example.application.services.RobaService;
import com.example.application.services.StavkaNalogaService;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


@RolesAllowed("ADMIN")
@PageTitle("Stavke naloga")
@Route(value = "stavkeNaloga", layout = MainLayout.class)
public class TabelaStavkaNalogaView extends Div {
    @Autowired
    StavkaNalogaService stavkaNalogaService;
    @Autowired
    private NalogService nalogService;
    @Autowired
    private RobaService robaService;
    private Grid<StavkaNalogaDTO> grid;
    private AuthenticatedUser authenticatedUser;

    public TabelaStavkaNalogaView(StavkaNalogaService stavkaNalogaService, NalogService nalogService, RobaService robaService, AuthenticatedUser authenticatedUser) {
        this.stavkaNalogaService = stavkaNalogaService;
        this.nalogService = nalogService;
        this.robaService = robaService;
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        iscrtajDugmeDodaj();
        iscrtajGrid();
    }

    private void iscrtajGrid() {
        //Kreiranje grida
        grid = new Grid<>(StavkaNalogaDTO.class, false);
        grid.addColumn(StavkaNalogaDTO::getKolicina).setHeader("Kolicina");
        //grid.addColumn(NalogDTO::getUser).setHeader("User");
        grid.addColumn(stavkaNaloga -> {
            NalogDTO nalogDTO = stavkaNaloga.getNalog();
            if (nalogDTO != null) {
                //return nalogDTO.getUser().getUsername() + " (" + nalogDTO.getUser().getIme() + " " + nalogDTO.getUser().getPrezime() + ")";
                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + nalogDTO.getUser().getUsername());    //rado
                return "Id: " + nalogDTO.getId() + ", kreiran: " + nalogDTO.getVremeKreiranja();
            } else {
                return "N/A";
            }
        }).setHeader("Nalog");
        grid.addColumn(stavkaNaloga -> {
            RobaDTO robaDTO = stavkaNaloga.getRoba();
            if (robaDTO != null) {
                return robaDTO.getNaziv() + " (Sifra: " + robaDTO.getSifra() + ")";
            } else {
                return "N/A";
            }
        }).setHeader("Roba");
        grid.setSizeFull();

        grid.addColumn(new ComponentRenderer<>(stavkaNalogaDTO -> {
            HorizontalLayout layout = new HorizontalLayout();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> editStavkaNaloga(stavkaNalogaDTO));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addClickListener(e -> deleteStavkaNaloga(stavkaNalogaDTO.getId()));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            layout.add(editButton, deleteButton);
            return layout;
        })).setHeader("Actions");

        grid.setItems(query -> stavkaNalogaService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
        add(grid);
    }

    private void iscrtajDugmeDodaj(){
        Button addButton = new Button("Dodaj", event -> addStavkaNaloga());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.add(addButton);
        add(horizontalLayout);
    }


    private void addStavkaNaloga(){
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        Binder<StavkaNaloga> binder = new Binder<>(StavkaNaloga.class);

        IntegerField kolicinaField = new IntegerField("Kolicina");
        kolicinaField.setRequiredIndicatorVisible(true);

        TextField nalogField = new TextField("Id naloga");
        nalogField.setRequiredIndicatorVisible(true);

        TextField robaField = new TextField("Sifra robe");
        robaField.setRequiredIndicatorVisible(true);


        binder.forField(kolicinaField)
                .asRequired("Field is required")
                .withValidator(new IntegerRangeValidator("Field must be positive.", 0, Integer.MAX_VALUE))
                .bind(StavkaNaloga::getKolicina, StavkaNaloga::setKolicina);

        binder.forField(nalogField)
                .asRequired("Field is required")
                .bind (
                    stavkaNaloga -> stavkaNaloga.getNalog() != null ? String.valueOf(stavkaNaloga.getNalog().getId()) : "",
                    (stavkaNaloga, nalogId) -> {
                        if(nalogId != null){
                            Optional<Nalog> nalog = nalogService.findById(Long.parseLong(nalogId));
                            stavkaNaloga.setNalog(nalog.orElse(null));
                        }else{
                            stavkaNaloga.setNalog(null);
                        }
                    }
                );

        binder.forField(robaField)
                .asRequired("Field is required")
                .bind (
                    stavkaNaloga -> stavkaNaloga.getRoba() != null ? stavkaNaloga.getRoba().getSifra() : "",
                    (stavkaNaloga, robaSifra) -> {
                        if(robaSifra != null){
                            Roba roba = robaService.findBySifra(robaSifra);
                            stavkaNaloga.setRoba(roba);
                        }else{
                            stavkaNaloga.setRoba(null);
                        }
                    }
                );

        Button saveButton = new Button("Save", event -> {
            StavkaNaloga stavkaNaloga = new StavkaNaloga();
            if (binder.writeBeanIfValid(stavkaNaloga)) {
                stavkaNalogaService.create(stavkaNaloga);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(kolicinaField, nalogField, robaField, saveButton, cancelButton));
        dialog.open();
    }



    private void editStavkaNaloga(StavkaNalogaDTO dto){
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        Binder<StavkaNaloga> binder = new Binder<>(StavkaNaloga.class);

        IntegerField kolicinaField = new IntegerField("Kolicina");
        kolicinaField.setRequiredIndicatorVisible(true);

        TextField nalogField = new TextField("Id naloga");
        nalogField.setRequiredIndicatorVisible(true);

        TextField robaField = new TextField("Sifra robe");
        robaField.setRequiredIndicatorVisible(true);


        binder.forField(kolicinaField)
                .asRequired("Field is required")
                .withValidator(new IntegerRangeValidator("Field must be positive.", 0, Integer.MAX_VALUE))
                .bind(StavkaNaloga::getKolicina, StavkaNaloga::setKolicina);

        binder.forField(nalogField)
                .asRequired("Field is required")
                .bind (
                        stavkaNaloga -> stavkaNaloga.getNalog() != null ? String.valueOf(stavkaNaloga.getNalog().getId()) : "",
                        (stavkaNaloga, nalogId) -> {
                            if(nalogId != null){
                                Optional<Nalog> nalog = nalogService.findById(Long.parseLong(nalogId));
                                stavkaNaloga.setNalog(nalog.orElse(null));
                            }else{
                                stavkaNaloga.setNalog(null);
                            }
                        }
                );

        binder.forField(robaField)
                .asRequired("Field is required")
                .bind (
                        stavkaNaloga -> stavkaNaloga.getRoba() != null ? stavkaNaloga.getRoba().getSifra() : "",
                        (stavkaNaloga, robaSifra) -> {
                            if(robaSifra != null){
                                Roba roba = robaService.findBySifra(robaSifra);
                                stavkaNaloga.setRoba(roba);
                            }else{
                                stavkaNaloga.setRoba(null);
                            }
                        }
                );

        //da popuni polja inicijalno u formu
        StavkaNaloga nn = Konverzija.konvertujUEntitet(dto, StavkaNaloga.class);
        binder.readBean(nn);

        Button saveButton = new Button("Save", event -> {
            StavkaNaloga stavkaNaloga = new StavkaNaloga();
            if (binder.writeBeanIfValid(stavkaNaloga)) {
                stavkaNaloga.setId(nn.getId());
                stavkaNalogaService.update(stavkaNaloga);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(kolicinaField, nalogField, robaField, saveButton, cancelButton));
        dialog.open();
    }
    private void deleteStavkaNaloga(Long id){
        stavkaNalogaService.deleteById(id);
        osveziPrikaz();
    }
    private void osveziPrikaz(){
        grid.setItems(query -> stavkaNalogaService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
    }

}
