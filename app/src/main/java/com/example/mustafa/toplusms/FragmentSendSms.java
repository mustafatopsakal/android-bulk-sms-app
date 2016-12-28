package com.example.mustafa.toplusms;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSendSms extends Fragment {

    //COMPONENTLER
    EditText etContact, etMessage;
    TextView tvCredit, tvChoosingContacts, tvDate, tvMessageSize;
    ImageButton ibContact, ibNameTag;
    Button btSend;
    Spinner spin;
    Switch switchButton;

    //SINIF NESNELERİ
    XmlGet obj;
    UserSessionManager session;

    //DEĞİŞKENLER
    ArrayList<String> arraySpinner;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String credit, sendSmsAnswer;

    public static ArrayList<Contact> choosingContacts = new ArrayList<Contact>();
    static String etChoosingContacts = new String();
    static String sendNextDate = "", originator= "", message = "", contact = "";
    static Integer mYear, mMonth, mDay, mHour, mMinute, messageCount;
    static Calendar c;

    public FragmentSendSms() {
        // Required empty public constructor
    }

    //STATİC DEĞİŞKENLERİN SIFIRLANMASI
    public static void Delete()
    {
        choosingContacts.clear();
        etChoosingContacts = "";
        sendNextDate = "";
        originator = "";
        message = "";
        contact = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //COMPONENTLERİN TANIMLANMASI
        View view =  inflater.inflate(R.layout.fragment_send_sms, container, false);
        getActivity().setTitle("Sms Gönder");
        tvCredit = (TextView) view.findViewById(R.id.tvCredit);
        tvChoosingContacts = (TextView) view.findViewById(R.id.tvChoosingContacts);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvMessageSize = (TextView) view.findViewById(R.id.tvMessageSize);
        etContact = (EditText) view.findViewById(R.id.etClients);
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        ibContact = (ImageButton) view.findViewById(R.id.ibContacts);
        ibNameTag = (ImageButton) view.findViewById(R.id.ibTag);
        btSend = (Button) view.findViewById(R.id.btSend);
        switchButton = (Switch) view.findViewById(R.id.swSendNextDate);

        //KULLANICI OTURUMU VE XML NESNESİ OLUŞTURULMASI
        obj = new XmlGet();
        session = new UserSessionManager(getActivity());
        spin = (Spinner) view.findViewById(R.id.spTitle);


        //KULLANICI BAŞLIĞI SETLENİR.
        if(originator.equals("")) {
            try {
                //KULLANICI BAŞLIĞI SORGULAMASI VE SPİNNERDA GÖSTERİLMESİ.
                originator = new XmlPost().execute(obj.getOriginators(session.getUserName(), session.getPassword()), obj.getOriginatorUrl).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        arraySpinner = new ArrayList<String>(Arrays.asList(originator.split("\\t")));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arraySpinner);
        spin.setAdapter(dataAdapter);

        //KONTÖR MİKTARI EKRANDA GÖSTERİLİR.
        setCredit();

        //SEÇİLMİŞ KİŞİLER EKRANDA GÖSTERİLİR.
        setChoosingSize();

        //MESAJ KUTUSU VE ALICI KUTUSU SETLENİYOR
        setEditText(etContact,contact);
        setEditText(etMessage,message);

        //MESAJ KUTUSUNUN UZUNLUĞU VE MESAJ SAYISI KULLANICIYA GÖSTERİLİYOR.
        if(message.length() != 0) {
            tvMessageSize.setText("");
            tvMessageSize.setText(message.toString().trim().length() + " karakter, " + messageCount + " mesaj");
        }

        //İSİM PARAMETRESİ EKLENMESİ
        ibNameTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMessage.append("{isim} ");
            }
        });

        //İLERİ TARİHLİ GÖNDERİM SWİTCHİ
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    pickDate();
                }

                else {
                    sendNextDate = "";
                    tvDate.setText("");
                }
            }
        });

        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contact = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //KULLANICI MESAJ KUTUSUNA MESAJ YAZDIĞINDA EKRANDA BUNUN BOYUTUNUN GÖSTERİLMESİ.
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                message = charSequence.toString();

                if(charSequence.length() == 0) {
                        tvMessageSize.setText("");
                }

                else {
                        messageCount = (charSequence.length() / 161) + 1;
                        tvMessageSize.setText(charSequence.toString().trim().length() + " karakter, " + messageCount + " mesaj");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //REHBER ERİŞİM İZNİ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else {
            if(ContactActivity.contact.isEmpty()) {
                ContactGet contactGet = new ContactGet(getActivity());
                try {
                    ContactActivity.contact = contactGet.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        //REHBERİ AÇMA BLOĞU
        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                startActivityForResult(intent,1);
            }
        });

        //SUNUCUYA MESAJ GÖNDERME İSTEĞİ POST EDİLİR.
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String message = etMessage.getText().toString().trim();
                etChoosingContacts = etContact.getText().toString();

                if(choosingContacts.size() < 1 && etChoosingContacts.equals(""))
                {
                    new AlertDialog.Builder(getActivity()).setTitle("Uyarı").setMessage("Hiçbir numara seçmediniz!").
                            setNegativeButton("TAMAM", null).create().show();
                }

                else if(message.equals(""))
                {
                    new AlertDialog.Builder(getActivity()).setTitle("Uyarı").setMessage("Gönderilecek metin boş bırakılamaz!").
                            setNegativeButton("TAMAM", null).create().show();
                }

                else
                {
                    int personNumber = choosingContacts.size();

                    if(!etChoosingContacts.isEmpty()){
                        personNumber = personNumber + etChoosingContacts.split(",").length;
                    }

                    final int finalPersonNumber = personNumber;
                    new AlertDialog.Builder(getActivity()).setMessage(personNumber * messageCount + " adet mesaj gönderilecek. Emin misiniz?").
                            setNegativeButton("VAZGEÇ", null).setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {

                                        try {
                                            sendSmsAnswer = new XmlPost().execute(obj.sendSmsWithName(session.getUserName(),
                                                    session.getPassword(),spin.getSelectedItem().toString(),message,sendNextDate,choosingContacts,etChoosingContacts),obj.sendSmsUrl).get();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                        if(sendSmsAnswer.startsWith("$")) {
                                            new AlertDialog.Builder(getActivity()).setTitle("Bilgi").setMessage(finalPersonNumber * messageCount + " adet mesaj gönderildi.").
                                                    setNegativeButton("TAMAM", null).create().show();
                                            choosingContacts.clear();
                                            etContact.setText("");
                                            etMessage.setText("");
                                            tvDate.setText("");
                                            setChoosingSize();
                                            setCredit();
                                            sendNextDate = "";
                                            switchButton.setChecked(false);
                                        }
                                        else{
                                            new AlertDialog.Builder(getActivity()).setTitle("Uyarı").setMessage("Bir hata oluştu.").
                                                    setNegativeButton("TAMAM", null).create().show();
                                        }
                                    }
                                }).create().show();

                }

            }
        });
        return view;
    }

   private void setEditText(EditText editText,String message)
   {
       if(!message.isEmpty())
       {
           editText.setText(message);
       }
   }

    //KONTÖR SORGULAMASI YAPILMASI VE EKRANDA GÖSTERİLMESİ
    private void setCredit()
    {
        try {

            credit = new XmlPost().execute(obj.getCredit(session.getUserName(),session.getPassword()),obj.getCreditUrl).get();
            credit = Integer.toString(Math.round(Float.parseFloat(credit.substring(1).toString())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        tvCredit.setText("Kontör:"+ credit);
    }

    //SEÇİLEN NUMARALARIN ALINMASI
    private void setChoosingSize()
    {

        if(choosingContacts.size() > 0) {
            tvChoosingContacts.setText("Alıcılar: " + "(" + choosingContacts.size() + ")");
        }
        else {
            tvChoosingContacts.setText("Alıcılar");
        }
    }

    //TAKVİMDEN TARİH SEÇİMİ
    public void pickDate()
    {

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                pickTime(year, monthOfYear, dayOfMonth);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switchButton.setChecked(false);
            }
        });


        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    //SAAT SEÇİMİ
    public void pickTime(final int year, final int monthOfYear, final int dayOfMonth)
    {

        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(mYear.equals(year) && mMonth.equals(monthOfYear) &&
                        mDay.equals(dayOfMonth) && hourOfDay < mHour.intValue())
                {
                    pickTime (year, monthOfYear,dayOfMonth);
                    Toast.makeText(getActivity(),"Geçmiş Zaman Seçildi", Toast.LENGTH_SHORT).show();
                    switchButton.setChecked(false);
                }

                else if(mYear.equals(year) && mMonth.equals(monthOfYear) &&
                        mDay.equals(dayOfMonth) && hourOfDay == mHour.intValue() && minute < mMinute)
                {
                    Toast.makeText(getActivity(),"Geçmiş Zaman Seçildi", Toast.LENGTH_SHORT).show();
                    switchButton.setChecked(false);
                }

                else
                {
                    c.set(year, monthOfYear, dayOfMonth,hourOfDay,minute);
                    sendNextDate = formatter.format(c.getTime());
                    tvDate.setText("Gönderme Zamanı: " + sendNextDate);
                }

            }
        },mHour,mMinute,false);

        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switchButton.setChecked(false);
            }
        });

        timePickerDialog.show();
    }

    //KULLANICININ REHBERE ERİŞİM İZNİ VERİP VERMEDİĞİNİN DENETLENMESİ
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(),"İzin verildi.", Toast.LENGTH_SHORT).show();
                if(ContactActivity.contact.isEmpty())
                {
                    ContactGet contactGet = new ContactGet(getActivity());
                    try {
                        ContactActivity.contact = contactGet.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Toast.makeText(getActivity(),"Erişim izni reddedildi.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //REHBER EKRANINDAN GERİYE DÖNÜŞ.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == ContactActivity.RESULT_OK){
                String State = data.getStringExtra("foodName");
                setChoosingSize();
            }
        }
    }

    //OPTİONS MENÜ SETLEMELERİ
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //OPTİONS MENÜ SETLEMELERİ
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sms,menu);
    }

    //OPTİONS MENÜ SETLEMELERİ
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item1:
                etMessage.append("{isim} ");
                return true;

            case R.id.item2:
                etMessage.setText("");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
