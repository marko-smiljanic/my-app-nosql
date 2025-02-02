package com.example.application.views.tabelaKorisnika;

import com.example.application.DTO.Konverzija;
import com.example.application.DTO.UserDTO;
import com.example.application.entity.Role;
import com.example.application.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.List;

//@DenyAll
//@PermitAll
//@AnonymousAllowed
//@RolesAllowed({"ADMIN", "USER"})
@RolesAllowed("ADMIN")
@PageTitle("Korisnici")
@Route(value = "korisnici", layout = MainLayout.class)
public class TabelaKorisnikaView extends Div {
    @Autowired
    private UserService userService;
    private ListDataProvider<UserDTO> dataProvider;  //ono sto prikazujem
    private Grid<UserDTO> grid;
    private AuthenticatedUser authenticatedUser;
    @Autowired PasswordEncoder passwordEncoder;


    public TabelaKorisnikaView(UserService userService, AuthenticatedUser authenticatedUser, PasswordEncoder passwordEncoder) {
        //refreshGrid();
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;
        this.passwordEncoder = passwordEncoder;

        setSizeFull();
        iscrtajDugmeDodaj();
        iscrtajGrid();
    }

    private void iscrtajGrid() {
        //Kreiranje grida
        grid = new Grid<>(UserDTO.class, false);
        grid.addColumn(UserDTO::getUsername).setHeader("Username");
        grid.addColumn(UserDTO::getIme).setHeader("First Name");
        grid.addColumn(UserDTO::getPrezime).setHeader("Last Name");
        grid.addColumn(UserDTO::getTelefon).setHeader("Phone");
        grid.addColumn(UserDTO::getRoles).setHeader("Roles");
        grid.setSizeFull();

        //Kreiranje reda grida sa podacima i dugmadima
        grid.addColumn(new ComponentRenderer<>(userDTO -> {
            HorizontalLayout layout = new HorizontalLayout();
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> editUser(userDTO));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addClickListener(e -> deleteUser(userDTO.getId()));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

            layout.add(editButton, deleteButton);
            return layout;
        })).setHeader("Actions");

        //testirano - - RADI!!!!!!
        //provera prava pristupa mi ovde ne treba za delove koda jer je cela ruta (komponenta) zastice za rolu admin anotacijom
//        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz   " + authenticatedUser.hasRole(Role.USER));

        //bez lazy loading-a
//        dataProvider = new ListDataProvider<>(userService.findAll2());
//        grid.setDataProvider(dataProvider);

        //moram da sve pretvorim u stream ovde ili da implementiram da lazy find all ne vraca list nego stream
        grid.setItems(query -> userService.lazyFindAll(query.getPage(), query.getPageSize()).stream());

        add(grid);  // Dodaj grid u osnovnu komponentu
    }

    private void iscrtajDugmeDodaj(){
        Button addButton = new Button("Dodaj", event -> addUser());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonDodajLayout = new HorizontalLayout();
        buttonDodajLayout.setWidth("100%");
        buttonDodajLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonDodajLayout.add(addButton);
        add(buttonDodajLayout);
    }

    private void addUser() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Kreiraj Binder za UserDTO
        Binder<UserDTO> binder = new Binder<>(UserDTO.class);

        // Kreiraj polja za unos
        TextField usernameField = new TextField("Username");
        usernameField.setRequired(true);         //ovo radi samo ako nemam binder
        usernameField.setRequiredIndicatorVisible(true);
        usernameField.setErrorMessage("Field is required!!!");   //ovo radi samo ako nemam binder

        TextField imeField = new TextField("First Name");
        imeField.setRequired(true);
        imeField.setRequiredIndicatorVisible(true);
        imeField.setErrorMessage("Field is required!!!");

        TextField prezimeField = new TextField("Last Name");
        prezimeField.setRequired(true);
        prezimeField.setRequiredIndicatorVisible(true);
        prezimeField.setErrorMessage("Field is required!!!");

        TextField telefonField = new TextField("Phone");
        telefonField.setPattern("\\+?\\d*");
        telefonField.setErrorMessage("Phone number must contain only numbers and an optional '+' sign, and no space.");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setErrorMessage("Password is required!!!");

        CheckboxGroup<Role> roleCheckboxGroup = new CheckboxGroup<>("Roles");
        roleCheckboxGroup.setLabel("Select Roles");
        roleCheckboxGroup.setItems(EnumSet.allOf(Role.class));
        roleCheckboxGroup.setItemLabelGenerator(Role::name);
        roleCheckboxGroup.setRequired(true);
        roleCheckboxGroup.setErrorMessage("At least one role must be selected.");

        //Postavka polja za binder
        binder.forField(usernameField)
                .asRequired("Username is required")
                .bind(UserDTO::getUsername, UserDTO::setUsername);

        binder.forField(imeField)
                .asRequired("First Name is required")
                .bind(UserDTO::getIme, UserDTO::setIme);

        binder.forField(prezimeField)
                .asRequired("Last Name is required")
                .bind(UserDTO::getPrezime, UserDTO::setPrezime);

        binder.forField(telefonField)
                .withValidator(new StringLengthValidator("Phone number must contain only numbers and an optional '+' sign, and no space.", 0, 20))
                .withValidator(new RegexpValidator("Phone number must contain only numbers and an optional '+' sign, and no space.", "\\+?\\d*"))
                .bind(UserDTO::getTelefon, UserDTO::setTelefon);

        binder.forField(passwordField)
                .withValidator(new StringLengthValidator("Password must be at least 8 characters long", 8, null))
                .asRequired("Password is required")
                .bind(UserDTO::getHashedPassword, (dto, password) -> {
                    if (password != null && !password.isEmpty()) {
                        dto.setHashedPassword(passwordEncoder.encode(password));
                    }
                });

        binder.forField(roleCheckboxGroup)
                .withValidator(roles -> !roles.isEmpty(), "At least one role must be selected.")
                .bind(UserDTO::getRoles, UserDTO::setRoles);


        Button saveButton = new Button("Save", event -> {
            UserDTO userDTO = new UserDTO();
            if (binder.writeBeanIfValid(userDTO)) {
                User newUser = Konverzija.konvertujUEntitet(userDTO, User.class);
                userService.create(newUser);        //kada radim kreiranje novog ne smem imati setovan id

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(usernameField, imeField, prezimeField, telefonField, passwordField, roleCheckboxGroup, saveButton, cancelButton));
        dialog.open();
    }

    private void editUser(UserDTO dto) {
        // Kreiraj dijalog
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Kreiraj Binder
        Binder<UserDTO> binder = new Binder<>(UserDTO.class);

        // Kreiraj polja za unos
        TextField imeField = new TextField("First Name");
        imeField.setRequired(true);

        TextField prezimeField = new TextField("Last Name");
        prezimeField.setRequired(true);

        TextField telefonField = new TextField("Phone");
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        passwordField.setValue("");
        dto.setHashedPassword("");

        CheckboxGroup<Role> roleCheckboxGroup = new CheckboxGroup<>("Roles");
        roleCheckboxGroup.setRequired(true);
        roleCheckboxGroup.setItems(EnumSet.allOf(Role.class));
        roleCheckboxGroup.setItemLabelGenerator(Role::name);

        //ovako se binduje ako nemamo validatore
        //        binder.bind(prezimeField, UserDTO::getPrezime, UserDTO::setPrezime);

        // Postavi polja za Binder, odnosno uvezi ime field sa user getime a na dugme setuj ime?
        binder.forField(imeField)
                .withValidator(new StringLengthValidator("First name is required", 1, 20))
                .bind(UserDTO::getIme, UserDTO::setIme);

        binder.forField(prezimeField)
                .withValidator(new StringLengthValidator("Last name is required", 1, 30))
                .bind(UserDTO::getPrezime, UserDTO::setPrezime);

        binder.forField(telefonField)
                .withValidator(new StringLengthValidator("Phone number must contain only numbers and an optional '+' sign, and no space.", 0, 20))
                .withValidator(new RegexpValidator("Phone number must contain only numbers and an optional '+' sign, and no space.", "\\+?\\d*"))
                .bind(UserDTO::getTelefon, UserDTO::setTelefon);

        binder.forField(passwordField)
                .withValidator(new StringLengthValidator("Password need min 8 characters.", 8, 255))
                .bind(UserDTO::getHashedPassword, (UserDTO, password) -> {    //Getter metoda za lozinku, gore sam je stavio na dto set hes lozinka "", ali treba u modelu izmeniti da vraca "", nakon toga ide setter lambda funkcija
                        if (password != null && !password.isEmpty()) {
                            dto.setHashedPassword(passwordEncoder.encode(password));
                        }
                    }
                );

        binder.forField(roleCheckboxGroup)
                .withValidator(roles -> !roles.isEmpty(), "At least one role must be selected")
                .bind(UserDTO::getRoles, UserDTO::setRoles);

        //Postavlja inicijalnu vrednost forme odnosno postavlja trenutne vrednosti iz dto objekta
        binder.readBean(dto);

        //Validacija preko bindera
        Button saveButton = new Button("Save", event -> {
            if (binder.writeBeanIfValid(dto)) {
                User n = Konverzija.konvertujUEntitet(dto, User.class);
                n.setId(dto.getId());       //kada radim update moram setovati id Useru n
                userService.update(n);

                this.osveziPrikaz();
                dialog.close();
            } else {
                Notification.show("Please fill all required fields.");
            }
        });

        //Ako otkaze zatvori dijalog bez izmena
        Button cancelButton = new Button("Cancel", event -> dialog.close());

        dialog.add(new VerticalLayout(imeField, prezimeField, telefonField, passwordField, roleCheckboxGroup, saveButton, cancelButton));
        dialog.open();
    }

    private void deleteUser(Long id){
        this.userService.delete(id);
        this.osveziPrikaz();
    }

    //ako ponovo pozovem crtanje prikaza kao na androidu sto sam radio, ovde nece raditi jer se ovde zadrzi stari prikaz a novi i azurirani se iscrta ispod
    //za azuriranje podataka radim preko data providera, jer je to ugradjena vaadin-va komponenta
    private void osveziPrikaz(){
        grid.setItems(query -> userService.lazyFindAll(query.getPage(), query.getPageSize()).stream());
    }








//stari grid koji nema lazy loading, samo iz data providera vadim podatke i priakzujem

//    private void iscrtajGrid(){
//        //true ili false znaci da li zelim automasko kreiranje kolona na osnovu entiteta
//        //mapiranje na polja se radi preko geter i seter metoda, znaci json ignore nema svrhu o ovom slucaju
//        grid = new Grid<>(UserDTO.class, false);
//        grid.addColumn(UserDTO::getUsername).setHeader("Userame");
//        grid.addColumn(UserDTO::getIme).setHeader("First name");
//        grid.addColumn(UserDTO::getPrezime).setHeader("Last name");
//        grid.addColumn(UserDTO::getTelefon).setHeader("Phone");
//        grid.addColumn(UserDTO::getRoles).setHeader("Role");
//
//        grid.addColumn(new ComponentRenderer<>(userDTO -> {
//            // Create a horizontal layout to hold both buttons
//            HorizontalLayout layout = new HorizontalLayout();
//
//            // Create and configure the "Edit" button
//            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
//            editButton.addClickListener(e -> editUser(userDTO));
//            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
//
//            // Create and configure the "Delete" button
//            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
//            deleteButton.addClickListener(e -> deleteUser(userDTO.getId()));
//            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
//
//            // Add both buttons to the layout
//            layout.add(editButton, deleteButton);
//
//            // Return the layout containing both buttons
//            return layout;
//        })).setHeader("Actions");
//
//
////        List<UserDTO> users = userService.findAll2();
////        grid.setItems(users);
//        dataProvider = new ListDataProvider<>(userService.findAll2());
//        grid.setDataProvider(dataProvider);
//        add(grid);  //dodaj u osnovnu komponentu
//
//        //testirano - - RADI!!!!!!
//        //provera prava pristupa mi ovde ne treba za delove koda jer je cela ruta (komponenta) zastice za rolu admin anotacijom
////        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz   " + authenticatedUser.hasRole(Role.USER));
//    }


//stari nacini validacije - bez bindera

//    private void addUser() {
//        Dialog dialog = new Dialog();
//        dialog.setWidth("400px");
//
//        // Kreiraj polja za unos
//
//        TextField usernameField = new TextField("Userame");
//        usernameField.setRequired(true);
//        usernameField.setRequiredIndicatorVisible(true);
//        usernameField.setErrorMessage("Field is required!!!");
//
//
//        TextField imeField = new TextField("First Name");
//        imeField.setRequired(true);
//        imeField.setRequiredIndicatorVisible(true);
//        imeField.setErrorMessage("Field is required!!!");
//
//        TextField prezimeField = new TextField("Last Name");
//        prezimeField.setRequired(true);
//        prezimeField.setRequiredIndicatorVisible(true);
//        prezimeField.setErrorMessage("Field is required!!!");
//
//        TextField telefonField = new TextField ("Phone");
//        telefonField.setPattern("\\+?\\d*");
//        telefonField.setErrorMessage("Phone number must contain only numbers and an optional '+' sign, and no space.");
//
//        PasswordField passwordField = new PasswordField("Password");
//        passwordField.setRequired(true);
//        passwordField.setRequiredIndicatorVisible(true);
//        passwordField.setErrorMessage("Password is required!!!");
//
//        CheckboxGroup<Role> roleCheckboxGroup = new CheckboxGroup<>("Roles");
//        roleCheckboxGroup.setLabel("Select Roles");
//        roleCheckboxGroup.setItems(EnumSet.allOf(Role.class));
//        roleCheckboxGroup.setItemLabelGenerator(Role::name);
//        roleCheckboxGroup.setRequired(true);
//        roleCheckboxGroup.setErrorMessage("At least one role must be selected.");
//        //roleCheckboxGroup.setValue(dto.getRoles());
//
//        Button saveButton = new Button("Save", event -> {
//            if (roleCheckboxGroup.getValue().isEmpty() || telefonField.isInvalid() || usernameField.isEmpty() || imeField.isEmpty() || prezimeField.isEmpty() || passwordField.isEmpty()) {
//                Notification.show("Please fill all required fields.");
//                return;
//            }
//            // Kreiraj novog usera
//            User newUser = new User();
//            newUser.setUsername(usernameField.getValue());
//            newUser.setIme(imeField.getValue());
//            newUser.setPrezime(prezimeField.getValue());
//            newUser.setTelefon(telefonField.getValue() != null ? telefonField.getValue() : "");
//            newUser.setRoles(roleCheckboxGroup.getSelectedItems());
//            String password = passwordField.getValue();
//            if (password != null && !password.isEmpty()) {
//                newUser.setHashedPassword(passwordEncoder.encode(password));
//            }
//
//            // Cuvanje u bazu novog usera
//            userService.create(newUser);
//
//            // Osvezavanje prikaza grida
//            this.osveziPrikaz();
//            dialog.close();
//        });
//
//        Button cancelButton = new Button("Cancel", event -> dialog.close());
//        dialog.add(new VerticalLayout(usernameField, imeField, prezimeField, telefonField, passwordField, roleCheckboxGroup, saveButton, cancelButton));
//        dialog.open();
//    }



//    private void editUser(UserDTO dto) {
//        // Kreiraj dijalog
//        Dialog dialog = new Dialog();
//        dialog.setWidth("400px");
//
//        // Kreiraj polja za unos
//        TextField imeField = new TextField("First Name");
//        imeField.setValue(dto.getIme() != null ? dto.getIme() : "");
//        imeField.setRequired(true);
//        imeField.setRequiredIndicatorVisible(true);
//        imeField.setErrorMessage("Field is required!!!");
//
//        TextField prezimeField = new TextField("Last Name");
//        prezimeField.setValue(dto.getPrezime() != null ? dto.getPrezime() : "");
//        prezimeField.setRequired(true);
//        prezimeField.setRequiredIndicatorVisible(true);
//        prezimeField.setErrorMessage("Field is required!!!");
//
//        TextField  telefonField = new TextField ("Phone");
//        telefonField.setValue(dto.getTelefon() != null ? dto.getTelefon() : "");
//        telefonField.setPattern("\\+?\\d*");
//        telefonField.setErrorMessage("Phone number must contain only numbers and an optional '+' sign, and no space.");
//
//        PasswordField passwordField = new PasswordField("Password");
//        //necu da setujem lozinku u polje
//        //passswordField.setValue(userDTO.getHashedPassword() == null ? passwordEncoder.encode(userDTO.getHashedPassword()) : "");
//        passwordField.setRequired(true);
//        passwordField.setRequiredIndicatorVisible(true);
//        passwordField.setErrorMessage("Password is required!!!");
//
//        CheckboxGroup<Role> roleCheckboxGroup = new CheckboxGroup<>("Roles");
//        roleCheckboxGroup.setLabel("Select Roles");
//        roleCheckboxGroup.setItems(EnumSet.allOf(Role.class));
//        roleCheckboxGroup.setItemLabelGenerator(Role::name);
//        roleCheckboxGroup.setRequired(true);
//        roleCheckboxGroup.setErrorMessage("At least one role must be selected.");
//        roleCheckboxGroup.setValue(dto.getRoles());   //da bude popunjeno
//
//
//        // Kreiraj dugme za čuvanje izmena
//        Button saveButton = new Button("Save", event -> {
//            if (roleCheckboxGroup.getValue().isEmpty() || telefonField.isInvalid() || imeField.isEmpty() || prezimeField.isEmpty() || passwordField.isEmpty()) {
//                Notification.show("Please fill all required fields.");
//                return;
//            }
//            dto.setIme(imeField.getValue());
//            dto.setPrezime(prezimeField.getValue());
//            dto.setTelefon(telefonField.getValue().toString());
//            dto.setRoles(roleCheckboxGroup.getSelectedItems());
//            String password = passwordField.getValue();
//            if (password != null && !password.isEmpty()) {
//                dto.setHashedPassword(passwordEncoder.encode(password));
//            }
//
//            // Servis uzera za cuvanje u bazu i pretvaranje dto prosledjenih objekata u User objekat
//            User n = Konverzija.konvertujUEntitet(dto, User.class);
//            n.setId(dto.getId());
//            userService.update(n);
//
//            this.osveziPrikaz();
//            dialog.close();
//        });
//
//        //Otkazivanje izmena
//        Button cancelButton = new Button("Cancel", event -> dialog.close());
//
//        dialog.add(new VerticalLayout(imeField, prezimeField, telefonField, passwordField, roleCheckboxGroup, saveButton, cancelButton));
//        dialog.open();
//    }

//    private void osveziPrikaz(){
//        //azuriranje preko providera ali bez lazy loading-a
//        List<UserDTO> updatedUsers = userService.findAll2();
//        dataProvider.getItems().clear();
//        dataProvider.getItems().addAll(updatedUsers);
//        dataProvider.refreshAll();
//    }




}
