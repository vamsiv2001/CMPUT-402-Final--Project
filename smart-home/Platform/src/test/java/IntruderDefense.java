package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class IntruderDefense {
    /**
     * Test to ensure the alarm sounds for forced entry, detecting an intruder and sending a message to the access panel.
     */
    @Test
    public void soundAlarmForForcedEntry() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.ALARM_STATE, true); // alarm is enabled
        newState.put(IoTValues.SMART_LOCK_STATE, true); // lock the door
        newState.put(IoTValues.DOOR_STATE, true); // open the door

        StringBuffer log = new StringBuffer();
        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, log);

        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE)); // assert that the alarm goes off when door is
        // forced open
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE)); // assert that the house knows there is an
        // intruder
        assertTrue(log.toString().contains("possible intruder detected")); // assert that a message is sent to the
        // access panel
    }

    /**
     * Test to verify the alarm sounds for a broken window when the house is unoccupied, locking the door and detecting an intruder.
     */
    @Test
    public void soundAlarmForBrokenWindowWhenUnoccupied() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.BROKEN_WINDOW, true); // break a window
        newState.put(IoTValues.ALARM_STATE, true); // alarm is enabled

        StringBuffer log = new StringBuffer();
        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, log);

        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE)); // assert that the alarm goes off
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that door is locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE)); // assert that the house knows there is an
        // intruder
        assertTrue(log.toString().contains("possible intruder detected")); // assert that a message is sent to the
        // access panel
    }

    /**
     * Test to confirm the alarm sounds for unexpected proximity in the house, ensuring the door is locked and an intruder is detected.
     */
    @Test
    public void soundAlarmForUnexpectedProximity() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, false); // close the door
        newState.put(IoTValues.PROXIMITY_STATE, true); // somebody is in the house
        newState.put(IoTValues.ALARM_STATE, true); // alarm is enabled

        StringBuffer log = new StringBuffer();
        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, log);

        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE)); // assert that the alarm goes off
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that door is locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE)); // assert that the house knows there is an
        // intruder
        assertTrue(log.toString().contains("possible intruder detected")); // assert that a message is sent to the
        // access panel
    }

    /**
     * Test to check if the alarm sounds when the door is unexpectedly opened while no one is home, locking the door and detecting an intruder.
     */
    @Test
    public void doorUnexpectedlyOpened() {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true); // open the door
        newState.put(IoTValues.PROXIMITY_STATE, false); // nobody is home
        newState.put(IoTValues.ALARM_STATE, true); // alarm is enabled

        StringBuffer log = new StringBuffer();
        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, log);

        assertTrue((Boolean) evaluatedState.get(IoTValues.ALARM_ACTIVE)); // assert that the alarm goes off
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that door is locked
        assertTrue((Boolean) evaluatedState.get(IoTValues.INTRUDER_STATE)); // assert that the house knows there is an
        // intruder
        assertTrue(log.toString().contains("possible intruder detected")); // assert that a message is sent to the
        // access panel
    }
}
