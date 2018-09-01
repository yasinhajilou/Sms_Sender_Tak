package com.example.yasin.taksmssender.Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Adapter.RecyclerAdapterSms;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;

public class Message {

    public static void alert_msg(final Context context, String title, String message , final String Id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        // Set Dialog Title
        alertDialog.setTitle(title);

        // Set Dialog Message
        alertDialog.setMessage(message);


        // Set OK Button
        alertDialog.setPositiveButton("آره", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SQLiteOpenHelper db = new SQLiteOpenHelper(context);
//                boolean check = db.delete(Id);
//                if (check){
//
//                    RecyclerView recyclerView = (RecyclerView) ((Activity)context).findViewById(R.id.recyclerViewMain);
//                    RecyclerAdapterSms adapter = new RecyclerAdapterSms(context , LandScape.getData(context));
//                    recyclerView.setAdapter(adapter);
//
//                    LinearLayoutManager manager = new LinearLayoutManager(context);
//                    manager.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(manager);
//                    recyclerView.setAdapter(adapter);
//                    Toast.makeText(context, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "خطایی رخ داده است", Toast.LENGTH_LONG).show();
//                }
            }
        });

        alertDialog.setNegativeButton("بیخیال", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Show Alert Message
        alertDialog.create().show();
    }
}
