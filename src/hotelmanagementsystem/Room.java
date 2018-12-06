/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelmanagementsystem;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Adrian Szmurło
 */
public class Room extends RecursiveTreeObject<Room> {
    
    private StringProperty id;
    private StringProperty roomType;
    private StringProperty roomNumber;
    private StringProperty numberOfPeople;
    private StringProperty ac;
    private StringProperty balcony;
    private StringProperty roomPhone;
    private StringProperty roomPrice;
    private StringProperty roomStatus;
    
    public Room() {
        super();
    }

    public Room(String id, String roomType, String roomNumber, String numberOfPeople, String ac, String balcony, String roomPhone, String roomPrice, String roomStatus) {
        this.id = new SimpleStringProperty(id);
        this.roomType = new SimpleStringProperty(roomType);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.numberOfPeople = new SimpleStringProperty(numberOfPeople);
        this.ac = new SimpleStringProperty(ac);
        this.balcony = new SimpleStringProperty(balcony);
        this.roomPhone = new SimpleStringProperty(roomPhone);
        this.roomPrice = new SimpleStringProperty(roomPrice);
        this.roomStatus = new SimpleStringProperty(roomStatus);
    }

    public StringProperty getId() {
        return id;
    }

    public StringProperty getRoomType() {
        return roomType;
    }

    public StringProperty getRoomNumber() {
        return roomNumber;
    }

    public StringProperty getNumberOfPeople() {
        return numberOfPeople;
    }

    public StringProperty getAc() {
        return ac;
    }

    public StringProperty getBalcony() {
        return balcony;
    }

    public StringProperty getRoomPhone() {
        return roomPhone;
    }

    public StringProperty getRoomPrice() {
        return roomPrice;
    }

    public StringProperty getRoomStatus() {
        return roomStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.roomType, other.roomType)) {
            return false;
        }
        if (!Objects.equals(this.roomNumber, other.roomNumber)) {
            return false;
        }
        if (!Objects.equals(this.numberOfPeople, other.numberOfPeople)) {
            return false;
        }
        if (!Objects.equals(this.ac, other.ac)) {
            return false;
        }
        if (!Objects.equals(this.balcony, other.balcony)) {
            return false;
        }
        if (!Objects.equals(this.roomPhone, other.roomPhone)) {
            return false;
        }
        if (!Objects.equals(this.roomPrice, other.roomPrice)) {
            return false;
        }
        if (!Objects.equals(this.roomStatus, other.roomStatus)) {
            return false;
        }
        return true;
    }
    
    
}
