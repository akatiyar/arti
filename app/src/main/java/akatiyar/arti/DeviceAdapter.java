package akatiyar.arti;

import android.content.Context;
import android.util.Log;
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

import java.util.List;

import akatiyar.arti.model.Device;

/**
 * Created by abhinav on 4/14/15.
 */
public class DeviceAdapter extends ArrayAdapter<Device> {

    private static final String TAG = ArtiActivity.class.getName();

    private final Context context;

    public DeviceAdapter(Context context, int resource, List<Device> devices) {
        super(context, resource, devices);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.device_summary, parent, false);
        TextView batteryText = (TextView) rowView.findViewById(R.id.d_battery);
        ParseImageView fridgeImage = (ParseImageView) rowView.findViewById(R.id.d_image);

        final String battery = context.getResources().getString(R.string.battery);

        Device device = getItem(position);
        String deviceId = device.getName();
        String batteryPercentage = "Unknown";
        batteryPercentage = String.valueOf(device.get("battery"));


        batteryText.setText(deviceId + " " + battery + " " + batteryPercentage + "%");
        ParseFile photoFile = device.getPhotoFile();
        if (photoFile != null) {
            Log.d(TAG, "Loading photo file " + photoFile.getUrl());
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
