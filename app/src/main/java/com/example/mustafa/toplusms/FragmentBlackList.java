package com.example.mustafa.toplusms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBlackList extends Fragment {

    //COMPONENTLER
    ListView lvBlackList;
    EditText etNumbers;
    Button btAdd;

    //DEĞİŞKENLER
    ArrayList<String> blackList = new ArrayList<String>();;
    String serverAnswer;
    XmlGet xmlObj;
    UserSessionManager session;
    BlackListAdapter blackListAdapter;
    static String numbers = "";

    public static void Delete()
    {
        numbers = "";
    }
    public FragmentBlackList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        xmlObj = new XmlGet();
        session = new UserSessionManager(getActivity());
        View view = inflater.inflate(R.layout.fragment_black_list, container, false);
        getActivity().setTitle("Kara Liste");

        lvBlackList = (ListView) view.findViewById(R.id.lvBlackList);
        etNumbers = (EditText) view.findViewById(R.id.etNumbers);
        btAdd = (Button) view.findViewById(R.id.btAddBlackList);
        getBlackList();

        etNumbers.setText(numbers);
        etNumbers.setSelection(numbers.length());

        etNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numbers = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etNumbers.getText().toString().equals(""))
                {
                    try {
                        serverAnswer = new XmlPost().execute(xmlObj.addBlackList(session.getUserName(),session.getPassword(),numbers),
                                xmlObj.addBlackListUrl).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(serverAnswer.equals("") || serverAnswer.equals("20") || serverAnswer.equals("23")) {

                    }
                    else if(serverAnswer.equals("30")) {
                        Toast.makeText(getActivity(),"Hesap Aktivasyonu Sağlanmamış",Toast.LENGTH_SHORT).show();
                        etNumbers.setText("");
                    }
                    else if(serverAnswer.startsWith("$0")) {
                        Toast.makeText(getActivity(),"Eklemek İstediğiniz Numarayı Kontrol Ediniz",Toast.LENGTH_SHORT).show();
                        etNumbers.setText("");
                    }
                    else if(serverAnswer.startsWith("$")) {
                        Toast.makeText(getActivity(),"İşlem Başarılı",Toast.LENGTH_SHORT).show();
                        etNumbers.setText("");
                        getBlackList();
                    }
                    else{
                        Toast.makeText(getActivity(),"Bilinmeyen Bir Hata Oluştu",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Sorgulanacak numara boş bırakılmamalıdır.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void getBlackList()
    {
        try {
            serverAnswer = new XmlPost().execute(xmlObj.getBlackList(session.getUserName(),session.getPassword()),
                    xmlObj.getBlackListUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        blackList.clear();

        if(serverAnswer == "" || serverAnswer.equals("20") || serverAnswer.equals("23")) {

        }
        else {
            blackList = new ArrayList<String>(Arrays.asList(serverAnswer.split("\\n")));
        }

        blackListAdapter = new BlackListAdapter(getActivity(), blackList, this);
        lvBlackList.setAdapter(blackListAdapter);
    }

}
