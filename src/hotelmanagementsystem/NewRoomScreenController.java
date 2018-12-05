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
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.PreparedStatement;
import static hotelmanagementsystem.ReservationScreenController.pressShiftTab;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javafx.scene.control.TextFormatter;
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
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Adrian Szmurło
 */
public class NewRoomScreenController implements Initializable {

    @FXML
    private StackPane stackepane;
    @FXML
    private JFXTextField roomNumber;
    @FXML
    private JFXTextField roomPhone;
    @FXML
    private JFXTextField roomPrice;
    @FXML
    private JFXComboBox<String> ac;
    @FXML
    private JFXComboBox<String> balcony;
    @FXML
    private JFXComboBox<String> numPeople;
    @FXML
    private JFXComboBox<String> roomType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeValues();
        addValidation();
    }    

   @FXML
    private void back(MouseEvent event) {
        Stage rooms = new Stage();
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("RoomsScreen.fxml"));
        } 
        catch (IOException ex) {
            Logger.getLogger(NewRoomScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage current = (Stage)stackepane.getScene().getWindow();
        Scene scene = new Scene(root);
        
        rooms.setScene(scene);
        rooms.initStyle(StageStyle.TRANSPARENT);
        
        current.hide();
        rooms.show();
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
        clearForm();
    }

    @FXML
    private void add() { // (MouseEvent event)
        int res = 0;
        String sql = "INSERT INTO room (roomType,roomNumber,numberOfPeople,ac,balcony,roomPhone,roomPrice,roomStatus) VALUES (?,?,?,?,?,?,?,?)";
        Connection connection = DBConnection.getConnection();
        
        if (!checkEmptyFields()) {
            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                ps.setString(1, roomType.getValue().toString());
                ps.setString(2, roomNumber.getText().toString());
                ps.setString(3, numPeople.getValue().toString());
                ps.setString(4, ac.getValue().toString());
                ps.setString(5, balcony.getValue().toString());
                ps.setString(6, roomPhone.getText().toString());
                ps.setString(7, roomPrice.getText().toString());
                ps.setString(8, "available");
                
                res = ps.executeUpdate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(NewRoomScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (res > 0) {
                Image image = new Image("img/mooo.png");
                Notifications notification = Notifications.create().title("Done").text("New room added successfully")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();

                clearForm();
            } 
            else {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong (room number or room phone are probably already used).")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }
        }
    }
    
    private boolean checkEmptyFields() {
        if (roomNumber.getText().toString().equals("") || roomPhone.getText().toString().equals("") || roomPrice.getText().toString().equals("") || 
                ac.getValue().toString().equals("") || balcony.getValue().toString().equals("") || numPeople.getValue().toString().equals("") || 
                roomType.getValue().toString().equals("")) {
            
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
    
    private void clearForm() {
        roomNumber.setText("");
        roomPhone.setText("");
        roomPrice.setText("");
        ac.setValue("");
        balcony.setValue("");
        roomType.setValue("");
        numPeople.setValue("");
    }

    private void initializeValues() {
        ObservableList ynList = FXCollections.observableArrayList();
        ynList.add("yes");
        ynList.add("no");
        ac.setItems(ynList);
        balcony.setItems(ynList);
        
        ObservableList roomTypeList = FXCollections.observableArrayList();
        roomTypeList.add("A*");
        roomTypeList.add("A");
        roomTypeList.add("B*");
        roomTypeList.add("B");
        roomTypeList.add("C*");
        roomTypeList.add("C");
        roomType.setItems(roomTypeList);
        
        ObservableList numPeopleList = FXCollections.observableArrayList();
        numPeopleList.add("1");
        numPeopleList.add("2");
        numPeopleList.add("3");
        numPeopleList.add("4");
        numPeopleList.add("5");
        numPeopleList.add("6");
        numPeopleList.add("7");
        numPeopleList.add("8");
        numPeople.setItems(numPeopleList);
        
        ac.setValue("");
        balcony.setValue("");
        roomType.setValue("");
        numPeople.setValue("");
    }
    
    private void addValidation() {
        Robot r;
        try {
            r = new Robot();
            
            roomNumber.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("[1-9]?[0-9]?[0-9]?")) ? change : null));
            roomNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        //if ((!duration.getText().matches(".{1,2}") || duration.getText().matches("0[0-9]?"))&& !duration.getText().equals("")) {  
                        if (roomNumber.getText().matches("0[0-9]?[0-9]?") && !roomNumber.getText().equals("")) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Room number error");
                            alert.setHeaderText("Room number can not start with 0.");
                            alert.setContentText("Please enter correct room number.");
                            alert.showAndWait();

                            roomNumber.setText("");
                            
                            pressShiftTab(r);
                        }
                    }
                }
            });
            
            roomPhone.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("[0-9]*")) ? change : null));
            roomPhone.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!roomPhone.getText().matches(".{4,8}") && !roomPhone.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Phone error");
                            alert.setHeaderText("Room phone number must be 4-8 numbers long.");
                            alert.setContentText("Please enter correct room phone number.");
                            alert.showAndWait();

                            roomPhone.setText("");
                            
                            ReservationScreenController.pressShiftTab(r);
                        }
                    }
                }
            });
            
            roomPrice.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("[1-9]?[0-9]?[0-9]?[0-9]?")) ? change : null));
            roomPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        //if ((!duration.getText().matches(".{1,2}") || duration.getText().matches("0[0-9]?"))&& !duration.getText().equals("")) {  
                        if (roomPrice.getText().matches("0[0-9]?[0-9]?[0-9]?") && !roomPrice.getText().equals("")) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Room price error");
                            alert.setHeaderText("Room price can not start with 0.");
                            alert.setContentText("Please enter correct room price.");
                            alert.showAndWait();

                            roomPrice.setText("");
                            
                            pressShiftTab(r);
                        }
                    }
                }
            });
        }
        catch (AWTException ex) {
            Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
