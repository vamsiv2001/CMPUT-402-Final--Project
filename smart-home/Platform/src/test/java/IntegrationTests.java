package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class IntegrationTests {
    /**
     * Test the interaction between the smart lock, geofence, and intruder detection when an unregistered phone is detected.
     */
    @Test
    public void testSmartLockAndGeofenceWithIntruderDetectionUnregisteredPhone() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.GEOFENCE_STATE, true);
        newState.put(IoTValues.DETECTED_CELLPHONES, List.of("unknown_phone"));
        newState.put(IoTValues.KNOWN_CELLPHONES, List.of("known_phone1", "known_phone2"));

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());

        // assert intruder is detected and door is locked and closed
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test the interaction between the smart lock and geofence with intruder detection when only registered phones are detected.
     */
    @Test
    public void testSmartLockAndGeofenceWithIntruderDetectionOnlyRegisteredPhones() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.GEOFENCE_STATE, true);
        newState.put(IoTValues.ALL_CLEAR, false);
        newState.put(IoTValues.DETECTED_CELLPHONES, List.of("known_phone1"));
        newState.put(IoTValues.KNOWN_CELLPHONES, List.of("known_phone1", "known_phone2"));

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert intruder is not detected and door is unlocked
        assertFalse((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
        assertFalse((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test that the system detects an intruder and activates the alarm when windows are broken and the system is armed.
     */
    @Test
    public void testIntruderDetectionAndAlarmWhenWindowsBroken() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.ALARM_STATE, true);
        newState.put(IoTValues.BROKEN_WINDOW, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());

        // assert intruder is detected, alarm is active and door is closed and locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE));
        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to detect an intruder if the door is open, no one is home, and proximity is detected.
     */
    @Test
    public void testIntruderIsDetectedIfDoorIsOpenNoOneIsHomeAndProximityDetected() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true);
        newState.put(IoTValues.PROXIMITY_STATE, false);
        newState.put(IoTValues.ALARM_STATE, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert intruder is detected, alarm is active and door is closed and locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE));
        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to detect an intruder if the door is open, no one is home, and the lock is on.
     */
    @Test
    public void testIntruderIsDetectedIfDoorIsOpenAndNoOneIsHomeAndLockIsOn() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true);
        newState.put(IoTValues.SMART_LOCK_STATE, true);
        newState.put(IoTValues.ALARM_STATE, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert intruder is detected, alarm is active and door is closed and locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE));
        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to detect an intruder if the door is closed and the house is suddenly occupied.
     */
    @Test
    public void testIntruderIsDetectedIfDoorIsClosedAndHouseIsSuddenlyOccupied() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, false);
        newState.put(IoTValues.PROXIMITY_STATE, true);
        newState.put(IoTValues.ALARM_STATE, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert intruder is detected, alarm is active and door is closed and locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE));
        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE));
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test that the smart lock engages when the house is empty and the away timer is activated.
     */
    @Test
    public void testSmartLockEngagesWhenAwayTimerActivated() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.PROXIMITY_STATE, false);
        newState.put(IoTValues.AWAY_TIMER, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert smart lock is locked when away timer is activated
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to detect an intruder if the door is open, someone is home, and the lock is on.
     */
    @Test
    public void testIntruderIsDetectedIfDoorIsOpenAndNoOneIsHomeAndLockIsOn2() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true);
        newState.put(IoTValues.PROXIMITY_STATE, true);
        newState.put(IoTValues.SMART_LOCK_STATE, true);
        newState.put(IoTValues.ALARM_STATE, true);


        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert intruder is detected
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
    }

    /**
     * Test to confirm that the intruder situation is handled when an all-clear signal is given.
     */
    @Test
    public void ifAllClearGivenIntruderIsHandled() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.ALL_CLEAR, true);
        newState.put(IoTValues.INTRUDER_STATE, true);

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert intruder situation has been handled
        assertFalse((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE));
    }
}
