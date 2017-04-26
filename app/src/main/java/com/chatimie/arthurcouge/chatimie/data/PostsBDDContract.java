package com.chatimie.arthurcouge.chatimie.data;

import android.provider.BaseColumns;

/**
 * Created by Quentin for ChatIMIE on 30/03/2017.
 */

public class PostsBDDContract {
    public static final class PostsEntry implements BaseColumns{
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_MESSAGE_NAME = "message";
        public static final String COLUMN_PSEUDO_NAME = "pseudo";
        public static final String COLUMN_DATE_NAME = "date";
    }
}
