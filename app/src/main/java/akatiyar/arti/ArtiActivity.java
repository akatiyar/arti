package akatiyar.arti;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class ArtiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arti);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        final TextView batteryText = (TextView) findViewById(R.id.battery);
        final String battery = getResources().getString(R.string.battery);
        final ParseImageView fridgeImage = (ParseImageView) findViewById(R.id.fridge);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DeviceState")
                .whereEqualTo("deviceId", "fd1234cam2")
                .addDescendingOrder("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !objects.isEmpty()) {
                    ParseObject deviceState = objects.get(0);
                    String batteryPercentage = String.valueOf(deviceState.get("battery"));
                    batteryText.setText(battery + " " + batteryPercentage + "%");
                    ParseFile photoFile = deviceState.getParseFile("photo");
                    if (photoFile != null) {
                        fridgeImage.setParseFile(photoFile);
                        fridgeImage.loadInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                // nothing to do
                            }
                        });
                    }
                } else {
                    batteryText.setText(battery + " NA");
                }
            }
        });

    }

}
