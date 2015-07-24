package com.tmobtech.tmobbeaconproject.utility;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.tmobtech.tmobbeaconproject.MainActivity;
import com.tmobtech.tmobbeaconproject.PlaceBeaconActivity;

/**
 * Created by ozberkcetin on 24/07/15.
 */
public class ParseCore {
    Activity activity;


    public  ParseCore (Activity activity)
    {
        this.activity=activity;
    }



    public   void registerUser(String userName,String passWord,String email) throws ParseException {
        ParseUser parseUser=new ParseUser();
        parseUser.setUsername(userName);
        parseUser.setPassword(passWord);
        parseUser.setEmail(email);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null)
                {

                    Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(activity, MainActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }


            }
        });


    }


    public   void authenticateUser(String usr, String psw) throws ParseException {


        String username=usr.trim();
        String password=psw.trim();
        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder("ErrorBuilder");
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append("BlankUser");
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append("ErrorJoin");
            }
            validationError = true;
            validationErrorMessage.append("BlankPassowrd");
        }
        validationErrorMessage.append("EndError");

        // If there is a validation error, display the error
        if (validationError) {
            Log.e("ValidationError","ValidationError");
            return ;
        }
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e != null) {

                    //isValiadate[0] =false;

                      Toast.makeText(activity,"Login Failed",Toast.LENGTH_LONG).show();

                } else {
                    Intent intent=new Intent(activity,MainActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);


                }
            }
        });


    }



}
