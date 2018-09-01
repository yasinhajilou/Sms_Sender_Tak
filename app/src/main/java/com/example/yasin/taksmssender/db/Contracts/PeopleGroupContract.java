package com.example.yasin.taksmssender.db.Contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public final class PeopleGroupContract {

    public static final String CONTENT_AUTHORITY_GROUP = "com.example.android.TakSmsSender";

    public static final Uri BASE_CONTENT_URI_GROUP = Uri.parse("content://"+CONTENT_AUTHORITY_GROUP);

    public static final String PATH_GROUP = "PeopleGroup";

    public static final class GroupEntry implements BaseColumns{

        public static final Uri CONTENT_URI_GROUP = Uri.withAppendedPath(BASE_CONTENT_URI_GROUP , PATH_GROUP);

        public static final String TABLE_NAME_GROUP = "PeopleGroup";

        public static final String ID = BaseColumns._ID;

        public static final  String COLUMN_GROUP_TITLE = "GroupTitle";

        public static final String COLUMN_MEMBER_INFORMATION = "MemberInformation";

        public static final String COLUMN_CREATION_DATE = "CreationDate";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_GROUP + "/" + PATH_GROUP;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_GROUP + "/" + PATH_GROUP;

    }

}
