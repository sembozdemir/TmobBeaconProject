package ParseData;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by ozberkcetin on 24/07/15.
 */
public class ParseCore {
    Activity activity;
    public  ParseCore(Activity activity)
    {
        this.activity=activity;
        com.parse.Parse.enableLocalDatastore(activity);

        com.parse.Parse.initialize(activity, "HXHT2n4P9lx1C4bZ6zPc0YBEcN0lDlMUM3ktpWaf", "M1dwVFvcLMa8uP9tNGtqFF8TZc8CJDfsrEecmnNh");
    }

    public   void registerParse(String userName,String passWord,String email) throws ParseException {
        ParseUser parseUser=new ParseUser();
        parseUser.setUsername(userName);
        parseUser.setPassword(passWord);
        parseUser.setEmail(email);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null)
                {
                    Log.e("Success","Success");
                }
                else
                    Log.e("False","False");
            }
        });


    }


    public void authenticateUser (String user,String password) throws ParseException {
        ParseUser.logInInBackground(user, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e != null) {
                    // Show the error message
                  Log.e("bos","bos");
                } else {
                    Log.e("bosyyy","bosyy");

                }
            }
        });
    }


}
