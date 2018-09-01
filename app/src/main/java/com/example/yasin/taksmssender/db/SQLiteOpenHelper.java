package com.example.yasin.taksmssender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DB_NAME = "SmsSender.db";
    private static final String TBL_NAME = "Texts";
    private static final String TBL_NAME2 = "Groups";
    private static final String TBL_NAME3 = "Amount";
    private static final String TBL_NAME4 = "SmsHistory";

    public SQLiteOpenHelper(Context context) {
        super(context,DB_NAME , null , 8);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT , Subject TEXT , Content TEXT)");
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_NAME2 +" (Id INTEGER PRIMARY KEY AUTOINCREMENT , GroupName TEXT , MemberFullName TEXT , MemberPhone TEXT)");
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_NAME3 + " (Id INTEGER PRIMARY KEY AUTOINCREMENT , SmsCounterSim TEXT , SmsCounterWifi , Year TEXT , Month TEXT)");
        sqLiteDatabase.execSQL(" CREATE TABLE " + TBL_NAME4 + "(Id INTEGER PRIMARY KEY AUTOINCREMENT , SmsContent TEXT , sendTime TEXT , SendDate TEXT , SendId TEXT , ToNumber TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_NAME3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_NAME4);
        onCreate(sqLiteDatabase);
    }

//    public boolean insertSmsHistory(String smsContent , String sendTime , String sendDate , String sendId , String toNumber){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("SmsContent" , smsContent );
//        cv.put("sendTime" , sendTime );
//        cv.put("sendDate" , sendDate );
//        cv.put("SendId" , sendId );
//        cv.put("ToNumber" , toNumber);
//        long res = db.insert(TBL_NAME4 , null , cv);
//
//        if (res == -1){
//            return false;
//        }else {
//            return true;
//        }
//    }
//    public boolean delete(String id){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        long res = db.delete(TBL_NAME , "Id=?" , new String[]{id});
//
//        if ( res == 0){
//            return false;
//        }else {
//            return true;
//        }
//    }
//    public boolean deleteGroups(String id){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        long res = db.delete(TBL_NAME2 , "Id=?" , new String[]{id});
//
//        if ( res == 0){
//            return false;
//        }else {
//            return true;
//        }
//    }
//
//    public boolean insert(String subject , String content){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("Subject" , subject);
//        cv.put("Content" ,content );
//        long res = db.insert(TBL_NAME , null , cv);
//
//        if (res == -1){
//            return false;
//        }else {
//            return true;
//        }
//    }
//    public boolean insertGroups(String groupName , String memberFullName , String memberPhone){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("GroupName" , groupName );
//        cv.put("MemberFullName" , memberFullName );
//        cv.put("MemberPhone" , memberPhone);
//        long res = db.insert(TBL_NAME2 , null , cv);
//
//        if (res == -1){
//            return false;
//        }else {
//            return true;
//        }
//
//    }
//
//    public boolean insertAmount(String counterSim , String counterWifi , String year , String month){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("SmsCounterSim" , counterSim );
//        cv.put("SmsCounterWifi" , counterWifi );
//        cv.put("Year" , year );
//        cv.put("Month" , month);
//        long res = db.insert(TBL_NAME3 , null , cv);
//
//        if (res == -1){
//            return false;
//        }else {
//            return true;
//        }
//
//    }
//
//    public boolean updateGroup(String Id ,String memberFullName , String memberPhone , String groupName){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("Id" , Id);
//        cv.put("GroupName" , groupName);
//        cv.put("MemberFullName" , memberFullName);
//        cv.put("MemberPhone" , memberPhone);
//
//        long res = db.update(TBL_NAME2 , cv , "Id=?" , new String[]{Id});
//
//        if (res < 1){
//            return false;
//        }else {
//            return true;
//        }
//    }
//
//    public boolean update(String Id ,String subject , String content){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("Id" , Id);
//        cv.put("Subject" , subject);
//        cv.put("Content" , content);
//
//        long res = db.update(TBL_NAME , cv , "Id=?" , new String[]{Id});
//
//        if (res < 1){
//            return false;
//        }else {
//            return true;
//        }
//    }
//
//
//    public Cursor showContent(String name){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor data = db.rawQuery(" SELECT Content FROM "+TBL_NAME+ " WHERE Subject==?" , new String[]{name});
//
//        return data;
//    }
//
//    public Cursor showMemberOfClass(String groupName){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor data = db.rawQuery(" SELECT MemberFullName FROM "+TBL_NAME2+ " WHERE GroupName==?" , new String[]{groupName});
//
//        return data;
//    }
//
//    public Cursor showMemberOfClassWithNumber(String groupName){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor data = db.rawQuery(" SELECT * FROM "+TBL_NAME2+ " WHERE GroupName==?" , new String[]{groupName});
//
//        return data;
//    }
//
//    public Cursor showAllData(){
//        SQLiteDatabase db =g this.etWritableDatabase();
//
//        try {
//            Cursor datas = db.rawQuery("SELECT * FROM " + TBL_NAME , null);
//            return datas;
//        }catch (Exception e){
//            return null;
//        }
//
//    }
//
//    public Cursor showAllDataGroups(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            Cursor datas = db.rawQuery("SELECT * FROM " + TBL_NAME2 , null);
//            return datas;
//        }catch (Exception e){
//            return null;
//        }
//
//
//
//    }
//
//    public Cursor showAllDataSmsHistory(){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor datas = db.rawQuery("SELECT * FROM " + TBL_NAME4 , null);
//
//        return datas;
//    }
//
//    public Cursor showSmsMonitor(String monthName){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor data = db.rawQuery(" SELECT * FROM "+TBL_NAME3+ " WHERE Month==?" , new String[]{monthName});
//        return data;
//    }
}
