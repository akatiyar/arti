package akatiyar.arti;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by abhinav on 4/5/15.
 */
public class ArtiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, getString(R.string.parse_application_id),
                getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // ParseUser.enableAutomaticUser();

        // ParseUser.getCurrentUser().saveInBackground();

        // ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        // ParseACL.setDefaultACL(defaultACL, true);

        // ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

