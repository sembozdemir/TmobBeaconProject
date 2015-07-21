package com.tmobtech.tmobbeaconproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class SetPlaceFragment extends Fragment {
    private static final String LOG_TAG = SetPlaceFragment.class.getSimpleName();

    private FrameLayout frameLayout;
    private ImageView mapImageView;
    private MyDbHelper myDbHelper;
    private PlaceBeaconActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setplacefragment, null);

        initViews(view);
        myDbHelper = new MyDbHelper(getActivity());
        parentActivity = (PlaceBeaconActivity) getActivity();
        setImageView(parentActivity.getImagePath());

        return view;
    }

    private void setImageView(String imagePath) {
//        Cursor cursor = myDbHelper.getMapFromId(mapID);
//        if (cursor.moveToFirst()) {
//            do {
//                String imagePath = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH));
//                Picasso.with(getActivity())
//                        .load(imagePath)
//                        .fit()
//                        .centerInside()
//                        .into(mapImageView);
//            } while (cursor.moveToNext());
//        }
        Picasso.with(getActivity())
                .load(imagePath)
                .fit()
                .centerInside()
                .into(mapImageView);
    }

    private void initViews(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.framePlace);
        mapImageView = (ImageView) view.findViewById(R.id.imageViewPlace);
    }
}
