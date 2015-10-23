package org.coursera.capstone.t1dteensclient.provider;

import android.net.Uri;

public final class ServiceContract {

    public static String AUTHORITY = "org.coursera.capstone.T1DTeensService.provider";
    public static final String SCHEME = "content";

    public static String CHECKINS_TABLE_NAME = "checkins";
    public static final Uri CHECKINS_DATA_URI = Uri.parse("content://" + AUTHORITY + "/" + CHECKINS_TABLE_NAME);
    public static final Uri CHECKINS_DATA_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + CHECKINS_TABLE_NAME + "/#");

    public static String CHECKINS_COLUMN_ID = "_id";
    public static String CHECKINS_COLUMN_CHECKING_ID = "checking_id";
    public static String CHECKINS_COLUMN_STATUS = "status";
    public static String CHECKINS_COLUMN_USER_ID = "user_id";
    public static String CHECKINS_COLUMN_TIMESTAMP = "timestamp";

    public static String ANSWERS_TABLE_NAME = "answers";
    public static final Uri ANSWERS_DATA_URI = Uri.parse("content://" + AUTHORITY + "/" + ANSWERS_TABLE_NAME);
    public static final Uri ANSWERS_DATA_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + ANSWERS_TABLE_NAME + "/#");

    public static String ANSWERS_COLUMN_ID = "_id";
    public static String ANSWERS_COLUMN_ANSWER_ID = "answer_id";
    public static String ANSWERS_COLUMN_TEXT = "text";
    public static String ANSWERS_COLUMN_CHECKIN_ID = "checkin_id";
    public static String ANSWERS_COLUMN_QUESTION_ID = "question_id";
    public static String ANSWERS_COLUMN_TIMESTAMP = "timestamp";

    public static String QUESTIONS_TABLE_NAME = "questions";
    public static final Uri QUESTIONS_DATA_URI = Uri.parse("content://" + AUTHORITY + "/" + QUESTIONS_TABLE_NAME);
    public static final Uri QUESTIONS_DATA_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + QUESTIONS_TABLE_NAME + "/#");

    public static String QUESTIONS_COLUMN_ID = "_id";
    public static String QUESTIONS_COLUMN_QUESTION_ID = "question_id";
    public static String QUESTIONS_COLUMN_TEXT = "text";
    public static String QUESTIONS_COLUMN_ANSWER_TYPE = "answer_type";
    public static String QUESTIONS_COLUMN_REQUIRED = "required";
    public static String QUESTIONS_COLUMN_TIMESTAMP = "timestamp";

    public static String OPTIONS_TABLE_NAME = "options";
    public static final Uri OPTIONS_DATA_URI = Uri.parse("content://" + AUTHORITY + "/" + OPTIONS_TABLE_NAME);
    public static final Uri OPTIONS_DATA_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + OPTIONS_TABLE_NAME + "/#");

    public static String OPTIONS_COLUMN_ID = "_id";
    public static String OPTIONS_COLUMN_OPTION_ID = "option_id";
    public static String OPTIONS_COLUMN_TEXT = "text";
    public static String OPTIONS_COLUMN_QUESTION_ID = "question_id";
    public static String OPTIONS_COLUMN_TIMESTAMP = "timestamp";
    public static String OPTIONS_COLUMN_WEIGHT = "weight";

    public static String RELATIONS_TABLE_NAME = "relations";
    public static final Uri RELATIONS_DATA_URI = Uri.parse("content://" + AUTHORITY + "/" + RELATIONS_TABLE_NAME);
    public static final Uri RELATIONS_DATA_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/" + RELATIONS_TABLE_NAME + "/#");

    public static String RELATIONS_COLUMN_ID = "_id";
    public static String RELATIONS_COLUMN_RELATION_ID = "relation_id";
    public static String RELATIONS_COLUMN_SUBSCRIBER = "subscriber";
    public static String RELATIONS_COLUMN_SUBSCRIPTION = "subscription";
    public static String RELATIONS_COLUMN_STATUS = "status";
    public static String RELATIONS_COLUMN_TIMESTAMP = "timestamp";
}
