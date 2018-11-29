/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Adrian Szmurło
 */
public class CustomerReservationData extends RecursiveTreeObject<CustomerReservationData> {
    
    StringProperty id;
    StringProperty clientId;
    StringProperty name;
    StringProperty email;
    StringProperty address;
    StringProperty phone;
    StringProperty roomNumber;
    StringProperty duration;
    StringProperty startDate;
    StringProperty endDate;
    StringProperty numOfPeople;
    StringProperty paymentType;
    StringProperty discountPercent;
    StringProperty total;
    
    public CustomerReservationData() {
        super();
    }

    public CustomerReservationData(String id, String clientId, String name, String email, String address, String phone, String roomNumber, 
            String duration, String startDate, String endDate, String numOfPeople, String paymentType, String discountPercent, String total) {
        this.id = new SimpleStringProperty(id);
        this.clientId = new SimpleStringProperty(clientId);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.duration = new SimpleStringProperty(duration);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.numOfPeople = new SimpleStringProperty(numOfPeople);
        this.paymentType = new SimpleStringProperty(paymentType);
        this.discountPercent = new SimpleStringProperty(discountPercent);
        this.total = new SimpleStringProperty(total);
    }
}