package com.example.yasin.taksmssender.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.Contracts.PeopleGroupContract.GroupEntry;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract.SmsEntry;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract.PeopleEntry;
import com.example.yasin.taksmssender.db.Contracts.DateInformation.DateEntry;
import com.example.yasin.taksmssender.db.Contracts.TimeInformationContract.TimeEntry;
import com.example.yasin.taksmssender.db.Contracts.SmsCounterContract.CounterEntry;
import com.example.yasin.taksmssender.db.Contracts.SmsHistoryContract.HistoryEntry;






import java.security.acl.Group;

public class SQLiteOpenHelperTak extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Tak.db";

    private static final int VERSION = 3;

    public SQLiteOpenHelperTak(Context context) {
        super(context, DATABASE_NAME, null , VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SMS_CONTENT_TABLE = "CREATE TABLE IF NOT EXISTS "+ SmsEntry.TABLE_NAME_SMS + " ( "+
                SmsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                SmsEntry.COLUMN_CONTENT_SMS + " TEXT NOT NULL," +
                SmsEntry.COLUMN_SUBJECT_SMS + " TEXT NOT NULL ); ";

        String CREATE_PEOPLE_INFORMATION_TABLE = "CREATE TABLE IF NOT EXISTS "+PeopleEntry.TABLE_NAME_PEOPLE + " ( "+
                PeopleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                PeopleEntry.COLUMN_FULL_NAME_PEOPLE + " TEXT NOT NULL," +
                PeopleEntry.COLUMN_DATE_OF_BIRTH + " INTEGER , "+
                PeopleEntry.COLUMN_GENDER_PEOPLE + " INTEGER, "+
                PeopleEntry.COLUMN_PHONE_NUMBER + " TEXT NOT NULL,"+
                PeopleEntry.COLUMN_REGISTER_DATE + " INTEGER ); ";

        String CREATE_PEOPLE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS "+ GroupEntry.TABLE_NAME_GROUP + " ( "+
                GroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                GroupEntry.COLUMN_GROUP_TITLE + " TEXT NOT NULL," +
                GroupEntry.COLUMN_CREATION_DATE+ " INTEGER,"+
                GroupEntry.COLUMN_MEMBER_INFORMATION + " INTEGER NOT NULL ); ";

        String CREATE_DATE_INFORMATION_TABLE = "CREATE TABLE IF NOT EXISTS "+ DateEntry.TABLE_NAME_DATE + " ( "+
                DateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DateEntry.COLUMN_YEAR + " INTEGER NOT NULL," +
                DateEntry.COLUMN_MONTH + " INTEGER NOT NULL ,"+
                DateEntry.COLUMN_DAY + " INTEGER NOT NULL ); ";

        String CREATE_TIME_INFORMATION_TABLE = "CREATE TABLE IF NOT EXISTS "+ TimeEntry.TABLE_NAME_TIME + " ( "+
                TimeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TimeEntry.COLUMN_HOUR + " INTEGER NOT NULL," +
                TimeEntry.COLUMN_MINUTE + " INTEGER NOT NULL ,"+
                TimeEntry.COLUMN_SECOND + " INTEGER ); ";

        String CREATE_SMS_COUNTER_TABLE = "CREATE TABLE IF NOT EXISTS "+ CounterEntry.TABLE_NAME_COUNTER + " ( "+
                CounterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CounterEntry.COLUMN_DATE + " INTEGER ," +
                CounterEntry.COLUMN_TIME + " INTEGER ,"+
                CounterEntry.COLUMN_WIFI_COUNTER + " INTEGER NOT NULL , " +
                CounterEntry.COLUMN_SIMCARD_COUNTER + " INTEGER NOT NULL);";
        String CREATE_SMS_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS "+ HistoryEntry.TABLE_NAME_HISTORY+ " ( "+
                HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                HistoryEntry.COLUMN_SMS_INFORMATION + " INTEGER ," +
                HistoryEntry.COLUMN_SEND_DATE + " INTEGER ,"+
                HistoryEntry.COLUMN_SEND_TIME + " INTEGER , " +
                HistoryEntry.COLUMN_STATUS + " INTEGER , " +
                HistoryEntry.COLUMN_TARGET + " INTEGER );";

        db.execSQL(CREATE_SMS_CONTENT_TABLE);
        db.execSQL(CREATE_PEOPLE_GROUP_TABLE);
        db.execSQL(CREATE_PEOPLE_INFORMATION_TABLE);
        db.execSQL(CREATE_DATE_INFORMATION_TABLE);
        db.execSQL(CREATE_TIME_INFORMATION_TABLE);
        db.execSQL(CREATE_SMS_COUNTER_TABLE);
        db.execSQL(CREATE_SMS_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion){
            case 2 :
                db.execSQL("DROP TABLE IF EXISTS "+PeopleEntry.TABLE_NAME_PEOPLE);
                onCreate(db);
                break;
            case 3:
                db.execSQL("ALTER TABLE "+HistoryEntry.TABLE_NAME_HISTORY +" ADD COLUMN "+HistoryEntry.COLUMN_SEND_WAY +" INTEGER ");
                break;
//            case 4:
//                db.execSQL("ALTER TABLE "+ TimeEntry.TABLE_NAME_TIME + " ADD COLUMN " + TimeEntry.COLUMN_PARENT_ID + " INTEGER NOT NULL");
//                db.execSQL("ALTER TABLE " + DateEntry.TABLE_NAME_DATE + " ADD COLUMN " + DateEntry.COLUMN_PARENT_ID + " INTEGER NOT NULL");
//                break;
        }
    }
}
