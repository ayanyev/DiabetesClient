package org.coursera.capstone.t1dteensclient.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "t1dteens_db";
    private static int DATABASE_VERSION = 1;
    private Context mContext;

    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    private static String CREATE_CHECKINS_DATA_TABLE =
            "CREATE TABLE "
                    + ServiceContract.CHECKINS_TABLE_NAME
                    + " ("
                    + ServiceContract.CHECKINS_COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ServiceContract.CHECKINS_COLUMN_CHECKING_ID
                    + " INTEGER, "
                    + ServiceContract.CHECKINS_COLUMN_USER_ID
                    + " INTEGER, "
                    + ServiceContract.CHECKINS_COLUMN_STATUS
                    + " TEXT, "
                    + ServiceContract.CHECKINS_COLUMN_TIMESTAMP
                    + " INTEGER) ";

    private static String CREATE_ANSWERS_DATA_TABLE =
            "CREATE TABLE "
                    + ServiceContract.ANSWERS_TABLE_NAME
                    + " ("
                    + ServiceContract.ANSWERS_COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ServiceContract.ANSWERS_COLUMN_ANSWER_ID
                    + " INTEGER, "
                    + ServiceContract.ANSWERS_COLUMN_TEXT
                    + " TEXT, "
                    + ServiceContract.ANSWERS_COLUMN_VALUE
                    + " INTEGER, "
                    + ServiceContract.ANSWERS_COLUMN_CHECKIN_ID
                    + " INTEGER, "
                    + ServiceContract.ANSWERS_COLUMN_QUESTION_ID
                    + " INTEGER,"
                    + ServiceContract.ANSWERS_COLUMN_TIMESTAMP
                    + " INTEGER) ";

    private static String CREATE_QUESTIONS_DATA_TABLE =
            "CREATE TABLE "
                    + ServiceContract.QUESTIONS_TABLE_NAME
                    + " ("
                    + ServiceContract.QUESTIONS_COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ServiceContract.QUESTIONS_COLUMN_QUESTION_ID
                    + " INTEGER, "
                    + ServiceContract.QUESTIONS_COLUMN_TEXT
                    + " TEXT, "
                    + ServiceContract.QUESTIONS_COLUMN_ANSWER_TYPE
                    + " TEXT, "
                    + ServiceContract.QUESTIONS_COLUMN_REQUIRED
                    + " INTEGER,"
                    + ServiceContract.QUESTIONS_COLUMN_RATEABLE
                    + " INTEGER,"
                    + ServiceContract.QUESTIONS_COLUMN_ORDER
                    + " INTEGER,"
                    + ServiceContract.ANSWERS_COLUMN_TIMESTAMP
                    + " INTEGER) ";

    private static String CREATE_OPTIONS_DATA_TABLE =
            "CREATE TABLE "
                    + ServiceContract.OPTIONS_TABLE_NAME
                    + " ("
                    + ServiceContract.OPTIONS_COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ServiceContract.OPTIONS_COLUMN_OPTION_ID
                    + " INTEGER, "
                    + ServiceContract.OPTIONS_COLUMN_TEXT
                    + " TEXT, "
                    + ServiceContract.OPTIONS_COLUMN_QUESTION_ID
                    + " INTEGER, "
                    + ServiceContract.OPTIONS_COLUMN_WEIGHT
                    + " INTEGER, "
                    + ServiceContract.OPTIONS_COLUMN_TIMESTAMP
                    + " INTEGER) ";

    private static String CREATE_RELATIONS_DATA_TABLE =
            "CREATE TABLE "
                    + ServiceContract.RELATIONS_TABLE_NAME
                    + " ("
                    + ServiceContract.RELATIONS_COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ServiceContract.RELATIONS_COLUMN_RELATION_ID
                    + " INTEGER, "
                    + ServiceContract.RELATIONS_COLUMN_SUBSCRIBER
                    + " INTEGER, "
                    + ServiceContract.RELATIONS_COLUMN_SUBSCRIPTION
                    + " INTEGER, "
                    + ServiceContract.RELATIONS_COLUMN_STATUS
                    + " TEXT, "
                    + ServiceContract.RELATIONS_COLUMN_TIMESTAMP
                    + " INTEGER) ";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CHECKINS_DATA_TABLE);
        db.execSQL(CREATE_ANSWERS_DATA_TABLE);
        db.execSQL(CREATE_QUESTIONS_DATA_TABLE);
        db.execSQL(CREATE_OPTIONS_DATA_TABLE);
        db.execSQL(CREATE_RELATIONS_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "
                + ServiceContract.CHECKINS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "
                + ServiceContract.ANSWERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "
                + ServiceContract.QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "
                + ServiceContract.OPTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "
                + ServiceContract.RELATIONS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
