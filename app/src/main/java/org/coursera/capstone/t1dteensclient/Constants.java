package org.coursera.capstone.t1dteensclient;

public class Constants {
    public static final String SERVER_URL = "https://10.0.3.2:8443";
    public static final String TOKEN_PATH = "/oauth/token";
    public static String CLIENT_ID = "mobile";

    public static final String DATE_PATTERN ="d MMMM yyyy";

    public static final String ACCOUNT_TYPE = "org.coursera.capstone";
    public static final String ACCOUNT ="syncaccount";

    public static final String LAST_TIME_CHECKINS_SYNCED ="lastTimeCheckinsSynced";
    public static final String LAST_TIME_QUESTIONS_SYNCED ="lastTimeOptionsSynced";
    public static final String LAST_TIME_RELATIONS_SYNCED ="lastTimeRelationsSynced";

    public static final int INIT_ASYNC_TASK = 0;
    public static final int INIT_CURSOR_LOADER = 1;

    public static final String CURRENT_USERNAME ="currentUserUsername";
    public static final String CURRENT_PASSWORD ="currentUserPassword";
    public static final String CURRENT_ID ="currentUserId";
    public static final String GUEST_USERNAME ="guest";
    public static final String GUEST_PASSWORD ="guest";
    public static final int GUEST_ID = 2;

}
