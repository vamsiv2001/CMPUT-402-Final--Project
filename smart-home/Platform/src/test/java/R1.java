package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class R1 {

    // Test when the light is turned on and the house is occupied.
    @Test
    public void whenLightIsTurnedOnAndHouseIsOccupied_LightStaysOn() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        StringBuffer log = new StringBuffer();

        Map<String, Object> newState = StateUtilities.createDefaultState();
        newState.put(IoTValues.PROXIMITY_STATE, true);
        newState.put(IoTValues.LIGHT_STATE, true);

        Map<String, Object> evaluatedState = stateEvaluator.evaluateState(newState, log);

        assertTrue((Boolean) evaluatedState.get(IoTValues.LIGHT_STATE));
        assertTrue(log.toString().contains("Light on"));
    }

    // Test when the light is turned on and the house is vacant.
    @Test
    public void whenLightIsTurnedOnAndHouseIsVacant_LightTurnsOff() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        StringBuffer log = new StringBuffer();

        Map<String, Object> newState = StateUtilities.createDefaultState();
        newState.put(IoTValues.PROXIMITY_STATE, false);
        newState.put(IoTValues.LIGHT_STATE, true);

        Map<String, Object> evaluatedState = stateEvaluator.evaluateState(newState, log);

        assertFalse((Boolean) evaluatedState.get(IoTValues.LIGHT_STATE));
        assertTrue(log.toString().contains("Cannot turn on light because user not home"));
    }

    // Test when the light is already off and the house is occupied.
    @Test
    public void whenLightIsOffAndHouseIsOccupied_LightStaysOff() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        StringBuffer log = new StringBuffer();

        Map<String, Object> newState = StateUtilities.createDefaultState();
        newState.put(IoTValues.PROXIMITY_STATE, true);
        newState.put(IoTValues.LIGHT_STATE, false);

        stateEvaluator.evaluateState(newState, log);

        assertFalse((Boolean) newState.get(IoTValues.LIGHT_STATE));
    }

    // Test when the light is already off and the house is vacant.
    @Test
    public void whenLightIsOffAndHouseIsVacant_LightStaysOff() {
        StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();
        StringBuffer log = new StringBuffer();

        Map<String, Object> newState = StateUtilities.createDefaultState();
        newState.put(IoTValues.PROXIMITY_STATE, false);
        newState.put(IoTValues.LIGHT_STATE, false);

        stateEvaluator.evaluateState(newState, log);

        assertFalse((Boolean) newState.get(IoTValues.LIGHT_STATE));
    }
}
