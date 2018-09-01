package com.example.yasin.taksmssender.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

import java.util.ArrayList;

public class LandScape {
    private String title;
    private  String description;
    private String id;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ArrayList<LandScape> getData(Context context){
        SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(context);
        SQLiteDatabase database = openHelperTak.getReadableDatabase();
        Cursor datas = database.rawQuery("SELECT * FROM "+ SmsContentContract.SmsEntry.TABLE_NAME_SMS , null);
        ArrayList<LandScape> dataList = new ArrayList<>();
        if (datas.getCount() <= 0){
            datas.close();
            Toast.makeText(context, "اطلاعاتی برای نمایش وجود ندارد.", Toast.LENGTH_LONG).show();
            return null;
        }else {
            while (datas.moveToNext()){
                LandScape landScape = new LandScape();
                landScape.setTitle(datas.getString(datas.getColumnIndex(SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS)));
                landScape.setDescription(datas.getString(datas.getColumnIndex(SmsContentContract.SmsEntry.COLUMN_CONTENT_SMS)));
                landScape.setId(datas.getString(datas.getColumnIndex(SmsContentContract.SmsEntry._ID)));
                dataList.add(landScape);
            }
            datas.close();
            return dataList;

        }
    }


}

