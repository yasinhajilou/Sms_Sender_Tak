package com.example.yasin.taksmssender.db.Contracts;

import android.net.Uri;
import android.provider.BaseColumns;

public final class SmsCounterContract {

    public static final String CONTENT_AUTHORITY_COUNTER = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_COUNTER = Uri.parse("content://"+CONTENT_AUTHORITY_COUNTER);

    public static final String PATH_COUNTER = "SmsCounter";

    public static final class CounterEntry implements BaseColumns{

        public static final Uri CONTENT_URI_COUNTER = Uri.withAppendedPath(BASE_CONTENT_URI_COUNTER , PATH_COUNTER);

        public static final String TABLE_NAME_COUNTER = "SmsCounter";

        public static final String ID = BaseColumns._ID;

        public static final String COLUMN_DATE = "Date";

        public static final String COLUMN_TIME = "Time";

        public static final String COLUMN_SIMCARD_COUNTER = "SimCardCounter";

        public static final String COLUMN_WIFI_COUNTER = "WifiCounter";
    }
}
