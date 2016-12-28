package com.example.mustafa.toplusms;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    //COMPONENTLER
    UserSessionManager session;
    TextView tvKeepSessionOpen, tvForgotPassword, tvFreeRegister;
    ImageView ivIcon;
    Button btLogin;
    EditText etUserName, etPassword;
    Switch keepSessionOpen;
    ProgressBar mProgressBar;
    String serverAnswer = "";
    XmlGet obj = new XmlGet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //KULLANICI ÖNCEDEN GİRİŞ YAPMIŞSA BU SAYFA SONLANDIRILIYOR.
        session = new UserSessionManager(getApplicationContext());
        if(session.checkLogin())
        {
            try{
                serverAnswer = new XmlPost().execute(obj.getCredit(session.getUserName(),session.getPassword()),obj.getCreditUrl).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(serverAnswer.startsWith("$")) {
                Intent i = new Intent(this, NavigationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(i);
                finish();
            }
        }

        //COMPONENTLER
        tvKeepSessionOpen=(TextView)findViewById(R.id.tvKeepSessionOpen);
        tvForgotPassword=(TextView)findViewById(R.id.tvForgotPassword);
        tvFreeRegister = (TextView)findViewById(R.id.tvFreeRegister);
        ivIcon = (ImageView)findViewById(R.id.ivMutluSoft);
        btLogin = (Button) findViewById(R.id.btLogin);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword  = (EditText) findViewById(R.id.etPassword);
        keepSessionOpen = (Switch) findViewById(R.id.swKeepSessionOpen);
        ivIcon.setImageResource(R.drawable.mutluicon);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        etUserName.setText(session.getUserName());
        etPassword.setText(session.getPassword());

        tvKeepSessionOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OTURUM AÇIK KALSIN YAZISINDA SWİTCHİN TERS SETLENMESİ
                keepSessionOpen.setChecked(!keepSessionOpen.isChecked());
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("https://login.mutlucell.com/forgotpassword/"));
                startActivity(viewIntent);
            }
        });

        tvFreeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("https://login.mutlucell.com/register/?idbayi=MUTLUCELL"));
                startActivity(viewIntent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(view.VISIBLE);

                //KULLANICININ GİRİŞ YAPMASI VE KONTROLLERİ.
                String etUser = etUserName.getText().toString();
                String etPsw = etPassword.getText().toString();

                if(!isNetworkAvailable())
                {
                    mProgressBar.setVisibility(view.INVISIBLE);
                    new AlertDialog.Builder(LoginActivity.this).setTitle("UYARI").setMessage("İnternet Bağlantısı Yok!").
                            setNegativeButton("TAMAM", null).create().show();
                }
                else if(etUser.equals("") || etPsw.equals("")) {
                    mProgressBar.setVisibility(view.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Kullanıcı Adı ve Parola Boş Bırakılamaz!",Toast.LENGTH_SHORT).show();
                }

                else if( etUser.length() < 7 && etPsw.length() < 6) {
                    mProgressBar.setVisibility(view.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Geçersiz Kullanıcı/Parola",Toast.LENGTH_SHORT).show();
                }

                else {
                    try{
                        serverAnswer = new XmlPost().execute(obj.getCredit(etUser,etPsw),obj.getCreditUrl).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(serverAnswer.startsWith("$")) {
                        session.createUserLoginSession(etUser, etPsw, keepSessionOpen.isChecked());
                        Intent myIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                        finish();
                    }

                    else {
                        mProgressBar.setVisibility(view.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Bir Hata Oluştu",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
