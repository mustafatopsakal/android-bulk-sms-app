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
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentReportNumber extends Fragment {

    //COMPONENTLER
    EditText etSubscriber;
    TextView tvStartDate, tvEndDate;
    ImageButton ibStartDate, ibEndDate, ibReportNumber;
    ListView lvMessages;

    //DEĞİŞKENLER
    ArrayList<String> arrayMessages;
    ArrayList<String> partMessages;
    static ArrayList<Message> messageList = new ArrayList<Message>();

    String serverAnswer;
    XmlGet xmlObj;
    UserSessionManager session;
    MessageAdapter messageAdapter;

    static Integer mYear, mMonth, mDay, mHour, mMinute;
    static Calendar c;
    static String subscriberNo = "";

    public FragmentReportNumber() {
        // Required empty public constructor
    }

    public static void Delete()
    {
        subscriberNo = "";
        messageList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        xmlObj = new XmlGet();
        session = new UserSessionManager(getActivity());

        View view =  inflater.inflate(R.layout.fragment_report_number, container, false);
        getActivity().setTitle("Numaradan Rapor Sorgulama");

        etSubscriber = (EditText) view.findViewById(R.id.etSubscriber);
        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        ibStartDate = (ImageButton) view.findViewById(R.id.ibStartDate);
        ibEndDate = (ImageButton) view.findViewById(R.id.ibEndDate);
        lvMessages = (ListView) view.findViewById(R.id.lvMessageReport);
        ibReportNumber = (ImageButton) view.findViewById(R.id.ibReportNumber);

        setDate();
        etSubscriber.setText(subscriberNo);
        etSubscriber.setSelection(subscriberNo.length());

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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ibReportNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etSubscriber.getText().toString().equals(""))
                {
                    getMessages();
                }
                else{
                    Toast.makeText(getActivity(),"Sorgulanacak numara boş bırakılmamalıdır.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void getMessages()
    {
        try {
            serverAnswer = new XmlPost().execute(xmlObj.reportNumber(session.getUserName(),session.getPassword(),
                    etSubscriber.getText().toString(),tvStartDate.getText().toString(),tvEndDate.getText().toString()),xmlObj.reportNumberUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        messageList.clear();


        if(serverAnswer == "" || serverAnswer.equals("20") || serverAnswer.equals("23")) {

        }
        else {
            arrayMessages = new ArrayList<String>(Arrays.asList(serverAnswer.split("\\n")));

            for(int i=0;i<arrayMessages.size();i++) {
                partMessages = new ArrayList<String>(Arrays.asList(arrayMessages.get(i).split("\\t")));
                String state =  stateCode(partMessages.get(2));
                messageList.add(new Message(state,partMessages.get(0),partMessages.get(1)));
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
            }
        },mHour,mMinute,false);

        timePickerDialog.show();
    }

    public String stateCode(String code)
    {
       int state = Integer.parseInt(code);

        switch(state)
        {
            case 0: code = "GÖNDERİLMEDİ"; break;
            case 1: code = "İŞLENİYOR"; break;
            case 2: code = "GÖNDERİLDİ"; break;
            case 3: code = "BAŞARILI"; break;
            case 4: code = "BEKLEMEDE"; break;
            case 5: code = "ZAMANAŞIMI"; break;
            case 6: code = "BAŞARISIZ"; break;
            case 7: code = "REDDEDİLDİ"; break;
            case 11: code = "BİLİNMİYOR"; break;
            case 12: code = "HATYOK"; break;
            case 13: code = "HATALI"; break;
            default: code = "BİLİNMİYOR"; break;
        }
        return code;
    }

}
