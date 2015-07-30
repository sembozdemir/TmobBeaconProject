package com.tmobtech.tmobbeaconproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.tmobtech.tmobbeaconproject.utility.ParseCore;

/*
* Created by Deniz Katipoglu
 * */
public class SignInActivity extends ActionBarActivity {
    private EditText userName, password;
    private Button signIn;
    private TextView signUp;
    ParseCore parseCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        initialize();




        //giriş button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    parseCore.authenticateUser(userName.getText().toString(),password.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        //kayıt olma ekranını açar..
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initialize() {
        userName = (EditText) findViewById(R.id.etUserName);
        password = (EditText) findViewById(R.id.etPass);
        signIn = (Button) findViewById(R.id.btnSingIn);
        signUp = (TextView) findViewById(R.id.singUp);
        parseCore=new ParseCore(SignInActivity.this);
    }
}
