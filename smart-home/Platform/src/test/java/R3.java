package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class R3 {
    /**
     * Test to ensure that if the house is vacant, the door is closed.
     */
    @Test
    public void testHouseVacantThenCloseDoor() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true); // Assume door is initially open
        newState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant

        Map<String, Object> evaluatedState = stateEvaluator.evaluateState(newState, new StringBuffer());

        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE)); // Verify door is closed
    }

    /**
     * Test to ensure that the door remains closed when the house is vacant, 
     * even if there was an attempt to open the door.
     */
    @Test
    public void testCantOpenDoorWhenHouseVacant() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true); // Attempt to set door state as open
        newState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant
        newState.put(IoTValues.DOOR_OPEN, true); // Explicit attempt to open the door

        Map<String, Object> evaluatedState = stateEvaluator.evaluateState(newState, new StringBuffer());

        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE)); // Verify door remains closed
    }

    /**
     * Test to ensure that the door is automatically closed if it was open when the house becomes vacant.
     */
    @Test
    public void testDoorClosesWhenHouseBecomesVacant() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, true); // Door is initially open
        newState.put(IoTValues.PROXIMITY_STATE, false); // Then, the house becomes vacant

        Map<String, Object> evaluatedState = stateEvaluator.evaluateState(newState, new StringBuffer());

        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE)); // Verify door is closed
    }

    /**
     * Test to ensure that if the house is vacant and the door is initially closed, it remains closed even if there is an attempt to open it.
     */
    @Test
    public void testDoorStaysClosedWhenAttemptToOpenWhileHouseVacant() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.DOOR_STATE, false); // Door is initially closed
        newState.put(IoTValues.PROXIMITY_STATE, false); // House is vacant
        newState.put(IoTValues.DOOR_OPEN, true); // Attempt to open the door

        Map<String, Object> evaluatedState = stateEvaluator.evaluateState(newState, new StringBuffer());

        assertFalse((Boolean) evaluatedState.get(IoTValues.DOOR_STATE)); // Verify door remains closed
    }
}