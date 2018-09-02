package com.example.yasin.taksmssender;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.yasin.taksmssender.Model.ContactChip;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;
import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity {

    SQLiteOpenHelperTak openHelperTak;
    SQLiteDatabase database;
    ChipsInput chipsInput;
    List<ContactChip> mContactList;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        chipsInput = findViewById(R.id.chipsInputContactUs);
        toolbar = (Toolbar)findViewById(R.id.ToolBarEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mContactList = new ArrayList<>();
        getContactList();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getContactList() {
        Cursor phones = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);

        // loop over all contacts
        if(phones != null) {
            while (phones.moveToNext()) {
                // get contact info
                String phoneNumber = null;
                String id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String avatarUriString = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                Uri avatarUri = null;
                if(avatarUriString != null)
                    avatarUri = Uri.parse(avatarUriString);

                // get phone number
                if (Integer.parseInt(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

                    while (pCur != null && pCur.moveToNext()) {
                        phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    pCur.close();

                }

                ContactChip contactChip = new ContactChip(id, avatarUri, name, phoneNumber);
                // add contact to the list
                mContactList.add(contactChip);
            }
            phones.close();
        }

        // pass contact list to chips input
        chipsInput.setFilterableList(mContactList);
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
                mContactList.add(contactChip);
            }
        }
        cursor.close();

        // pass contact list to chips input
        chipsInput.setFilterableList(mContactList);
    }
}
