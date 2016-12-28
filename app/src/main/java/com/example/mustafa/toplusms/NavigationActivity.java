package com.example.mustafa.toplusms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        FragmentSendSms fragmentSendSms = new FragmentSendSms();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.relativelayout_for_fragment, fragmentSendSms, fragmentSendSms.getTag()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final UserSessionManager session = new UserSessionManager(this);

        //NAVİGATİON MENÜSÜNDE TIKLANAN İTEMLERE GÖRE AÇILACAK FRAGMENTLER
        if (id == R.id.nav_sendSms) {
            FragmentSendSms fragmentSendSms = new FragmentSendSms();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment, fragmentSendSms, fragmentSendSms.getTag()).commit();
        }
        else if(id == R.id.nav_inbox)
        {
            FragmentInbox fragmentInbox = new FragmentInbox();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment, fragmentInbox, fragmentInbox.getTag()).commit();
        }
        else if(id == R.id.nav_reportNumber)
        {
            FragmentReportNumber fragmentReportNumber  = new FragmentReportNumber();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment, fragmentReportNumber, fragmentReportNumber.getTag()).commit();
        }
        else if(id == R.id.nav_packageReport)
        {
            FragmentPackageReport fragmentPackageReport  = new FragmentPackageReport();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment, fragmentPackageReport, fragmentPackageReport.getTag()).commit();
        }
        else if(id == R.id.nav_blackList)
        {
            FragmentBlackList fragmentBlackList  = new FragmentBlackList();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment, fragmentBlackList, fragmentBlackList.getTag()).commit();
        }
        else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(this).setMessage("Toplu SMS uygulamasından çıkış yapmak üzeresiniz.").
                    setNegativeButton("VAZGEÇ", null).
                    setPositiveButton("ÇIKIŞ YAP", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    session.logoutUser();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                    ContactActivity.Delete();
                    FragmentInbox.Delete();
                    FragmentPackageReport.Delete();
                    FragmentReportNumber.Delete();
                    FragmentSendSms.Delete();
                    FragmentBlackList.Delete();
                    finish();
                }
            }).create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
