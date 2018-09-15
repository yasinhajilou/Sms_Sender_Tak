package com.example.yasin.taksmssender;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.yasin.taksmssender.Adapter.RecyclerAdapterAllContacts;
import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;


public class ContactsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapterAllContacts adapterAllContacts;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        toolbar = findViewById(R.id.toolBarContacts);
        final SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.inflate(R.menu.menu_speed_dial);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_new_contact, R.drawable.ic_person_add_black_24dp)
                        .setLabel(getString(R.string.New_Contact))
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create()

        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_new_group, R.drawable.ic_group_add_black_24dp)
                        .setLabel(getString(R.string.New_Group))
                        .setLabelColor(Color.WHITE)
                        .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
                        .create()

        );


        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_new_contact:
                        Intent intent = new Intent(ContactsActivity.this , AddNewItemActivity.class);
                        intent.putExtra("intent_situation" , 1);
                        startActivity(intent);
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_new_group:
                        intent = new Intent(ContactsActivity.this, AddNewItemActivity.class);
                        intent.putExtra("intent_situation" , 2);
                        startActivity(intent);
                        return false;
                    default:
                        return false;
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerViewContacts);

        adapterAllContacts = new RecyclerAdapterAllContacts(ContactsActivity.this , getData());

        if (getData()!=null){
            LinearLayoutManager manager = new LinearLayoutManager(ContactsActivity.this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapterAllContacts);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            // Grab your RecyclerView and the RecyclerViewFastScroller from the layout

            VerticalRecyclerViewFastScroller fastScroller = findViewById(R.id.fast_scroller);

            // Connect the recycler to the scroller (to let the scroller scroll the list)
            fastScroller.setRecyclerView(recyclerView);

            // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
            recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        }


    }

    //this method will make event listener for backward button on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getData()!=null){
            adapterAllContacts = new RecyclerAdapterAllContacts(ContactsActivity.this , getData());
            LinearLayoutManager manager = new LinearLayoutManager(ContactsActivity.this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapterAllContacts);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }
    }

    private List<Contacts> getData() {
        SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(this);
        SQLiteDatabase database = openHelperTak.getReadableDatabase();
        List<Contacts> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE , new String[]{});
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Contacts contacts = new Contacts();
                contacts.setFullName(cursor.getString(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE)));
                contacts.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER)));
                list.add(contacts);
            }
            cursor.close();
            return list;
        }else {
            return null;
        }
    }

}


