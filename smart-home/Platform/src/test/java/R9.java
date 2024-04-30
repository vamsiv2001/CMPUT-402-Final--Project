package tartan.smarthome.resources;

import org.junit.jupiter.api.Test;

import tartan.smarthome.resources.StateUtilities;
import tartan.smarthome.resources.StaticTartanStateEvaluator;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import java.lang.StringBuffer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class R9 {
  /**
   * This test makes sure the correct passcode is required to disable a sounding
   * alarm.
  */
  @Test
  public void correctPasscodeRequiredToDisableAlarm() {
    // Initialize state evaluator
    StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();

    // Create a test state
    Map<String, Object> newState = StateUtilities.createDefaultState();

    newState.put(IoTValues.ALARM_ACTIVE, true);
    newState.put(IoTValues.ALARM_STATE, false);
    newState.put(IoTValues.DOOR_STATE, false);
    newState.put(IoTValues.PROXIMITY_STATE, true);

    newState.put(IoTValues.ALARM_PASSCODE, "1234");
    newState.put(IoTValues.GIVEN_PASSCODE, "1234");
    
    // Evaluate the state
    Map<String, Object> evaluation = stateEvaluator.evaluateState(newState, new StringBuffer());

    // Make sure the alarm is disabled after a correct password is entered
    assertFalse((Boolean) evaluation.get((IoTValues.ALARM_ACTIVE)));
    assertFalse((Boolean) evaluation.get((IoTValues.ALARM_STATE)));
  }


  /**
   * This test makes sure the correct passcode is required to disable a sounding
   * alarm.
  */
  @Test
  public void incorrectPasscodeLengthAlarmShouldBeEnabled() {
    // Initialize state evaluator
    StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();

    // Create a test state
    Map<String, Object> newState = StateUtilities.createDefaultState();

    newState.put(IoTValues.ALARM_ACTIVE, true);
    newState.put(IoTValues.ALARM_STATE, false);
    newState.put(IoTValues.DOOR_STATE, false);
    newState.put(IoTValues.PROXIMITY_STATE, true);
    
    // given passcode is shorter than alarm passcode
    newState.put(IoTValues.ALARM_PASSCODE, "1234");
    newState.put(IoTValues.GIVEN_PASSCODE, "123");

    // Evaluate the state
    Map<String, Object> evaluation = stateEvaluator.evaluateState(newState, new StringBuffer());
    assertTrue((Boolean) evaluation.get((IoTValues.ALARM_ACTIVE)));
    assertTrue((Boolean) evaluation.get((IoTValues.ALARM_STATE)));
  }


  /**
   * given passcode should be same as the alarm passcode (Alphabets)
   * alarm.
  */
  @Test
  public void incorrectPasscodeAlarmShouldBeEnabled() {
    // Initialize state evaluator
    StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();

    // Create a test state
    Map<String, Object> newState = StateUtilities.createDefaultState();

    newState.put(IoTValues.ALARM_ACTIVE, true);
    newState.put(IoTValues.ALARM_STATE, false);
    newState.put(IoTValues.DOOR_STATE, false);
    newState.put(IoTValues.PROXIMITY_STATE, true);

    // given passcode is different from alarm passcode
    newState.put(IoTValues.ALARM_PASSCODE, "LockA");
    newState.put(IoTValues.GIVEN_PASSCODE, "LoCkB");

    Map<String, Object> evaluation = stateEvaluator.evaluateState(newState, new StringBuffer());

    assertTrue((Boolean) evaluation.get((IoTValues.ALARM_ACTIVE)));
    assertTrue((Boolean) evaluation.get((IoTValues.ALARM_STATE)));
  }

  /**
   * given passcode to disable a sounding a sounding alarm
   * is longer than the alarm passcode
  */
  @Test
  public void givenPasscodeIsLongerThanAlarmPasscode() {
    // Initialize state evaluator
    StaticTartanStateEvaluator stateEvaluator = new StaticTartanStateEvaluator();

    // Create a test state
    Map<String, Object> newState = StateUtilities.createDefaultState();

    newState.put(IoTValues.ALARM_ACTIVE, true);
    newState.put(IoTValues.ALARM_STATE, false);
    newState.put(IoTValues.DOOR_STATE, false);
    newState.put(IoTValues.PROXIMITY_STATE, true);

    //Given Passcode is longer than alarm Passcode 
    newState.put(IoTValues.ALARM_PASSCODE, "1234");
    newState.put(IoTValues.GIVEN_PASSCODE, "122444"); 

    // Evaluate the state
    Map<String, Object> evaluation = stateEvaluator.evaluateState(newState, new StringBuffer());
    assertTrue((Boolean) evaluation.get(IoTValues.ALARM_ACTIVE));
    assertTrue((Boolean) evaluation.get(IoTValues.ALARM_STATE));
  }

}
