package com.example.AutoSched.HttpBodies;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Data
public class SessionId {

    private int userid;

    private int sessionid;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Random random = new Random();

    public SessionId(int userid, int sessionid) {
        this.userid = userid;
        this.sessionid = sessionid;
    }

    public SessionId(int UserId) {
        this.userid = UserId;
        this.sessionid = random.nextInt();
    }


}
