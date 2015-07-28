package com.tmobtech.tmobbeaconproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tmobtech.tmobbeaconproject.entity.LocalBeaconPower;

import java.util.List;

/**
 * Created by semih on 21.07.2015.
 */
public class BeaconPowerListAdapter extends ArrayAdapter<LocalBeaconPower> {

    // View lookup cache
    private static class ViewHolder {
        TextView textViewName;
        TextView textViewDistance;
        CheckBox checkBoxIsAdded;
    }

    public BeaconPowerListAdapter(Context context, List<LocalBeaconPower> objects) {
        super(context, R.layout.list_item_dialog_place, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LocalBeaconPower beaconPower = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_dialog_place, parent, false);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textView_beacon_name);
            viewHolder.textViewDistance = (TextView) convertView.findViewById(R.id.textView_distance);
            viewHolder.checkBoxIsAdded = (CheckBox) convertView.findViewById(R.id.checkBox_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.textViewName.setText(beaconPower.getBeaconName());
        viewHolder.textViewDistance.setText("" + beaconPower.getBeaconDistance());
        viewHolder.checkBoxIsAdded.setChecked(beaconPower.isBeaconIsAdded());

        viewHolder.checkBoxIsAdded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    SetPlaceFragment.checkedBeaconPowersListe.get(position).setBeaconIsAdded(true);
                }
                else {
                    SetPlaceFragment.checkedBeaconPowersListe.get(position).setBeaconIsAdded(false);
                }


            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
