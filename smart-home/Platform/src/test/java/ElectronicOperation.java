import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;

public class ElectronicOperation {
    /**
     * Test to verify that the smart lock remains locked when no password is given for unlocking.
     */
    @Test
    void unlockOperationRequiresPasswordNoPasswordGiven() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, true);
        newState.put(IoTValues.NIGHT_START, 20);
        newState.put(IoTValues.NIGHT_END, 5);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("07:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        // try to unlock with no password
        newState.put(IoTValues.NIGHT_STATE, false);
        newState.put(IoTValues.SMART_LOCK_STATE, false);
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, "");
        Map<String, Object> lockedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert smart lock is on (locked)
        assertTrue((Boolean) lockedState.get(IoTValues.SMART_LOCK_STATE));

    }

    /**
     * Test to verify that the smart lock unlocks when the correct password is provided.
     */
    @Test
    void unlockOperationRequiresPasswordWithPasswordGiven() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, true);
        newState.put(IoTValues.NIGHT_START, 20);
        newState.put(IoTValues.NIGHT_END, 5);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("07:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        // try to unlock with password
        newState.put(IoTValues.SMART_LOCK_STATE, false);
        newState.put(IoTValues.SMART_LOCK_PASSWORD, "lol");
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, "lol");
        Map<String, Object> unlockedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        System.out.println(unlockedState);
        //assert smart lock is off (unlocked)
        assertFalse((Boolean) unlockedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to confirm that locking the smart lock does not require a password.
     */
    @Test
    public void lockOperationDoesNotRequirePassword() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, true);
        newState.put(IoTValues.NIGHT_START, 20);
        newState.put(IoTValues.NIGHT_END, 5);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("07:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        newState.put(IoTValues.SMART_LOCK_STATE, true);
        Map<String, Object> evaluatedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert smart lock is on (locked)
        assertTrue((Boolean) evaluatedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to check if the smart lock automatically relocks itself when it becomes unlocked without a password.
     */
    @Test
    void smartLockLocksIfItBecomesUnlockedWithoutAPassword() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, true);
        newState.put(IoTValues.NIGHT_START, 20);
        newState.put(IoTValues.NIGHT_END, 5);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("07:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        newState.put(IoTValues.SMART_LOCK_STATE, false);
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, "");
        Map<String, Object> lockedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert smart lock is on (locked)
        assertTrue((Boolean) lockedState.get(IoTValues.SMART_LOCK_STATE));
    }

    /**
     * Test to ensure that the smart lock remains locked when an incorrect password is used for unlocking.
     */
    @Test
    void unlockOperationIsInvalidWithIncorrectPassword() throws ParseException {
        StaticTartanStateEvaluator tartanEvaluator = new StaticTartanStateEvaluator();
        Map<String, Object> newState = StateUtilities.createDefaultState();

        newState.put(IoTValues.NIGHT_STATE, true);
        newState.put(IoTValues.NIGHT_START, 20);
        newState.put(IoTValues.NIGHT_END, 5);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date testTime = sdf.parse("08:00");
        tartanEvaluator.setFixedTime(testTime.getTime());

        // try to unlock with password
        newState.put(IoTValues.SMART_LOCK_STATE, false);
        newState.put(IoTValues.SMART_LOCK_PASSWORD, "outkast");
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, "planB");
        Map<String, Object> unlockedState = tartanEvaluator.evaluateState(newState, new StringBuffer());
        // assert smart lock is on (locked)
        assertTrue((Boolean) unlockedState.get(IoTValues.SMART_LOCK_STATE));
    }
}
