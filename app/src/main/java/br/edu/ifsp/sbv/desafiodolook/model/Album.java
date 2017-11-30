package br.edu.ifsp.sbv.desafiodolook.model;

/**
 * Created by Adriel on 11/29/2017.
 */

public class Album {

    private Integer albumID;
    private Integer userID;
    private String urlPicture;

    public Album(){}

    public Album(Integer albumID, Integer userID, String urlPicture){
        this.albumID = albumID;
        this.userID = userID;
        this.urlPicture = urlPicture;
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

}