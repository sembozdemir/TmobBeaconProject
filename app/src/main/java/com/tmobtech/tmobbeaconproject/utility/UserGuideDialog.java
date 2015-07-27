package com.tmobtech.tmobbeaconproject.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tmobtech.tmobbeaconproject.R;

/**
 * Created by ozberkcetin on 23/07/15.
 */
public class UserGuideDialog {
    Activity activity;
    private String TAG="UserGuideDialog";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isShown;
    String activityName;
    String homePage="Sag alt tarafta bulunan + butonuna basarak ister kamerandan isterseniz galeriden haritanizi yukleyebilirsiniz.";
    String setBeaconPage="Bu sayfada tanitmak istediginiz beaconlari harita uzerinde istediginiz noktalara tiklayarak beacon ozelliklerini girip kaydedebilirsiniz";
    String setPlacePage="Bu sayafada tanitmak istediginiz yer lokasyonlarini harita uzerinde istediginiz noktalara tiklayarak harita ozelliklerini firip kaydedebilirsiniz";
    public UserGuideDialog(Activity activity,String activityName)
    {
        this.activity=activity;
        this.activityName=activityName;
        createDialog();

    }






    public void createDialog()
    {
         sharedPreferences= PreferenceManager.getDefaultSharedPreferences(activity);
         editor=sharedPreferences.edit();
        try {
            isShown=sharedPreferences.getBoolean(activityName,true);

        }
        catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }
        if (isShown) {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_guide);
            dialog.setTitle("Info");
            Button Ok = (Button) dialog.findViewById(R.id.button5);
            final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
            TextView detail = (TextView) dialog.findViewById(R.id.textView);

            switch (activityName)
            {
                case "homePage": detail.setText(homePage);
                break;
                case "setBeaconPage": detail.setText(setBeaconPage);
                break;
                case  "setPlacePage" : detail.setText(setPlacePage);
            }
            Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {

                        editor.putBoolean(activityName, false);
                        editor.commit();
                        dialog.cancel();

                    }
                    else
                        dialog.cancel();
                }
            });


            dialog.show();

        }

    }
}
