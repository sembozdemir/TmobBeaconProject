package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.BeaconManager.FindBeacon;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;
import com.tmobtech.tmobbeaconproject.utility.Utility;
import com.tmobtech.tmobbeaconproject.views.BeaconMarkerView;
import com.tmobtech.tmobbeaconproject.views.PlaceMarkerView;

import java.util.List;


/**
 * Created by Ozberk on 15.7.2015.
 */
public class SetPlaceFragment extends Fragment implements View.OnTouchListener {
    private static final String LOG_TAG = SetPlaceFragment.class.getSimpleName();

    private FrameLayout frameLayout;
    private ImageView mapImageView;
    private MyDbHelper myDbHelper;
    private PlaceBeaconActivity parentActivity;
    private List<Beacon> beaconList;
    private List<Place> placeList;
    private FrameLayout.LayoutParams layoutParams1;
    private float x;
    private float y;
    private Dialog dialog;
    private FindBeacon findBeacon;
    static List<BeaconPower> beaconPowersListe;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setplacefragment, null);


        initViews(view);
        layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDbHelper = new MyDbHelper(getActivity());
        parentActivity = (PlaceBeaconActivity) getActivity();
        setImageView(parentActivity.getImagePath());
        beaconList = Utility.getBeaconList(parentActivity.getMapID(), getActivity());
        placeList = Utility.getPlaceList(getActivity(), parentActivity.getMapID());
        placeBeacons(beaconList);
        placePlaces(placeList);
        findBeacon = new FindBeacon(getActivity());

        return view;
    }

    private void placePlaces(List<Place> placeList) {
        try {
            if (placeList.size() > 0) {
                for(Place place : placeList) {
                    final PlaceMarkerView placeMarkerView = new PlaceMarkerView(getActivity());
                    placeMarkerView.setX(place.getApsis());
                    placeMarkerView.setY(place.getOrdinat());
                    placeMarkerView.setPlace(place);
                    placeMarkerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Place place = ((PlaceMarkerView) v).getPlace();
                            createDialog(place);
                            dialog.show();
                        }
                    });
                    frameLayout.addView(placeMarkerView, layoutParams1);
                }
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "placePlaces Error: " + e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        findBeacon.stopBeaconBindService();
    }

    private void placeBeacons(List<Beacon> beaconList) {
        try {
            if (beaconList.size() > 0) {
                for(Beacon beacon : beaconList) {
                    final BeaconMarkerView beaconMarkerView = new BeaconMarkerView(getActivity());
                    beaconMarkerView.setX(beacon.getApsis());
                    beaconMarkerView.setY(beacon.getOrdinat());
                    beaconMarkerView.setBeacon(beacon);

                    frameLayout.addView(beaconMarkerView, layoutParams1);
                }
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "placeBeacons Error: " + e.toString());
        }
    }

    private void setImageView(String imagePath) {
        Picasso.with(getActivity())
                .load(imagePath)
                .fit()
                .centerInside()
                .into(mapImageView);
        mapImageView.setOnTouchListener(this);
    }

    private void initViews(View view) {
        frameLayout = (FrameLayout) view.findViewById(R.id.framePlace);
        mapImageView = (ImageView) view.findViewById(R.id.imageViewPlace);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == mapImageView.getId()) {
            x = event.getX();
            y = event.getY();
            createDialog(null);
            dialog.show();
        }
        return false;
    }

    private void createDialog(final Place place) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_place);
        ImageButton refreshButton = (ImageButton) dialog.findViewById(R.id.imageButton_refresh);
        Button saveDialogButton = (Button) dialog.findViewById(R.id.button_save_place);
        Button delDialogButton = (Button) dialog.findViewById(R.id.button_delete_place);
        Button updateDialogButton = (Button) dialog.findViewById(R.id.button_update_place);
        final EditText placeNameEditText = (EditText) dialog.findViewById(R.id.editText_place_name);
        final ListView beaconPowerListView = (ListView) dialog.findViewById(R.id.listView_beacon);
        final BeaconPowerListAdapter beaconPowerListAdapter;
        if (place != null) { // it will be edit place dialog
            dialog.setTitle("Edit Place");
            saveDialogButton.setVisibility(View.GONE);
           beaconPowersListe= Utility.getBeaconPowersFromDb(getActivity(),place.getPlaceId());
            beaconPowersListe=Utility.getCheckedBeaconPowers(beaconPowersListe,findBeacon,parentActivity.getMapID(),getActivity());
            beaconPowerListAdapter = new BeaconPowerListAdapter(getActivity(),
                    beaconPowersListe);
            beaconPowerListView.setAdapter(beaconPowerListAdapter);

            placeNameEditText.setText(place.getPlaceName());
            updateDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updatePlace(place);
                    dialog.cancel();
                }
            });

            delDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePlace();
                    dialog.cancel();
                }
            });
        } else {
            dialog.setTitle("New Place"); // it will be new place dialog
            updateDialogButton.setVisibility(View.GONE);
            delDialogButton.setVisibility(View.GONE);
            beaconPowersListe=Utility.getBeaconPowers(findBeacon,parentActivity.getMapID(),getActivity());
//            beaconPowersListe= Utility.getBeaconPowersFromDb(getActivity(),place.getPlaceId());
//            beaconPowersListe=Utility.getCheckedBeaconPowers(beaconPowersListe, findBeacon, parentActivity.getMapID(), getActivity());
            beaconPowerListAdapter = new BeaconPowerListAdapter(getActivity(),
                    beaconPowersListe);
            beaconPowerListView.setAdapter(beaconPowerListAdapter);


            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beaconPowerListAdapter.clear();
                    beaconPowerListAdapter.addAll(
                            Utility.getBeaconPowers(findBeacon,
                                    parentActivity.getMapID(),
                                    getActivity()));
                    beaconPowerListAdapter.notifyDataSetChanged();
                }
            });

            saveDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PlaceMarkerView placeMarkerView = new PlaceMarkerView(getActivity());
                    placeMarkerView.setX(x - 64);
                    placeMarkerView.setY(y - 64);
                    Place place = new Place(placeNameEditText.getText().toString(),
                            placeMarkerView.getX(),
                            placeMarkerView.getY(),
                            beaconPowersListe);
                    placeMarkerView.setPlace(place);
                    placeMarkerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Place place = ((PlaceMarkerView) v).getPlace();
                            createDialog(place);
                            dialog.show();
                        }
                    });
                    savePlace(place);
                    frameLayout.addView(placeMarkerView, layoutParams1);
                    dialog.cancel();
                }
            });
        }
    }

    private void updatePlace(Place place) {

        Toast.makeText(getActivity(), "updated", Toast.LENGTH_LONG).show();
        Long placeId= myDbHelper.insertPlace(place.getPlaceName(), place.getApsis(), place.getOrdinat(), parentActivity.getMapID());
        place.setPlaceId(placeId);
        List<BeaconPower> beaconPowers = place.getBeaconPowerList();
        for (BeaconPower beaconPower : beaconPowers) {

            if (beaconPower.isAdded())
                myDbHelper.updateBeaconMeasured(beaconPower.getBeacon().getId(), place.getPlaceId(), beaconPower.getDistance());
           

        }
    }


    private void deletePlace() {
        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_LONG).show();
    }

    private void savePlace(Place place) {
        Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG).show();
       Long placeId= myDbHelper.insertPlace(place.getPlaceName(), place.getApsis(), place.getOrdinat(), parentActivity.getMapID());
        place.setPlaceId(placeId);
        List<BeaconPower> beaconPowers = place.getBeaconPowerList();
        for (BeaconPower beaconPower : beaconPowers) {

            if (beaconPower.isAdded())
            myDbHelper.insertBeaconMeasure(beaconPower.getBeacon().getId(), place.getPlaceId(), beaconPower.getDistance());


        }
    }
}
