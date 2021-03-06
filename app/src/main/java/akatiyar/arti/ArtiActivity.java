package akatiyar.arti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.List;

import akatiyar.arti.model.ArtiContent;
import akatiyar.arti.model.Device;

/**
 * Created by abhinav on 4/29/15.
 */
public class ArtiActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        OnFragmentInteractionListener {

    private static final String TAG = ArtiActivity.class.getName();
    private static final int LOGIN_REQUEST = 1754;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arti);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ParseUser.getCurrentUser() == null) {
            // Prompt user to log in.
            promptForLogin();
        } else {
            showFridge();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, createFragment(position + 1))
                .commit();
    }

    private Fragment createFragment(int position) {
        switch (position) {
            case ArtiContent.HOME:
                return HomeFragment.newInstance();
            case ArtiContent.ACCOUNT:
                return AccountFragment.newInstance();
            case ArtiContent.ABOUT:
                return AboutFragment.newInstance();
        }
        return null;//Should never happen
    }

    public void onFragmentAttached(int number) {
        switch (number) {
            case ArtiContent.HOME:
                mTitle = getString(R.string.title_home);
                break;
            case ArtiContent.ACCOUNT:
                mTitle = getString(R.string.title_account);
                break;
            case ArtiContent.ABOUT:
                mTitle = getString(R.string.title_about);
                break;
        }
    }

    public void onLogout() {
        // User clicked to log out.
        ParseUser.logOut();

        promptForLogin();
    }

    private void promptForLogin() {
        ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                ArtiActivity.this);
        startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                showFridge();
            }
        }
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (mReturningWithResult) {
//            // Commit your transactions here.
//        }
//        // Reset the boolean flag back to false for next time.
//        mReturningWithResult = false;
//    }

    private void showFridge() {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        Log.d(TAG, "Listing devices for " + currentUser.getUsername());
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

                        ParseQuery<Device> query = ParseQuery.getQuery(Device.class)
                                .whereEqualTo("deviceId", deviceId)
                                .addDescendingOrder("updatedAt");
                        query.findInBackground(new FindCallback<Device>() {
                            public void done(List<Device> objects, ParseException e) {
                                if (e == null && !objects.isEmpty()) {
                                    Device deviceState = objects.get(0);
                                    ArtiContent.addDevice(deviceState);

                                    Log.d(TAG, "Found " + deviceState + " for device id " + deviceId);
                                } else {
                                    Device deviceState = new Device();
                                    deviceState.setName(deviceId);
                                    ArtiContent.addDevice(deviceState);
                                    Log.d(TAG, "Could not load device state for " + deviceId);
                                }
                                HomeFragment.newInstance().notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "No devices found for " + currentUser.getUsername());
                    // TODO Display a message on screen.
                }
            }
        });

        // Take user to home screen after login.
        mNavigationDrawerFragment.selectItem(ArtiContent.HOME - 1);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.arti, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
