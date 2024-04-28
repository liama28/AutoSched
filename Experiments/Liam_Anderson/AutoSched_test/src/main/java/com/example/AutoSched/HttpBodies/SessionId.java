package com.example.AutoSched.HttpBodies;

import java.util.Random;

public class SessionId {

    private int UserId;

    private int SessionId;

    Random random = new Random();

    public SessionId(int UserId) {
        this.UserId = UserId;
        this.SessionId = random.nextInt();
    }

    public void setSessionId(int sessionId) {
        SessionId = sessionId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getSessionId() {
        return SessionId;
    }

    public int getUserId() {
        return UserId;
    }


}
