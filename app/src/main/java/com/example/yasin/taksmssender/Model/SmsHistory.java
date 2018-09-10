package com.example.yasin.taksmssender.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.DateInformation;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.Contracts.SmsHistoryContract;
import com.example.yasin.taksmssender.db.Contracts.TimeInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

import java.util.ArrayList;

public class SmsHistory {
    private String Date;
    private String MessageContent;
    private String sendTo;
    private String time;
    private String status;
    private int ImgWay;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public static ArrayList<SmsHistory> getData(Context context) {
        SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(context);
        SQLiteDatabase database = openHelperTak.getReadableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + SmsHistoryContract.HistoryEntry.TABLE_NAME_HISTORY, null);

        if (res.getCount() <= 0) {
            return new ArrayList<>();
        } else {
            ArrayList<SmsHistory> dataList = new ArrayList<>();
            while (res.moveToNext()) {
                int columnIndexIdTime = res.getColumnIndex(SmsHistoryContract.HistoryEntry.COLUMN_SEND_TIME);
                int columnIndexIdDate = res.getColumnIndex(SmsHistoryContract.HistoryEntry.COLUMN_SEND_DATE);
                int columnIndexIdSms = res.getColumnIndex(SmsHistoryContract.HistoryEntry.COLUMN_SMS_INFORMATION);
                int columnIndexIdPeople = res.getColumnIndex(SmsHistoryContract.HistoryEntry.COLUMN_TARGET);

                Cursor cursorTime = database.rawQuery("SELECT * FROM " + TimeInformationContract.TimeEntry.TABLE_NAME_TIME + " WHERE " + TimeInformationContract.TimeEntry.ID + "= ?", new String[]{res.getInt(columnIndexIdTime) + ""});
                Cursor cursorDate = database.rawQuery("SELECT * FROM " + DateInformation.DateEntry.TABLE_NAME_DATE + " WHERE " + DateInformation.DateEntry.ID + " = ?", new String[]{res.getInt(columnIndexIdDate) + ""});
                Cursor cursorSms = database.rawQuery("SELECT * FROM " + SmsContentContract.SmsEntry.TABLE_NAME_SMS + " WHERE " + SmsContentContract.SmsEntry._ID + " = ?", new String[]{res.getInt(columnIndexIdSms) + ""});
                Cursor cursorPeople = database.rawQuery("SELECT * FROM " + PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE + " WHERE " + PeopleInformationContract.PeopleEntry._ID + " = ?", new String[]{res.getInt(columnIndexIdPeople) + ""});
//

                if (cursorTime.moveToFirst() && cursorDate.moveToFirst() && cursorPeople.moveToFirst() && cursorPeople.moveToFirst()) {
                    cursorTime.moveToFirst();
                    cursorDate.moveToFirst();
                    cursorSms.moveToFirst();
                    cursorPeople.moveToFirst();
                    SmsHistory landScape = new SmsHistory();
                    landScape.setMessageContent(cursorSms.getString(cursorSms.getColumnIndex(SmsContentContract.SmsEntry.COLUMN_CONTENT_SMS)));
                    String time = cursorTime.getString(cursorTime.getColumnIndex(TimeInformationContract.TimeEntry.COLUMN_HOUR)) + " : " + cursorTime.getString(cursorTime.getColumnIndex(TimeInformationContract.TimeEntry.COLUMN_MINUTE));
                    landScape.setTime(time);
                    String date = cursorDate.getString(cursorDate.getColumnIndex(DateInformation.DateEntry.COLUMN_MONTH)) + " / " + cursorDate.getString(cursorDate.getColumnIndex(DateInformation.DateEntry.COLUMN_DAY));
                    landScape.setDate(date);
                    landScape.setSendTo(cursorPeople.getString(cursorPeople.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE)));
                    int way = res.getInt(res.getColumnIndex(SmsHistoryContract.HistoryEntry.COLUMN_SEND_WAY));
                    if (way == SmsHistoryContract.HistoryEntry.sim){
                        landScape.setImgWay(R.drawable.simcard);
                    }else {
                        landScape.setImgWay(R.drawable.wifi);
                    }

                    dataList.add(landScape);

                    cursorPeople.close();
                    cursorTime.close();
                    cursorSms.close();
                    cursorDate.close();
                }

            }


            res.close();
            return dataList;
        }

    }

    public void setImgWay(int imgWay) {
        ImgWay = imgWay;
    }

    public int getImgWay() {
        return ImgWay;
    }
}
