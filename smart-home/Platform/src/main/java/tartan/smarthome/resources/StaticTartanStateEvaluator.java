package tartan.smarthome.resources;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import java.util.List;
import java.util.ArrayList;

public class StaticTartanStateEvaluator implements TartanStateEvaluator {

    private String formatLogEntry(String entry) {
        Long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        return "[" + sdf.format(new Date(timeStamp)) + "]: " + entry + "\n";
    }

    private Long fixedTime = null;

    public void setFixedTime(Long fixedTime) {
        this.fixedTime = fixedTime;
    }

    private long getCurrentTimeMillis() {
        if (fixedTime != null) {
            return fixedTime;
        } else {
            return System.currentTimeMillis();
        }
    }

    /**
     * Ensure the requested state is permitted. This method checks each state
     * variable to ensure that the house remains in a consistent state.
     *
     * @param state The new state to evaluate
     * @param log   The log of state evaluations
     * @return The evaluated state
     */
    @Override
    public Map<String, Object> evaluateState(Map<String, Object> inState, StringBuffer log) {

        // These are the state variables that reflect the current configuration of the
        // house

        Integer tempReading = null; // the current temperature
        Integer targetTempSetting = null; // the user-desired temperature setting
        Integer humidityReading = null; // the current humidity
        Boolean doorState = null; // the state of the door (true if open, false if closed)
        Boolean smartLockState = null; // the state of the smartLock (true if locked, false if unlocked)
        String smartLockPassword = null; //
        String givenSmartLockPassword = null;
        Boolean lightState = null; // the state of the light (true if on, false if off)
        Boolean proximityState = null; // the state of the proximity sensor (true if house occupied, false if vacant)
        Boolean brokenWindow = null; // the state of the window sensor (true if a window is broken, false if all
                                     // windows are intact)
        Boolean intruderState = null; // the state of the intruder defense system (true if intruder detected, false if
                                      // no intruder detected)
        Boolean alarmState = null; // the alarm state (true if enabled, false if disabled)
        Boolean humidifierState = null; // the humidifier state (true if on, false if off)
        Boolean heaterOnState = null; // the heater state (true if on, false if off)
        Boolean chillerOnState = null; // the chiller state (true if on, false if off)
        Boolean alarmActiveState = null; // the alarm active state (true if alarm sounding, false if alarm not sounding)
        Boolean awayTimerState = false; // assume that the away timer did not trigger this evaluation
        Boolean awayTimerAlreadySet = false;
        String alarmPassCode = null;
        String experimentGroup = null;
        String hvacSetting = null; // the HVAC mode setting, either Heater or Chiller
        String givenPassCode = "";
        String nightStartTime = null;
        String nightEndTime = null;
        Boolean nightState = null;
        Boolean geofenceState = null;
        List<String> detectedCellphones = new ArrayList<String>();
        List<String> knownCellphones = new ArrayList<String>();
        Boolean allClear = null;

        Set<String> keys = inState.keySet();
        for (String key : keys) {

            if (key.equals(IoTValues.TEMP_READING)) {
                tempReading = (Integer) inState.get(key);
            } else if (key.equals(IoTValues.HUMIDITY_READING)) {
                humidityReading = (Integer) inState.get(key);
            } else if (key.equals(IoTValues.TARGET_TEMP)) {
                targetTempSetting = (Integer) inState.get(key);
            } else if (key.equals(IoTValues.HUMIDIFIER_STATE)) {
                humidifierState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.DOOR_STATE)) {
                doorState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.SMART_LOCK_STATE)) {
                smartLockState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.SMART_LOCK_PASSWORD)) {
                smartLockPassword = (String) inState.get(key);
            } else if (key.equals(IoTValues.GIVEN_SMART_LOCK_PASSWORD)) {
                givenSmartLockPassword = (String) inState.get(key);
            } else if (key.equals(IoTValues.LIGHT_STATE)) {
                lightState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.PROXIMITY_STATE)) {
                proximityState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.BROKEN_WINDOW)) {
                brokenWindow = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.INTRUDER_STATE)) {
                intruderState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.ALARM_STATE)) {
                alarmState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.HEATER_STATE)) {
                heaterOnState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.CHILLER_STATE)) {
                chillerOnState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.HVAC_MODE)) {
                hvacSetting = (String) inState.get(key);
            } else if (key.equals(IoTValues.ALARM_PASSCODE)) {
                alarmPassCode = (String) inState.get(key);
            } else if (key.equals(IoTValues.EXPERIMENT_GROUP)) {
                experimentGroup = (String) inState.get(key);
            } else if (key.equals(IoTValues.GIVEN_PASSCODE)) {
                givenPassCode = (String) inState.get(key);
            } else if (key.equals(IoTValues.AWAY_TIMER)) {
                // This is a hack!
                awayTimerState = (Boolean) inState.getOrDefault(key, false);
            } else if (key.equals(IoTValues.ALARM_ACTIVE)) {
                alarmActiveState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.NIGHT_START)) {
                nightStartTime = inState.get(key).toString();
            } else if (key.equals(IoTValues.NIGHT_END)) {
                nightEndTime = inState.get(key).toString();
            } else if (key.equals(IoTValues.GEOFENCE_STATE)) {
                geofenceState = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.DETECTED_CELLPHONES)) {
                detectedCellphones = (List<String>) inState.get(key);
            } else if (key.equals(IoTValues.KNOWN_CELLPHONES)) {
                knownCellphones = (List<String>) inState.get(key);
            } else if (key.equals(IoTValues.ALL_CLEAR)) {
                allClear = (Boolean) inState.get(key);
            } else if (key.equals(IoTValues.NIGHT_STATE)) {
                nightState = (Boolean) inState.get(key);
            }
        }

        Long timeStamp = getCurrentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date(timeStamp));

        int currentHour = Integer.parseInt(currentTime.split(":")[0]);
        int nightStartHour = Integer.parseInt(nightStartTime.split(":")[0]);
        int nightEndHour = Integer.parseInt(nightEndTime.split(":")[0]);

        if (nightStartHour <= nightEndHour) {
            if (currentHour >= nightStartHour && currentHour < nightEndHour) {
                log.append(formatLogEntry("Night mode on"));
                nightState = true;
            }
        } else if (currentHour >= nightStartHour || currentHour < nightEndHour) {
            log.append(formatLogEntry("Night mode on"));
            nightState = true;
        } else {
            log.append(formatLogEntry("Night mode off"));
            nightState = false;
        }

        if (nightState) {
            log.append(formatLogEntry("Night mode on"));
            smartLockState = true;
        }

        if (smartLockState) {
            // lock smart lock
            log.append(formatLogEntry("Smartlock: locked"));
            smartLockState = true;
        } else if (!smartLockState) {
            // unlock smart lock
            if (!givenSmartLockPassword.isEmpty() && givenSmartLockPassword.equals(smartLockPassword)) {
                log.append(formatLogEntry("Smartlock: unlocked"));
                smartLockState = false;
            } else if (givenSmartLockPassword.isEmpty()) {
                log.append(formatLogEntry("Possible Intruder detected"));
                smartLockState = true;
            } else {
                log.append(formatLogEntry("Smartlock: Incorrect or no password was provided"));
                smartLockState = true;
            }
        }

        if (lightState == true) {
            // The light was activated
            if (!proximityState) {
                log.append(formatLogEntry("Cannot turn on light because user not home"));
                lightState = false;
            } else {
                log.append(formatLogEntry("Light on"));
            }
        }

        // The door is now open
        if (doorState) {
            if (!proximityState && alarmState) {
                // door open and no one home and the alarm is set - sound alarm, close the door,
                // and lock the door
                log.append(formatLogEntry("possible intruder detected"));
                alarmActiveState = true;
                intruderState = true;
            }
            // if forced entry
            else if (smartLockState && alarmState) {
                log.append(formatLogEntry("possible intruder detected"));
                alarmActiveState = true;
                intruderState = true;
            }
            // House vacant, close the door
            else if (!proximityState) {
                // close the door
                doorState = false;
                log.append(formatLogEntry("Closed door because house vacant"));
            } else {
                log.append(formatLogEntry("Door open"));
            }

            // The door is open the alarm is to be set and somebody is home - this is not
            // allowed so discard the processStateUpdate
        }
        // The door is now closed
        else if (!doorState) {
            // the door is closed - if the house is suddenly occupied this is a break-in,
            // sound the alarm and lock the door
            if (alarmState && proximityState) {
                log.append(formatLogEntry("possible intruder detected"));
                alarmActiveState = true;
                intruderState = true;
            } else {
                log.append(formatLogEntry("Closed door"));
            }
        }

        // Auto lock the house
        if (awayTimerState == true) {
            lightState = false;
            doorState = false;
            smartLockState = true;
            alarmState = true;
            awayTimerState = false;
        }
        // if windows are broken when nobody is home
        if (brokenWindow && alarmState) {
            log.append(formatLogEntry("possible intruder detected"));
            alarmActiveState = true;
            intruderState = true;
        }

        if (geofenceState) {
            for (String phone : detectedCellphones) {
                if (!knownCellphones.contains(phone) && (allClear == null || allClear == false)) {
                    log.append(formatLogEntry("Possible intruder detected"));
                    alarmActiveState = true;
                    intruderState = true;
                }
            }
            log.append(formatLogEntry("Smartlock: unlocked"));
            smartLockState = false;
        }

        // the user has arrived
        if (proximityState) {
            log.append(formatLogEntry("House is occupied"));
            // if the alarm has been disabled, then turn on the light for the user

            if (!lightState && !alarmState) {
                lightState = true;
                log.append(formatLogEntry("Turning on light"));
            }

        }

        if (intruderState) {
            log.append(formatLogEntry("Smartlock: locked"));
            doorState = false;
            smartLockState = true;
            geofenceState = false;
        }

        if (allClear) {
            log.append(formatLogEntry("All clear"));
            intruderState = false;
        }

        // set the alarm
        if (alarmState) {
            log.append(formatLogEntry("Alarm enabled"));
        } else if (!alarmState) { // attempt to disable alarm

            if (!proximityState) {
                alarmState = true;

                log.append(formatLogEntry("Cannot disable the alarm, house is empty"));
            }

            if (alarmActiveState) {
                if (givenPassCode.length() > 0 && givenPassCode.compareTo(alarmPassCode) < 0) {
                    log.append(formatLogEntry("Cannot disable alarm, invalid passcode given"));
                    alarmState = true;

                } else {
                    log.append(formatLogEntry("Correct passcode entered, disabled alarm"));
                    alarmActiveState = false;
                }
            }
        }

        if (!alarmState) {
            log.append(formatLogEntry("Alarm disabled"));
        }

        if (!alarmState) { // alarm disabled
            alarmActiveState = false;
        }

        // determine if the alarm should sound. There are two cases
        // 1. the door is opened when no one is home
        // 2. the house is suddenly occupied
        try {
            if ((alarmState && !doorState && proximityState) || (alarmState && doorState && !proximityState)) {
                log.append(formatLogEntry("Activating alarm"));
                alarmActiveState = true;
            }
        } catch (NullPointerException npe) {
            // Not enough information to evaluate alarm
            log.append(formatLogEntry("Warning: Not enough information to evaluate alarm"));
        }

        // Is the heater needed?
        if (tempReading < targetTempSetting) {
            log.append(formatLogEntry("Turning on heater, target temperature = " + targetTempSetting
                    + "F, current temperature = " + tempReading + "F"));
            heaterOnState = true;

            // Heater already on
        } else {
            // Heater not needed
            heaterOnState = false;
        }

        if (tempReading > targetTempSetting) {
            // Is the heater needed?
            if (chillerOnState != null) {
                if (!chillerOnState) {
                    log.append(formatLogEntry("Turning on air conditioner target temperature = " + targetTempSetting
                            + "F, current temperature = " + tempReading + "F"));
                    chillerOnState = true;
                } // AC already on
            }
        }
        // AC not needed
        else {
            chillerOnState = false;
        }

        if (chillerOnState) {
            hvacSetting = "Chiller";
        } else if (heaterOnState) {
            hvacSetting = "Heater";
        }
        // manage the HVAC control

        if (hvacSetting.equals("Heater")) {

            if (chillerOnState == true) {
                log.append(formatLogEntry("Turning off air conditioner"));
            }

            chillerOnState = false; // can't run AC
            humidifierState = false; // can't run dehumidifier with heater
        }

        if (hvacSetting.equals("Chiller")) {

            if (heaterOnState == true) {
                log.append(formatLogEntry("Turning off heater"));
            }

            heaterOnState = false; // can't run heater when the A/C is on
        }

        if (humidifierState && hvacSetting.equals("Chiller")) {
            log.append(formatLogEntry("Enabled Dehumidifier"));
        } else {
            log.append(formatLogEntry("Automatically disabled dehumidifier when running heater"));
            humidifierState = false;
        }

        Map<String, Object> newState = new Hashtable<>();
        newState.put(IoTValues.DOOR_STATE, doorState);
        newState.put(IoTValues.SMART_LOCK_STATE, smartLockState);
        newState.put(IoTValues.AWAY_TIMER, awayTimerState);
        newState.put(IoTValues.LIGHT_STATE, lightState);
        newState.put(IoTValues.PROXIMITY_STATE, proximityState);
        newState.put(IoTValues.BROKEN_WINDOW, brokenWindow);
        newState.put(IoTValues.INTRUDER_STATE, intruderState);
        newState.put(IoTValues.ALARM_STATE, alarmState);
        newState.put(IoTValues.HUMIDIFIER_STATE, humidifierState);
        newState.put(IoTValues.HEATER_STATE, heaterOnState);
        newState.put(IoTValues.CHILLER_STATE, chillerOnState);
        newState.put(IoTValues.ALARM_ACTIVE, alarmActiveState);
        newState.put(IoTValues.HVAC_MODE, hvacSetting);
        newState.put(IoTValues.ALARM_PASSCODE, alarmPassCode);
        newState.put(IoTValues.EXPERIMENT_GROUP, experimentGroup);
        newState.put(IoTValues.GIVEN_PASSCODE, givenPassCode);
        newState.put(IoTValues.SMART_LOCK_PASSWORD, smartLockPassword);
        newState.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, givenSmartLockPassword);
        newState.put(IoTValues.GEOFENCE_STATE, geofenceState);
        newState.put(IoTValues.ALL_CLEAR, allClear);
        newState.put(IoTValues.NIGHT_STATE, nightState);

        return newState;
    }
}