/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Adrian Szmurło
 */
public class ReservationScreenController implements Initializable {

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXTextField address;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField duration;
    @FXML
    private JFXComboBox<String> num_people;
    @FXML
    private JFXComboBox<String> paymentType;
    @FXML
    private JFXTextField roomType;
    @FXML
    private JFXTextField roomNumber;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField services;
    @FXML
    private JFXTextField total;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker endDate;
    @FXML
    private StackPane stackepane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now().plusDays(1));
       
        duration.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    endDate.setValue(LocalDate.now().plusDays(Long.parseLong(duration.getText().toString())));
                }
            }
        });
        
        DateFormatting.dateFormatter(startDate);
        DateFormatting.dateFormatter(endDate);
        
        ObservableList numPeopleList = FXCollections.observableArrayList();
        numPeopleList.add("1");
        numPeopleList.add("2");
        numPeopleList.add("3");
        numPeopleList.add("4");
        numPeopleList.add("5");
        numPeopleList.add("6");
        numPeopleList.add("7");
        numPeopleList.add("8");
        num_people.setItems(numPeopleList);
        
        ObservableList paymentTypeList = FXCollections.observableArrayList();
        paymentTypeList.add("card");
        paymentTypeList.add("cash");
        paymentTypeList.add("paypal");
        paymentTypeList.add("transfer");
        paymentType.setItems(paymentTypeList);
    }  
    
    @FXML
    private void back(MouseEvent event) {
        Stage home = new Stage();
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        } 
        catch (IOException ex) {
            Logger.getLogger(AdminScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage current = (Stage)roomNumber.getScene().getWindow();
        Scene scene = new Scene(root);
        
        home.setScene(scene);
        home.initStyle(StageStyle.TRANSPARENT);
        
        current.hide();
        home.show();
    }

    @FXML
    private void close(MouseEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(new Text("Close"));
        dialogLayout.setBody(new Text("Do you want to exit?"));
        
        JFXButton ok = new JFXButton("Ok");
        JFXButton cancel = new JFXButton("Cancel");
        
        JFXDialog dialog = new JFXDialog(stackepane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        
        dialogLayout.setActions(ok, cancel);
        dialog.show();
    }

    @FXML
    private void reset(MouseEvent event) {
        name.setText("");
        email.setText("");
        address.setText("");
        phone.setText("");
        num_people.setValue("");
        paymentType.setValue("");
        duration.setText("");
        roomType.setText(""); // brak czyszczenia dat :/
        roomNumber.setText("");
        price.setText("");
        services.setText("");
        total.setText("");
    }

    @FXML
    private void book(MouseEvent event) {
        int res = 0;
        String sql = "INSERT INTO customer (name, email, address, phone, numOfPeople, paymentType, duration, roomType, roomNumber, startDate, endDate, price, services, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        
        if(!checkEmptyFields()) {
            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                ps.setString(1, name.getText().toString());
                ps.setString(2, email.getText().toString());
                ps.setString(3, address.getText().toString());
                ps.setString(4, phone.getText().toString());
                ps.setString(5, num_people.getValue().toString());
                ps.setString(6, paymentType.getValue().toString());
                ps.setString(7, duration.getText().toString());
                ps.setString(8, roomType.getText().toString());
                ps.setString(9, roomNumber.getText().toString());
                ps.setString(10, startDate.getValue().toString());
                ps.setString(11, endDate.getValue().toString());
                ps.setString(12, price.getText().toString());
                ps.setString(13, services.getText().toString());
                ps.setString(14, total.getText().toString());

                res = ps.executeUpdate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (res > 0) {
                Image image = new Image("img/mooo.png");
                Notifications notification = Notifications.create().title("Done").text("Reservation submitted successfully")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
                updateStatus();
            } 
            else {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }
        }
    }

    @FXML
    private void print(MouseEvent event) { // puste na razie - do implementacji
        
    }

    private void updateStatus() {
        String text = roomNumber.getText().toString().trim();
        String sql = "UPDATE room SET roomStatus=? WHERE roomNumber=?";
        Connection connection = DBConnection.getConnection();
        
        try {
            PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
            ps.setString(1, "busy");
            ps.setString(2, text);
            
            ps.executeUpdate();
        } 
        catch (SQLException ex) {
            Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean checkEmptyFields() {
        if (name.getText().toString().equals("") || email.getText().toString().equals("") || address.getText().toString().equals("") || 
                phone.getText().toString().equals("") || num_people.getValue().toString().equals("") || paymentType.getValue().toString().equals("") || 
                duration.getText().toString().equals("") || roomType.getText().toString().equals("") || roomNumber.getText().toString().equals("") || 
                startDate.getValue().toString().equals("") || endDate.getValue().toString().equals("") || price.getText().toString().equals("") || 
                services.getText().toString().equals("") || total.getText().toString().equals("")) {
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("One or more fields are empty");
            alert.setContentText("Please fill all the required fields.");
            alert.showAndWait();
            
            return true;
        }
        else 
            return false;
    }
}
