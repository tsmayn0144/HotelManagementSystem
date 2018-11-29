/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Adrian Szmurło
 */
public class CustomersScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private JFXTreeTableView<CustomerReservationData> treeView;

    @FXML
    private JFXTextField searchText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        pressedEnter(searchText);
        loadAllCustomers("SELECT * FROM customerreservationdata");
    }    
    
    public void loadAllCustomers(String sql) {
        
        JFXTreeTableColumn<CustomerReservationData, String> id = new JFXTreeTableColumn<>("Id");
        id.setPrefWidth(100);
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().id;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> client_id = new JFXTreeTableColumn<>("Client id");
        client_id.setPrefWidth(100);
        client_id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().clientId;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> name = new JFXTreeTableColumn<>("Name");
        name.setPrefWidth(90);
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().name;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> email = new JFXTreeTableColumn<>("Email");
        email.setPrefWidth(125);
        email.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().email;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> address = new JFXTreeTableColumn<>("Address");
        address.setPrefWidth(120);
        address.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().address;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> phone = new JFXTreeTableColumn<>("Phone");
        phone.setPrefWidth(95);
        phone.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().phone;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> room_number = new JFXTreeTableColumn<>("Room number");
        room_number.setPrefWidth(100);
        room_number.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().roomNumber;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> num_of_people = new JFXTreeTableColumn<>("Number of people");
        num_of_people.setPrefWidth(110);
        num_of_people.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().numOfPeople;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> duration = new JFXTreeTableColumn<>("Duration");
        duration.setPrefWidth(110);
        duration.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().duration;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> start_date = new JFXTreeTableColumn<>("Start date");
        start_date.setPrefWidth(90);
        start_date.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().startDate;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> end_date = new JFXTreeTableColumn<>("End date");
        end_date.setPrefWidth(90);
        end_date.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().endDate;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> payment_type = new JFXTreeTableColumn<>("Payment type");
        payment_type.setPrefWidth(105);
        payment_type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().paymentType;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> discount = new JFXTreeTableColumn<>("Discount %");
        discount.setPrefWidth(90);
        discount.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().discountPercent;
            }
        });
        
        JFXTreeTableColumn<CustomerReservationData, String> total = new JFXTreeTableColumn<>("Total");
        total.setPrefWidth(90);
        total.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<CustomerReservationData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<CustomerReservationData, String> param) {
                return param.getValue().getValue().total;
            }
        });
        
        ObservableList<CustomerReservationData> customersReservationData = FXCollections.observableArrayList();
        
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                customersReservationData.add(new CustomerReservationData(rs.getInt(1)+"", rs.getInt(2)+"", rs.getString(3), rs.getString(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), 
                        rs.getInt(13)+"", rs.getString(14))); 
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CustomersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final TreeItem<CustomerReservationData> root = new RecursiveTreeItem<CustomerReservationData>(customersReservationData, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(client_id, name, email, address, phone, room_number, num_of_people, duration, start_date, end_date, payment_type, discount, total);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    @FXML
    private void searchByRoomNumber() { //(MouseEvent event)
        loadAllCustomers("SELECT * FROM customer WHERE roomNumber = '" + searchText.getText().toString().trim() + "'");
        if (searchText.getText().toString().equals("")) {
            loadAllCustomers("SELECT * FROM customer");
        }
    }

    @FXML
    private void back(MouseEvent event) {
        Stage home = new Stage();
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("AdminScreen.fxml"));
        } 
        catch (IOException ex) {
            Logger.getLogger(AdminScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage current = (Stage)searchText.getScene().getWindow();
        Scene scene = new Scene(root);
        
        home.setScene(scene);
        home.initStyle(StageStyle.TRANSPARENT);
        
        current.hide();
        home.show();
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
}
