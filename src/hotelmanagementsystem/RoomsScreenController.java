/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
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
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Adrian Szmurło
 */
public class RoomsScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    String status = null;
    
    @FXML
    private JFXTreeTableView<Room> treeView;
    @FXML
    private JFXTextField search_text;
    @FXML
    private JFXCheckBox busy;
    @FXML
    private JFXCheckBox available;
    @FXML
    private StackPane stackepane;
    @FXML
    private JFXCheckBox all;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pressedEnter(search_text);
        loadAllRooms("SELECT * FROM room");
    }    
    
    public void loadAllRooms(String sql) {
        
        JFXTreeTableColumn<Room, String> room_id = new JFXTreeTableColumn<>("Room id");
        room_id.setPrefWidth(90);
        room_id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getId();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_type = new JFXTreeTableColumn<>("Room type");
        room_type.setPrefWidth(100);
        room_type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomType();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_number = new JFXTreeTableColumn<>("Room number");
        room_number.setPrefWidth(100);
        room_number.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomNumber();
            }
        });
        
        JFXTreeTableColumn<Room, String> num_of_people = new JFXTreeTableColumn<>("Number of people");
        num_of_people.setPrefWidth(120);
        num_of_people.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getNumberOfPeople();
            }
        });
        
        JFXTreeTableColumn<Room, String> ac = new JFXTreeTableColumn<>("A/C");
        ac.setPrefWidth(90);
        ac.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getAc();
            }
        });
        
        JFXTreeTableColumn<Room, String> balcony = new JFXTreeTableColumn<>("Balcony");
        balcony.setPrefWidth(90);
        balcony.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getBalcony();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_phone = new JFXTreeTableColumn<>("Room phone");
        room_phone.setPrefWidth(100);
        room_phone.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomPhone();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_price = new JFXTreeTableColumn<>("Room price");
        room_price.setPrefWidth(100);
        room_price.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomPrice();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_status = new JFXTreeTableColumn<>("Room status");
        room_status.setPrefWidth(100);
        room_status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomStatus();
            }
        });
        
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                rooms.add(new Room(rs.getInt(1)+"", rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(RoomsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final TreeItem<Room> root = new RecursiveTreeItem<Room>(rooms, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(room_id, room_type, room_number, num_of_people, ac, balcony, room_phone, room_price, room_status);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    @FXML
    private void searchByRoomNumber() { //(MouseEvent event)
        loadAllRooms("SELECT * FROM room WHERE roomNumber='" + search_text.getText().toString().trim() + "'");
        if (search_text.getText().toString().equals("")) {
            loadAllRooms("SELECT * FROM room");
        }
    }

    @FXML
    private void makeItAvailable(MouseEvent event) {
        String text = search_text.getText().toString().trim();
        int res = 0;
        String sql = "UPDATE room SET roomStatus=? WHERE roomNumber=?";
        Connection connection = DBConnection.getConnection();
        
        try {
            PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
            ps.setString(1, "available");
            ps.setString(2, text);
            
            res = ps.executeUpdate();
        } 
        catch (SQLException ex) {
            Logger.getLogger(RoomsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(res > 0) {
            Image image = new Image("img/mooo.png");
            Notifications notification = Notifications.create().title("Done").text("Room updated successfully")
                    .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
            notification.darkStyle();
            notification.show();
            loadAllRooms("SELECT * FROM room");
            search_text.setText("");
        }
        else {
            Image image = new Image("img/delete.png");
            Notifications notification = Notifications.create().title("Error").text("Something went wrong")
                    .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
            notification.darkStyle();
            notification.show();
            loadAllRooms("SELECT * FROM room");
            search_text.setText("");
        }
    }

    @FXML
    private void searchByStatus(MouseEvent event) {
        loadAllRooms(status);
    }

    @FXML
    private void searchBusy(ActionEvent event) {
        available.setSelected(false);
        all.setSelected(false);
        status = "SELECT * FROM room WHERE roomStatus = 'busy'";
    }

    @FXML
    private void searchAvailable(ActionEvent event) {
        busy.setSelected(false);
        all.setSelected(false);
        status = "SELECT * FROM room WHERE roomStatus = 'available'";
    }

    @FXML
    private void searchAll(ActionEvent event) {
        busy.setSelected(false);
        available.setSelected(false);
        status = "SELECT * FROM room";
    }
    
    @FXML
    private void goBack(MouseEvent event) {
        Stage home = new Stage();
        Parent root = null;
        
        try {
            if (LoginScreenController.userRole.equals("admin"))
                root = FXMLLoader.load(getClass().getResource("HomeScreenAdmin.fxml"));
            else
                root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
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
    
    private void pressedEnter(JFXTextField textfield) {
        textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    searchByRoomNumber();
                }
            }
        });
    }

    @FXML
    private void addRoom(MouseEvent event) {
        Stage addingRoom = new Stage();
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("NewRoomScreen.fxml"));
        } 
        catch (IOException ex) {
            Logger.getLogger(RoomsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage current = (Stage)stackepane.getScene().getWindow();
        Scene scene = new Scene(root);
        
        addingRoom.setScene(scene);
        addingRoom.initStyle(StageStyle.TRANSPARENT);
        
        current.hide();
        addingRoom.show();
    }

}
