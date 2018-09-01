package com.example.yasin.taksmssender;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Adapter.RecyclerAdapterGroups;
import com.example.yasin.taksmssender.Adapter.RecyclerAdapterSms;
import com.example.yasin.taksmssender.Model.Groups;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;

public class ManagingGroupsActivity extends AppCompatActivity {

    String name;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managing_groups);

//        setUpRecyclerView();
    }

//    public void setUpRecyclerView(){
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGroups);
//        RecyclerAdapterGroups adapter = new RecyclerAdapterGroups(ManagingGroupsActivity.this , Groups.getData(ManagingGroupsActivity.this));
//        recyclerView.setAdapter(adapter);
//
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//    }
}
