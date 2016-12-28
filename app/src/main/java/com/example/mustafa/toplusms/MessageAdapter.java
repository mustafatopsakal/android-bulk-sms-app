package com.example.mustafa.toplusms;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mustafa on 18.09.2016.
 */
public class MessageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Message> messageList;

    public MessageAdapter(Activity activity, ArrayList<Message> messageList){
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;

        if(view==null){
            view = mInflater.inflate(R.layout.message_layout, null);
            viewHolder = new ViewHolderItem();
            viewHolder.tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            viewHolder.tvMessage = (TextView) view.findViewById(R.id.tvMessages);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderItem) view.getTag();
        }

        final Message message = messageList.get(i);
        viewHolder.tvPhoneNumber.setText(message.getNumber());
        viewHolder.tvDate.setText(message.getDate());
        viewHolder.tvMessage.setText(message.getMessage());

        return view;
    }

    static class ViewHolderItem {
        TextView tvPhoneNumber,tvDate,tvMessage;
    }
}
