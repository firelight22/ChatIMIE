package com.chatimie.arthurcouge.chatimie.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Quentin for ChatIMIE on 25/04/2017.
 */

public class PostContentProviderContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.chatimie.arthurcouge.chatimie";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_POST = "posts";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class TaskEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POST).build();


        // Task table and column names
        public static final String TABLE_NAME = "posts";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_PSEUDO = "pseudo";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE = "date";

    }
}