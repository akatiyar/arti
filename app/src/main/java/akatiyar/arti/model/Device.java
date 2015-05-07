package akatiyar.arti.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Objects;

/**
 * Created by abhinav on 4/28/15.
 */
@ParseClassName("DeviceState")
public class Device extends ParseObject {

    public Device() {
        // A default constructor is required.
    }

    public String getName() {
        return getString("deviceId");
    }

    public void setName(String name) {
        put("deviceId", name);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

    public int getBattery() {
        return getInt("battery");
    }

    public void setBattery(int battery) {
        put("battery", battery);
    }

    public int getTemperature() {
        return getInt("temp");
    }

    public void setTemperature(int temp) {
        put("temp", temp);
    }

    public int getHumidity() {
        return getInt("humidity");
    }

    public void setHumidity(int humidity) {
        put("humidity", humidity);
    }

    public void copyFrom(Device device){
        setName(device.getName());
        setPhotoFile(device.getPhotoFile());
        setBattery(device.getBattery());
        setTemperature(device.getTemperature());
        setHumidity(device.getHumidity());
    }

    @Override
    public String toString() {
        return getName(); // for test only.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        String deviceName = device.getName();

        String thisDeviceName = getName();

        return (thisDeviceName == null) ? (deviceName == null) : thisDeviceName.equals(deviceName);
    }

    @Override
    public int hashCode() {
        String thisDeviceName = getName();
        return thisDeviceName==null?0:thisDeviceName.hashCode();
    }

}
