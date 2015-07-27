package com.tmobtech.tmobbeaconproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.entity.BeaconMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semih on 13.07.2015.
 */
public class MyGridAdapter extends BaseAdapter {
    private static final String LOG_TAG = MyGridAdapter.class.getSimpleName();
    private Context mContext;
    private final List<BeaconMap> beaconMaps;

    public MyGridAdapter(Context context, List<BeaconMap> beaconMaps) {
        mContext = context;
        this.beaconMaps = beaconMaps;
    }

    @Override
    public int getCount() {
        return beaconMaps.size();
    }

    @Override
    public BeaconMap getItem(int position) {
        return beaconMaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid = null;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_item, null);
            TextView textViewMapDesc = (TextView) grid.findViewById(R.id.textView_map_desc);
            textViewMapDesc.setText(beaconMaps.get(position).getName());
            ImageView imageViewMap = (ImageView) grid.findViewById(R.id.imageView_map);
            Log.d(LOG_TAG, "On Adapter: getView(): " + beaconMaps.get(position).getImagePath());
            Picasso.with(mContext)
                    .load(beaconMaps.get(position).getImagePath())
                    .resize(100, 100)
                    .centerCrop()
                    .into(imageViewMap);
        } else {
            grid = convertView;
        }

        return grid;
    }
}
