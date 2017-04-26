package com.chatimie.arthurcouge.chatimie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.chatimie.arthurcouge.chatimie.bg.MyIntentService;
import com.chatimie.arthurcouge.chatimie.bg.NotifUtils;
import com.chatimie.arthurcouge.chatimie.data.PostContentProviderContract;
import com.chatimie.arthurcouge.chatimie.data.PostsBDDContract;
import com.chatimie.arthurcouge.chatimie.data.PostsBDDHelper;
import com.chatimie.arthurcouge.chatimie.receivers.ReceiverWifi;

import java.util.Date;

public class MessageActivity extends AppCompatActivity implements MessageAdapter.ListItemClickListener {
    private RecyclerView messageList;
    public static SharedPreferences sp;
    private EditText editTextMessage;
    private MessageAdapter messageAdapter;
    private static SQLiteDatabase mDb;

    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageAdapter = new MessageAdapter(this,null,getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextMessage = (EditText) findViewById(R.id.plainTextMessage);
        setSupportActionBar(toolbar);

        pseudo = getIntent().getExtras().getString("pseudo");

        messageList = (RecyclerView) findViewById(R.id.recyclerViewMessageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messageList.setLayoutManager(linearLayoutManager);
        //messageList.setAdapter(messageAdapter);

        /*TextView textViewPrenom = (TextView) findViewById(R.id.textViewPrenom);
        textViewPrenom.setText();*/
        PostsBDDHelper mBddHelper = new PostsBDDHelper(this);
        mDb = mBddHelper.getWritableDatabase();
        Cursor allPosts = getAllPosts();

        messageAdapter = new MessageAdapter(this,allPosts,getApplicationContext());
        messageList.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // add send message here
        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        super.registerReceiver(new ReceiverWifi(), filters);
        NotifUtils.notify(getBaseContext());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO(3) ajouter notre message à la liste
                //TODO (4) afficher un format correct de date
                //TODO (5) go to bottom of the list
                if (pseudo.isEmpty() || pseudo.equals(""))
                    pseudo = "default";
                messageAdapter.addToListe(new Message(
                        editTextMessage.getText().toString(),
                        pseudo,
                        DateFormat.format("dd-MM hh:mm a", new Date()).toString()
                ));
                //TODO(1) Clear l'editText quand j'envoie
                editTextMessage.setText("");
                //TODO(2) Close Keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editTextMessage.clearFocus();
                //TODO scrollToPosition
                messageList.scrollToPosition(messageAdapter.getItemCount()-1);
                //MessageActivity
                Intent myIntentService = new Intent(getApplicationContext(), MyIntentService.class);
                startService(myIntentService);
            }
        });
    }
    private Cursor getAllPosts(){
        ContentResolver contentResolver = getContentResolver();
        return contentResolver.query(Uri.parse(PostContentProviderContract.BASE_CONTENT_URI + "/" +
        PostContentProviderContract.PATH_POST), null,null,null,null);
        //return mDb.query(
        //        PostsBDDContract.PostsEntry.TABLE_NAME,
        //        null,
        //        null,
        //        null,
        //        null,
        //        null,
        //        PostsBDDContract.PostsEntry.COLUMN_DATE_NAME
        //);
    }

    private void removePost(long id){
        ContentResolver contentResolver = getContentResolver();

        contentResolver.delete(Uri.parse(PostContentProviderContract.BASE_CONTENT_URI + "/"
                + PostContentProviderContract.PATH_POST),PostContentProviderContract.TaskEntry._ID + "=" + id,null);
        //mDb.delete(PostsBDDContract.PostsEntry.TABLE_NAME,
        //        PostsBDDContract.PostsEntry._ID + "=" + id,null);
    }

    @Override
    public void onListItemClick(Message message, View view) {
        //Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
        Intent tweetUser = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/"+message.getPseudo()));
        startActivity(tweetUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_clear_sp:
                ContentResolver contentResolver = getContentResolver();
                contentResolver.delete(Uri.parse(PostContentProviderContract.BASE_CONTENT_URI + "/" +
                        PostContentProviderContract.PATH_POST), null,null);
                break;
            case R.id.changer_pseudo:
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this,R.style.Theme_AppCompat_Light_Dialog);

                LayoutInflater inflater = getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.dialog_change_pseudo, null))
                        // Add action buttons
                        .setPositiveButton("C'est pas faux", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                            }
                        })
                        .setNegativeButton("No !", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
                break;
            default:
                break;
        }
        return true;
    }
    public static SQLiteDatabase getmDb() {
        return mDb;
    }
    public static void setmDb(SQLiteDatabase mDb) {
        MessageActivity.mDb = mDb;
    }
}
