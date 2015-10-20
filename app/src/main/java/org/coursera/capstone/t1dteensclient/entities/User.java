package org.coursera.capstone.t1dteensclient.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.entities.enums.UserGender;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private long id;

    private String firstName;
    private String lastName;
    private int medicalRecord;
    private String username;
    private String password;
    private Boolean enabled;
    @JsonFormat(shape= JsonFormat.Shape.NUMBER, pattern = Constants.DATE_PATTERN)
    private Date dateOfBirth;
    private UserGender gender;
    private UserType userType;
    private Date timestamp;
    private List<CheckIn> checkIns = new ArrayList<>();

    public User() {

        this.enabled = true;
    }

    public User(String firstName, String lastName, int medicalRecord, String username, String password,
                Date dateOfBirth, UserGender gender, UserType userType) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.medicalRecord = medicalRecord;
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.userType = userType;
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

    public void setUsername(String login) {
        this.username = login;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CheckIn> getCheckIns() {
        return checkIns;
    }

    public void setCheckIns(List<CheckIn> checkIns) {
        this.checkIns = checkIns;
    }

    public int getMedicalRecord() {
        return medicalRecord;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMedicalRecord(int medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    /*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }*/
}
