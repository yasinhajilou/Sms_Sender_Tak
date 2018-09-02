package com.example.yasin.taksmssender.Fragment;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.yasin.taksmssender.ContactUsActivity;
import com.example.yasin.taksmssender.ContactsActivity;
import com.example.yasin.taksmssender.MainActivity;
import com.example.yasin.taksmssender.Model.ContactChip;
import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

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
    AppCompatRadioButton radioPhoneNum , radioContacts;
    ChipsInput chipsInput;
    EditText edtPhoneNumber;
    String phoneNumber;
    Button btnSend;
    int chipsLenght ;
    List<ContactChip> contactsList;
    String contactName;
    String contactPhone;
    AlertDialog dialogChooseWay;

    public SoloSendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_solo_send, container, false);


        spinnerSmsSubject = view.findViewById(R.id.spinnerSmsSubject);
        radioContacts = view.findViewById(R.id.radioContacts);
        radioPhoneNum = view.findViewById(R.id.radioPhoneNum);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        chipsInput = view.findViewById(R.id.chips_input);
        btnSend = view.findViewById(R.id.btnSend);

        chipsInput.setVisibility(View.GONE);
        contactsList = new ArrayList<>();
        getContacts(getContext());


        setUpSpinner(getContext());

        spinnerSmsSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioContacts.setChecked(false);
                edtPhoneNumber.setEnabled(true);
                chipsInput.setVisibility(View.GONE);
            }
        });

        radioContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPhoneNum.setChecked(false);
                edtPhoneNumber.setEnabled(false);
                chipsInput.setVisibility(View.VISIBLE);

            }
        });

        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chipInterface, int i) {
                if (i>1){
                    Toast.makeText(getContext(), "پیام فقط به مخاطب اول ارسال می شود", Toast.LENGTH_LONG).show();
                }
                chipsLenght = i;
            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {
                chipsLenght = i ;
            }

            @Override
            public void onTextChanged(CharSequence charSequence) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioPhoneNum.isChecked() && !radioContacts.isChecked()){
                    Toast.makeText(getContext(), "لطفا یک روش ارسال را انتخاب کنید", Toast.LENGTH_SHORT).show();
                }else {


                    if (radioContacts.isChecked()){

                        if (chipsLenght == 1){
                            List<? extends ChipInterface> chipInfor = chipsInput.getSelectedChipList();
                            contactPhone= chipInfor.get(0).getInfo();
                            contactName= chipInfor.get(0).getLabel();
                            //show dialog
                            chooseWay(getContext());
                        }else {
                            if (chipsLenght > 1){
                                Toast.makeText(getContext(), "بیش از یک مخاطب انتخاب شده!", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getContext(), "مخاطبی انتخاب نشده.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }




                }
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

    //get Contacts from database
    public void getContacts(Context context){
        openHelperTak = new SQLiteOpenHelperTak(context);
        database = openHelperTak.getReadableDatabase();
        String query = "SELECT * FROM "+ PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE ;
        Cursor cursor = database.rawQuery(query , null);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                int id =
                        cursor.getInt(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry._ID));
                String phoneNumberChips =
                        cursor.getString(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER));
                String fullName =
                        cursor.getString(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE));

                ContactChip contactChip = new ContactChip(id+"", null, fullName, phoneNumberChips);
                // add contact to the list
                contactsList.add(contactChip);
            }
        }
        cursor.close();

        // pass contact list to chips input
        chipsInput.setFilterableList(contactsList);
    }

    //show dialog view for choosing send way
    public void chooseWay(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.choose_send_way, null);
        builder.setView(view);
        dialogChooseWay = builder.create();
        dialogChooseWay.show();
    }
}



