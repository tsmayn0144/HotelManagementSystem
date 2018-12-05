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
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mysql.jdbc.PreparedStatement;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.print.DocFlavor;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Adrian Szmurło
 */
public class EmployeesScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private JFXTreeTableView<Employee> treeView;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField fullName;
    @FXML
    private JFXTextField address;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXComboBox<String> userType;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXTextField search_text;
    @FXML
    private StackPane stackepane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeData();
        treeViewSelectionListener();
        addValidation();

        loadAllEmployees("SELECT * FROM users");
    }    
    
    public void loadAllEmployees(String sql) {
        
        JFXTreeTableColumn<Employee, String> id = new JFXTreeTableColumn<>("Id");
        id.setPrefWidth(70);
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getId();
            }
        });
        
        JFXTreeTableColumn<Employee, String> user_name = new JFXTreeTableColumn<>("Username");
        user_name.setPrefWidth(120);
        user_name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getUsername();
            }
        });
        
        JFXTreeTableColumn<Employee, String> pass = new JFXTreeTableColumn<>("Password");
        pass.setPrefWidth(115);
        pass.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getPassword();
            }
        });
        
        JFXTreeTableColumn<Employee, String> full_name = new JFXTreeTableColumn<>("Full name");
        full_name.setPrefWidth(140);
        full_name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getFullName();
            }
        });
        
        JFXTreeTableColumn<Employee, String> user_address = new JFXTreeTableColumn<>("Address");
        user_address.setPrefWidth(130);
        user_address.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getAddress();
            }
        });
        
        JFXTreeTableColumn<Employee, String> phone_number = new JFXTreeTableColumn<>("Phone");
        phone_number.setPrefWidth(120);
        phone_number.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getPhone();
            }
        });
        
        JFXTreeTableColumn<Employee, String> start_date = new JFXTreeTableColumn<>("Start date");
        start_date.setPrefWidth(110);
        start_date.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getStartDate();
            }
        });
        
        JFXTreeTableColumn<Employee, String> user_type = new JFXTreeTableColumn<>("User type");
        user_type.setPrefWidth(115);
        user_type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Employee, String> param) {
                return param.getValue().getValue().getUserType();
            }
        });
        
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                employees.add(new Employee(rs.getInt(1)+"", rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(EmployeesScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final TreeItem<Employee> root = new RecursiveTreeItem<Employee>(employees, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(id, user_name, pass, full_name, user_address, phone_number, start_date, user_type);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }
    
    @FXML
    void back(MouseEvent event) {
        Stage home = new Stage();
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("AdminScreen.fxml"));
        } 
        catch (IOException ex) {
            Logger.getLogger(AdminScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage current = (Stage)search_text.getScene().getWindow();
        Scene scene = new Scene(root);
        
        home.setScene(scene);
        home.initStyle(StageStyle.TRANSPARENT);
        
        current.hide();
        home.show();
    }

    @FXML
    void close(MouseEvent event) {
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
    void search() {
        if (search_text.getText().toString().equals("")) {
            loadAllEmployees("SELECT * FROM users");
        }
        else {
            String text = search_text.getText().toString();
            String firstLetter = String.valueOf(search_text.getText().charAt(0));
            
            if (Character.isLowerCase(text.charAt(0))) {
                text = search_text.getText().replace(firstLetter, firstLetter.toUpperCase());
                /*loadAllEmployees("SELECT * FROM users WHERE fullName LIKE '%" + search_text.getText().toString().trim() + "%' OR "
                    + "fullName LIKE '" + text + "%' OR fullName LIKE '%" + text + "%'");*/
            }
            else if (Character.isUpperCase(text.charAt(0))) {
                text = search_text.getText().replace(firstLetter, firstLetter.toLowerCase());
            }

            String text_uppercase = search_text.getText().toString().toLowerCase(); // String sprowadza ewentualne łańcuchy pisane wielkimi 
                                                                                    // literami do lowercase'a
            text_uppercase = text_uppercase.replace(String.valueOf(text_uppercase.charAt(0)), String.valueOf(text_uppercase.charAt(0)).toUpperCase());
            loadAllEmployees("SELECT * FROM users WHERE fullName LIKE '%" + search_text.getText().toString().trim() + "%' OR "
                    + "fullName LIKE '" + text + "%' OR fullName LIKE '%" + text + "%' OR fullName LIKE '%" + text.toLowerCase() + "%' OR "
                    + "fullName LIKE '" + text_uppercase + "%' OR fullName LIKE '%" + text_uppercase + "%'");
        }

        clearForm();
    }
    
    @FXML
    void clear(MouseEvent event) {
        clearForm();
    }

    @FXML
    void insert(MouseEvent event) {
        int res = 0;
        String sql = "INSERT INTO users (username,password,fullName,address,phone,startDate,userType) VALUES (?,?,?,?,?,?,?)";
        Connection connection = DBConnection.getConnection();
        
        if (!checkEmptyFields()) {
            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                ps.setString(1, username.getText().toString());
                ps.setString(2, password.getText().toString());
                ps.setString(3, fullName.getText().toString());
                ps.setString(4, address.getText().toString());
                ps.setString(5, phone.getText().toString());
                ps.setString(6, startDate.getValue().toString());
                ps.setString(7, userType.getValue().toString());

                res = ps.executeUpdate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(EmployeesScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (res > 0) {
                Image image = new Image("img/mooo.png");
                Notifications notification = Notifications.create().title("Done").text("New user added successfully")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
                loadAllEmployees("SELECT * FROM users");
                clearForm();
                search_text.setText("");
            } 
            else {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong (check if username is not taken).")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }
        }
    }

    @FXML
    void update(MouseEvent event) {
        if (treeView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Update error");
            alert.setHeaderText("No user selected");
            alert.setContentText("In order to update a user, you need to choose the user first.");
            alert.showAndWait();
        }
        else {
            String id = String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getId().getValueSafe());

            int res = 0;
            String sql = "UPDATE users SET username=?, password=?, fullName=?, address=?, phone=?, startDate=?, userType=? WHERE id=?";
            Connection connection = DBConnection.getConnection();

            if (!checkEmptyFields()) {
                try {
                    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                    ps.setString(1, username.getText().toString());
                    ps.setString(2, password.getText().toString());
                    ps.setString(3, fullName.getText().toString());
                    ps.setString(4, address.getText().toString());
                    ps.setString(5, phone.getText().toString());
                    ps.setString(6, startDate.getValue().toString());
                    ps.setString(7, userType.getValue().toString());
                    ps.setString(8, id);

                    res = ps.executeUpdate();
                } catch (SQLException e) {
                    Logger.getLogger(EmployeesScreenController.class.getName()).log(Level.SEVERE, null, e);
                }

                if (res > 0) {
                    Image image = new Image("img/mooo.png");
                    Notifications notification = Notifications.create().title("Done").text("User updated successfully")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
                    loadAllEmployees("SELECT * FROM users");
                    clearForm();
                    search_text.setText("");
                } else {
                    Image image = new Image("img/delete.png");
                    Notifications notification = Notifications.create().title("Error").text("Something went wrong (check if username is not taken).")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
                }
            }
        }
        
    }
    
    @FXML
    void delete(MouseEvent event) {
        if (treeView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete error");
            alert.setHeaderText("No user selected");
            alert.setContentText("In order to delete a user, you need to choose the user first.");
            alert.showAndWait();
        }
        else {
            String id = String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getId().getValueSafe());
            int res = 0;
            String sql = "DELETE FROM users WHERE id=?";
            Connection connection = DBConnection.getConnection();

            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                ps.setString(1, id);

                res = ps.executeUpdate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(EmployeesScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (res > 0) {
                Image image = new Image("img/mooo.png");
                Notifications notification = Notifications.create().title("Done").text("User removed successfully.")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
                loadAllEmployees("SELECT * FROM users");
                search_text.setText("");
            } else {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong.")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }
        }
    }

    private void clearForm() {
        username.setText("");
        password.setText("");
        fullName.setText("");
        address.setText("");
        phone.setText("");
        userType.setValue("");
        startDate.setValue(LocalDate.now());
    }
    
    private void pressedEnter(JFXTextField textfield) {
        textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    search();
                }
            }
        });
    }
    
    private boolean checkEmptyFields() {
        if (username.getText().toString().equals("") || password.getText().toString().equals("") || fullName.getText().toString().equals("") || 
                address.getText().toString().equals("") || phone.getText().toString().equals("") || startDate.getValue().toString().equals("") || 
                userType.getValue().toString().equals("")) {
            
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

    private void treeViewSelectionListener() {
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Employee>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Employee>> observable, TreeItem<Employee> oldValue, TreeItem<Employee> newValue) {
                //String name = treeView.getSelectionModel().getSelectedItem().getValue().getFullName().getValueSafe();
                search_text.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getFullName().getValueSafe()));

                username.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getUsername().getValueSafe()));
                password.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getPassword().getValueSafe()));
                fullName.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getFullName().getValueSafe()));
                address.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getAddress().getValueSafe()));
                phone.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getPhone().getValueSafe()));
                userType.setValue(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getUserType().getValueSafe()));
                startDate.setValue(LocalDate.parse(treeView.getSelectionModel().getSelectedItem().getValue().getStartDate().getValue()));
            }
        });
    }

    private void initializeData() {
        startDate.setValue(LocalDate.now());
        DateFormatting.dateFormatter(startDate);
        
        pressedEnter(search_text);
        
        ObservableList userTypeList = FXCollections.observableArrayList();
        userTypeList.add("admin");
        userTypeList.add("normal");
        userType.setItems(userTypeList);
        
        //userType.setValue("");
    }
    
    private void addValidation() {
        Robot r;
        try {
            r = new Robot();
            
            search_text.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("([a-zA-Z\\s]*)?")) ? change : null));

            username.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!username.getText().matches("([a-zA-Z0-9]){6,15}") && !username.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Username error");
                            alert.setHeaderText("Username must be 6 - 15 characters long (letters and numbers only).");
                            alert.setContentText("Please enter correct username.");
                            alert.showAndWait();

                            username.setText("");
                            
                            ReservationScreenController.pressShiftTab(r);
                        }
                    }
                }
            });

            password.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!password.getText().matches("(?=.*[a-zA-Z0-9])(?=.*[!@#$%&*_\\-+=.])(?=\\S+$).{6,15}") && !password.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Password error");
                            alert.setHeaderText("Password must be 6 - 15 characters long (no whitespaces, at least 1 special character).");
                            alert.setContentText("Please enter correct password.");
                            alert.showAndWait();

                            password.setText("");
                            
                            ReservationScreenController.pressShiftTab(r);
                        }
                    }
                }
            });

            fullName.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!fullName.getText().matches("[A-Z][a-zA-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃ\\-\\s]{3,30}") && !fullName.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Name error");
                            alert.setHeaderText("Full name must be 4 - 30 characters long (letters and '-' only) and begin with uppercase character.");
                            alert.setContentText("Please enter correct full name.");
                            alert.showAndWait();

                            fullName.setText("");
                            
                            ReservationScreenController.pressShiftTab(r);
                        }
                    }
                }
            });

            address.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!address.getText().matches("[a-zA-Z0-9\\.\\,\\-\\s/ęóąśłżźćńĘÓĄŚŁŻŹĆŃ]{5,50}") && !address.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Address error");
                            alert.setHeaderText("Address must be 5 - 50 characters long (letters, numbers and .,-/ only).");
                            alert.setContentText("Please enter correct address.");
                            alert.showAndWait();

                            address.setText("");
                            
                            ReservationScreenController.pressShiftTab(r);
                        }
                    }
                }
            });

            phone.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("\\+{0,1}[0-9]*")) ? change : null));
            phone.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!phone.getText().matches(".{9,15}") && !phone.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Phone error");
                            alert.setHeaderText("Phone must be 9 - 15 numbers long (optional + at the beginning).");
                            alert.setContentText("Please enter correct phone number.");
                            alert.showAndWait();

                            phone.setText("");
                            
                            ReservationScreenController.pressShiftTab(r);
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
