package com.example.mustafa.toplusms;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by mustafa on 17.08.2016.
 */
public class ContactAdapter extends BaseAdapter {

    //REHBERİN GÖRÜNTÜSÜNÜN OLUŞTURULACAĞI ADAPTER SINIFI
    private LayoutInflater mInflater;
    private ArrayList<Contact> contacts;
    private ArrayList<Contact> copyContacts;
    private TextDrawable.IBuilder mDrawableBuilder;
    private final int HIGHLIGHT_COLOR = 0x999be6ff;

    public ContactAdapter(Activity activity, ArrayList<Contact> contacts) {
        this.contacts = contacts;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        copyContacts = new ArrayList<Contact>();
        copyContacts.addAll(contacts);
        mDrawableBuilder = TextDrawable.builder().rect();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHolderItem viewHolder;

        if(view==null){
            view = mInflater.inflate(R.layout.contact_layout, null);
            viewHolder = new ViewHolderItem();
            viewHolder.tvContactName = (TextView) view.findViewById(R.id.tvContactName);
            viewHolder.tvContactNumber = (TextView) view.findViewById(R.id.tvContactNumber);
            viewHolder.ivContactPicture = (ImageView) view.findViewById(R.id.ibContactPhoto);
            viewHolder.checkIcon = (ImageView) view.findViewById(R.id.check_icon);
            viewHolder.rlContactLine = (RelativeLayout)view.findViewById(R.id.rLContactLine);
            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolderItem) view.getTag();
        }

        //BİR KİŞİNİN ADININ VE NUMARASININ GÖSTERİLMESİ
        final Contact contact = contacts.get(position);
        viewHolder.tvContactName.setText(contact.getcName());
        viewHolder.tvContactNumber.setText(contact.getcNumber());

        //KİŞİLERİN RESİMLERİ VE ÖNCEDEN SEÇİLMİŞLERİN YÜKLENMESİ
        if(contact.isCheckbox()) {
            viewHolder.ivContactPicture.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
            viewHolder.checkIcon.setVisibility(View.VISIBLE);
            viewHolder.rlContactLine.setBackgroundColor(HIGHLIGHT_COLOR);
        }

        else {
            viewHolder.ivContactPicture.setImageDrawable(contact.getDrawable());
            viewHolder.checkIcon.setVisibility(View.GONE);
            viewHolder.rlContactLine.setBackgroundColor(Color.TRANSPARENT);
        }

        //KİŞİLERİN RESİMLERİNİN VE SEÇİLMİŞLİĞİNİN YÜKLENMESİ
        viewHolder.rlContactLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.isCheckbox())
                {
                    contact.setCheckbox(false);
                    FragmentSendSms.choosingContacts.remove(contact);
                    viewHolder.ivContactPicture.setImageDrawable(contact.getDrawable());
                    viewHolder.checkIcon.setVisibility(View.GONE);
                    viewHolder.rlContactLine.setBackgroundColor(Color.TRANSPARENT);
                }

                else
                {
                    contact.setCheckbox(true);
                    FragmentSendSms.choosingContacts.add(contact);
                    viewHolder.ivContactPicture.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
                    viewHolder.checkIcon.setVisibility(View.VISIBLE);
                    viewHolder.rlContactLine.setBackgroundColor(HIGHLIGHT_COLOR);
                }
            }
        });

        viewHolder.ivContactPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.isCheckbox())
                {
                    contact.setCheckbox(false);
                    FragmentSendSms.choosingContacts.remove(contact);
                    viewHolder.ivContactPicture.setImageDrawable(contact.getDrawable());
                    viewHolder.checkIcon.setVisibility(View.GONE);
                    viewHolder.rlContactLine.setBackgroundColor(Color.TRANSPARENT);
                }

                else
                {
                    contact.setCheckbox(true);
                    FragmentSendSms.choosingContacts.add(contact);
                    viewHolder.ivContactPicture.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
                    viewHolder.checkIcon.setVisibility(View.VISIBLE);
                    viewHolder.rlContactLine.setBackgroundColor(HIGHLIGHT_COLOR);
                }
            }
        });

        return view;
    }

    static class ViewHolderItem {
        TextView tvContactName;
        TextView tvContactNumber;
        ImageView ivContactPicture;
        ImageView checkIcon;
        RelativeLayout rlContactLine;
    }

    public void contactFilter(String charText)
    {
       //FİLTRELEME FONKSİYONU
        charText = charText.toLowerCase(Locale.getDefault());
        contacts.clear();

        if(charText.trim().length() == 0)
        {
            contacts.addAll(copyContacts);
        }
        else
        {
            charText = charText.replace(".","");

            for(Contact v0 : copyContacts)
            {
                if(v0.getcName().toLowerCase().trim().startsWith(charText.trim()))
                {
                    contacts.add(v0);
                    continue;
                }
                String [] splites = v0.getcName().toLowerCase().trim().split( "\\s++");

                if(splites != null)
                {

                    for(String s: splites)
                    {
                        if(s.startsWith(charText.trim()))
                        {
                            contacts.add(v0);
                            break;
                        }
                    }
                }
            }

        }
        notifyDataSetChanged();
    }
}
