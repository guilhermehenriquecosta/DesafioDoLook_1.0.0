package br.edu.ifsp.sbv.desafiodolook.model;

import java.io.Serializable;

/**
 * Created by Adriel on 11/30/2017.
 */

public class Duel implements Serializable {
    private Integer duelID;
    private Album albumLeft;
    private Album albumRight;

    public Duel(){}

    public Duel(Integer duelID, Album albumLeft, Album albumRight){
        this.duelID = duelID;
        this.albumLeft = albumLeft;
        this.albumRight = albumRight;
    }

    public Integer getDuelID() {
        return duelID;
    }

    public void setDuelID(Integer duelID) {
        this.duelID = duelID;
    }

    public Album getAlbumLeft() {
        return albumLeft;
    }

    public void setAlbumLeft(Album albumLeft) {
        this.albumLeft = albumLeft;
    }

    public Album getAlbumRight() {
        return albumRight;
    }

    public void setAlbumRight(Album albumRight) {
        this.albumRight = albumRight;
    }
}
