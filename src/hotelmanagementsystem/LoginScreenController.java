/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Adrian Szmurło
 */
public class LoginScreenController implements Initializable {
    
    public static String userRole = "";
    
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private StackPane stackepane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pressedEnter(password, username);
    }    

    @FXML
    private void loginButton() { //loginButton(MouseEvent event) 
        
        if(username.getText().toString().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login error");
            alert.setHeaderText("Username is empty");
            alert.setContentText("Please enter your username");
            alert.showAndWait();
        }
        else if(password.getText().toString().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login error");
            alert.setHeaderText("Password is empty");
            alert.setContentText("Please enter your password");
            alert.showAndWait();
        }
        else {
            boolean isExist = false;
            String userPass = "";
            String userType = "";
        
            String sql = "SELECT * FROM users WHERE username = '" + username.getText().toString().trim() + "'";
            Connection connection = DBConnection.getConnection();

            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    isExist = true;
                    userPass = rs.getString(3);
                    userType = rs.getString(8);
                }

                if (isExist) {
                    if (password.getText().toString().trim().equals(userPass)) {
                        if (userType.equals("admin")) {
                            Stage adminScreen = new Stage();
                            Parent root = null;

                            userRole = "admin";
                            
                            try {
                                root = FXMLLoader.load(getClass().getResource("AdminScreen.fxml"));
                            } 
                            catch (IOException ex) {
                                Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            Stage current = (Stage) username.getScene().getWindow();
                            Scene scene = new Scene(root, 1366, 730);

                            adminScreen.setScene(scene);
                            adminScreen.initStyle(StageStyle.TRANSPARENT);

                            current.hide();
                            adminScreen.show();
                        } 
                        else {
                            Stage homeScreen = new Stage();
                            Parent root = null;

                            try {
                                root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
                            } 
                            catch (IOException ex) {
                                Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            Stage current = (Stage) username.getScene().getWindow();
                            Scene scene = new Scene(root, 1366, 730);

                            homeScreen.setScene(scene);
                            homeScreen.initStyle(StageStyle.TRANSPARENT);

                            current.hide();
                            homeScreen.show();
                        }
                    }
                    else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login error");
                    alert.setHeaderText("Wrong user credentials");
                    alert.setContentText("Please check your password");
                    alert.showAndWait();
                }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login error");
                    alert.setHeaderText("Wrong user credentials");
                    alert.setContentText("Please check your username");
                    alert.showAndWait();
                }
            } 
            catch (SQLException ex) {
                Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void cancelButton(MouseEvent event) {
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
    
    private void pressedEnter(JFXPasswordField pass, JFXTextField login) {
        login.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    //MouseEvent event = null;
                    loginButton();//loginButton(event);
                }
            }
        });
        pass.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    //MouseEvent event = null;
                    loginButton(); //loginButton(event);
                }
            }
        });
    }
}
