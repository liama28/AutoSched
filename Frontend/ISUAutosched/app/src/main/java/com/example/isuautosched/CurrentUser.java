package com.example.isuautosched;

public class CurrentUser {
	private static int id;
	private static long sessionId;

	public static void setId(int userId) {
		id = userId;
	}

	public static void setSessionId(long sessionIds) {
		sessionId = sessionIds;
	}

	public static int getId() {
		return id;
	}

	public static long getSessionId() {
		return sessionId;
	}
}
