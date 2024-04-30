package tartan.smarthome.resources.iotcontroller;

/**
 * Constant values used in the house
 *
 * Project: LG Exec Ed Program
 * Copyright: Copyright (c) 2015 Jeffrey S. Gennari
 * Versions:
 * 1.0 November 2015 - initial version
 */
public abstract class IoTValues {

    // state readings
    public static final String TEMP_READING = "TR";
    public static final String HUMIDITY_READING = "HR";
    public static final String HUMIDIFIER_STATE = "HUS";
    public static final String DOOR_STATE = "DS";
    public static final String SMART_LOCK_STATE = "SLS";
    public static final String LIGHT_STATE = "LS";
    public static final String PROXIMITY_STATE = "PS";
    public static final String BROKEN_WINDOW = "BW";
    public static final String INTRUDER_STATE = "IS";
    public static final String ALARM_STATE = "AS";
    public static final String HVAC_MODE = "HM";
    public static final String ALARM_ACTIVE = "AA";
    public static final String HEATER_STATE = "HES";
    public static final String CHILLER_STATE = "CHS";
    public static final String GEOFENCE_STATE = "GFS";
    public static final String DETECTED_CELLPHONES = "DC";
    public static final String KNOWN_CELLPHONES = "KC";
    public static final String ALL_CLEAR = "AC";
    public static final String NIGHT_STATE = "NST";
   
    // protocol control values
    public static final String PARAM_DELIM = ";";
    public static final String MSG_DELIM = ":";
    public static final String PARAM_EQ = "=";
    public static final String MSG_END = ".";

    // target temperature
    public static final String TARGET_TEMP = "TT";

    public static final String NIGHT_START = "NS";
    public static final String NIGHT_END = "NE";

    public static final String DOOR_CLOSE = "0";
    public static final String DOOR_OPEN = "1";

    public static final String LOCK_UNLOCKED = "0";
    public static final String LOCK_LOCKED = "1";

    public static final String LIGHT_ON = "1";
    public static final String LIGHT_OFF = "0";

    public static final String HUMIDIFIER_ON = "1";
    public static final String HUMIDIFIER_OFF = "0";

    public static final String ALARM_ENABLED  = "1";
    public static final String ALARM_DISABLED = "0";

    public static final String ALARM_ON = "1";
    public static final String ALARM_OFF = "0";

    public static final String HEATER_ON = "1";
    public static final String HEATER_OFF = "0";

    public static final String CHILLER_ON = "1";
    public static final String CHILLER_OFF = "0";

    public static final String GEOFENCE_ON = "1";
    public static final String GEOFENCE_OFF = "0";

    public static final String ALL_CLEAR_ON = "1";
    public static final String ALL_CLEAR_OFF = "0";

    public static final String OK = "OK";

    public static final String ALARM_DELAY = "ALARM_DELAY";
    public static final String ALARM_PASSCODE = "ALARM_PASSCODE";
    public static final String GIVEN_PASSCODE = "GIVEN_PASSCODE";

    public static final String SMART_LOCK_PASSWORD = "SMART_LOCK_PASSWORD";
    public static final String GIVEN_SMART_LOCK_PASSWORD = "GIVEN_SMART_LOCK_PASSWORD";

    public static final String GET_STATE = "GS";
    public static final String SET_STATE = "SS";
    public static final String STATE_UPDATE = "SU";

    public static final String SETTINGS_FILE = "settings.txt";
    public static final String USERS_DB = "users.txt";
	public static final String AWAY_TIMER = "AW";
    public static final String EXPERIMENT_GROUP = "EG";
}
