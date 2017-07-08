package com.cp.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cp.user.R;
import com.cp.user.model.HistoryAddress;

import java.util.List;

/**
 * Created by yi on 08/07/2017.
 */

public class MenuAdapter extends ArrayAdapter<HistoryAddress> {

    private List<HistoryAddress> historyAddressList;
    private Context context;
    private int layoutResID;

    public MenuAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HistoryAddress> historyAddressList) {
        super(context, resource, historyAddressList);
        this.context = context;
        this.historyAddressList = historyAddressList;
        this.layoutResID = resource;

    }

    public void populate(List<HistoryAddress> list) {

        if (list == null) return;

        historyAddressList.clear();
        historyAddressList.addAll(list);
        notifyDataSetChanged();
    }

    public void reset() {
        historyAddressList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view.findViewById(R.id.address);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        HistoryAddress dItem = (HistoryAddress) this.historyAddressList.get(position);

        drawerHolder.ItemName.setText(TextUtils.isEmpty(dItem.getAddress()) ? "Address " + (position + 1) : dItem.getAddress());

        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName;
    }
}
