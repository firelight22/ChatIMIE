package com.chatimie.arthurcouge.chatimie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chatimie.arthurcouge.chatimie.data.PostContentProviderContract;
import com.chatimie.arthurcouge.chatimie.data.PostsBDDContract;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private ArrayList<Message> listMessage = new ArrayList<>();
    private Cursor cursor;
    private SQLiteDatabase mDb;
    private Context context;

    public interface ListItemClickListener {
        void onListItemClick(Message message, View view);
    }

    final private ListItemClickListener messageOnClickListener;

    public MessageAdapter(ListItemClickListener listener, @Nullable Cursor cursor, Context context) {
        messageOnClickListener = listener;
        mDb = MessageActivity.getmDb();
        this.context = context;
        if (cursor != null) {
            this.cursor = cursor;
            while (cursor.moveToNext()){
                addToListe(new Message(cursor.getString(cursor.getColumnIndex(PostsBDDContract.PostsEntry.COLUMN_MESSAGE_NAME)),
                        cursor.getString(cursor.getColumnIndex(PostsBDDContract.PostsEntry.COLUMN_PSEUDO_NAME)),
                        cursor.getString(cursor.getColumnIndex(PostsBDDContract.PostsEntry.COLUMN_DATE_NAME)
                        )));

            }
        }
    }


    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        Message message = listMessage.get(position);
        if (cursor!=null && cursor.moveToPosition(position)){
            holder.bind(cursor.getString(cursor.getColumnIndex(PostsBDDContract.PostsEntry.COLUMN_MESSAGE_NAME)),
                    cursor.getString(cursor.getColumnIndex(PostsBDDContract.PostsEntry.COLUMN_PSEUDO_NAME)),
                    cursor.getString(cursor.getColumnIndex(PostsBDDContract.PostsEntry.COLUMN_DATE_NAME)));
        }else{
            holder.bind(
                    message.getMessage(),
                    message.getPseudo(),
                    message.getHour()
            );
        }
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParent);

        return new MessageHolder(view);
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    //TODO addToListe
    public int addToListe(Message message){
        listMessage.add(message);
        //notifyItemInserted(listMessage.size() - 1);
        notifyDataSetChanged();
        //TODO Ajouter post SQLite
        addPost(message.getHour(),message.getMessage(),message.getPseudo());


        //SharedPreferences sp = MessageActivity.getSp();
        //sp.edit().putString(String.valueOf(sp.getAll().size()),
        //        message.getMessage()+";" +
        //        message.getHour()+ ";"+
        //        message.getPseudo()
        //).apply();

        return listMessage.indexOf(message);
    }

    public void clearListe(){
        listMessage.clear();
        notifyDataSetChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemMessage;
        TextView itemPseudo;
        TextView itemHour;

        public MessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            itemMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            itemPseudo = (TextView) itemView.findViewById(R.id.textViewPseudo);
            itemHour = (TextView) itemView.findViewById(R.id.textViewHour);
        }

        void bind(String message, String pseudo, String hour) {
            itemMessage.setText(message);
            itemPseudo.setText(pseudo);
            itemHour.setText(hour);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Message message = listMessage.get(clickedPosition);
            messageOnClickListener.onListItemClick(message, view);
        }

    }
    private void addPost(String date,String message,String pseudo){
        ContentResolver contentResolver =  context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(PostsBDDContract.PostsEntry.COLUMN_DATE_NAME,date);
        cv.put(PostsBDDContract.PostsEntry.COLUMN_MESSAGE_NAME,message);
        cv.put(PostsBDDContract.PostsEntry.COLUMN_PSEUDO_NAME,pseudo);

        contentResolver.insert(Uri.parse(PostContentProviderContract.BASE_CONTENT_URI + "/" +
                PostContentProviderContract.PATH_POST),cv);
    }
}
