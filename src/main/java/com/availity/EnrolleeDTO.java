package com.availity;

import java.util.ArrayList;
import java.util.Arrays;

public class EnrolleeDTO {

    private String userId;
    private String firstName;
    private String lastName;
    private Integer version;
    private String insuranceName;


    public EnrolleeDTO(String userId, String firstName, String lastName, Integer version, String insuranceName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.version = version;
        this.insuranceName = insuranceName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    @Override
    public String toString() {
        return String.join(",", this.getUserId(), this.getFirstName(), this.getLastName(), this.getVersion().toString(), this.insuranceName);
    }
}
