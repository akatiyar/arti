package akatiyar.arti.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abhinav on 4/29/15.
 */
public class ArtiContent {

    public static final int HOME = 1;
    public static final int ACCOUNT = 2;
    public static final int ABOUT = 3;

    public static final List<Device> DEVICES = new ArrayList<>();

    private static Map<String, Device> deviceMap = new HashMap<>();

    public static synchronized void addDevice(Device device){
        if(device==null) return;
        String name = device.getName();

        Device oldRecord = deviceMap.get(name);
        if(oldRecord == null){
            deviceMap.put(name, device);
            DEVICES.add(device);
        } else {
            oldRecord.copyFrom(device);
        }
    }
}
