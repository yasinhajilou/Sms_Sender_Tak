package com.example.yasin.taksmssender.Model;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.yasin.taksmssender.db.SQLiteOpenHelper;

import java.util.ArrayList;

public class Groups {
    private String GroupName;
    private String GroupSize;

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupSize(String groupSize) {
        GroupSize = groupSize;
    }

    public String getGroupSize() {
        return GroupSize;
    }

//    public static ArrayList<Groups> getData(Context context) {
//
//        SQLiteOpenHelper db = new SQLiteOpenHelper(context);
//        Cursor res = db.showAllDataGroups();
//        ArrayList<Groups> dataList = new ArrayList<>();
//        int groupSize = res.getCount();
//        if ( groupSize == 0) {
//            Toast.makeText(context, "اطلاعاتی برای نمایش وجود ندارد.", Toast.LENGTH_LONG).show();
//        }else {
//            String[] name  = new String[res.getCount()];
//            int counter =0;
//            while (res.moveToNext()) {
//                Groups groups = new Groups();
//                if (counter == 0){
//                    name[counter] = res.getString(1);
//                    groups.setGroupName(name[counter]);
//                    Cursor resNumber = db.showMemberOfClass(name[counter]);
//                    groups.setGroupSize(resNumber.getCount() +"");
//                    dataList.add(groups);
//                }else {
//                    name[counter] = res.getString(1);
//                    if (name[counter].equals(name[counter - 1])){
//                        //its same and not need to add in list
//                    }else {
//                        groups.setGroupName(name[counter]);
//                        Cursor resNumber = db.showMemberOfClass(name[counter]);
//                        groups.setGroupSize(resNumber.getCount() +"");
//                        dataList.add(groups);
//                    }
//                }
//                counter++;
//            }
//        }
//
//        return dataList;
//    }
}
