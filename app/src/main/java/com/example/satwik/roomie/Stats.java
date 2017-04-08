package com.example.satwik.roomie;

import android.graphics.Bitmap;

/**
 * Created by satwik on 07-04-2017.
 */
public class Stats {

    private String owes;
    private String user1,user2;




public Stats()
{

}

    public Stats(String user1, String user2, String owes)
    {

        this.setOwes(owes);
        this.setUser1(user1);
        this.setUser2(user2);

    }


    public void setOwes(String owes) {
        this.owes = owes;
    }

    public String getOwes() {

        return owes;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }


}
