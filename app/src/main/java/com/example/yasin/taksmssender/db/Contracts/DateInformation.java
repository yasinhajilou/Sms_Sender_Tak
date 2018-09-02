package com.example.yasin.taksmssender.db.Contracts;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DateInformation {

    public static final String CONTENT_AUTHORITY_DATE = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_DATE = Uri.parse("content://"+CONTENT_AUTHORITY_DATE);

    public static final String PATH_DATE = "DateInformation";

    public static final class DateEntry implements BaseColumns {

        public static final Uri CONTENT_URI_DATE = Uri.withAppendedPath(BASE_CONTENT_URI_DATE , PATH_DATE);

        public static final String TABLE_NAME_DATE = "DateInformation";

        public static final String ID = BaseColumns._ID;

        public static final String COLUMN_YEAR = "Year";

        public static final String COLUMN_MONTH = "Month";

        public static final String COLUMN_DAY = "Day";

        public static final String COLUMN_PARENT_ID = "ParentId";

    }


}
