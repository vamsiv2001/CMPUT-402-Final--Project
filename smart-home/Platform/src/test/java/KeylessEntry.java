package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGenerator.Simple;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class KeylessEntry {

    /**
     * Tests if keyless entry is granted with a recognized phone.
     */
    @Test
    public void testKeylessEntryWithRegisteredPhone() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.GEOFENCE_STATE, true);
        newState.put(IoTValues.DETECTED_CELLPHONES, List.of("known_phone1"));
        newState.put(IoTValues.KNOWN_CELLPHONES, List.of("known_phone1", "known_phone2"));

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        StringBuffer log = new StringBuffer();

        assertFalse((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that the Smart Lock is unlocked

    }

    /**
     * Tests if keyless entry is declined with an unknown phone.
     */
    @Test
    public void testKeylessEntryNotGrantedToUnknownPhone() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.GEOFENCE_STATE, true);
        newState.put(IoTValues.DETECTED_CELLPHONES, List.of("unknown_phone"));
        newState.put(IoTValues.KNOWN_CELLPHONES, List.of("known_phone1", "known_phone2"));
        newState.put(IoTValues.ALARM_STATE, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        StringBuffer log = new StringBuffer();

        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that the Smart Lock is locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE)); // assert that the alarm is active
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE)); // assert that an intruder is detected

    }

    /**
     * Test to verify that an unknown phone is granted keyless entry when an all-clear signal is given,
     * even when the geofence state is active and the alarm is enabled.
     */
    @Test
    public void testKeylessGrantedToUnknownPhoneIfAllClearGiven() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.GEOFENCE_STATE, true);
        newState.put(IoTValues.ALL_CLEAR, true);
        newState.put(IoTValues.DETECTED_CELLPHONES, List.of("unknown_phone"));
        newState.put(IoTValues.KNOWN_CELLPHONES, List.of("known_phone1", "known_phone2"));
        newState.put(IoTValues.ALARM_STATE, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        StringBuffer log = new StringBuffer();

        assertFalse((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that the Smart Lock is unlocked
    }
}
