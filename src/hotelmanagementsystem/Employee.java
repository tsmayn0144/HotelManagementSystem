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

public class Employee extends RecursiveTreeObject<Employee> {
    
    private StringProperty id;
    private StringProperty username;
    private StringProperty password;
    private StringProperty fullName;
    private StringProperty address;
    private StringProperty phone;
    private StringProperty startDate;
    private StringProperty userType;

    public StringProperty getId() {
        return id;
    }

    public StringProperty getUsername() {
        return username;
    }

    public StringProperty getPassword() {
        return password;
    }

    public StringProperty getFullName() {
        return fullName;
    }
    
    public StringProperty getAddress() {
        return address;
    }

    public StringProperty getPhone() {
        return phone;
    }

    public StringProperty getStartDate() {
        return startDate;
    }

    public StringProperty getUserType() {
        return userType;
    }
    
    public Employee() {
        super();
    }

    public Employee(String id, String username, String password, String fullName, String address, String phone, String startDate, String userType) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.fullName = new SimpleStringProperty(fullName);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.startDate = new SimpleStringProperty(startDate);
        this.userType = new SimpleStringProperty(userType);
    }
}