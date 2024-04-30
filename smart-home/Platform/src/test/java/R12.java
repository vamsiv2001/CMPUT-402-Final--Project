package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class R12 {
    StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();

    /**
     * Verifies that the dehumidifier turns off when the heater is turned on.
     */
    @Test
    public void testHeaterOnDehumidifierOff() {
        Map<String, Object> testState = StateUtilities.createDefaultState();
        testState.put(IoTValues.HEATER_STATE, true);
        testState.put(IoTValues.HUMIDIFIER_STATE, true); // Dehumidifier is initially on

        Map<String, Object> evaluation = stateEvaluator.evaluateState(testState, new StringBuffer());
        assertTrue((Boolean) evaluation.get(IoTValues.HEATER_STATE));
        assertFalse((Boolean) evaluation.get(IoTValues.HUMIDIFIER_STATE));
    }

    /**
     * When switching HVAC mode to heater, ensures the dehumidifier is turned off.
     */
    @Test
    public void testSwitchToHeaterModeTurnsDehumidifierOff() {
        Map<String, Object> testState = StateUtilities.createDefaultState();
        testState.put(IoTValues.HUMIDIFIER_STATE, true); // Dehumidifier is initially on
        testState.put(IoTValues.HVAC_MODE, "Heater"); // Switch to heater mode

        Map<String, Object> evaluation = stateEvaluator.evaluateState(testState, new StringBuffer());
        assertFalse((Boolean) evaluation.get(IoTValues.HUMIDIFIER_STATE));
    }

    /**
     * Tests that attempting to turn the dehumidifier on while the heater is on fails.
     */
    @Test
    public void testDehumidifierStaysOffWhenHeaterIsOn() {
        Map<String, Object> testState = StateUtilities.createDefaultState();
        testState.put(IoTValues.HEATER_STATE, true); // Heater is on
        testState.put(IoTValues.HUMIDIFIER_STATE, false); // Attempt to turn dehumidifier on

        Map<String, Object> evaluation = stateEvaluator.evaluateState(testState, new StringBuffer());
        assertTrue((Boolean) evaluation.get(IoTValues.HEATER_STATE));
        assertFalse((Boolean) evaluation.get(IoTValues.HUMIDIFIER_STATE));
    }

    /**
     * Ensures the dehumidifier and chiller turn off when switching to heater mode.
     */
    @Test
    public void testChillerAndDehumidifierOffWhenHeaterOn() {
        Map<String, Object> testState = StateUtilities.createDefaultState();
        testState.put(IoTValues.HUMIDIFIER_STATE, true); // Dehumidifier is initially on
        testState.put(IoTValues.CHILLER_STATE, true); // Chiller is initially on
        testState.put(IoTValues.HVAC_MODE, "Heater"); // Switch to heater mode

        Map<String, Object> evaluation = stateEvaluator.evaluateState(testState, new StringBuffer());
        assertTrue((Boolean) evaluation.get(IoTValues.HEATER_STATE));
        assertFalse((Boolean) evaluation.get(IoTValues.HUMIDIFIER_STATE));
        assertFalse((Boolean) evaluation.get(IoTValues.CHILLER_STATE));
    }

    /**
     * Simulates system startup with both heater and dehumidifier on, ensuring the dehumidifier turns off.
     */
    @Test
    public void testSystemStartupWithHeaterAndDehumidifierOn() {
        Map<String, Object> testState = StateUtilities.createDefaultState();
        testState.put(IoTValues.HEATER_STATE, true); // Heater is on
        testState.put(IoTValues.HUMIDIFIER_STATE, true); // Dehumidifier is also on
        
        Map<String, Object> evaluation = stateEvaluator.evaluateState(testState, new StringBuffer());
        assertTrue((Boolean) evaluation.get(IoTValues.HEATER_STATE), "Heater should remain on after startup.");
        assertFalse((Boolean) evaluation.get(IoTValues.HUMIDIFIER_STATE), "Dehumidifier should turn off if both were on at startup.");
    }
}
