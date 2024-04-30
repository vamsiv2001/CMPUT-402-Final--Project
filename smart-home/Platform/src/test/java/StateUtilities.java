package tartan.smarthome.resources;

import tartan.smarthome.resources.iotcontroller.IoTValues;

import java.util.Hashtable;
import java.util.Map;

public class StateUtilities {
  public static Map<String, Object> createDefaultState() {
        Map<String, Object> newState = new Hashtable<>();

        newState.put(IoTValues.PROXIMITY_STATE, false);
        newState.put(IoTValues.AWAY_TIMER, false);
        newState.put(IoTValues.ALARM_STATE, false);
        newState.put(IoTValues.ALARM_ACTIVE, false);
        newState.put(IoTValues.ALARM_PASSCODE, "123");
        newState.put(IoTValues.EXPERIMENT_GROUP, "1");
        newState.put(IoTValues.GIVEN_PASSCODE, "123");
        newState.put(IoTValues.DOOR_STATE, false);
        newState.put(IoTValues.SMART_LOCK_STATE, false);
        newState.put(IoTValues.INTRUDER_STATE, false);
        newState.put(IoTValues.BROKEN_WINDOW, false);
        newState.put(IoTValues.LIGHT_STATE, false);
        newState.put(IoTValues.HVAC_MODE, "Heater");
        newState.put(IoTValues.TEMP_READING, 25);
        newState.put(IoTValues.TARGET_TEMP, 28);
        newState.put(IoTValues.NIGHT_START, 22);
        newState.put(IoTValues.NIGHT_END, 06);
        newState.put(IoTValues.HUMIDIFIER_STATE, false);
        newState.put(IoTValues.SMART_LOCK_PASSWORD, "123");
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, "123");
        newState.put(IoTValues.GEOFENCE_STATE, false);
        newState.put(IoTValues.ALL_CLEAR, false);
        newState.put(IoTValues.NIGHT_STATE, false);

        return newState;
    }
}
