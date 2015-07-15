package com.tmobtech.tmobbeaconproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tmobtech.tmobbeaconproject.data.MyDbHelper;

/**
 * Created by Kerim on 15.7.2015.
 */
public class SetBeaconFragment extends Fragment implements  View.OnTouchListener {

    View markerViewClass;
    FrameLayout frameLayout;
    static float x;
    static float y;
    ImageView mapImageView;
    Bitmap marker;
    Bitmap mapBitmap;
    long mapID;
    Intent intent;
    MyDbHelper myDbHelper;
    Cursor cursor;
    String imagePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.setbeaconfragment,null) ;
        initialize();
        mapImageView=(ImageView)view.findViewById(R.id.imageView);


        imagePath = new PlaceBeaconActivity().getImagePath();





        //Imageview icine ornek bir resim koydum

        // mapImageView.setImageResource(R.drawable.map2);
        //ImageView Long Click Listener
        mapImageView.setOnTouchListener(this);





        try {
            Picasso.with(getActivity())
                    .load(imagePath).fit().centerCrop()
                    .into(mapImageView);

        }
        catch (Exception e)
        {
            Log.e("BeaconFragmenError",e.toString());
        }



        Log.e("ImagePath=",imagePath);



        return view;
    }

    private void initialize()
    {


      //  myDbHelper=new MyDbHelper(getActivity());




    }


    @Override
    public boolean onTouch(View v, MotionEvent event){


        final View marker=v;


        frameLayout=(FrameLayout)getActivity().findViewById(R.id.frame1);
        if (v.getId()==mapImageView.getId())
        {

            final Dialog dialog=new Dialog(getActivity());
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

                    LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
}
