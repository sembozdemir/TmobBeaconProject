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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.ParseData.Constants;
import com.tmobtech.tmobbeaconproject.customviews.BeaconMarkerView;
import com.tmobtech.tmobbeaconproject.customviews.PlaceMarkerView;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;
import com.tmobtech.tmobbeaconproject.entity.Beacon;
import com.tmobtech.tmobbeaconproject.entity.BeaconPower;
import com.tmobtech.tmobbeaconproject.entity.LocalBeaconPower;
import com.tmobtech.tmobbeaconproject.entity.Place;
import com.tmobtech.tmobbeaconproject.utility.FindBeacon;
import com.tmobtech.tmobbeaconproject.utility.UserGuideDialog;
import com.tmobtech.tmobbeaconproject.utility.Utility;

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
    static List<LocalBeaconPower> checkedBeaconPowersListe;
    private PlaceMarkerView mClickedPlaceMarkerView;
    static List<LocalBeaconPower> beaconPowersListe;
     BeaconPowerListAdapter beaconPowerListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_place, null);

        UserGuideDialog userGuideDialog = new UserGuideDialog(getActivity(),"setPlacePage");
        initViews(view);
        layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parentActivity = (PlaceBeaconActivity) getActivity();
        setImageView(parentActivity.getImagePath());
        beaconList = Utility.getBeaconFromParse(parentActivity.getMapID());
        placeList = Utility.getPlaceFromParse(parentActivity.getMapID());
        placeBeacons(beaconList);
        placePlaces(placeList);
        findBeacon = new FindBeacon(getActivity());

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        findBeacon.stopBeaconBindService();
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
                            mClickedPlaceMarkerView = (PlaceMarkerView) v;
                            Place place = mClickedPlaceMarkerView.getPlace();
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

        if (place != null) { // it will be edit place dialog
            dialog.setTitle("Edit Place");
            saveDialogButton.setVisibility(View.GONE);
            beaconPowersListe = Utility.getBeaconPowersFromParse(place.getObjectId());
            checkedBeaconPowersListe = Utility.getCheckedBeaconPowers(beaconPowersListe, findBeacon, beaconList);
            beaconPowerListAdapter = new BeaconPowerListAdapter(getActivity(),
                    checkedBeaconPowersListe);
            beaconPowerListView.setAdapter(beaconPowerListAdapter);

            placeNameEditText.setText(place.getPlaceName());

            updateDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    place.setPlaceName(placeNameEditText.getText().toString());

                    updatePlace(place);
                    dialog.cancel();
                }
            });

            delDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePlace(place);
                    frameLayout.removeView(mClickedPlaceMarkerView);
                    dialog.cancel();
                }
            });
        } else {
            dialog.setTitle("New Place"); // it will be new place dialog
            updateDialogButton.setVisibility(View.GONE);
            delDialogButton.setVisibility(View.GONE);
            checkedBeaconPowersListe =Utility.getBeaconPowers(findBeacon,beaconList);
            beaconPowerListAdapter = new BeaconPowerListAdapter(getActivity(), checkedBeaconPowersListe);
            beaconPowerListView.setAdapter(beaconPowerListAdapter);



            saveDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PlaceMarkerView placeMarkerView = new PlaceMarkerView(getActivity());
                    placeMarkerView.setX(x - 64);
                    placeMarkerView.setY(y - 64);
                    Place newplace = new Place();
                    newplace.setPlaceName(placeNameEditText.getText().toString());
                    newplace.setApsis(placeMarkerView.getX());
                    newplace.setOrdinat(placeMarkerView.getY());
                    newplace.setMapId(parentActivity.getMapID());

                    placeMarkerView.setPlace(newplace);
                    placeMarkerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mClickedPlaceMarkerView = (PlaceMarkerView) v;
                            Place place = ((PlaceMarkerView) v).getPlace();
                            createDialog(place);
                            dialog.show();
                        }
                    });
                    savePlace(newplace);
                    frameLayout.addView(placeMarkerView, layoutParams1);
                    dialog.cancel();
                }
            });
        }

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedBeaconPowersListe =Utility.getBeaconPowers(findBeacon,beaconList);
                beaconPowerListAdapter=new BeaconPowerListAdapter(getActivity(),checkedBeaconPowersListe);
                beaconPowerListView.setAdapter(beaconPowerListAdapter);
            }
        });
    }

    private void updatePlace(Place place) {
        place.saveInBackground();
        Toast.makeText(getActivity(), "updated", Toast.LENGTH_LONG).show();

        ParseQuery<BeaconPower> query = ParseQuery.getQuery(BeaconPower.class);
        final String placeId = place.getObjectId();
        query.whereEqualTo(Constants.COLUMN_BEACON_MEASURE_PLACE_ID, placeId);
        query.findInBackground(new FindCallback<BeaconPower>() {
            @Override
            public void done(List<BeaconPower> list, ParseException e) {
                if (e == null) {
                    for (BeaconPower beaconPower : list) {
                        beaconPower.deleteInBackground();
                    }
                    for (LocalBeaconPower checkedBeaconPower : checkedBeaconPowersListe) {
                        if (checkedBeaconPower.isBeaconIsAdded()) {
                            checkedBeaconPower.toBeaconPower(placeId).saveInBackground();
                        }
                    }
                }
            }
        });
    }


    private void deletePlace(Place place) {
        ParseQuery<BeaconPower> parseQuery = ParseQuery.getQuery(BeaconPower.class);
        parseQuery.whereEqualTo(Constants.COLUMN_BEACON_MEASURE_PLACE_ID, place.getObjectId());
        parseQuery.findInBackground(new FindCallback<BeaconPower>() {
            @Override
            public void done(List<BeaconPower> list, ParseException e) {
                if (e == null) {
                    for (BeaconPower beaconPower : list) {
                        beaconPower.deleteInBackground();
                    }
                }
            }
        });
        place.deleteInBackground();
        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_LONG).show();
    }

    private void savePlace(final Place place) {
        Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG).show();

        place.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    String placeId = place.getObjectId();

                    List<BeaconPower> beaconPowers = Utility.getBeaconPowerToSave(checkedBeaconPowersListe, placeId);
                    for (BeaconPower beaconPower : beaconPowers) {
                        beaconPower.saveInBackground();
                    }
                }
            }
        });
    }
}
