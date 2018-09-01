package com.example.yasin.taksmssender.db.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.yasin.taksmssender.db.Contracts.SmsContentContract.SmsEntry;
import com.example.yasin.taksmssender.db.Contracts.SmsContentContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

public class TakProvider extends ContentProvider {

    private SQLiteOpenHelperTak dbHelper;

    private static final int Smses = 100;

    private static final int Sms_Id = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(SmsContentContract.CONTENT_AUTHORITY_SMS , SmsContentContract.PATH_SMS , Smses);
        sUriMatcher.addURI(SmsContentContract.CONTENT_AUTHORITY_SMS , SmsContentContract.PATH_SMS + "/#" , Sms_Id);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new SQLiteOpenHelperTak(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case Smses:
                cursor = database.query(SmsEntry.TABLE_NAME_SMS , projection , selection , selectionArgs , null , null , null);
                break;
            case Sms_Id:
                selection = SmsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(SmsEntry.TABLE_NAME_SMS , projection , selection , selectionArgs , null , null , null);
                break;
            default:
                throw  new IllegalArgumentException("Error in uri Matcher :" + uri);

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case Smses:
                try {
                    return insertPet(uri , values);
                }catch (Exception e){
                    throw new IllegalArgumentException("please fill the field");
                }
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        String subject = values.getAsString(SmsEntry.COLUMN_SUBJECT_SMS);
        String Content = values.getAsString(SmsEntry.COLUMN_CONTENT_SMS);

        if (subject.isEmpty() || Content.isEmpty()){
            Toast.makeText(getContext(), "لطفا تمام فیلد هارا پر کنید", Toast.LENGTH_SHORT).show();
            return null;
        }
        SQLiteDatabase database =  dbHelper.getWritableDatabase();

        long id = database.insert(SmsEntry.TABLE_NAME_SMS , null , values);
        if (id == -1){
            Toast.makeText(getContext(), "خطایی رخ داده است", Toast.LENGTH_SHORT).show();
            return null;
        }

        return ContentUris.withAppendedId(uri , id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
