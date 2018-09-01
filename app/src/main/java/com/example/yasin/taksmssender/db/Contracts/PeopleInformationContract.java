package com.example.yasin.taksmssender.db.Contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PeopleInformationContract {

    private PeopleInformationContract(){}

    public static final String CONTENT_AUTHORITY_PEOPLE = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_PEOPLE = Uri.parse("content://"+CONTENT_AUTHORITY_PEOPLE);

    public static final String PATH_PEOPLE = "PeopleInformation";

    public static final class PeopleEntry implements BaseColumns {

        public static final Uri CONTENT_URI_PEOPLE = Uri.withAppendedPath(BASE_CONTENT_URI_PEOPLE , PATH_PEOPLE);

        public final static String TABLE_NAME_PEOPLE = "PeopleInformation";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_FULL_NAME_PEOPLE = "FullName";

        public final static String COLUMN_GENDER_PEOPLE = "Gender";

        public final static String COLUMN_DATE_OF_BIRTH = "DateOfBirth";

        public final static String COLUMN_PHONE_NUMBER = "PhoneNumber";

        public final static String COLUMN_REGISTER_DATE = "RegisterDate";


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_PEOPLE + "/" + PATH_PEOPLE;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_PEOPLE + "/" + PATH_PEOPLE;
    }
}
