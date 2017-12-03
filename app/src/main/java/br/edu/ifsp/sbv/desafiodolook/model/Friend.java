package br.edu.ifsp.sbv.desafiodolook.model;

import java.io.Serializable;

/**
 * Created by Adriel on 12/1/2017.
 */

public class Friend implements Serializable {
    private int friendID;
    private User userCurrent;
    private User userFollow;

    public Friend(){}

    public Friend(int friendID, User userCurrent, User userFollow){
        this.friendID = friendID;
        this.userCurrent = userCurrent;
        this.userFollow = userFollow;
    }

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

    public User getUserCurrent() {
        return userCurrent;
    }

    public void setUserCurrent(User userCurrent) {
        this.userCurrent = userCurrent;
    }

    public User getUserFollow() {
        return userFollow;
    }

    public void setUserFollow(User userFollow) {
        this.userFollow = userFollow;
    }
}
