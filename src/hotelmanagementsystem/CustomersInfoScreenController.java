/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class CustomersInfoScreenController implements Initializable {

    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private StackPane stackepane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        int[][] data = readCustomersNumber();
        //System.out.println(Arrays.deepToString(data)); // wypisanie tablicy dla testu
        fillChart(data);
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
        } catch (IOException ex) {
            Logger.getLogger(CustomersInfoScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Stage current = (Stage) lineChart.getScene().getWindow();
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

    private int[][] readCustomersNumber() {
        int[][] tab = new int[2][12]; // zczytywanie z bazy clientId w celu dodania go do tabeli reservation
        String sql = "SELECT COUNT(*) AS 'suma' FROM reservation WHERE ? = (SELECT MONTH(startDate)) AND ? = (SELECT YEAR(startDate))";
        
        Connection connection = DBConnection.getConnection();

        try {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 12; j++) {
                    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
                    ps.setString(1, String.valueOf(j+1));
                    ps.setString(2, String.valueOf(i+2018));
                    
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        tab[i][j] = rs.getInt(1);
                    }
                }
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(CustomersInfoScreenController.class.getName()).log(Level.SEVERE, null, ex);
            
            Image image = new Image("img/delete.png");
            Notifications notification = Notifications.create().title("Error").text("Something went wrong (unable to read clientId).")
                    .hideAfter(Duration.seconds(3)).position(Pos.BOTTOM_LEFT).graphic(new ImageView(image));
            notification.darkStyle();
            notification.show();
        }

        return tab;
    }

    private void fillChart(int data[][]) {
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2018");
        series1.getData().add(new XYChart.Data("Jan", data[0][0]));
        series1.getData().add(new XYChart.Data("Feb", data[0][1]));
        series1.getData().add(new XYChart.Data("Mar", data[0][2]));
        series1.getData().add(new XYChart.Data("Apr", data[0][3]));
        series1.getData().add(new XYChart.Data("May", data[0][4]));
        series1.getData().add(new XYChart.Data("Jun", data[0][5]));
        series1.getData().add(new XYChart.Data("Jul", data[0][6]));
        series1.getData().add(new XYChart.Data("Aug", data[0][7]));
        series1.getData().add(new XYChart.Data("Sep", data[0][8]));
        series1.getData().add(new XYChart.Data("Oct", data[0][9]));
        series1.getData().add(new XYChart.Data("Nov", data[0][10]));
        series1.getData().add(new XYChart.Data("Dec", data[0][11]));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2019");
        series2.getData().add(new XYChart.Data("Jan", data[1][0]));
        series2.getData().add(new XYChart.Data("Feb", data[1][1]));
        series2.getData().add(new XYChart.Data("Mar", data[1][2]));
        series2.getData().add(new XYChart.Data("Apr", data[1][3]));
        series2.getData().add(new XYChart.Data("May", data[1][4]));
        series2.getData().add(new XYChart.Data("Jun", data[1][5]));
        series2.getData().add(new XYChart.Data("Jul", data[1][6]));
        series2.getData().add(new XYChart.Data("Aug", data[1][7]));
        series2.getData().add(new XYChart.Data("Sep", data[1][8]));
        series2.getData().add(new XYChart.Data("Oct", data[1][9]));
        series2.getData().add(new XYChart.Data("Nov", data[1][10]));
        series2.getData().add(new XYChart.Data("Dec", data[1][11]));

        lineChart.getData().addAll(series1, series2);
    }
}
