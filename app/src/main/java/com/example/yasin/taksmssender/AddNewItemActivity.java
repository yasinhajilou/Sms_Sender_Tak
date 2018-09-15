package com.example.yasin.taksmssender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.yasin.taksmssender.Fragment.AddContactFragment;

public class AddNewItemActivity extends AppCompatActivity {

    FrameLayout layoutAddGroup , layoutAddContatc;
    Intent intent;
    int situation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        layoutAddContatc = findViewById(R.id.frameAddContact);
        layoutAddGroup = findViewById(R.id.frameAddGroup);

        intent = getIntent();
        situation = intent.getIntExtra("intent_situation",0);

        if (situation == 1){
            layoutAddContatc.setVisibility(View.VISIBLE);
        }else {
            if (situation == 2){
                layoutAddGroup.setVisibility(View.VISIBLE);
            }
        }


    }
}
