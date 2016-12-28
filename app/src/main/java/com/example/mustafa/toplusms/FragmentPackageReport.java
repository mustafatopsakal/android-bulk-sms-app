package com.example.mustafa.toplusms;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
public class FragmentPackageReport extends Fragment {

    //COMPONENTLER
    TextView tvStartDate, tvEndDate;
    ImageButton ibStartDate, ibEndDate;
    ListView lvPackages;

    //DEĞİŞKENLER
    ArrayList<String> arrayMessages;
    ArrayList<String> partMessages;
    static ArrayList<Package> packageList = new ArrayList<Package>();

    String serverAnswer;
    XmlGet xmlObj;
    UserSessionManager session;
    PackageAdapter packageAdapter;

    static Integer mYear, mMonth, mDay, mHour, mMinute;
    static Calendar c;

    public static void Delete()
    {
        packageList.clear();
    }

    public FragmentPackageReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        xmlObj = new XmlGet();
        session = new UserSessionManager(getActivity());
        View view = inflater.inflate(R.layout.fragment_package_report, container, false);
        getActivity().setTitle("Paket Raporu");

        tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        ibStartDate = (ImageButton) view.findViewById(R.id.ibStartDate);
        ibEndDate = (ImageButton) view.findViewById(R.id.ibEndDate);
        lvPackages = (ListView) view.findViewById(R.id.lvPackageReport);

        setDate();
        getMessages();

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

        return view;
    }

    public void getMessages()
    {
        try {
            serverAnswer = new XmlPost().execute(xmlObj.packageReport(session.getUserName(),session.getPassword(),
                    tvStartDate.getText().toString(),tvEndDate.getText().toString()),xmlObj.packageReportUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        packageList.clear();


        if(serverAnswer.equals("")|| serverAnswer.equals("20") || serverAnswer.equals("23") || serverAnswer.equals("30")) {

        }
        else {
            arrayMessages = new ArrayList<String>(Arrays.asList(serverAnswer.split("\\n")));

            for(int i=0;i<arrayMessages.size();i++) {
                partMessages = new ArrayList<String>(Arrays.asList(arrayMessages.get(i).split("\\t")));
                packageList.add(new Package(partMessages.get(0),partMessages.get(1),partMessages.get(2),
                        partMessages.get(3),partMessages.get(4),partMessages.get(5),partMessages.get(6),
                        partMessages.get(7),partMessages.get(8),partMessages.get(9),partMessages.get(10),partMessages.get(11)));
            }
        }

        packageAdapter = new PackageAdapter(getActivity(), packageList);
        lvPackages.setAdapter(packageAdapter);
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
       /* c.add(Calendar.MONTH,-1);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());*/
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
                getMessages();
            }
        },mHour,mMinute,false);

        timePickerDialog.show();
    }

}
