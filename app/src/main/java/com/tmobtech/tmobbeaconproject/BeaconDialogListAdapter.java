package com.tmobtech.tmobbeaconproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.List;

/**
 * Created by ozberkcetin on 20/07/15.
 */
public class BeaconDialogListAdapter extends BaseAdapter {

    Activity activity;
    List<Beacon> list;
    LayoutInflater inflater;

    public BeaconDialogListAdapter(Activity activity, List<org.altbeacon.beacon.Beacon> list)
    {
        this.activity=activity;
        this.list=list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
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
        View view=inflater.inflate(R.layout.list_item_dialog_beacon,null);
        TextView beaconId=(TextView)view.findViewById(R.id.textView_dialog_list_mac_addrs);
        TextView distance=(TextView)view.findViewById(R.id.textView_dialog_list_distance);
        beaconId.setText(list.get(position).getBluetoothAddress());
        distance.setText(String.valueOf(list.get(position).getDistance()).substring(0,3)+"m");
        return  view;
    }
}
