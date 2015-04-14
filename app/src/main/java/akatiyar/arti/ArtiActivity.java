package akatiyar.arti;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;


public class ArtiActivity extends Activity {
    private static final String TAG = ArtiActivity.class.getName();
    private static final int LOGIN_REQUEST = 0;

    private ListView deviceListView;
    private ArrayAdapter<ParseObject> deviceAdapter;

    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;

    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arti);

        titleTextView = (TextView) findViewById(R.id.profile_title);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        nameTextView = (TextView) findViewById(R.id.profile_name);
        loginOrLogoutButton = (Button) findViewById(R.id.login_or_logout_button);
        titleTextView.setText(R.string.profile_title_logged_in);

        // ParseAnalytics.trackAppOpenedInBackground(getIntent());
        deviceListView = (ListView) findViewById(R.id.device_list);
        deviceAdapter = new DeviceAdapter(this, R.layout.device_state);
        deviceListView.setAdapter(deviceAdapter);

        // Prompt for login/signup
        loginOrLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // User clicked to log out.
                    ParseUser.logOut();
                    currentUser = null;
                    showProfileLoggedOut();
                } else {
                    // User clicked to log in.
                    ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                            ArtiActivity.this);
                    startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            showProfileLoggedIn();
        } else {
            showProfileLoggedOut();
        }
    }

    /**
     * Shows the profile of the given user.
     */
    private void showProfileLoggedIn() {
        titleTextView.setText(R.string.profile_title_logged_in);
        emailTextView.setText(currentUser.getEmail());
        String fullName = currentUser.getString("name");
        if (fullName != null) {
            nameTextView.setText(fullName);
        }
        loginOrLogoutButton.setText(R.string.profile_logout_button_label);

        showFridge();
    }


    private void showFridge() {
        // find devices for current user.
        // Assuming currentUser is not null if we have reached here in the code.
        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("_User")
                .whereEqualTo("username", currentUser.getUsername());
        // TODO handle authorized users later
        ParseQuery<ParseObject> devicesQuery = ParseQuery.getQuery("Device")
                .whereMatchesQuery("owner", innerQuery);
        // TODO Fix DeviceState table to keep a pointer to Device table for deviceId
        // and query device states as below to read in one call.
        //ParseQuery<ParseObject> deviceStateQuery = ParseQuery.getQuery("DeviceState")
        //        .whereMatchesQuery("deviceId", devicesQuery);

        devicesQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> devices, ParseException e) {
                if (e == null && !devices.isEmpty()) {
                    for (ParseObject device : devices) {
                        final String deviceId = String.valueOf(device.get("deviceId"));
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("DeviceState")
                                .whereEqualTo("deviceId", deviceId)
                                .addDescendingOrder("updatedAt");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null && !objects.isEmpty()) {
                                    ParseObject deviceState = objects.get(0);
                                    deviceAdapter.add(deviceState);
                                } else {
                                    Log.d(TAG, "Could not load device state for " + deviceId);
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "No devices found for " + currentUser.getUsername());
                    // TODO Display a message on screen.
                }
            }
        });


    }

    /**
     * Show a message asking the user to log in, toggle login/logout button text.
     */
    private void showProfileLoggedOut() {
        titleTextView.setText(R.string.profile_title_logged_out);
        emailTextView.setText("");
        nameTextView.setText("");
        loginOrLogoutButton.setText(R.string.profile_login_button_label);
        deviceAdapter.clear();
    }
}
