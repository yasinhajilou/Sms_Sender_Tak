package com.example.yasin.taksmssender.Fragment;


import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.example.yasin.taksmssender.Class.Utilities;


import com.example.yasin.taksmssender.Model.ContactChip;

import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.DateInformation;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.Contracts.SmsCounterContract;
import com.example.yasin.taksmssender.db.Contracts.SmsHistoryContract;
import com.example.yasin.taksmssender.db.Contracts.TimeInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelper;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    AppCompatRadioButton radioPhoneNum, radioContacts;
    ChipsInput chipsInput;
    EditText edtPhoneNumber;
    String phoneNumber;
    Button btnSend;
    int chipsLenght;
    List<ContactChip> contactsList;
    String contactName;
    AlertDialog dialogChooseWay;
    String message;
    String messageSubject;
    int messageId;
    ProgressBar progressBar;
    int lengthOfMes;

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
        progressBar = view.findViewById(R.id.progressSoloSend);

        chipsInput.setVisibility(View.GONE);
        contactsList = new ArrayList<>();
        getContacts(getContext());


        setUpSpinner(getContext());

        spinnerSmsSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    openHelperTak = new SQLiteOpenHelperTak(getContext());
                    database = openHelperTak.getReadableDatabase();

                    Cursor cursor = database.rawQuery("SELECT * FROM " + SmsContentContract.SmsEntry.TABLE_NAME_SMS + " WHERE " +
                            SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS + "==?", new String[]{selectedItem});
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        message = cursor.getString(cursor.getColumnIndex(SmsContentContract.SmsEntry.COLUMN_CONTENT_SMS));
                        messageId = cursor.getInt(cursor.getColumnIndex(SmsContentContract.SmsEntry._ID));
                    }
                    cursor.close();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
                if (i > 1) {
                    Toast.makeText(getContext(), "پیام فقط به مخاطب اول ارسال می شود", Toast.LENGTH_LONG).show();
                }
                chipsLenght = i;
            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {
                chipsLenght = i;
            }

            @Override
            public void onTextChanged(CharSequence charSequence) {

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioPhoneNum.isChecked() && !radioContacts.isChecked()) {
                    Toast.makeText(getContext(), "لطفا یک روش ارسال را انتخاب کنید", Toast.LENGTH_SHORT).show();
                } else {
                    if (radioContacts.isChecked()) {
                        if (chipsLenght == 1) {
                            List<? extends ChipInterface> chipInfor = chipsInput.getSelectedChipList();
                            phoneNumber = chipInfor.get(0).getInfo();
                            contactName = chipInfor.get(0).getLabel();
                            //show dialog
                            chooseWay(getContext());
                        } else {
                            if (chipsLenght > 1) {
                                Toast.makeText(getContext(), "بیش از یک مخاطب انتخاب شده!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "مخاطبی انتخاب نشده.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        if (radioPhoneNum.isChecked()){
                            phoneNumber = edtPhoneNumber.getText().toString();
                            if (phoneNumber.length() == 11){
                                chooseWay(getContext());
                            }else {
                                Toast.makeText(getContext(), "تلفن وارد شده صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
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

        Cursor cursorSmsContents = database.rawQuery("SELECT " + SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS + " FROM " + SmsContentContract.SmsEntry.TABLE_NAME_SMS, null);
        categories = new ArrayList<>();
        int columnIndexSubject = cursorSmsContents.getColumnIndex(SmsContentContract.SmsEntry.COLUMN_SUBJECT_SMS);

        if (cursorSmsContents.getCount() == 0) {
            categories.add("اطلاعاتی برای نمایش وجود ندارد");
            cursorSmsContents.close();
        } else {
            while (cursorSmsContents.moveToNext()) {
                categories.add(cursorSmsContents.getString(columnIndexSubject));
            }
            cursorSmsContents.close();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSmsSubject.setAdapter(dataAdapter);
    }

    //check network availability
    @NonNull
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }

    }

    //get Contacts from database
    public void getContacts(Context context) {
        openHelperTak = new SQLiteOpenHelperTak(context);
        database = openHelperTak.getReadableDatabase();
        String query = "SELECT * FROM " + PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id =
                        cursor.getInt(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry._ID));
                String phoneNumberChips =
                        cursor.getString(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER));
                String fullName =
                        cursor.getString(cursor.getColumnIndex(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE));

                ContactChip contactChip = new ContactChip(id + "", null, fullName, phoneNumberChips);
                // add contact to the list
                contactsList.add(contactChip);
            }
        }
        cursor.close();

        // pass contact list to chips input
        chipsInput.setFilterableList(contactsList);
    }

    //show dialog view for choosing send way
    public void chooseWay(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.choose_send_way, null);
        ImageView imgSimcard = view.findViewById(R.id.imgSimCard);
        ImageView imgInternet = view.findViewById(R.id.imgWifi);
        TextView txtSim = view.findViewById(R.id.txtSimCard);
        TextView txtInternet = view.findViewById(R.id.txtInternet);
        builder.setView(view);
        dialogChooseWay = builder.create();
        dialogChooseWay.show();


        txtSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimCardWay();
                dialogChooseWay.dismiss();
            }
        });

        imgSimcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimCardWay();
                dialogChooseWay.dismiss();
            }
        });


        txtInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetWay();
                dialogChooseWay.dismiss();
            }
        });



        imgInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetWay();
                dialogChooseWay.dismiss();
            }
        });

    }

    //on click for simCard Button in alertDialog
    public void SimCardWay() {
        sendSms();
        dialogChooseWay.dismiss();
    }

    //this method is for sending in online moode
    public void InternetWay() {
        progressBar.setVisibility(View.VISIBLE);
        if (isNetworkAvailable()) {
            isBlackList handler = new isBlackList();
            String url = "http://owjpayam.ir/ajax.php?page=check_is_number_in_blacklist";
            handler.execute(url);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            dialogChooseWay.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("عدم اتصال");
            builder.setMessage("این دستگاه به اینترنت وصل نمی باشد.");
            builder.setIcon(R.drawable.cancel);
            builder.setNeutralButton("باشه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }


    //send sms in online mood
    public class sendSmsOnline extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            String username = "9365174613";
            String password = "5529962718";
            String lineNumber = "30005633233983";
            params[0] += "user=" + username + "&pass=" + password + "&text=" + message + "&to=" + phoneNumber + "&lineNo=" + lineNumber;


            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(message);

            long peopleId = getContactId(phoneNumber);
            long smsDateId = addSmsDate();
            long smsTimeId = addSmsTime();

            //add history record
            long smsHistoryId = addSmsHistory(messageId, smsTimeId, smsDateId, peopleId, SmsHistoryContract.HistoryEntry.Internet);

            //add counter record
            long smsCounterId = addSmsCounter(smsDateId, smsTimeId,0 , parts.size());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(s);
            builder.setNeutralButton("باشه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();


        }
    }


    //check phone number is in black list or not
    public class isBlackList extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            RequestBody body = new FormBody.Builder()
                    .add("note", phoneNumber)
                    .add("regbtn", "چک کردن وضعیت شماره ها")
                    .build();
            Log.d("RequestBody", "was made ");

            Request request = new Request.Builder()
                    .url(params[0])
                    .post(body)
                    .build();


            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lengthOfMes = s.length();
            dialogChooseWay.dismiss();
            if (lengthOfMes == 60) {
                sendSmsOnline online = new sendSmsOnline();
                String url_send_sms = "http://ip.sms.ir/SendMessage.ashx?";
                online.execute(url_send_sms);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("لیست سیاه مخابرات");
                builder.setMessage("متاسفانه این خط در در لیست سیاه می باشد،آیا مایلید که از طریق سیم کارت ارسال نمایید؟");
                builder.setIcon(R.drawable.warning);
                builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendSms();
                    }
                });
                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }

    //send Sms In SimCard Mode
    public void sendSms() {

        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(message);
            sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);

            long peopleId = getContactId(phoneNumber);
            long smsDateId = addSmsDate();
            long smsTimeId = addSmsTime();

            //add history record
            long smsHistoryId = addSmsHistory(messageId, smsTimeId, smsDateId, peopleId, SmsHistoryContract.HistoryEntry.sim);

            //add counter record
            long smsCounterId = addSmsCounter(smsDateId, smsTimeId, parts.size(), 0);

            Toast.makeText(getContext(), "پیام ارسال شد",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

//        writeNewContactAfterPermission();
    }


    // add current 'date' in to the database and return record id that recently added
    private long addSmsDate() {
        openHelperTak = new SQLiteOpenHelperTak(getContext());
        database = openHelperTak.getWritableDatabase();

        //get current date in 3 index y/m/d
        String[] date = Utilities.getCurrentShamsidate().split("/");

        ContentValues values = new ContentValues();

        values.put(DateInformation.DateEntry.COLUMN_YEAR, date[0]);
        values.put(DateInformation.DateEntry.COLUMN_MONTH, date[1]);
        values.put(DateInformation.DateEntry.COLUMN_DAY, date[2]);
//        values.put(DateInformation.DateEntry.COLUMN_PARENT_ID, parentId);
        return database.insert(DateInformation.DateEntry.TABLE_NAME_DATE, null, values);
    }

    // add current 'Time' in to the database and return record id that recently added
    private long addSmsTime() {
        openHelperTak = new SQLiteOpenHelperTak(getContext());
        database = openHelperTak.getWritableDatabase();

        //get Current Time
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date dateTime = new Date();
        String currentTime = "" + dateFormat.format(dateTime);
        String[] time = currentTime.split(":");


        ContentValues cv = new ContentValues();

//        cv.put(TimeInformationContract.TimeEntry.COLUMN_PARENT_ID, parentId);
        cv.put(TimeInformationContract.TimeEntry.COLUMN_HOUR, time[0]);
        cv.put(TimeInformationContract.TimeEntry.COLUMN_MINUTE, time[1]);
        cv.put(TimeInformationContract.TimeEntry.COLUMN_SECOND, time[2]);

        return database.insert(TimeInformationContract.TimeEntry.TABLE_NAME_TIME, null, cv);
    }

    // add sms information to the SmsCounter table and return that id
    private long addSmsCounter(long idCurrentDate, long idCurrentTime, int SimCardCount, int WifiCount) {

        openHelperTak = new SQLiteOpenHelperTak(getContext());
        database = openHelperTak.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SmsCounterContract.CounterEntry.COLUMN_DATE, idCurrentDate);
        cv.put(SmsCounterContract.CounterEntry.COLUMN_TIME, idCurrentTime);
        cv.put(SmsCounterContract.CounterEntry.COLUMN_SIMCARD_COUNTER, SimCardCount);
        cv.put(SmsCounterContract.CounterEntry.COLUMN_WIFI_COUNTER, WifiCount);
        return database.insert(SmsCounterContract.CounterEntry.TABLE_NAME_COUNTER, null, cv);
    }

    // add new Content in to the smsHistory table
    private long addSmsHistory(int smsSelectedId, long idCurrentTime, long idCurrentDate, long peopleId, int sendWay) {
        openHelperTak = new SQLiteOpenHelperTak(getContext());
        database = openHelperTak.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SmsHistoryContract.HistoryEntry.COLUMN_SMS_INFORMATION, smsSelectedId);
        cv.put(SmsHistoryContract.HistoryEntry.COLUMN_SEND_TIME, idCurrentTime);
        cv.put(SmsHistoryContract.HistoryEntry.COLUMN_SEND_DATE, idCurrentDate);
        cv.put(SmsHistoryContract.HistoryEntry.COLUMN_TARGET, peopleId);
        cv.put(SmsHistoryContract.HistoryEntry.COLUMN_STATUS, SmsHistoryContract.HistoryEntry.status_sent);
        cv.put(SmsHistoryContract.HistoryEntry.COLUMN_SEND_WAY, sendWay);
        return database.insert(SmsHistoryContract.HistoryEntry.TABLE_NAME_HISTORY, null, cv);
    }

    //getContactId in database whether exist or not(made that)
    public long getContactId(final String phoneNumber) {
        openHelperTak = new SQLiteOpenHelperTak(getContext());
        database = openHelperTak.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT " + PeopleInformationContract.PeopleEntry._ID + " FROM " + PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE + " WHERE " + PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER + "==?",
                new String[]{phoneNumber});
        int columnIndexId = cursor.getColumnIndex(PeopleInformationContract.PeopleEntry._ID);
        final long[] id = new long[1];
        if (cursor.moveToNext()) {
            cursor.moveToFirst();
            id[0] = cursor.getInt(columnIndexId);
            cursor.close();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view1 = getLayoutInflater().inflate(R.layout.dialo_add_new_contact, null);
            builder.setCancelable(false);
            builder.setView(view1);
            Button btnDone = view1.findViewById(R.id.btnDoneAddContact);
            final EditText edtGetContactName = view1.findViewById(R.id.edtAddContactDialog);
            final AlertDialog dialog = builder.create();
            dialog.show();
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String contactsName = edtGetContactName.getText().toString();

                    if (contactsName.length() != 0) {
                        database = openHelperTak.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE, contactsName);
                        cv.put(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER, phoneNumber);
                        id[0] = database.insert(PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE, null, cv);
                        cv.clear();
                        Toast.makeText(getContext() , "با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        return id[0];
    }
}




