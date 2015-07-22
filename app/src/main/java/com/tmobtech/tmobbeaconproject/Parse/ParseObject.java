package com.tmobtech.tmobbeaconproject.Parse;

import android.app.Activity;

/**
 * Created by ozberkcetin on 20/07/15.
 */
public class ParseObject {
    private String TAG="ParseObjectError";
    Activity activity;
    public ParseObject(Activity activity)
    {
        this.activity=activity;
        com.parse.Parse.enableLocalDatastore(activity);

        com.parse.Parse.initialize(activity, "HXHT2n4P9lx1C4bZ6zPc0YBEcN0lDlMUM3ktpWaf", "M1dwVFvcLMa8uP9tNGtqFF8TZc8CJDfsrEecmnNh");

    }


}
