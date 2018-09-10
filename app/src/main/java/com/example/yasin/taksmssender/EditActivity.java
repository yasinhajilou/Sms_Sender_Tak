package com.example.yasin.taksmssender;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Adapter.FragmentAdapterSms;
import com.example.yasin.taksmssender.Adapter.RecyclerAdapterSms;
import com.example.yasin.taksmssender.Fragment.EditSmsFragment;
import com.example.yasin.taksmssender.Fragment.SmsHistoryFragment;
import com.example.yasin.taksmssender.Fragment.SmsMonitorFragment;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;

public class EditActivity extends AppCompatActivity {


    TabLayout tabLayout;
    FragmentAdapterSms adapter;
    ViewPager viewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        toolbar = (Toolbar)findViewById(R.id.ToolBarEdit);
        viewPager = findViewById(R.id.viewPagerSms);
        adapter = new FragmentAdapterSms(getSupportFragmentManager());
        adapter.addFragment(new EditSmsFragment(), "پیام ها");
        adapter.addFragment(new SmsHistoryFragment(), "تاریخچه");
        adapter.addFragment(new SmsMonitorFragment(), "نمودار");
        viewPager.setAdapter(adapter);



        tabLayout = (TabLayout) findViewById(R.id.tabLayOutSms);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("مدیریت اطلاعات");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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

    public void sayHi(){
        viewPager.setAdapter(adapter);
    }
}
