package com.example.mustafa.toplusms;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by mustafa on 30.09.2016.
 */
public class BlackListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<String> blackList;
    Activity activity;
    FragmentBlackList fragmentBlackList;

    public BlackListAdapter(Activity activity, ArrayList<String> blackList, FragmentBlackList fragmentBlackList){
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.blackList = blackList;
        this.activity = activity;
        this.fragmentBlackList = fragmentBlackList;
    }

    @Override
    public int getCount() {
        return blackList.size();
    }

    @Override
    public Object getItem(int i) {
        return blackList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;

        if(view==null){
            view = mInflater.inflate(R.layout.blacklist_layout, null);
            viewHolder = new ViewHolderItem();
            viewHolder.tvNumber = (TextView) view.findViewById(R.id.tvNumber);
            viewHolder.ibDelete = (ImageButton) view.findViewById(R.id.ibDelete);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderItem) view.getTag();
        }

        viewHolder.tvNumber.setText(blackList.get(i));

        viewHolder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(activity).setMessage("Numara kara listeden çıkarılacak. Emin misiniz?").setNegativeButton("VAZGEÇ", null).
                        setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                            String serverAnswer = "";
                            XmlGet xmlObj = new XmlGet();
                            UserSessionManager session = new UserSessionManager(activity);

                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    serverAnswer = new XmlPost().execute(xmlObj.deleteBlackList(session.getUserName(),session.getPassword(),blackList.get(i)),
                                            xmlObj.deleteBlackListUrl).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                                if(serverAnswer.startsWith("$")) {
                                    fragmentBlackList.getBlackList();
                                    new AlertDialog.Builder(activity).setTitle("Bilgi").setMessage("Seçilen numara listeden çıkarıldı.").
                                            setNegativeButton("TAMAM", null).create().show();
                                }
                                else if(serverAnswer.startsWith("$0")){
                                    new AlertDialog.Builder(activity).setTitle("Uyarı").setMessage("Bir hata oluştu.").
                                            setNegativeButton("TAMAM", null).create().show();
                                }
                                else{
                                    new AlertDialog.Builder(activity).setTitle("Uyarı").setMessage("Bir hata oluştu.").
                                            setNegativeButton("TAMAM", null).create().show();
                                }
                            }
                        }).create().show();
            }
        });

        return view;
    }

    static class ViewHolderItem {
        TextView tvNumber;
        ImageButton ibDelete;
    }
}
