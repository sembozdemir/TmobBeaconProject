package com.tmobtech.tmobbeaconproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PlaceBeaconActivity extends ActionBarActivity {
    static long mapID;
    MyDbHelper myDbHelper;
    Cursor cursor;
    static String imagePath;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_beacon);
        initialize();

        cursor = myDbHelper.getMapFromId(mapID);
        try {
            if (cursor.moveToFirst())
                do {
                    imagePath = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH));
                }
                while (cursor.moveToNext());
        } catch (Exception e) {
            Log.e("DATABASE ERROR", e.toString());
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager fm = getSupportFragmentManager();
        pagerAdapter = new PageAdapter(PlaceBeaconActivity.this, fm);
        viewPager.setAdapter(pagerAdapter);
    }


    private void initialize() {
        myDbHelper = new MyDbHelper(PlaceBeaconActivity.this);
        mapID = getIntent().getLongExtra("mapId", 0);
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getMapID() {
        return mapID;
    }



    /*
    @Override
    public boolean onTouch(View v, MotionEvent event){


        final View marker=v;


        frameLayout=(FrameLayout)findViewById(R.id.frame1);
        if (v.getId()==mapImageView.getId())
        {

            final Dialog dialog=new Dialog(PlaceBeaconActivity.this);
            dialog.setTitle("Beacon Tanimlama");
            dialog.setContentView(R.layout.dialog);
            dialog.show();
            Button kaydetDialogBtn=(Button)dialog.findViewById(R.id.button);
            Button silDialogBtn=(Button)dialog.findViewById(R.id.button2);



            final EditText markerName=(EditText)dialog.findViewById(R.id.editText);
            x=event.getX();
            y=event.getY();
            kaydetDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FrameLayout.LayoutParams layoutParams1=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LayoutInflater inflater=(LayoutInflater)PlaceBeaconActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View markerView=inflater.inflate(R.layout.markerframelayout,null);
                    markerView.setTag(markerName.getText());




                    Log.e("X=", x + "");
                    markerView.setX(x);

                    markerView.setY(x);
                    markerView.setY(y);
                    frameLayout.addView(markerView,layoutParams1);
                    dialog.cancel();

                    markerView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            markerViewClass=v;
                            dialog.show();
                        }
                    });




                }
            });


            silDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ViewGroup parentView = (ViewGroup) markerViewClass.getParent();
                        parentView.removeView(markerViewClass);
                        dialog.cancel();
                    }
                    catch (Exception e){
                        Log.e("Eror Remove View :",e.toString());
                    }


                }
            });






        }




        return false;
    }
    */
}
