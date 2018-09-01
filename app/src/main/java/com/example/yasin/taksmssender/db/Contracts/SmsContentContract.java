package com.example.yasin.taksmssender.db.Contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SmsContentContract {

    private SmsContentContract(){}

    public static final String CONTENT_AUTHORITY_SMS = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_SMS = Uri.parse("content://"+CONTENT_AUTHORITY_SMS);

    public static final String PATH_SMS = "SmsContent";

    public static final class SmsEntry implements BaseColumns{

        public static final Uri CONTENT_URI_SMS = Uri.withAppendedPath(BASE_CONTENT_URI_SMS , PATH_SMS);

        public final static String TABLE_NAME_SMS = "SmsContent";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_SUBJECT_SMS = "Subject";

        public final static String COLUMN_CONTENT_SMS ="Content";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_SMS + "/" + PATH_SMS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_SMS + "/" + PATH_SMS;
    }

}
