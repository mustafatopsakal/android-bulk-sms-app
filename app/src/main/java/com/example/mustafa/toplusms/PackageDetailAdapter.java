package com.example.mustafa.toplusms;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by mustafa on 28.09.2016.
 */
public class PackageDetailAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<PackageDetail> packageDetailList;

    public PackageDetailAdapter(Activity activity, ArrayList<PackageDetail> packageDetailList){
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.packageDetailList = packageDetailList;
    }

    @Override
    public int getCount() {
        return packageDetailList.size();
    }

    @Override
    public Object getItem(int i) {
        return packageDetailList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolderItem viewHolder;

        if(view==null){
            view = mInflater.inflate(R.layout.package_detail, null);
            viewHolder = new ViewHolderItem();
            viewHolder.tvTag = (TextView) view.findViewById(R.id.tvTag);
            viewHolder.tvValue = (TextView) view.findViewById(R.id.tvValue);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderItem) view.getTag();
        }

        final PackageDetail packageDetail = packageDetailList.get(i);
        viewHolder.tvTag.setText(packageDetail.getTag());
        viewHolder.tvValue.setText(packageDetail.getValue());

        return view;
    }

    static class ViewHolderItem {
        TextView tvTag, tvValue;
    }
}
