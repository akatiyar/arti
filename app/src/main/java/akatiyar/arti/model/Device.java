package akatiyar.arti.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

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
    public void setName(String name){
        put("deviceId", name);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }
    public void setPhotoFile(ParseFile file){
        put("photo", file);
    }

    public int getBattery() {
        return getInt("battery");
    }
    public void setBattery(int battery){
        put("battery", battery);
    }

    public int getTemperature() {
        return getInt("temp");
    }
    public void setTemperature(int temp){
        put("temp", temp);
    }

    public int getHumidity() {
        return getInt("humidity");
    }
    public void setHumidity(int humidity){
        put("humidity", humidity);
    }

    @Override
    public String toString(){
        return getName(); // for test only.
    }
}
