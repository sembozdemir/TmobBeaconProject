package com.tmobtech.tmobbeaconproject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ozberkcetin on 20/07/15.
 */
public class SpinnerAdapter extends BaseAdapter {

    Activity activity;
    List<Beacon> list;
    LayoutInflater inflater;

    public SpinnerAdapter (Activity activity,List<org.altbeacon.beacon.Beacon> list)
    {
        this.activity=activity;
        this.list=list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.spinnerxml,null);
        TextView beaconId=(TextView)view.findViewById(R.id.textView3);
        TextView distance=(TextView)view.findViewById(R.id.textView4);
        beaconId.setText(list.get(position).getBluetoothAddress());
        distance.setText(String.valueOf(list.get(position).getDistance()).substring(0,3)+"m");
        return  view;
    }
}