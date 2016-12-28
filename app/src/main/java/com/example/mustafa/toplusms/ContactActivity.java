package com.example.mustafa.toplusms;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //REHBER AKTİVİTESİ, REHBER SAYFASI.
    ListView lvContact;

    //REHBERİN 1 KEZ ÇEKİLMESİ İÇİN BU DEĞERLER STATİK OLMALIDIR.
    static ArrayList<Contact> contact = new ArrayList<Contact>();
    static ArrayList<Contact> copyContact = new ArrayList<Contact>();
    ContactAdapter conAdapter;

    public static void Delete()
    {
        contact.clear();
        copyContact.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle("Rehber");

        lvContact = (ListView) findViewById(R.id.lvContact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LİSTE KOPYALANARAK BU KOPYA LİSTE ADAPTERE GÖNDERİLİYOR VE FİLTRELENİYOR.
        copyContact = (ArrayList<Contact>) contact.clone();
        conAdapter = new ContactAdapter(this, copyContact);
        lvContact.setAdapter(conAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        conAdapter.contactFilter(s);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //EKRANDA SEARCH VİEW COMPONENTİNİN GÖSTERİMİ

        getMenuInflater().inflate(R.menu.menu_contact, menu);

        SearchView mSearchView = new SearchView(ContactActivity.this);
        MenuItem mItem = menu.add(0,1,0,"");
        mItem.setActionView(mSearchView);
        mItem.setIcon(android.R.drawable.ic_menu_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setQueryHint("Bir kişi arayın");
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    //HOME TUŞUNA TIKLANILDIĞINDA
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("foodName", "");
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.selectItems:
                if(FragmentSendSms.choosingContacts.size() > 0) {
                    FragmentSendSms.choosingContacts.clear();
                    for (Contact c : contact) {
                        c.setCheckbox(false);
                    }
                }
                else {
                    FragmentSendSms.choosingContacts = (ArrayList<Contact>) contact.clone();
                    for (Contact c : contact) {
                        c.setCheckbox(true);
                    }
                }

                lvContact.setAdapter(conAdapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
