package com.example.yasin.taksmssender.db.Contracts;

import android.net.Uri;
import android.provider.BaseColumns;

public final class SmsHistoryContract {
    public static final String CONTENT_AUTHORITY_HISTORY = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_HISTORY = Uri.parse("content://"+CONTENT_AUTHORITY_HISTORY);

    public static final String PATH_HISTORY = "SmsHistory";

    public static final class HistoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI_TIME = Uri.withAppendedPath(BASE_CONTENT_URI_HISTORY , PATH_HISTORY);

        public static final String TABLE_NAME_HISTORY = "SmsHistory";

        public static final String ID = BaseColumns._ID;

        public static final String COLUMN_SMS_INFORMATION = "SmsInformation";

        public static final String COLUMN_SEND_TIME = "SendTime";

        public static final String COLUMN_SEND_DATE = "SendDate";

        public static final String COLUMN_TARGET = "Target";

        public static final String COLUMN_STATUS = "Status";

        public static final int status_sent = 0;

        public static final String COLUMN_SEND_WAY = "SendWay";

        public static final int status_delivered = 1;

        public static final int sim = 0 ;

        public static final int Internet = 1;

    }
}
