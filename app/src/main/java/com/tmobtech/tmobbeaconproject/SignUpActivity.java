package com.tmobtech.tmobbeaconproject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.tmobtech.tmobbeaconproject.utility.ParseCore;


/*
* Created by Deniz Katipoglu
 * */

public class SignUpActivity extends ActionBarActivity {

    private EditText email,userName,password;
    private Button signUp;
    ParseCore parseCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();




        //kaydetme buttonu listener..
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    parseCore.registerUser(userName.getText().toString(),password.getText().toString(),email.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void initialize() {
        email = (EditText) findViewById(R.id.etEmail);
        userName = (EditText) findViewById(R.id.etUserName);
        password = (EditText) findViewById(R.id.etPass);
        signUp = (Button) findViewById(R.id.btnSingUp);
        parseCore=new ParseCore(SignUpActivity.this);
    }
}
