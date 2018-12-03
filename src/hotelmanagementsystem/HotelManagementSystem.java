/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Adrian Szmurło
 */
public class HotelManagementSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        
        updateRoomStatusAvailable();
        updateRoomStatusBusy();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void updateRoomStatusAvailable() {
        System.out.println("test 2");
        int res2 = 0;
        
        String check_rooms_to_make_available = "SELECT room.roomNumber FROM room, reservation "
                + "WHERE room.roomNumber=reservation.roomNumber AND room.roomStatus='busy' AND reservation.endDate=?";
        
        String sql_to_make_available = "UPDATE room SET room.roomStatus='available' WHERE room.roomNumber IN "
                + "(SELECT r2.roomNumber FROM (select * from room) AS r2, reservation "
                + "WHERE r2.roomNumber=reservation.roomNumber AND reservation.endDate=?)";
        
        Connection connection = DBConnection.getConnection();
        
        try {
            PreparedStatement ps_check_to_make_available = (PreparedStatement)connection.prepareStatement(check_rooms_to_make_available);
            ps_check_to_make_available.setString(1, LocalDate.now().toString());
            ResultSet rs2 = ps_check_to_make_available.executeQuery();
            
            if (rs2.next()) {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql_to_make_available);
                //ps.setString(1, "busy");
                ps.setString(1, LocalDate.now().toString());

                res2 = ps.executeUpdate();

                if (res2 > 0) {
                    Image image = new Image("img/mooo.png");
                    Notifications notification = Notifications.create().title("Done").text("Empty rooms made 'available' successfully.")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
                } 
                else {
                    Image image = new Image("img/delete.png");
                    Notifications notification = Notifications.create().title("Error").text("Unable to make empty rooms 'available'.")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
                }
            }
            else {
                Image image = new Image("img/info.png");
                    Notifications notification = Notifications.create().title("Info").text("No rooms to make them 'available' today.")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(HotelManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateRoomStatusBusy() {
        System.out.println("test1");
        int res1 = 0;

        String check_rooms_to_make_busy = "SELECT room.roomNumber FROM room, reservation "
                + "WHERE room.roomNumber=reservation.roomNumber AND room.roomStatus='available' AND reservation.startDate=?";
        
        String sql_to_make_busy = "UPDATE room SET room.roomStatus='busy' WHERE room.roomNumber IN "
                + "(SELECT r2.roomNumber FROM (select * from room) AS r2, reservation "
                + "WHERE r2.roomNumber=reservation.roomNumber AND reservation.startDate=?)";
        
        Connection connection = DBConnection.getConnection();
        
        try {
            PreparedStatement ps_check_to_make_busy = (PreparedStatement)connection.prepareStatement(check_rooms_to_make_busy);
            ps_check_to_make_busy.setString(1, LocalDate.now().toString());
            ResultSet rs = ps_check_to_make_busy.executeQuery();
            
            if (rs.next()) {
                PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql_to_make_busy);
                //ps.setString(1, "busy");
                ps.setString(1, LocalDate.now().toString());

                res1 = ps.executeUpdate();

                if (res1 > 0) {
                    Image image = new Image("img/mooo.png");
                    Notifications notification = Notifications.create().title("Done").text("Booked rooms made 'busy' successfully.")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
                } 
                else {
                    Image image = new Image("img/delete.png");
                    Notifications notification = Notifications.create().title("Error").text("Unable to make booked rooms 'busy'.")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
                }
            }
            else {
                Image image = new Image("img/info.png");
                    Notifications notification = Notifications.create().title("Info").text("No rooms to make them 'busy' today.")
                            .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
                    notification.darkStyle();
                    notification.show();
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(HotelManagementSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
