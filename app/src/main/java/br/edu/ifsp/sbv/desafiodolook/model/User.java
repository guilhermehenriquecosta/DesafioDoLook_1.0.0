package br.edu.ifsp.sbv.desafiodolook.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Adriel on 11/20/2017.
 */

public class User implements Serializable {

    private Integer userID;
    private String email;
    private String password;
    private String userName;
    private String nickName;
    private String description;
    private String urlAvatar;
    private Date dateBirth;
    private String deviceID;
    private Boolean status;
    private Date dateCreation;

    public  User(){}

    public  User(Integer userID){
        this.userID = userID;
    }

    public User(Integer userID, String email, String userName, String urlAvatar){
        this.userID = userID;
        this.email = email;
        this.userName = userName;
        this.urlAvatar = urlAvatar;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}