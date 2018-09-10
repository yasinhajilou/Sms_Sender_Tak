package com.example.yasin.taksmssender;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.yasin.taksmssender.Adapter.FragmentAdapterSmsWay;
import com.example.yasin.taksmssender.Class.Utilities;
import com.example.yasin.taksmssender.Model.Contacts;
import com.example.yasin.taksmssender.Model.Groups;
import com.example.yasin.taksmssender.db.Contracts.DateInformation;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract.SmsEntry;
import com.example.yasin.taksmssender.db.Contracts.PeopleGroupContract.GroupEntry;
import com.example.yasin.taksmssender.db.Contracts.SmsCounterContract;
import com.example.yasin.taksmssender.db.Contracts.SmsHistoryContract;
import com.example.yasin.taksmssender.db.Contracts.TimeInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ProgressBar progressBarMain;
    String[] arrayPhone;
    AppCompatSpinner spinnerGroups;
    int lengthOfMes;
    String phoneNumber;
    String message;
    SQLiteOpenHelperTak openHelperTak;
    EditText edtPhoneNum;
    String itemSpinner;
    android.support.v7.widget.Toolbar toolbar;
    Button btnSend;
    Spinner spinner;
    AlertDialog dialogChooseWay;
    ProgressBar progressBar;
    String groupName;
    Button btnSendGroup;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int counterSmsHistory;
    String contactsName;
    SQLiteDatabase database;
    int smsSelectedId;
    int ContactPhoneSize;
    int lastSize;
    ViewPager viewPager;
    TextView txtLableSolo, txtLableGroup;
    int firstTime;
    private LottieAnimationView animationView;
    ImageButton imgPopup;
    FragmentAdapterSmsWay adapterSmsWay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        viewPager = findViewById(R.id.viewPagerMainAct);
        txtLableGroup = findViewById(R.id.txtLableGroup);
        txtLableSolo = findViewById(R.id.txtLableSolo);
        imgPopup = findViewById(R.id.imgBtnPopupMenu);


        preferences = getApplicationContext().getSharedPreferences("SmsHistoryCounter", MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();

        adapterSmsWay = new FragmentAdapterSmsWay(getSupportFragmentManager());
        viewPager.setAdapter(adapterSmsWay);
        viewPager.setCurrentItem(0);
        txtLableSolo.setTextSize(26);
        txtLableGroup.setTextSize(20);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            txtLableSolo.setTextColor(getColor(R.color.textTabLight));
            txtLableGroup.setTextColor(getColor(R.color.textTabBright));
        }

        txtLableGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        txtLableSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeLables(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, imgPopup);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        switch (itemId) {
                            case R.id.menuEdit:
                                Intent intent = new Intent(MainActivity.this , EditActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });




        //Forward old database information to new
        passOldDataToNew(MainActivity.this);

        //get Read and write and send sms permission
        setPermissionSoloSms();


        //codes that setting up spinner with group information
//        openHelperTak = new SQLiteOpenHelperTak(MainActivity.this);
//        database = openHelperTak.getReadableDatabase();
//        Cursor GroupCursor = database.rawQuery("SELECT * FROM "+SmsEntry.TABLE_NAME_SMS , null);
//        if (GroupCursor.getCount() != 0) {
//            fillGroupSpinner(database , GroupCursor);
//            GroupCursor.close();
//        }


//        spinnerGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                groupName = selectedItem;
//            } // to close the onItemSelected
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                phoneNumber = edtPhoneNum.getText().toString();
//                if (phoneNumber.isEmpty()) {
//                    Toast.makeText(MainActivity.this, "لطفا فیلد شماره تلفن را پر کنید", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (phoneNumber.length() == 11) {
//                        if (message != null) {
//                            singleOrGroup = 1;
//                            clickOnSend();
//                        } else {
//                            Toast.makeText(MainActivity.this, "پیامی وجود ندارد", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(MainActivity.this, "فرمت وارد شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//        btnSendGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setPermissionGroupSms();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterSmsWay = new FragmentAdapterSmsWay(getSupportFragmentManager());
        viewPager.setAdapter(adapterSmsWay);
    }

    //this method task is changing style of two text view that showed view pager status
    private void changeLables(int position) {
        switch (position) {
            case 0:
                txtLableSolo.setTextSize(26);
                txtLableGroup.setTextSize(20);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtLableSolo.setTextColor(getColor(R.color.textTabLight));
                    txtLableGroup.setTextColor(getColor(R.color.textTabBright));
                }
                break;
            case 1:
                txtLableSolo.setTextSize(20);
                txtLableGroup.setTextSize(26);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    txtLableSolo.setTextColor(getColor(R.color.textTabBright));
                    txtLableGroup.setTextColor(getColor(R.color.textTabLight));
                }
                break;
        }
    }

    @AfterPermissionGranted(123)
    private void setPermissionSoloSms() {

        String[] perms = {Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            //update or transfer contacts data to internal database
            setUpOrUpdateInternallContacts(MainActivity.this);
            viewPager.setAdapter(adapterSmsWay);
        } else {
            EasyPermissions.requestPermissions(MainActivity.this, "برای درست کار کردن برنامه نیاز است که مجوز دسترسی را صادر کنید.", 123, perms);
        }
    }

    @AfterPermissionGranted(111)
    private void setPermissionGroupSms() {
        String[] perms = {Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {
//            singleOrGroup = 2;
//            clickOnSend();
        } else {
            EasyPermissions.requestPermissions(MainActivity.this, "برای درست کار کردن برنامه نیاز به تایید کردن دسترسی هاست", 123, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case 123:
                Toast.makeText(this, "permission Accessed", Toast.LENGTH_SHORT).show();
                break;
            case 111:
                Toast.makeText(this, "permission Accessed", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "شما مجوز دسترسی را رد کردید،برنامه به درستی کار نمی کند!", Toast.LENGTH_LONG).show();
    }


    //this method make contacts in database or update that
    private void setUpOrUpdateInternallContacts(Context context) {

        lastSize = preferences.getInt("key_last_size", 0);
        ArrayList<Contacts> contacts = getContactsFromPhone(context);
        if (lastSize == 0) {
            openHelperTak = new SQLiteOpenHelperTak(context);
            database = openHelperTak.getWritableDatabase();
            lastSize = contacts.size();
            editor.putInt("key_last_size", lastSize);
            editor.commit();
            ContentValues cv = new ContentValues();
            for (int i = 0; i < lastSize; i++) {
                cv.put(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE, contacts.get(i).getFullName());
                cv.put(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER, contacts.get(i).getPhoneNumber());
                database.insert(PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE, null, cv);
                cv.clear();
            }

        } else {
            int nowSize = contacts.size();
            if (lastSize < nowSize) {
                openHelperTak = new SQLiteOpenHelperTak(context);
                database = openHelperTak.getWritableDatabase();
                editor.putInt("key_last_size", nowSize);
                editor.commit();
                ContentValues cv = new ContentValues();
                for (int i = 0; i < lastSize; i++) {
                    cv.put(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE, contacts.get(i).getFullName());
                    cv.put(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER, contacts.get(i).getPhoneNumber());
                    database.insert(PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE, null, cv);
                    cv.clear();
                }

            }
        }

    }

    //this method task is passing old database (Sms Contents) to new Database
    private void passOldDataToNew(Context context) {
        //check for is first time app ran
        firstTime = preferences.getInt("first_time", 0);
        editor.commit();
        if (firstTime == 0) {
            //sending old table information to new table
            SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(context);
            SQLiteOpenHelper openHelper = new SQLiteOpenHelper(context);
            SQLiteDatabase databaseTak = openHelperTak.getWritableDatabase();
            database = openHelper.getReadableDatabase();
            Cursor fullData = database.rawQuery("SELECT * FROM Texts ", null);
            if (fullData.getCount() != 0) {
                while (fullData.moveToNext()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS, fullData.getString(1));
                    contentValues.put(SmsContentContract.SmsEntry.COLUMN_CONTENT_SMS, fullData.getString(2));
                    databaseTak.insert(SmsContentContract.SmsEntry.TABLE_NAME_SMS, null, contentValues);
                }
                fullData.close();
            }
            editor.putInt("first_time", 1);
            editor.commit();
        }

    }

    //this method task is getting Original Contacts data from phone and return that data
    private ArrayList<Contacts> getContactsFromPhone(Context context) {
        ArrayList<Contacts> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (cursor2.moveToNext()) {
                        String s = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Contacts contacts = new Contacts();
                        contacts.setFullName(name);
                        contacts.setPhoneNumber(s);
                        list.add(contacts);
                    }
                }
            }
        }

        return list;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

//    @Override
//    protected void onStart() {
//        super.onStart();
//        setUpSpinner();
//    }
//
//    //Create popup menu from One Layout pattern
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Get menu inflater.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    //make event when click on menu Item in popupMenu
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Get menu item id.
//        int itemId = item.getItemId();
//
//
//        if (itemId == R.id.menuEdit) {
//            Intent intentEdit = new Intent(MainActivity.this, EditActivity.class);
//            startActivity(intentEdit);
//        } else if (itemId == R.id.menuExit) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("خروج");
//            builder.setMessage("آیا قصد خروج از برنامه رو دارید؟");
//            builder.setIcon(R.drawable.logout);
//            builder.setNegativeButton("بیخیال", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            }).setPositiveButton("آره", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
//                }
//            }).create().show();
//        } else if (itemId == R.id.menuContacts) {
//            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        // On selecting a spinner item
//        itemSpinner = parent.getItemAtPosition(position).toString();
//        openHelperTak = new SQLiteOpenHelperTak(MainActivity.this);
//        database = openHelperTak.getReadableDatabase();
//        Cursor cursor = database.rawQuery("SELECT * FROM " + SmsEntry.TABLE_NAME_SMS + " WHERE " + SmsEntry.COLUMN_SUBJECT_SMS + "==? ", new String[]{itemSpinner});
//        int columnIndexId = cursor.getColumnIndex(SmsEntry._ID);
//        int columnIndexContent = cursor.getColumnIndex(SmsEntry.COLUMN_CONTENT_SMS);
//        if (cursor.moveToNext()) {
//            smsSelectedId = cursor.getInt(columnIndexId);
//            message = cursor.getString(columnIndexContent);
//        }
//        cursor.close();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//

//

//
////    private void sendGroupSms() {
////        try {
////            SQLiteOpenHelper db = new SQLiteOpenHelper(this);
////            Cursor cursor = db.showMemberOfClassWithNumber(groupName);
////            int countOfData = cursor.getCount();
////            if (countOfData == 0) {
////                Toast.makeText(this, "اطلاعاتی وجود ندارد", Toast.LENGTH_SHORT).show();
////            } else {
////                arrayPhone = new String[countOfData];
////                int a = 0;
////                while (cursor.moveToNext()) {
////                    arrayPhone[a] = cursor.getString(3);
////                    a++;
////                }
////                Date dateTime = new Date();
////                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
////                String nowTime = "" + dateFormat.format(dateTime);
////                String[] date = Utilities.getCurrentShamsidate().split("/");
////
////                SmsManager sms = SmsManager.getDefault();
////                ArrayList<String> parts = sms.divideMessage(message);
////                for (int i = 0; i < countOfData; i++) {
////                    sms.sendMultipartTextMessage(arrayPhone[i], null, parts, null, null);
////                    db.insertSmsHistory(message, nowTime, date[0] + "/" + date[1] + "/" + date[2], counterSmsHistory + "", arrayPhone[i]);
////                }
////                counterSmsHistory++;
////
////
////                editor.putInt("key_counter", counterSmsHistory);
////                editor.commit();
////                db.insertAmount(countOfData * parts.size() + "", "0", date[0], date[1]);
////                Toast.makeText(this, "پیام ها ارسال شدن", Toast.LENGTH_SHORT).show();
////            }
////        } catch (Exception e) {
////            Toast.makeText(getApplicationContext(),
////                    e.getMessage(),
////                    Toast.LENGTH_LONG).show();
////            e.printStackTrace();
////        }
////    }
//

//
//    //this method filled spinner that showed groups Name
////    public void fillGroupSpinner(SQLiteDatabase database , Cursor res) {
////
////        List<String> spinnerArray = new ArrayList<String>();
////
////        if (res.getCount() == 0) {
////            spinnerArray.add("فاقد پیام");
////        } else {
////            int ColumnIndexSubject = res.getColumnIndex(SmsEntry.COLUMN_SUBJECT_SMS);
////            int ColumnIndexContent = res.getColumnIndex(SmsEntry.COLUMN_CONTENT_SMS);
////            String[] name = new String[res.getCount()];
////            int counter = 0;
////            while (res.moveToNext()) {
////                Groups groups = new Groups();
////                if (counter == 0) {
////                    name[counter] = res.getString(1);
////                    groups.setGroupName(name[counter]);
////                    spinnerArray.add(name[counter]);
////                } else {
////                    name[counter] = res.getString(1);
////                    if (name[counter].equals(name[counter - 1])) {
////                        //its same and not need to add in list
////                    } else {
////                        groups.setGroupName(name[counter]);
////                        spinnerArray.add(name[counter]);
////                    }
////                }
////                counter++;
////            }
////        }
////        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
////                this, android.R.layout.simple_spinner_item, spinnerArray);
////
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////
////        spinnerGroups.setAdapter(adapter);
////
////    }
//
////    //this method return Contact Name
////    public String getContactName(final String phoneNumber, Context context) {
////        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
////
////        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
////
////        String contactName = "";
////        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
////
////        if (cursor != null) {
////            if (cursor.moveToFirst()) {
////                contactName = cursor.getString(0);
////            }
////            cursor.close();
////        }
////
////        return contactName;
////    }
////
////    // This method will only insert an empty data to RawContacts.CONTENT_URI
////    // The purpose is to get a system generated raw contact id.
////    private long getRawContactId() {
////        // Inser an empty contact.
////        ContentValues contentValues = new ContentValues();
////        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
////        // Get the newly created contact raw id.
////        long ret = ContentUris.parseId(rawContactUri);
////        return ret;
////    }
////
////
////    // Insert newly created contact display name.
////    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName) {
////        ContentValues contentValues = new ContentValues();
////
////        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
////
////        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
////        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
////
////        // Put contact display name value.
////        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
////
////        getContentResolver().insert(addContactsUri, contentValues);
////
////    }
////
////    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr) {
////        try {
////            // Create a ContentValues object.
////            ContentValues contentValues = new ContentValues();
////
////            // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
////            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
////
////            // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
////            contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
////
////            // Put phone number value.
////            contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
////
////            // Calculate phone type by user selection.
////            int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
////
////            if ("home".equalsIgnoreCase(phoneTypeStr)) {
////                phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
////            } else if ("mobile".equalsIgnoreCase(phoneTypeStr)) {
////                phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
////            } else if ("work".equalsIgnoreCase(phoneTypeStr)) {
////                phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
////            }
////            // Put phone type value.
////            contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);
////
////            // Insert new contact data into phone contact list.
////            getContentResolver().insert(addContactsUri, contentValues);
////
////        } catch (Exception e) {
////            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
////        }
////
////    }
//
//

//


//
//    //write new contact if not exist
////    public void writeNewContactAfterPermission() {
////        if (getContactName(phoneNumber, this).length() == 0) {
////            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
////            View view1 = getLayoutInflater().inflate(R.layout.dialo_add_new_contact, null);
////            builder.setView(view1);
////            Button btnDone = view1.findViewById(R.id.btnDoneAddContact);
////            Button btnCancel = view1.findViewById(R.id.btnCancelAddContact);
////            final EditText edtGetContactName = view1.findViewById(R.id.edtAddContactDialog);
////            final AlertDialog dialog = builder.create();
////            dialog.show();
////            btnCancel.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    dialog.dismiss();
////                }
////            });
////            btnDone.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    contactsName = edtGetContactName.getText().toString();
////
////                    if (contactsName.length() != 0) {
////                        // Get android phone contact content provider uri.
////                        //Uri addContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
////                        // Below uri can avoid java.lang.UnsupportedOperationException: URI: content://com.android.contacts/data/phones error.
////                        Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
////
////                        // Add an empty contact and get the generated id.
////                        long rowContactId = getRawContactId();
////
////                        // Add contact name data.
////                        insertContactDisplayName(addContactsUri, rowContactId, contactsName);
////
////                        // Add contact phone data.
////                        insertContactPhoneNumber(addContactsUri, rowContactId, phoneNumber, "Mobile");
////
////                        Toast.makeText(getApplicationContext(), "با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
////                        dialog.dismiss();
////                    } else {
////                        Toast.makeText(MainActivity.this, "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
////                    }
////
////                }
////            });
////
////        }
////    }
//

