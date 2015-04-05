package akatiyar.arti;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

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
        Parse.initialize(this, "FQHluCbjFceB6JYBIdiusFV7hVEW10N0n1lvVbxe", "2yFSV9m86ffL2cmcRwgRQvY6sa7nGViWpTv8QfHK");


        ParseUser.enableAutomaticUser();

        ParseUser.getCurrentUser().saveInBackground();

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

