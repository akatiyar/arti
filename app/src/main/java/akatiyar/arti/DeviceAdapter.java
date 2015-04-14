package akatiyar.arti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;

/**
 * Created by abhinav on 4/14/15.
 */
public class DeviceAdapter extends ArrayAdapter<ParseObject> {

    private final Context context;

    public DeviceAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.device_state, parent, false);
        TextView batteryText = (TextView) rowView.findViewById(R.id.d_battery);
        ParseImageView fridgeImage = (ParseImageView) rowView.findViewById(R.id.d_image);

        final String battery = context.getResources().getString(R.string.battery);

        ParseObject deviceState = getItem(position);
        String deviceId = String.valueOf(deviceState.get("deviceId"));
        String batteryPercentage = "Unknown";
        if(deviceState.get("battery")!=null) {
            batteryPercentage = String.valueOf(deviceState.get("battery"));
        }

        batteryText.setText(deviceId + " " + battery + " " + batteryPercentage + "%");
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

        return rowView;
    }
}
