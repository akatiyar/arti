package akatiyar.arti;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseFile;
import com.parse.ParseImageView;

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
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.device_summary, parent, false);

        TextView nameText = (TextView) rowView.findViewById(R.id.d_name);
        TextView batteryText = (TextView) rowView.findViewById(R.id.d_battery);
        TextView tempText = (TextView) rowView.findViewById(R.id.d_temp);
        TextView humidityText = (TextView) rowView.findViewById(R.id.d_humidity);
        final ParseImageView fridgeImage = (ParseImageView) rowView.findViewById(R.id.d_image);

        final String battery = context.getResources().getString(R.string.battery);
        final String temp = context.getResources().getString(R.string.temp);
        final String humidity = context.getResources().getString(R.string.humidity);

        Device device = getItem(position);

        nameText.setText(device.getName());
        batteryText.setText(battery + device.getBattery() + "%");
        tempText.setText(temp + device.getTemperature() + " C");
        humidityText.setText(humidity + device.getHumidity() + "%");

        final ParseFile photoFile = device.getPhotoFile();
        if (photoFile != null) {
            Log.d(TAG, "Loading photo from " + photoFile.getUrl());
            ImageLoader.getInstance().displayImage(photoFile.getUrl(), fridgeImage);
        }

        return rowView;
    }
}
