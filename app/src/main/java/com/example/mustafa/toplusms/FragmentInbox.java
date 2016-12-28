package com.example.mustafa.toplusms;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInbox extends Fragment {

    //COMPONENTLER
    EditText etSubscriber;
    TextView tvStartDate, tvEndDate;
    ImageButton ibStartDate, ibEndDate;
    ListView lvMessages;

    //DEĞİŞKENLER
    ArrayList<String> arrayMessages;
    ArrayList<String> partMessages;
    ArrayList<Message> messageList = new ArrayList<Message>();

    String serverAnswer;
    XmlGet xmlObj;
    UserSessionManager session;
    MessageAdapter messageAdapter;

    static Integer mYear, mMonth, mDay, mHour, mMinute;
    static Calendar c;
    static String subscriberNo = "";

    public FragmentInbox() {
        // Required empty public constructor
    }

    public static void Delete()
    {
        subscriberNo = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        xmlObj = new XmlGet();
        session = new UserSessionManager(getActivity());

        View view =  inflater.inflate(R.layout.fragment_inbox, container, false);
        getActivity().setTitle("Gelen Kutusu");
        etSubscriber = (EditText) view.findViewById(R.id.etSubscriber);
        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        ibStartDate = (ImageButton) view.findViewById(R.id.ibStartDate);
        ibEndDate = (ImageButton) view.findViewById(R.id.ibEndDate);
        lvMessages = (ListView) view.findViewById(R.id.lvMessageReport);

        setDate();

        etSubscriber.setText(subscriberNo);
        etSubscriber.setSelection(subscriberNo.length());
        if(etSubscriber.getText().length() == 12)
        {
            getMessages();
        }

        ibStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(tvStartDate);
            }
        });

        ibEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(tvEndDate);
            }
        });

        etSubscriber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subscriberNo = charSequence.toString();
                if(charSequence.length() == 12) {
                   getMessages();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public void getMessages()
    {
        messageList.clear();

        try {
            serverAnswer = new XmlPost().execute(xmlObj.getInbox
                    (etSubscriber.getText().toString(), session.getPassword(),
                            tvStartDate.getText().toString(),tvEndDate.getText().toString()), xmlObj.getInboxUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(serverAnswer.equals("")) {
            messageList.add(new Message("Seçili Tarihler Arası Mesajınız Bulunmamaktadır.","",""));
        }
        else if(serverAnswer.equals("20") || serverAnswer.equals("23")) {
            messageList.add(new Message("Bir Hata Oluştu.","",""));
        }
        else if(serverAnswer.equals("30")) {
            messageList.add(new Message("Hesap Aktivasyonu Sağlanmamış.","",""));
        }
        else {
            arrayMessages = new ArrayList<String>(Arrays.asList(serverAnswer.split("\\n")));
            for(int i=0;i<arrayMessages.size();i++) {
                partMessages = new ArrayList<String>(Arrays.asList(arrayMessages.get(i).split("\\t")));

                String control = partMessages.get(1);
                for(Contact c : ContactActivity.contact) {
                    if (c.getcNumber() != null && (c.getcNumber().contains(partMessages.get(1)) ||
                            c.getcNumber().contains(partMessages.get(1).substring(1))  ||
                            c.getcNumber().contains("+" + partMessages.get(1))))
                    {
                        control = c.getcName() + "(" + partMessages.get(1) + ")";
                        break;
                    }
                }
                messageList.add(new Message(control,partMessages.get(0),partMessages.get(2)));
            }
        }

        messageAdapter = new MessageAdapter(getActivity(), messageList);
        lvMessages.setAdapter(messageAdapter);
    }

    public void setDate()
    {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        tvEndDate.setText(formatter.format(c.getTime()));
        c.add(Calendar.MONTH,-1);
        tvStartDate.setText(formatter.format(c.getTime()));
    }

    //TAKVİMDEN TARİH SEÇİMİ
    public void pickDate(final TextView textView)
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
                pickTime(year, monthOfYear, dayOfMonth, textView);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    //SAAT SEÇİMİ
    public void pickTime(final int year, final int monthOfYear, final int dayOfMonth, final TextView textView)
    {

        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    c.set(year, monthOfYear, dayOfMonth,hourOfDay,minute);
                    textView.setText(formatter.format(c.getTime()));
                    if(etSubscriber.getText().length() == 12)
                    {
                        getMessages();
                    }
            }
        },mHour,mMinute,false);

        timePickerDialog.show();
    }

}
