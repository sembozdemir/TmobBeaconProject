package com.tmobtech.tmobbeaconproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by semih on 21.07.2015.
 */
public class BeaconPowerListAdapter extends ArrayAdapter<BeaconPower> {

    // View lookup cache
    private static class ViewHolder {
        TextView textViewMacAddress;
        TextView textViewDistance;
        CheckBox checkBoxIsAdded;
    }

    public BeaconPowerListAdapter(Context context, BeaconPower[] objects) {
        super(context, R.layout.list_item_dialog_place, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BeaconPower beaconPower = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_dialog_place, parent, false);
            viewHolder.textViewMacAddress = (TextView) convertView.findViewById(R.id.textView_mac_address);
            viewHolder.textViewDistance = (TextView) convertView.findViewById(R.id.textView_distance);
            viewHolder.checkBoxIsAdded = (CheckBox) convertView.findViewById(R.id.checkBox_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.textViewMacAddress.setText(beaconPower.getBeacon().getMacAddress());
        viewHolder.textViewDistance.setText("" + beaconPower.getDistance());
        viewHolder.checkBoxIsAdded.setChecked(beaconPower.isAdded());
        // Return the completed view to render on screen
        return convertView;
    }
}
