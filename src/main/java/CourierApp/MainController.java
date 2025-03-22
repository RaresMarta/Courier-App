package CourierApp;

import Domain.Observer;
import Domain.Package;
import Domain.Courier;
import Domain.Point;
import Repository.CourierRepo;
import Repository.PackageRepo;
import Service.CService;
import Service.PService;

import static Domain.Point.distance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class MainController {
    public ListView pListView;
    CService cService;
    PService pService;
    ObservableList<Package> undeliveredPackages;

    @FXML
    public void initialize() {
        cService = new CService(new CourierRepo());
        pService = new PService(new PackageRepo());
        ObservableList<Package> packagesObsLst = FXCollections.observableList(pService.getPackages());
        pListView.setItems(packagesObsLst);

        for (Courier c : cService.getCouriers()) {
            createCourierWindow(c);
        }
        createMapViewWindow(undeliveredPackages);
    }

    private void createMapViewWindow(ObservableList<Package> packagesObsLst) {
        undeliveredPackages = FXCollections.observableList(pService.getUndelivered());
        ListView<Package> mapListView = new ListView<>(undeliveredPackages);

        Stage stage = new Stage();
        stage.setScene(new Scene(mapListView));
        stage.setTitle("Map View - Undelivered Packages");
        stage.show();

        pService.addObserver(new Observer() {
            @Override
            public void update() {
                undeliveredPackages.setAll(pService.getUndelivered());
                packagesObsLst.setAll(pService.getPackages());
            }
        });
    }

    private void createCourierWindow(Courier c) {
        Stage stage = new Stage();
        stage.setTitle("Courier " + c.getName());
        HBox hbox = new HBox();
        stage.setScene(new Scene(hbox));

        // Courier's undelivered packages list
        ListView<Package> courierListView = new ListView<>();
        ObservableList<Package> courierObsLst = FXCollections.observableList(pService.getCourierPackages(c));
        courierListView.setItems(courierObsLst);
        hbox.getChildren().add(courierListView);

        // Zone info label
        String zoneString = c.getZoneCenter() + "\n Radius: " + c.getZoneRadius();
        hbox.getChildren().add(new Label(zoneString));

        // Optimise button
        Button btn = optimizeButton(c, courierObsLst);

        // ComboBox to filter by street
        ObservableList<String> streets = FXCollections.observableList(pService.getAllStreets(pService.getCourierPackages(c)));
        streets.add("Show All");
        ComboBox<String> comboBox = streetsComboBox(c, streets, courierObsLst);

        // Deliver button
        Button deliverButton = new Button("Deliver");
        deliverButton.setOnAction(_ -> {
            Package p = courierListView.getSelectionModel().getSelectedItem();
            if (p != null) {
                p.setStatus(true);
                pService.updatePackage(p);
            }
        });

        // Observer for updates
        pService.addObserver(new Observer() {
            @Override
            public void update() {
                courierObsLst.setAll(pService.getCourierPackages(c));
            }
        });
        hbox.getChildren().addAll(btn, comboBox, deliverButton);
        stage.show();
    }

    private ComboBox<String> streetsComboBox(Courier c, ObservableList<String> streets, ObservableList<Package> courierObsLst) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setItems(streets);
        comboBox.setOnAction(_ -> {
            String selectedStreet = comboBox.getSelectionModel().getSelectedItem();
            Predicate<Package> predicate = (Package p) -> p.getAddress().contains(selectedStreet);

            if (Objects.equals(selectedStreet, "Show All")) {
                courierObsLst.setAll(pService.getCourierPackages(c));
            } else {
                courierObsLst.setAll(pService.getCourierPackages(c).stream().filter(predicate).toList());
            }
        });
        return comboBox;
    }

    private static Button optimizeButton(Courier c, ObservableList<Package> courierObsLst) {
        // Sort the courier’s packages by distance from the courier’s zone center
        Button btn = new Button("Optimise");
        Comparator<Package> distanceSort = (p1, p2) -> {
            float d1 = distance(p1.getLocation(), c.getZoneCenter());
            float d2 = distance(p2.getLocation(), c.getZoneCenter());
            return Float.compare(d1, d2); // ascending order
        };
        btn.setOnAction(_ -> courierObsLst.setAll(courierObsLst.stream().sorted(distanceSort).toList()));
        return btn;
    }

    @FXML
    void addPackage(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Add Package");
        VBox vbox = new VBox();
        stage.setScene(new Scene(vbox));

        TextField recipient = new TextField("Recipient");
        TextField address = new TextField("Address");
        TextField locationX = new TextField("LocationX");
        TextField locationY = new TextField("LocationY");
        Button submit = new Button("Submit");
        vbox.getChildren().addAll(recipient, address, locationX, locationY, submit);

        submit.setOnAction(_ -> { // observers are notified
            Point location = new Point(Float.parseFloat(locationX.getText()), Float.parseFloat(locationY.getText()));
            Package newPackage = new Package(
                    0,                // id
                    recipient.getText(), // recipient
                    address.getText(),   // address
                    location,            // location point
                    false);              // status
            pService.addPackage(newPackage);
        });
        stage.show();
    }

    @FXML
    void addCourier() {
        Stage stage = new Stage();
        stage.setTitle("Add Courier");
        VBox vbox = new VBox();
        stage.setScene(new Scene(vbox));

        TextField name = new TextField("Name");
        TextField streets = new TextField("Streets");
        TextField zoneCenterX = new TextField("ZoneCenterX");
        TextField zoneCenterY = new TextField("ZoneCenterY");
        TextField zoneRadius = new TextField("ZoneRadius");
        Button submit = new Button("Submit");
        vbox.getChildren().addAll(name, streets, zoneCenterX, zoneCenterY, zoneRadius, submit);

        submit.setOnAction(_ -> {
            Point zoneCenter = new Point(Float.parseFloat(zoneCenterX.getText()), Float.parseFloat(zoneCenterY.getText()));
            Courier newCourier = new Courier(
                    0,               // id
                    name.getText(),     // name
                    streets.getText(),  // streets
                    zoneCenter,         // zoneCenter point
                    Float.parseFloat(zoneRadius.getText())); // zoneRadius

            cService.addCourier(newCourier);
            createCourierWindow(newCourier);
        });

        stage.show();
    }
}