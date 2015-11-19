package org.coursera.capstone.t1dteensclient.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.coursera.capstone.t1dteensclient.Constants;
import org.coursera.capstone.t1dteensclient.Utils;
import org.coursera.capstone.t1dteensclient.entities.enums.UserGender;
import org.coursera.capstone.t1dteensclient.entities.enums.UserType;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Parcelable {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer medicalRecord;
    private String username;
    private String password;
    private Boolean enabled;
    @JsonFormat(shape= JsonFormat.Shape.NUMBER, pattern = Constants.DATE_PATTERN)
    private Date dateOfBirth;
    private UserGender gender;
    private UserType userType;
    private Date timestamp;
    private List<CheckIn> checkIns = new ArrayList<>();
    private String sharePolicy;

    public User() {

        this.enabled = true;
        this.sharePolicy = "111111";
    }

    public User(String username, String password) {

        this.username = username;
        this.password = password;
        this.enabled = true;
        this.sharePolicy = "111111";
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
        this.sharePolicy = "111111";
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

    public Long getId() {
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

    public Integer getMedicalRecord() {
        return medicalRecord;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMedicalRecord(Integer medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getSharePolicy() {
        return sharePolicy;
    }

    public void setSharePolicy(String sharePolicy) {
        this.sharePolicy = sharePolicy;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String toFullInfo(Context context) {

        StringBuilder builder = new StringBuilder();

        String gender = (this.gender == null
                ? "<private>"
                : String.valueOf(this.gender).toLowerCase());
        String dob = (this.dateOfBirth == null
                ? "<private>"
                : new DateTime(this.dateOfBirth).toString(Utils.getDatePattern(context)));
        String medrec = (this.medicalRecord == null
                ? "<private>"
                : String.valueOf(this.medicalRecord));
        String checkins = (this.checkIns.size() == 0
                ? "0 or <private>"
                : String.valueOf(this.getCheckIns().size()));

        builder
                .append("\n")
                .append("Name: ").append(this.toString()).append("\n")
                .append("User type: ").append(String.valueOf(this.userType).toLowerCase()).append("\n")
                .append("Gender: ").append(gender).append("\n")
                .append("Date of birth: ").append(dob).append("\n")
                .append("Med. record #: ").append(medrec).append("\n")
                .append("# of checkins: ").append(checkins);

        return builder.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeValue(this.medicalRecord);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeValue(this.enabled);
        dest.writeLong(dateOfBirth != null ? dateOfBirth.getTime() : -1);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeInt(this.userType == null ? -1 : this.userType.ordinal());
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1);
        dest.writeTypedList(checkIns);
        dest.writeString(this.sharePolicy);
    }

    protected User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.medicalRecord = (Integer) in.readValue(Integer.class.getClassLoader());
        this.username = in.readString();
        this.password = in.readString();
        this.enabled = (Boolean) in.readValue(Boolean.class.getClassLoader());
        long tmpDateOfBirth = in.readLong();
        this.dateOfBirth = tmpDateOfBirth == -1 ? null : new Date(tmpDateOfBirth);
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : UserGender.values()[tmpGender];
        int tmpUserType = in.readInt();
        this.userType = tmpUserType == -1 ? null : UserType.values()[tmpUserType];
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
        this.checkIns = in.createTypedArrayList(CheckIn.CREATOR);
        this.sharePolicy = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
