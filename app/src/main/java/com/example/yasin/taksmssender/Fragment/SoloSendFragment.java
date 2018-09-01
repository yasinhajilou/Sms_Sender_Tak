package com.example.yasin.taksmssender.Fragment;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoloSendFragment extends Fragment {

    AppCompatSpinner spinnerSmsSubject;
    SQLiteOpenHelperTak openHelperTak;
    SQLiteDatabase database;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int lastSize;
    List<String> categories;

    public SoloSendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solo_send, container, false);
        spinnerSmsSubject = view.findViewById(R.id.spinnerSmsSubject);

        setUpSpinner(getContext());

        spinnerSmsSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


        //set spinner for showing Sms Subjects
    public void setUpSpinner(Context context) {

        openHelperTak = new SQLiteOpenHelperTak(context);
        database = openHelperTak.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT " + SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS + " FROM " + SmsContentContract.SmsEntry.TABLE_NAME_SMS, null);
        categories = new ArrayList<>();
        int columnIndexSubject = cursor.getColumnIndex(SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS);

        if (cursor.getCount() == 0) {
            categories.add("اطلاعاتی برای نمایش وجود ندارد");
            cursor.close();
        } else {
            while (cursor.moveToNext()) {
                categories.add(cursor.getString(columnIndexSubject));
            }
            cursor.close();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSmsSubject.setAdapter(dataAdapter);
    }

}



