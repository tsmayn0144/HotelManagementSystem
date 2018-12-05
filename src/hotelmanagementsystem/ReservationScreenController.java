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
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mysql.jdbc.PreparedStatement;
import java.awt.AWTException;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.PrinterName;
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
    private JFXTextField total;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXDatePicker endDate;
    @FXML
    private StackPane stackepane;
    @FXML
    private JFXTextField discount;
    @FXML
    private JFXTreeTableView<Room> treeView;
    
    public static int nrRec = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeValues();
        addAllListeners();
        treeViewSelectionListener();
        addValidation();
        
        //loadAvailableRooms("SELECT * FROM room");
    }

    @FXML
    private void back(MouseEvent event) {
        Stage home = new Stage();
        Parent root = null;

        try {
            if (LoginScreenController.userRole.equals("admin")) {
                root = FXMLLoader.load(getClass().getResource("HomeScreenAdmin.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(AdminScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Stage current = (Stage) roomNumber.getScene().getWindow();
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
        clearForm();
    }

    @FXML
    private void book(MouseEvent event) {
        int res1 = 0;
        String sql1 = "INSERT INTO customer (name, email, address, phone) VALUES (?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();

        if (!checkEmptyFields()) {
            //////////////////////// CUSTOMER ///////////////////////
            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql1);
                ps.setString(1, name.getText().toString());
                ps.setString(2, email.getText().toString());
                ps.setString(3, address.getText().toString());
                ps.setString(4, phone.getText().toString());

                res1 = ps.executeUpdate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (res1 == 0) {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong (unable to add records to 'customer' table).")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }

            //////// clientId
            
            String client_id = ""; // zczytywanie z bazy clientId w celu dodania go do tabeli reservation
            String sql2 = "SELECT max(id) FROM customer";
            
            try {
                PreparedStatement ps2 = (PreparedStatement) connection.prepareStatement(sql2);
                ResultSet rs = ps2.executeQuery();

                while (rs.next()) {
                    client_id = rs.getString(1);
                }
            } 
            catch (SQLException ex) {
                Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (client_id == "") {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong (unable to read clientId).")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }

            ////////////////////// RESERVATION //////////////////////
            int res3 = 0;
            String sql3 = "INSERT INTO reservation (clientId, roomNumber, duration, startDate, endDate, numOfPeople, paymentType, discountPercent, total) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement ps3 = (PreparedStatement) connection.prepareStatement(sql3);

                ps3.setString(1, client_id);
                ps3.setString(2, roomNumber.getText().toString());
                ps3.setString(3, duration.getText().toString());
                ps3.setString(4, startDate.getValue().toString());
                ps3.setString(5, endDate.getValue().toString());
                ps3.setString(6, num_people.getValue().toString());
                ps3.setString(7, paymentType.getValue().toString());
                ps3.setString(8, discount.getText().toString());
                ps3.setString(9, total.getText().toString());

                res3 = ps3.executeUpdate();
            } 
            catch (SQLException ex) {
                Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (res1 > 0 && res3 > 0) {
                Image image = new Image("img/mooo.png");
                Notifications notification = Notifications.create().title("Done").text("Reservation submitted successfully")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
                if (startDate.getValue().toString().equals(LocalDate.now().toString())) {
                    System.out.println(startDate.getValue().toString());
                    System.out.println(LocalDate.now().toString());
                    updateStatus();
                }
            } 
            else {
                Image image = new Image("img/delete.png");
                Notifications notification = Notifications.create().title("Error").text("Something went wrong (unable to add records to 'reservation' table).")
                        .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                notification.darkStyle();
                notification.show();
            }

            //////////////////// CUSTOMER_RESERVATION_DATA //////////////////////////////////
            int res = 0;
            String sql = "INSERT INTO customerreservationdata (clientId, name, email, address, phone, roomNumber, duration, startDate, endDate, numOfPeople, paymentType, discountPercent, total) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);

                ps.setString(1, client_id);
                ps.setString(2, name.getText().toString());
                ps.setString(3, email.getText().toString());
                ps.setString(4, address.getText().toString());
                ps.setString(5, phone.getText().toString());
                ps.setString(6, roomNumber.getText().toString());
                ps.setString(7, duration.getText().toString());
                ps.setString(8, startDate.getValue().toString());
                ps.setString(9, endDate.getValue().toString());
                ps.setString(10, num_people.getValue().toString());
                ps.setString(11, paymentType.getValue().toString());
                ps.setString(12, discount.getText().toString());
                ps.setString(13, total.getText().toString());

                res = ps.executeUpdate();

                if (res > 0) {
                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
                    dialogLayout.setHeading(new Text("Receipt"));
                    dialogLayout.setBody(new Text("Do you want to print a receipt for that reservation?"));

                    JFXButton yes = new JFXButton("Yes");
                    JFXButton no = new JFXButton("No");

                    JFXDialog dialog = new JFXDialog(stackepane, dialogLayout, JFXDialog.DialogTransition.CENTER);

                    yes.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            print();
                            clearForm();
                            dialog.close();
                        }
                    });

                    no.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            clearForm();
                            dialog.close();
                        }
                    });

                    dialogLayout.setActions(yes, no);
                    dialog.show();
                }
            } 
            catch (SQLException ex) {
                Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void print() { // puste na razie - do implementacji
        PrinterJob pj = PrinterJob.getPrinterJob();        
        pj.setPrintable(new BillPrint(), getPageFormat(pj));

        try {
            pj.print();
        }
        catch (PrinterException ex) {
            Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateStatus() {
        String text = roomNumber.getText().toString().trim();
        String sql = "UPDATE room SET roomStatus=? WHERE roomNumber=?";
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
            ps.setString(1, "busy");
            ps.setString(2, text);

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkEmptyFields() {
        if (name.getText().toString().equals("") || email.getText().toString().equals("") || address.getText().toString().equals("")
                || phone.getText().toString().equals("") || num_people.getValue().toString().equals("") || paymentType.getValue().toString().equals("")
                || duration.getText().toString().equals("") || roomType.getText().toString().equals("") || roomNumber.getText().toString().equals("")
                || startDate.getValue().toString().equals("") || endDate.getValue().toString().equals("") || price.getText().toString().equals("")
                || discount.getText().toString().equals("") || total.getText().toString().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("One or more fields are empty");
            alert.setContentText("Please fill all the required fields.");
            alert.showAndWait();

            return true;
        } 
        else {
            return false;
        }
    }

    private void clearForm() {
        name.setText("");
        email.setText("");
        address.setText("");
        phone.setText("");
        num_people.setValue("");
        paymentType.setValue("");
        duration.setText("");
        roomType.setText("");
        roomNumber.setText("");
        price.setText("");
        discount.setText("0");
        total.setText("");
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now().plusDays(1));
    }

    private void addAllListeners() {
        duration.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    endDate.setValue(LocalDate.from(startDate.getValue()).plusDays(Long.parseLong(duration.getText().toString())));
                }
                /////////////////////////
                if (!newValue && !price.getText().equals("") && !discount.getText().equals("")) {
                    countTotal();
                }
            }
        });

        startDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (startDate.getValue().isBefore(LocalDate.now())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Date error");
                        alert.setHeaderText("Start date can not occur before today.");
                        alert.setContentText("Please choose correct date and try again.");
                        alert.showAndWait();
                        startDate.setValue(LocalDate.now());
                    }
                    else {
                        //if (startDate.getValue().isAfter(endDate.getValue()) && duration.getText().toString().equals("")) {
                        if (duration.getText().toString().equals("")) {
                            endDate.setValue(LocalDate.from(startDate.getValue()).plusDays(1));
                        } 
                        else {
                            endDate.setValue(LocalDate.from(startDate.getValue()).plusDays(Long.parseLong(duration.getText().toString())));
                        }
                    }
                }
            }
        });

        endDate.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    //if (endDate.getValue().isBefore(startDate.getValue()) && duration.getText().toString().equals("")) {
                    if (startDate.getValue().isAfter(endDate.getValue())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Date error");
                        alert.setHeaderText("Incorrect end date");
                        alert.setContentText("The end date has to be after the start date. Please choose end date again.");
                        alert.showAndWait();
                        
                        if (duration.getText().toString().equals("")) {
                            endDate.setValue(LocalDate.from(startDate.getValue()).plusDays(1));
                        } 
                        else {
                            endDate.setValue(LocalDate.from(startDate.getValue()).plusDays(Long.parseLong(duration.getText().toString())));
                        }
                    }
                    else {
                        Period period = Period.between(LocalDate.from(startDate.getValue()), LocalDate.from(endDate.getValue()));
                        int diff = period.getDays();
                        duration.setText(String.valueOf(diff));
                    }
                }
            }
        });

        roomNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    String sql = "SELECT roomPrice,roomType FROM room WHERE roomNumber=?";

                    Connection connection = DBConnection.getConnection();
                    try {
                        PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                        ps.setString(1, roomNumber.getText().toString());
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            price.setText(rs.getString(1));
                            roomType.setText(rs.getString(2));
                        } 
                        else {
                            price.setText("");
                            total.setText("");
                            roomType.setText("");
                        }
                    } 
                    catch (SQLException ex) {
                        Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //////////////////////////////
                if (!newValue && !duration.getText().equals("") && !price.getText().equals("") && !discount.getText().equals("")) {
                    countTotal();
                }
            }
        });

        discount.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue && !duration.getText().equals("") && !price.getText().equals("")) {
                    countTotal();
                }
            }
        });
    }

    private void initializeValues() {
        discount.setText("0");
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now().plusDays(1));
        num_people.setValue("");
        paymentType.setValue("");
        
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
    
    private void loadAvailableRooms(String sql) {
        
        JFXTreeTableColumn<Room, String> id = new JFXTreeTableColumn<>("Id");
        id.setPrefWidth(65);
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getId();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_number = new JFXTreeTableColumn<>("Room nr");
        room_number.setPrefWidth(90);
        room_number.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomNumber();
            }
        });
        
        JFXTreeTableColumn<Room, String> room_type = new JFXTreeTableColumn<>("Type");
        room_type.setPrefWidth(80);
        room_type.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomType();
            }
        });
        
        JFXTreeTableColumn<Room, String> num_of_people = new JFXTreeTableColumn<>("Num. of ppl");
        num_of_people.setPrefWidth(90);
        num_of_people.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getNumberOfPeople();
            }
        });
        
        JFXTreeTableColumn<Room, String> ac = new JFXTreeTableColumn<>("A/C");
        ac.setPrefWidth(81);
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
        
        JFXTreeTableColumn<Room, String> room_price = new JFXTreeTableColumn<>("Room price ($)");
        room_price.setPrefWidth(95);
        room_price.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().getRoomPrice();
            }
        });
        
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
            
            ps.setString(1, num_people.getValue().toString());
            ps.setString(2, startDate.getValue().toString());
            ps.setString(3, endDate.getValue().toString());
            ps.setString(4, startDate.getValue().toString());
            ps.setString(5, endDate.getValue().toString());
            ps.setString(6, startDate.getValue().toString());
            ps.setString(7, startDate.getValue().toString());
            ps.setString(8, endDate.getValue().toString());
            ps.setString(9, endDate.getValue().toString());
            //ps.setString(10, startDate.getValue().toString());
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                rooms.add(new Room(rs.getInt(1)+"", rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(ReservationScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final TreeItem<Room> root = new RecursiveTreeItem<Room>(rooms, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(id, room_number, room_type, num_of_people, ac, balcony, room_phone, room_price);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }
    
    private void treeViewSelectionListener() {
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Room>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Room>> observable, TreeItem<Room> oldValue, TreeItem<Room> newValue) {
                roomNumber.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getRoomNumber().getValueSafe()));
                roomType.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getRoomType().getValueSafe()));
                price.setText(String.valueOf(treeView.getSelectionModel().getSelectedItem().getValue().getRoomPrice().getValueSafe()));
            
                countTotal();
            } 
        });
    }

    @FXML
    private void showRooms(MouseEvent event) {
        /*
        SELECT * FROM room WHERE room.numberOfPeople>=2 AND room.roomNumber NOT IN (SELECT room.roomNumber FROM reservation, room WHERE reservation.roomNumber=room.roomNumber AND (((reservation.startDate >= '2018-12-02' AND reservation.startDate < '2018-12-10') OR (reservation.endDate > '2018-12-02' AND reservation.endDate <= '2018-12-10')) OR (('2018-12-02' >= reservation.startDate AND '2018-12-02' < reservation.endDate) OR ('2018-12-10' > reservation.startDate AND '2018-12-10' <= reservation.endDate))))
        */
        //loadAvailableRooms("SELECT * FROM room");
        if (num_people.getValue().toString().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Searching error");
            alert.setHeaderText("'How many people' field is empty");
            alert.setContentText("Please choose number of people and try again.");
            alert.showAndWait();
        }
        else {
            loadAvailableRooms("SELECT * FROM room WHERE room.numberOfPeople>=? AND room.roomNumber NOT IN "
                + "(SELECT room.roomNumber FROM reservation, room WHERE reservation.roomNumber=room.roomNumber AND "
                + "(((reservation.startDate >= ? AND reservation.startDate < ?) OR "
                + "(reservation.endDate > ? AND reservation.endDate <= ?)) OR "
                + "((? >= reservation.startDate AND ? < reservation.endDate) OR "
                + "(? > reservation.startDate AND ? <= reservation.endDate))))");
        }
    }
    
    private void countTotal() {
        double sum1 = Double.parseDouble(duration.getText().toString()) * Double.parseDouble(price.getText().toString());
        double sum2 = sum1 - ((Double.parseDouble(discount.getText().toString()) / 100) * sum1);
        int sum = (int) sum2;
        total.setText(String.valueOf(sum));
    }
    
    class BillPrint implements Printable { // klasa wewnętrzna
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            int result = NO_SUCH_PAGE;

            if (pageIndex == 0) {
                Graphics2D g2d = (Graphics2D) graphics;
                double width = pageFormat.getImageableWidth();
                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
                FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 7));

                int idLength = metrics.stringWidth("000");
                int amtLength = metrics.stringWidth("000000");
                int qtyLength = metrics.stringWidth("00000");
                int priceLength = metrics.stringWidth("000000");
                int prodLength = (int) width - idLength - amtLength - qtyLength - priceLength - 17;

                int productPosition = 0;
                int discountPosition = prodLength + 5;
                int pricePosition = discountPosition + idLength + 10;
                int qtyPosition = pricePosition + priceLength + 4;
                int amtPosition = qtyPosition + qtyLength;

                try {
                    /*Draw Header*/
                    int y = 20;
                    int yShift = 10;
                    int headerRectHeight = 15;
                    int headerRectHeighta = 40;

                    String user_name = name.getText();
                    String add = address.getText();
                    String phone_nr = phone.getText();
                    String num = num_people.getValue();
                    String dur = duration.getText();
                    String roomnr = roomNumber.getText();
                    String disc = discount.getText();
                    String payment_type = paymentType.getValue();

                    /*g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("-------------------------------------", 12, y);
                    y += yShift;
                    g2d.drawString("         Hotel Bill Receipt          ", 12, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 12, y);
                    y += headerRectHeight;*/
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("---------------------------------------------------------", 12, y);
                    y += yShift;
                    g2d.drawString("                    Hotel Bill Receipt                   ", 14, y);
                    y += yShift;
                    g2d.drawString("---------------------------------------------------------", 12, y);
                    y += 35;

                    g2d.drawString("---------------------------------------------------------", 12, y);
                    y += headerRectHeight;
                    g2d.drawString("  Name                 " + user_name + "  ", 12, y);
                    y += yShift;
                    g2d.drawString("  Address              " + add + "  ", 12, y);
                    y += yShift;
                    g2d.drawString("  Phone                " + phone_nr + "  ", 12, y);
                    y += yShift;
                    g2d.drawString("  Number of people     " + num + "  ", 12, y);
                    y += yShift;
                    g2d.drawString("  Duration             " + dur + " days ", 12, y);
                    y += yShift;
                    g2d.drawString("  Room number          " + roomnr + "  ", 12, y);
                    y += yShift;
                    g2d.drawString("  Discount             " + disc + "%  ", 12, y);
                    y += yShift;
                    g2d.drawString("  Payment type         " + payment_type + "  ", 12, y);
                    y += yShift;
                    g2d.drawString("---------------------------------------------------------", 12, y);
                    y += yShift;
                    g2d.drawString("                          Total price = " + total.getText().toString() + " ", 12, y);
                    y += yShift;
                    g2d.drawString("---------------------------------------------------------", 12, y);
                    y += yShift;
                    g2d.drawString("                   Hotel Phone Number                    ", 12, y);
                    y += yShift;
                    g2d.drawString("                       0123456789                        ", 12, y);
                    y += yShift;
                    g2d.drawString("*********************************************************", 12, y);
                    y += yShift;
                    g2d.drawString("            THANK YOU FOR VISITING OUR HOTEL             ", 12, y);
                    y += yShift;
                    g2d.drawString("*********************************************************", 12, y);
                    y += yShift;
                } 
                catch (Exception r) {
                    r.printStackTrace();
                }
                result = PAGE_EXISTS;
            }
            return result;
        }
    } // klasa wewnętrzna

    public PageFormat getPageFormat(PrinterJob pj) {
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convert_CM_To_PPI(12); // dla drukarki trzeba przekonwertować na ppi
        double height = convert_CM_To_PPI(headerHeight + middleHeight + footerHeight + 4);
        paper.setSize(width, height);
        paper.setImageableArea(0, 10, width, height - convert_CM_To_PPI(1));

        pf.setOrientation(PageFormat.PORTRAIT);
        pf.setPaper(paper);

        return pf;
    }

    public static double convert_CM_To_PPI(double cm) {
        return toPPI(cm * 0.393600787);
    }

    public static double toPPI(double inch) {
        return inch * 72d; // domyślnie 72ppi
    }
    
    private void addValidation() {
        Robot r;
        try {
            r = new Robot();
        
            name.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!name.getText().matches("[A-Z][a-zA-ZęóąśłżźćńĘÓĄŚŁŻŹĆŃ\\-\\s]{4,30}") && !name.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Name error");
                            alert.setHeaderText("Full name must be 4 - 30 characters long (letters and '-' only) and begin with uppercase character.");
                            alert.setContentText("Please enter correct name.");
                            alert.showAndWait();

                            name.setText("");

                            pressShiftTab(r);
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
                        //if (!phone.getText().matches("\\+{0,1}[0-9]{9,15}") && !phone.getText().equals("")) {  
                        if (!phone.getText().matches(".{9,15}") && !phone.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Phone error");
                            alert.setHeaderText("Phone must be 9 - 15 numbers long (optional + at the beginning).");
                            alert.setContentText("Please enter correct phone number.");
                            alert.showAndWait();

                            phone.setText("");
                            
                            pressShiftTab(r);
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
                            
                            pressShiftTab(r);
                        }
                    }
                }
            });

            email.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (!email.getText().matches("[a-zA-Z0-9_\\.\\-]+@[a-zA-Z0-9]+\\.[a-z]{2,3}") && !email.getText().equals("")) {

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Email format error");
                            alert.setHeaderText("Email must be 6 - 30 characters long (and contain @ and .xx suffix).");
                            alert.setContentText("Please enter correct email.");
                            alert.showAndWait();

                            email.setText("");
                            
                            pressShiftTab(r);
                        }
                    }
                }
            });

            duration.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("[1-9]?[0-9]?")) ? change : null));
            duration.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        //if ((!duration.getText().matches(".{1,2}") || duration.getText().matches("0[0-9]?"))&& !duration.getText().equals("")) {  
                        if (duration.getText().matches("0[0-9]?") && !duration.getText().equals("")) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Duration error");
                            alert.setHeaderText("Duration must be at least 1 day long.");
                            alert.setContentText("Please enter correct duration.");
                            alert.showAndWait();

                            duration.setText("");
                            
                            pressShiftTab(r);
                        }
                    }
                }
            });

            discount.setTextFormatter(new TextFormatter<>(change
                    -> (change.getControlNewText().matches("[0-9]?[0-9]?")) ? change : null));
            discount.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        if (discount.getText().matches("0[0-9]?") && !discount.getText().equals("")) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Discount error");
                            alert.setHeaderText("Discount must be a number between 0-99.");
                            alert.setContentText("Please enter correct discount.");
                            alert.showAndWait();

                            discount.setText("0");
                            
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
    
    public static void pressShiftTab(Robot r) {
        r.keyPress(KeyEvent.VK_SHIFT);
        r.keyPress(KeyEvent.VK_TAB);
        r.keyRelease(KeyEvent.VK_SHIFT);
        r.keyRelease(KeyEvent.VK_TAB);
    }
}
