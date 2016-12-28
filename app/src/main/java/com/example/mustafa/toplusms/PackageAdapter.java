package com.example.mustafa.toplusms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mustafa on 26.09.2016.
 */
public class PackageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Package> packageList;
    Activity activity;

    public PackageAdapter(Activity activity, ArrayList<Package> packageList){
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.packageList = packageList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return packageList.size();
    }

    @Override
    public Object getItem(int i) {
        return packageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;

        if(view==null){
            view = mInflater.inflate(R.layout.package_layout, null);
            viewHolder = new ViewHolderItem();
            viewHolder.tvOriginator = (TextView) view.findViewById(R.id.tvOriginator);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            viewHolder.tvStatistic = (TextView) view.findViewById(R.id.tvStatistic);
            viewHolder.rlPackageReport = (RelativeLayout) view.findViewById(R.id.rLPackageReport);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderItem) view.getTag();
        }

        final Package packageObj = packageList.get(i);
        viewHolder.tvOriginator.setText(packageObj.getPackageOriginator());
        viewHolder.tvDate.setText(packageObj.getPackageDate());
        viewHolder.tvStatistic.setText(packageObj.getPackageTotal() + " Toplam/" + packageObj.getSuccessful() + " Başarılı");

        viewHolder.rlPackageReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tıklanan Mesaj", (String.valueOf(packageObj.getPackageList().size())));
                Intent intent = new Intent(activity, PackageDetailActivity.class);
                intent.putExtra("PackageList", packageObj.getPackageList());
                activity.startActivity(intent);
            }
        });

        return view;
    }

    static class ViewHolderItem {
        TextView tvOriginator,tvDate,tvStatistic;
        RelativeLayout rlPackageReport;
    }
}
