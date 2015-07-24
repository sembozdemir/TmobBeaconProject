package com.tmobtech.tmobbeaconproject.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.tmobtech.tmobbeaconproject.R;

import com.tmobtech.tmobbeaconproject.ParseData.ParseCore;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
