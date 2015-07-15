package com.tmobtech.tmobbeaconproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class PlaceBeaconActivity extends ActionBarActivity implements View.OnTouchListener {

    View markerViewClass;
    FrameLayout frameLayout;
    static float x;
    static float y;
    ImageView mapImageView;
    Bitmap marker;
    Bitmap mapBitmap;
    String imagePath;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_beacon);
        initialize();

       intent=getIntent();


        //Imageview icine ornek bir resim koydum

        mapImageView.setImageResource(R.drawable.map2);
        //ImageView Long Click Listener
        mapImageView.setOnTouchListener(this);






    }

    private void initialize()
    {
        mapImageView=(ImageView) findViewById(R.id.imageView);




        marker=BitmapFactory.decodeResource(getResources(),R.drawable.map2);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_beacon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
                    markerView.setX(x-50);

                    markerView.setY(x);
                    markerView.setY(y-100);
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
}
