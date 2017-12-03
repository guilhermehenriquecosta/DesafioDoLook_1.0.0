package br.edu.ifsp.sbv.desafiodolook.model;

/**
 * Created by Guilherme on 03/12/2017.
 */

public class Ranking {
    private int friendID;
    private User userCurrent;
    private User userFollow;

    public Ranking(){}

    public Ranking(int friendID, User userCurrent, User userFollow){
        this.friendID = friendID;
        this.userCurrent = userCurrent;
        this.userFollow = userFollow;
    }

    public int getRankingID() {
        return friendID;
    }

    public void setRankingID(int friendID) {
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

