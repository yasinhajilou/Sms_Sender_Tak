package com.example.yasin.taksmssender.db.Contracts;

import android.net.Uri;
import android.provider.BaseColumns;

public final class TimeInformationContract {

    public static final String CONTENT_AUTHORITY_TIME = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_TIME = Uri.parse("content://"+CONTENT_AUTHORITY_TIME);

    public static final String PATH_TIME = "TimeInformation";

    public static final class TimeEntry implements BaseColumns{

        public static final Uri CONTENT_URI_TIME = Uri.withAppendedPath(BASE_CONTENT_URI_TIME , PATH_TIME);

        public static final String TABLE_NAME_TIME = "TimeInformation";

        public static final String ID = BaseColumns._ID;

        public static final String COLUMN_HOUR = "Hour";

        public static final String COLUMN_MINUTE = "Minute";

        public static final String COLUMN_SECOND = "Second";

        public static final String COLUMN_PARENT_ID = "ParentId";
    }

}
