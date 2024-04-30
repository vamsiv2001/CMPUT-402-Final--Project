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

public class NightLock {
    /**
     * Test if the door will automatically lock at night.
     * 
     * @throws ParseException
     */
    @Test
    public void automaticallyLockDoorAtNight() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, false);
        newState.put(IoTValues.NIGHT_START, 22);
        newState.put(IoTValues.NIGHT_END, 6);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("23:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());

        assertTrue((Boolean) evaluatedState.get(IoTValues.NIGHT_STATE)); // assert that it is night
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that the Smart Lock is locked
    }

    /**
     * Test if the door will stay locked at night.
     * 
     * @throws ParseException
     */
    @Test
    public void keepDoorLockedAtNight() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, false);
        newState.put(IoTValues.NIGHT_START, 22);
        newState.put(IoTValues.NIGHT_END, 6);
        newState.put(IoTValues.SMART_LOCK_STATE, false);
        newState.put(IoTValues.SMART_LOCK_PASSWORD, "outkast");
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, "outkast");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("23:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());

        assertTrue((Boolean) evaluatedState.get(IoTValues.NIGHT_STATE)); // assert that it is night
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE)); // assert that the Smart Lock is not
                                                                              // unlocked if it is night
    }

    /**
     * Test if the night state is false if current time is not within night lock
     * times.
     * 
     * @throws ParseException
     */
    @Test
    public void ensureNightStateIsNotOnAfterTimingPassed() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, true);
        newState.put(IoTValues.NIGHT_START, 22);
        newState.put(IoTValues.NIGHT_END, 6);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("07:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());

        assertFalse((Boolean) evaluatedState.get(IoTValues.NIGHT_STATE)); // assert that it is not night
    }

}
