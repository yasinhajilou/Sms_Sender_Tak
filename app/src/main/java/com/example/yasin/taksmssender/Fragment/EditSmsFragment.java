package com.example.yasin.taksmssender.Fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Adapter.RecyclerAdapterSms;
import com.example.yasin.taksmssender.EditActivity;
import com.example.yasin.taksmssender.Model.LandScape;
import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.DateInformation;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.Contracts.SmsCounterContract;
import com.example.yasin.taksmssender.db.Contracts.SmsHistoryContract;
import com.example.yasin.taksmssender.db.Contracts.TimeInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditSmsFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    String contentLength;
    AlertDialog dialog;
    AlertDialog.Builder builderAlert;
    SQLiteOpenHelper database ;
    RecyclerAdapterSms adapter ;

    public EditSmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_sms, container, false);
        fab = view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMain);


        setUpRecyclerView();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builderAlert = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_edit_data , null);
                final EditText edtSubject = (EditText) mView.findViewById(R.id.edtSubjectAlertDialog);
                final EditText edtContent = (EditText) mView.findViewById(R.id.edtContentAlertDialog);
                Button button = (Button)mView.findViewById(R.id.btnDoneAlertDialog);
                Button btnExit = (Button)mView.findViewById(R.id.btnExitAlertDialog);
                final TextView txtChar = (TextView)mView.findViewById(R.id.txtChar);
                edtContent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        contentLength = edtContent.getText().length()+"";

                        txtChar.setText(contentLength );
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subject = edtSubject.getText().toString();
                        String content = edtContent.getText().toString();
                        long check;
                        if (subject.isEmpty() || content.isEmpty()){
                            Toast.makeText(view.getContext(), "فیلدی نمی تواند خالی باشد.", Toast.LENGTH_SHORT).show();
                        }else {
                             SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(view.getContext());
                            SQLiteDatabase database1 = openHelperTak.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put(SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS , subject);
                            cv.put(SmsContentContract.SmsEntry.COLUMN_CONTENT_SMS , content);
                            check = database1.insert(SmsContentContract.SmsEntry.TABLE_NAME_SMS , null , cv);
                            if (check>0){
                                setUpRecyclerView();
                                Toast.makeText(view.getContext(), "اطلاعات با موفقیت ذخیره شد.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(view.getContext(), "خطایی رخ داده است.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builderAlert.setView(mView);
                dialog = builderAlert.create();
                dialog.show();
            }
        });
        return view;
    }


    //setUp for recyclerView
    public void setUpRecyclerView(){
        SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(getContext());
        SQLiteDatabase database = openHelperTak.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SmsContentContract.SmsEntry.TABLE_NAME_SMS ,null);
        if (cursor.getCount() > 0){
            adapter = new RecyclerAdapterSms(getContext() , LandScape.getData(getContext()));
            recyclerView.setAdapter(adapter);

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            cursor.close();
        }
    }

}
