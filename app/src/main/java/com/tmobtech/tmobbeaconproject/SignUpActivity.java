package com.tmobtech.tmobbeaconproject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
}
