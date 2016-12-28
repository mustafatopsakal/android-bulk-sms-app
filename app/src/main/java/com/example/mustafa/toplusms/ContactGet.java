package com.example.mustafa.toplusms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by mustafa on 17.08.2016.
 */
public class ContactGet extends AsyncTask<Void, Void, ArrayList<Contact>> {

    private ColorGenerator mColorGenerator;
    private TextDrawable.IBuilder mDrawableBuilder;
    ArrayList<String> cNumber = new ArrayList<String>();
    ArrayList<Contact> contact = new ArrayList<Contact>();
    Context content;

    public ContactGet(Context content) {
        this.content = content;
    }

    @Override
    protected ArrayList<Contact> doInBackground(Void... voids) {
        //ARKA PLANDA REHBERİN SORGULANMASI
        mColorGenerator = ColorGenerator.MATERIAL;
        mDrawableBuilder = TextDrawable.builder().rect();
        ContentResolver cr = content.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+","");
                        if(!cNumber.contains(phoneNo)) {
                            cNumber.add(phoneNo);
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(name.charAt(0)), mColorGenerator.getColor(name));
                            contact.add(new Contact(name,phoneNo,drawable));
                        }
                    }
                    pCur.close();
                }
            }
        }

        Collections.sort(contact,new ContactComparator());

        return contact;
    }

    class ContactComparator implements Comparator< Contact > {
        @Override
        public int compare(Contact contact, Contact t1) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            return collator.compare(contact.getcName(), t1.getcName());
        }
    }
}


