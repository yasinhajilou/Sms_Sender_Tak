package com.example.yasin.taksmssender;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Adapter.RecyclerAdapterContacts;
import com.example.yasin.taksmssender.Model.ContactChip;
import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ChipsInput mChipsInput;
    private List<ContactChip> mContactList;
    Button button ;
    RecyclerAdapterContacts adapter;
    RecyclerView recyclerView;
    private final static int Read_Contacts_Request_Code = 0;
    Toolbar toolbar;
    EditText edtGroupName;
    TextView txtGroupMember;
    Button btnDone , btnCancel;
    List<? extends ChipInterface> contactsSelected;
    SQLiteOpenHelper dataBase;
    String[] names;
    String[] phones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ButterKnife.bind(this);
        mContactList = new ArrayList<>();

        toolbar = (Toolbar)findViewById(R.id.toolBarContacts);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabContacts);
        mChipsInput = (ChipsInput)findViewById(R.id.chips_input);
        mChipsInput.setSelected(false);
        floatingActionButton.hide();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//
////        if (ContextCompat.checkSelfPermission(ContactsActivity.this , Manifest.permission.READ_CONTACTS )  == PackageManager.PERMISSION_GRANTED ){
////            getContactList();
////            setUpRecyclerView();
////        }else {
////            requestForReadContacts();
////        }

        mChipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chipInterface, int i) {
                floatingActionButton.show();
            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsSelected =  mChipsInput.getSelectedChipList();
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ContactsActivity.this);
                final AlertDialog alertDialog;
                View view = getLayoutInflater().inflate(R.layout.dialog_add_group , null);
                edtGroupName = (EditText) view.findViewById(R.id.edtGroupName);
                txtGroupMember = (TextView) view.findViewById(R.id.txtGroupMember);
                btnCancel = (Button)view.findViewById(R.id.btnCancelGroup);
                btnDone = (Button)view.findViewById(R.id.btnDoneGroup);
                StringBuilder listString = new StringBuilder();
                final int members = contactsSelected.size();
                names = new String[members];
                phones = new String[members];

                for(int i = 0 ; i < members ; i++){
                    names[i] = contactsSelected.get(i).getLabel();
                    phones[i] = contactsSelected.get(i).getInfo();
                    listString.append(names[i]).append(" (").append(phones[i]).append(") \n");
                }

                txtGroupMember.setText(listString + " \n" + members +" نفر ");
                dialog.setView(view);
                alertDialog = dialog.create();
                alertDialog.show();

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean check;
                        String className = edtGroupName.getText().toString();
                        if (!className.matches("")){
                            dataBase = new SQLiteOpenHelper(ContactsActivity.this);
                            for (int i = 0 ; i< members ; i++){
//                               check =  dataBase.insertGroups(className , names[i] , phones[i]);
//                               if (check){
//                                   alertDialog.dismiss();
//                               }else {
//                                   Toast.makeText(ContactsActivity.this, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();
//                                   alertDialog.dismiss();
//                                   break;
//                               }
                            }
                        }else {
                            Toast.makeText(ContactsActivity.this, "لطفا یک نام را انتخاب کنید", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

//    private void requestForReadContacts() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this , Manifest.permission.READ_CONTACTS)){
//            Toast.makeText(this, "شما باید اجازه این دسترسی رو تایید کنید", Toast.LENGTH_LONG).show();
//        }else {
//            ActivityCompat.requestPermissions(ContactsActivity.this , new String[]{Manifest.permission.READ_CONTACTS}, Read_Contacts_Request_Code);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case Read_Contacts_Request_Code :
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    getContactList();
//                    setUpRecyclerView();
//                }else {
//
//                }
//        }
//    }
//
//    private ArrayList<Contacts> getContacts(){
//        ArrayList<Contacts> list = new ArrayList<>();
//        StringBuilder builder = new StringBuilder();
//        ContentResolver contentResolver = getContentResolver() ;
//        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI , null , null , null , null);
//
//        if (cursor.getCount() > 0){
//            while (cursor.moveToNext()){
//                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//
//                if (hasPhoneNumber>0){
//                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,
//                            null , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?" ,
//                            new String[]{id} , null);
//                    while (cursor2.moveToNext()){
//                        String s = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        builder.append("Contact : ").append(name).append(" , Phone Number : ").append(s).append("\n \n");
//                        Contacts contacts = new Contacts();
//                        contacts.setFullName(name);
//                        contacts.setPhoneNumber(s);
//                        list.add(contacts);
//                    }
//                }
//            }
//        }
//
//        return list;
//    }
//
//    public void setUpRecyclerView(){
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewContacts);
//        adapter = new RecyclerAdapterContacts(this , getContacts() , mChipsInput);
//        recyclerView.setAdapter(adapter);
//
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//    }
//
//    private void getContactList() {
//        Cursor phones = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);
//
//        // loop over all contacts
//        if(phones != null) {
//            while (phones.moveToNext()) {
//                // get contact info
//                String phoneNumber = null;
//                String id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                String avatarUriString = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
//                Uri avatarUri = null;
//                if(avatarUriString != null)
//                    avatarUri = Uri.parse(avatarUriString);
//
//                // get phone number
//                if (Integer.parseInt(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    Cursor pCur = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
//
//                    while (pCur != null && pCur.moveToNext()) {
//                        phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    }
//
//                    pCur.close();
//
//                }
//
//                ContactChip contactChip = new ContactChip(id, avatarUri, name, phoneNumber);
//                // add contact to the list
//                mContactList.add(contactChip);
//            }
//            phones.close();
//        }
//
//        // pass contact list to chips input
//        mChipsInput.setFilterableList(mContactList);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}


