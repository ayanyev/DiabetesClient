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

    public static final String KEY_CURRENT_USERNAME ="currentUserUsername";
    public static final String KEY_CURRENT_PASSWORD ="currentUserPassword";
    public static final String KEY_CURRENT_ID ="currentUserId";
    public static final String KEY_CURRENT_FIRSTNAME ="currentUserFirstName";
    public static final String KEY_CURRENT_LASTNAME ="currentUserLastName";
    public static final String KEY_CURRENT_MEDREC ="currentUserMedrec";
    public static final String KEY_CURRENT_DOB ="currentUserDOB";
    public static final String KEY_CURRENT_GENDER ="currentUserGender";
    public static final String KEY_CURRENT_TYPE ="currentUserType";
    public static final String KEY_GUEST_USERNAME ="guest";
    public static final String KEY_GUEST_PASSWORD ="guest";

    public static final String KEY_FIRSTNAME_POLICY ="currentUserFirstName_policy";
    public static final String KEY_LASTNAME_POLICY ="currentUserLastName_policy";
    public static final String KEY_MEDREC_POLICY ="currentUserMedrec_policy";
    public static final String KEY_DOB_POLICY ="currentUserDOB_policy";
    public static final String KEY_GENDER_POLICY ="currentUserGender_policy";
    public static final String KEY_CHECKINS_POLICY ="currentUserCheckins_policy";

    public static final String KEY_REMINDERS_ENABLED ="reminders_enabled";
    public static final String KEY_REMINDER_TIME_1 ="reminder_1";
    public static final String KEY_REMINDER_TIME_2 ="reminder_2";
    public static final String KEY_REMINDER_TIME_3 ="reminder_3";

    public static final String SETALARM = "org.coursera.capstone.t1dteensclient.action.SETALARM";
    public static final String STOPALARMS = "org.coursera.capstone.t1dteensclient.action.STOPALARMS";
    public static final String STARTALARMS = "org.coursera.capstone.t1dteensclient.action.STARTALARMS";
    public static final String STARTNOTIFICATION = "org.coursera.capstone.t1dteensclient.action.STARTNOTIFICATION";
    public static final String STARTNOTIFICATIONFORDELAYED = "org.coursera.capstone.t1dteensclient.action.STARTNOTIFICATIONFORDELAYED";

    public static final String NEWCHECKIN = "org.coursera.capstone.t1dteensclient.action.NEWCHECKIN";
    public static final String SKIPCHECKIN = "org.coursera.capstone.t1dteensclient.action.SKIPCHECKIN";
    public static final String SHOWCHECKIN = "org.coursera.capstone.t1dteensclient.action.SHOWCHECKIN";
    public static final String DELAYCHECKIN = "org.coursera.capstone.t1dteensclient.action.DELAYCHECKIN";
    public static final String EDITCHECKIN = "org.coursera.capstone.t1dteensclient.action.EDITCHECKIN";

    public static final int GUEST_ID = 2;
    public static final long DELAY_INTERVAL = 1000 * 60 * 15;
    public static final int ALARM1 = 0;
    public static final int ALARM2 = 1;
    public static final int ALARM3 = 2;
    public static final int DELAYED_ALARM = 3;
    public static final int NEWCHECKIN_NOTIFICATION = 3;
    public static final long DAY_IN_MILLIS = 1000*60*60*24;

}
