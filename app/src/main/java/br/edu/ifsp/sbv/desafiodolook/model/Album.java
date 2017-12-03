package br.edu.ifsp.sbv.desafiodolook.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Adriel on 11/29/2017.
 */

public class Album implements Serializable {

    private Integer albumID;
    private Integer userID;
    private String urlPicture;
    private Date dateCreation;

    public Album(){}

    public Album(Integer albumID, Integer userID, String urlPicture){
        this.albumID = albumID;
        this.userID = userID;
        this.urlPicture = urlPicture;
    }

    public Album(Integer albumID, Integer userID, String urlPicture, Date dateCreation){
        this.albumID = albumID;
        this.userID = userID;
        this.urlPicture = urlPicture;
        this.dateCreation = dateCreation;
    }

    public Integer getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Integer albumID) {
        this.albumID = albumID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
}