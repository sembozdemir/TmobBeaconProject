package com.tmobtech.tmobbeaconproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

import javax.security.auth.login.LoginException;

/**
 * Created by Ozberk on 15.7.2015.
 */
public class PlaceBeaconActivity extends ActionBarActivity  {

    View markerViewClass;
    FrameLayout frameLayout;
    static float x;
    static float y;
    ImageView mapImageView;
    Bitmap marker;
    Bitmap mapBitmap;
    static long mapID;
    Intent intent;
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




       intent=getIntent();


        mapID=intent.getLongExtra("mapId", 0);






        //Imageview icine ornek bir resim koydum

       // mapImageView.setImageResource(R.drawable.map2);
        //ImageView Long Click Listener


        cursor=myDbHelper.getMapFromId(mapID);


        try {
            if (cursor.moveToFirst())
                do {
                    imagePath=cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_MAP_IMAGE_PATH));
                }
                while (cursor.moveToNext());

            Picasso.with(this)
                    .load(imagePath).fit().centerCrop()
                    .into(mapImageView);


        }
        catch (Exception e)
        {
            Log.e("DATABASE ERROR",e.toString());
        }

        Log.e("ImagePath=",imagePath);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        FragmentManager fm=getSupportFragmentManager() ;
        pagerAdapter=new PageAdapter(PlaceBeaconActivity.this,fm);
        viewPager.setAdapter(pagerAdapter);
    }





    private void initialize() {



        myDbHelper = new MyDbHelper(PlaceBeaconActivity.this);


    }

    public String getImagePath()
    {
        return  imagePath;
    }
    public  long getMapID(){return  mapID;}



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
