package tartan.smarthome.resources;

import tartan.smarthome.resources.iotcontroller.IoTControlManager;
import tartan.smarthome.resources.iotcontroller.IoTValues;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tartan.smarthome.TartanHomeSettings;
import tartan.smarthome.core.TartanHome;
import tartan.smarthome.core.TartanHomeData;
import tartan.smarthome.core.TartanHomeValues;
import tartan.smarthome.db.HomeDAO;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/***
 * The service layer for the Tartan Home System. Additional inputs and control
 * mechanisms should be accessed here.
 * Currently, this is mainly a proxy to make the existing hardware RESTful.
 */
public class TartanHomeService {

    // the controller for the house
    private IoTControlManager controller;

    // a logging system
    private static final Logger LOGGER = LoggerFactory.getLogger(TartanHomeService.class);

    // Home configuration parameters
    private String name;
    private String address;
    private Integer port;
    private String alarmDelay;
    private String nightStartTime;
    private String nightEndTime;
    private String alarmPasscode;
    private String smartLockPasscode;
    private String targetTemp;
    private String user;
    private String password;

    // status parameters
    private HomeDAO homeDAO;
    private boolean authenticated;

    // historian parameters
    private Boolean logHistory;
    private int historyTimer = 60000;

    // a/b testing
    private String experimentGroup;

    /**
     * Create a new Tartan Home Service
     * 
     * @param dao handle to a database
     */
    public TartanHomeService(HomeDAO dao) {
        this.homeDAO = dao;
    }

    /**
     * Initialize the settings
     * 
     * @param settings     the house settings
     * @param historyTimer historian delay
     */
    public void initializeSettings(TartanHomeSettings settings, Integer historyTimer) {

        this.user = settings.getUser();
        this.password = settings.getPassword();
        this.name = settings.getName();
        this.address = settings.getAddress();
        this.port = settings.getPort();
        this.authenticated = false;

        // User configuration
        this.targetTemp = settings.getTargetTemp();
        this.alarmDelay = settings.getAlarmDelay();
        this.nightStartTime = settings.getNightStartTime();
        this.nightEndTime = settings.getNightEndTime();
        this.alarmPasscode = settings.getAlarmPasscode();
        this.smartLockPasscode = settings.getSmartLockPasscode();

        this.historyTimer = historyTimer * 1000;
        this.logHistory = true;

        // a/b testing
        this.experimentGroup = settings.getExperimentGroup();

        // Create and initialize the controller for this house
        this.controller = new IoTControlManager(user, password, new StaticTartanStateEvaluator(),
                new TartanHomeReportService(this.experimentGroup));

        TartanHome temp = new TartanHome();
        temp.setAlarmDelay(alarmDelay);
        temp.setNightStartTime(nightStartTime);
        temp.setNightEndTime(nightEndTime);
        temp.setExperimentGroup(experimentGroup);

        Map<String, Object> userSettings = new Hashtable<String, Object>();
        userSettings.put(IoTValues.ALARM_DELAY, Integer.parseInt(this.alarmDelay));
        userSettings.put(IoTValues.NIGHT_START, Integer.parseInt(this.nightStartTime));
        userSettings.put(IoTValues.NIGHT_END, Integer.parseInt(this.nightEndTime));
        userSettings.put(IoTValues.TARGET_TEMP, Integer.parseInt(this.targetTemp));
        userSettings.put(IoTValues.ALARM_PASSCODE, this.alarmPasscode);
        userSettings.put(IoTValues.EXPERIMENT_GROUP, this.experimentGroup);
        userSettings.put(IoTValues.SMART_LOCK_PASSWORD, this.smartLockPasscode);
        controller.updateSettings(userSettings);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("House " + this.name + " configured");
        }
    }

    /**
     * Stop logging history
     */
    public void stopHistorian() {
        this.logHistory = false;
    }

    /**
     * Start a thread to log house history on a delay
     */
    public void startHistorian() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (logHistory) {
                    try {
                        TartanHome state = getState();
                        if (state != null) {
                            TartanHomeData home = new TartanHomeData(state);
                            if (LOGGER.isInfoEnabled()) {
                                LOGGER.info("Logging " + name + "@" + address + " state");
                            }
                            logHistory(home);
                        }

                        Thread.sleep(historyTimer);
                    } catch (Exception x) {
                        LOGGER.error("Failed to save " + name + "@" + address + " state");
                    }
                }
            }
        }).start();
    }

    /**
     * Save the current state of the house
     * 
     * @param tartanHomeData the current state in a Hibernate-aware format
     */
    @UnitOfWork
    private void logHistory(TartanHomeData tartanHomeData) {
        homeDAO.create(tartanHomeData);
    }

    /**
     * Get the name for this house
     * 
     * @return the house name
     */
    public String getName() {
        return name;
    }

    public Boolean authenticate(String user, String pass) {
        this.authenticated = (this.user.equals(user) && this.password.equals(pass));
        return this.authenticated;
    }

    /**
     * Get the house address
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the house's experiment group
     * 
     * @return the experiment group
     */
    public String getExperimentGroup() {
        return experimentGroup;
    }

    /**
     * Get the house conncected state
     * 
     * @return true if connected; false otherwise
     */
    public Boolean isConnected() {
        return controller.isConnected();
    }

    /**
     * Convert humidifier state
     * 
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTHumdifierState(TartanHome tartanHome) {
        if (tartanHome.getHumidifier().equals(TartanHomeValues.OFF))
            return false;
        else if (tartanHome.getHumidifier().equals(TartanHomeValues.ON))
            return true;
        return null;
    }

    /**
     * Convert light state
     * 
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTLightState(TartanHome tartanHome) {
        if (tartanHome.getLight().equals(TartanHomeValues.OFF))
            return false;
        else if (tartanHome.getLight().equals(TartanHomeValues.ON))
            return true;
        return null;
    }

    /**
     * Convert alarm armed state
     * 
     * @param tartanHome the home
     * @return true if armed; false if disarmed; otherwise null
     */
    private Boolean toIoTAlarmArmedState(TartanHome tartanHome) {
        if (tartanHome.getAlarmArmed().equals(TartanHomeValues.DISARMED))
            return false;
        else if (tartanHome.getAlarmArmed().equals(TartanHomeValues.ARMED))
            return true;
        return null;
    }

    /**
     * Convert alarm delay
     * 
     * @param tartanHome the home
     * @return the converted delay
     */
    private Integer toIoTAlarmDelay(TartanHome tartanHome) {
        return Integer.parseInt(tartanHome.getAlarmDelay());
    }

    private Integer toIoTNightStartTime(TartanHome tartanHome) {
        return Integer.parseInt(tartanHome.getNightStartTime());
    }

    private Integer toIoTNightEndTime(TartanHome tartanHome) {
        return Integer.parseInt(tartanHome.getNightEndTime());
    }

    /**
     * Convert alarm passcode
     * 
     * @param tartanHome the home
     * @return the passcode
     */
    private String toIoTPasscode(TartanHome tartanHome) {
        return tartanHome.getAlarmPasscode();
    }

    /**
     * Convert experiment group
     * 
     * @param tartanHome the home
     * @return the experiment group
     */
    private String toIoTExperimentGroup(TartanHome tartanHome) {
        return tartanHome.getExperimentGroup();
    }

    /**
     * Convert smart lock passcode
     * 
     * @param tartanHome the home
     * @return the smart lock passcode
     */
    private String toIoTSmartLockPasscode(TartanHome tartanHome) {
        return tartanHome.getSmartLockPasscode();
    }

    /**
     * Convert door state
     * 
     * @param tartanHome the home
     * @return true if open; false if closed' otherwise null
     */
    private Boolean toIoTDoorState(TartanHome tartanHome) {
        if (tartanHome.getDoor().equals(TartanHomeValues.CLOSED))
            return false;
        else if (tartanHome.getDoor().equals(TartanHomeValues.OPEN))
            return true;
        return null;
    }

    /**
     * Convert smartlock state
     * 
     * @param tartanHome the home
     * @return true if locked; false if unlocked' otherwise null
     */
    private Boolean toIoTSmartLockState(TartanHome tartanHome) {
        if (tartanHome.getSmartLock().equals(TartanHomeValues.LOCKED))
            return true;
        else if (tartanHome.getSmartLock().equals(TartanHomeValues.UNLOCKED))
            return false;
        return null;
    }

    /**
     * Convert proximity state
     * 
     * @param tartanHome the home
     * @return true if occupied; false if empty; otherwise null
     */
    private Boolean toIoTProximityState(TartanHome tartanHome) {
        if (tartanHome.getProximity().equals(TartanHomeValues.OCCUPIED))
            return true;
        else if (tartanHome.getProximity().equals(TartanHomeValues.EMPTY))
            return false;
        return null;
    }

    /**
     * Convert window state
     * 
     * @param tartanHome the home
     * @return true if a window is broken; false if all windows are intact;
     *         otherwise null
     */
    private Boolean toIoTWindowState(TartanHome tartanHome) {
        if (tartanHome.getBrokenWindow().equals(TartanHomeValues.BROKEN))
            return true;
        else if (tartanHome.getBrokenWindow().equals(TartanHomeValues.INTACT))
            return false;
        return null;
    }

    /**
     * Convert intruder state
     * 
     * @param tartanHome the home
     * @return true if intruder detected; false if no intruder detected; otherwise
     *         null
     */
    private Boolean toIoTIntruderState(TartanHome tartanHome) {
        if (tartanHome.getIntruder().equals(TartanHomeValues.DETECTED))
            return true;
        else if (tartanHome.getIntruder().equals(TartanHomeValues.ALLCLEAR))
            return false;
        return null;
    }

    /**
     * Convert geofence state
     * 
     * @param tartanHome the home
     * @return true if present; false if absent; otherwise null
     */
    private Boolean toIoTGeofenceState(TartanHome tartanHome) {
        if (tartanHome.getGeofenceState().equals(TartanHomeValues.PRESENT))
            return true;
        else if (tartanHome.getGeofenceState().equals(TartanHomeValues.ABSENT))
            return false;
        return null;
    }

    /**
     * Convert all clear state
     * 
     * @param tartanHome the home
     * @return true if all clear; false if not all clear; otherwise null
     */
    private Boolean toIoTAllClearState(TartanHome tartanHome) {
        if (tartanHome.getAllClear().equals(TartanHomeValues.CLEARED))
            return true;
        else if (tartanHome.getAllClear().equals(TartanHomeValues.NOTCLEARED))
            return false;
        return null;
    }

    /**
     * Convert alarm active state
     * 
     * @param tartanHome the home
     * @return true if active; false if inactive; otherwise null
     */
    private Boolean toIoTAlarmActiveState(TartanHome tartanHome) {
        if (tartanHome.getAlarmActive().equals(TartanHomeValues.ACTIVE))
            return true;
        else if (tartanHome.getAlarmActive().equals(TartanHomeValues.INACTIVE))
            return false;
        return null;
    }

    /**
     * Convert heater state
     * 
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTHeaterState(TartanHome tartanHome) {
        if (tartanHome.getHvacMode().equals(TartanHomeValues.HEAT)) {
            if (tartanHome.getHvacState().equals(TartanHomeValues.ON)) {
                return true;
            } else if (tartanHome.getHvacState().equals(TartanHomeValues.OFF)) {
                return false;
            }
        }
        return null;
    }

    /**
     * Convert chiller state
     * 
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTChillerState(TartanHome tartanHome) {
        if (tartanHome.getHvacMode().equals(TartanHomeValues.COOL)) {
            if (tartanHome.getHvacState().equals(TartanHomeValues.ON)) {
                return true;
            } else if (tartanHome.getHvacState().equals(TartanHomeValues.OFF)) {
                return false;
            }
        }
        return null;
    }

    /**
     * Convert target temperature state
     * 
     * @param tartanHome the home
     * @return converted target temperature
     */
    private Integer toIoTTargetTempState(TartanHome tartanHome) {
        return Integer.parseInt(tartanHome.getTargetTemp());
    }

    /**
     * Convert HVAC mode state
     * 
     * @param tartanHome the home
     * @return Heater, Chiller; or null
     */
    private String toIoTHvacModeState(TartanHome tartanHome) {
        if (tartanHome.getHvacMode().equals(TartanHomeValues.HEAT))
            return "Heater";
        else if (tartanHome.getHvacMode().equals(TartanHomeValues.COOL))
            return "Chiller";
        return null;
    }

    /**
     * Set the house state in the hardware
     * 
     * @param h the new state
     * @return true
     */
    public Boolean setState(TartanHome h) {
        synchronized (controller) {

            Map<String, Object> userSettings = new Hashtable<String, Object>();
            if (h.getAlarmDelay() != null) {
                this.alarmDelay = h.getAlarmDelay();
                userSettings.put(IoTValues.ALARM_DELAY, Integer.parseInt(this.alarmDelay));

            }
            if (h.getNightStartTime() != null) {
                this.nightStartTime = h.getNightStartTime();
                userSettings.put(IoTValues.NIGHT_START, Integer.parseInt(this.nightStartTime));
            }
            if (h.getNightEndTime() != null) {
                this.nightEndTime = h.getNightEndTime();
                userSettings.put(IoTValues.NIGHT_END, Integer.parseInt(this.nightEndTime));
            }

            if (h.getTargetTemp() != null) {
                this.targetTemp = h.getTargetTemp();
                userSettings.put(IoTValues.TARGET_TEMP, Integer.parseInt(this.targetTemp));
            }
            controller.updateSettings(userSettings);
            controller.processStateUpdate(toIotState(h));
        }
        return true;
    }

    /**
     * Fetch the current state of the house
     * 
     * @return the current state
     */
    public TartanHome getState() {

        TartanHome tartanHome = new TartanHome();

        tartanHome.setName(this.name);
        tartanHome.setAddress(this.address);
        tartanHome.setExperimentGroup(this.experimentGroup);

        tartanHome.setTargetTemp(this.targetTemp);
        tartanHome.setAlarmDelay(this.alarmDelay);
        tartanHome.setNightStartTime(this.nightStartTime);
        tartanHome.setNightEndTime(this.nightEndTime);

        tartanHome.setEventLog(controller.getLogMessages());
        tartanHome.setAuthenticated(String.valueOf(this.authenticated));

        Map<String, Object> state = null;
        synchronized (controller) {
            state = controller.getCurrentState();
            for (String l : controller.getLogMessages()) {
                LOGGER.info(l);
            }
        }
        if (state == null) {
            LOGGER.info("zUsing default state");
            // There is no state, but something must be returned.

            tartanHome.setTemperature(TartanHomeValues.UNKNOWN);
            tartanHome.setHumidity(TartanHomeValues.UNKNOWN);
            tartanHome.setTargetTemp(TartanHomeValues.UNKNOWN);
            tartanHome.setHumidifier(TartanHomeValues.UNKNOWN);
            tartanHome.setDoor(TartanHomeValues.UNKNOWN);
            tartanHome.setSmartLock(TartanHomeValues.UNKNOWN);
            tartanHome.setLight(TartanHomeValues.UNKNOWN);
            tartanHome.setProximity(TartanHomeValues.UNKNOWN);
            tartanHome.setBrokenWindow(TartanHomeValues.UNKNOWN);
            tartanHome.setIntruder(TartanHomeValues.UNKNOWN);
            tartanHome.setAlarmArmed(TartanHomeValues.UNKNOWN);
            tartanHome.setAlarmActive(TartanHomeValues.UNKNOWN);
            tartanHome.setHvacMode(TartanHomeValues.UNKNOWN);
            tartanHome.setHvacState(TartanHomeValues.UNKNOWN);
            tartanHome.setGeofenceState(TartanHomeValues.UNKNOWN);
            tartanHome.setAllClear(TartanHomeValues.UNKNOWN);

            return tartanHome;
        }

        // A valid state was found, so use it

        Set<String> keys = state.keySet();
        for (String key : keys) {
            LOGGER.info("State element: " + key + "=" + state.get(key));
            if (key.equals(IoTValues.TEMP_READING)) {
                tartanHome.setTemperature(String.valueOf(state.get(key)));
            } else if (key.equals(IoTValues.HUMIDITY_READING)) {
                tartanHome.setHumidity(String.valueOf(state.get(key)));
            } else if (key.equals(IoTValues.TARGET_TEMP)) {
                tartanHome.setTargetTemp(String.valueOf(state.get(key)));
            } else if (key.equals(IoTValues.HUMIDIFIER_STATE)) {
                Boolean humidifierState = (Boolean) state.get(key);
                if (humidifierState) {
                    tartanHome.setHumidifier(String.valueOf(TartanHomeValues.ON));
                } else {
                    tartanHome.setHumidifier(String.valueOf(TartanHomeValues.OFF));
                }
            } else if (key.equals(IoTValues.DOOR_STATE)) {
                Boolean doorState = (Boolean) state.get(key);
                if (doorState) {
                    tartanHome.setDoor(TartanHomeValues.OPEN);
                } else {
                    tartanHome.setDoor(TartanHomeValues.CLOSED);
                }
            } else if (key.equals(IoTValues.SMART_LOCK_STATE)) {
                Boolean smartLockState = (Boolean) state.get(key);
                if (smartLockState) {
                    tartanHome.setSmartLock(TartanHomeValues.LOCKED);
                } else {
                    tartanHome.setSmartLock(TartanHomeValues.UNLOCKED);
                }
            } else if (key.equals(IoTValues.LIGHT_STATE)) {
                Boolean lightState = (Boolean) state.get(key);
                if (lightState) {
                    tartanHome.setLight(TartanHomeValues.ON);
                } else {
                    tartanHome.setLight(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.PROXIMITY_STATE)) {
                Boolean proxState = (Boolean) state.get(key);
                if (proxState) {
                    tartanHome.setProximity(TartanHomeValues.OCCUPIED);
                } else {
                    tartanHome.setProximity(TartanHomeValues.EMPTY);
                }
            } else if (key.equals(IoTValues.BROKEN_WINDOW)) {
                Boolean bWindowState = (Boolean) state.get(key);
                if (bWindowState) {
                    tartanHome.setBrokenWindow(TartanHomeValues.BROKEN);
                } else {
                    tartanHome.setBrokenWindow(TartanHomeValues.INTACT);
                }
            } else if (key.equals(IoTValues.INTRUDER_STATE)) {
                Boolean intruState = (Boolean) state.get(key);
                if (intruState) {
                    tartanHome.setIntruder(TartanHomeValues.DETECTED);
                } else {
                    tartanHome.setIntruder(TartanHomeValues.ALLCLEAR);
                }
            } else if (key.equals(IoTValues.GEOFENCE_STATE)) {
                Boolean geoState = (Boolean) state.get(key);
                if (geoState) {
                    tartanHome.setGeofenceState(TartanHomeValues.PRESENT);
                } else {
                    tartanHome.setGeofenceState(TartanHomeValues.ABSENT);
                }
            } else if (key.equals(IoTValues.ALL_CLEAR)) {
                Boolean allClearState = (Boolean) state.get(key);
                if (allClearState) {
                    tartanHome.setAllClear(TartanHomeValues.CLEARED);
                } else {
                    tartanHome.setAllClear(TartanHomeValues.NOTCLEARED);
                }
            } else if (key.equals(IoTValues.ALARM_STATE)) {
                Boolean alarmState = (Boolean) state.get(key);
                if (alarmState) {
                    tartanHome.setAlarmArmed(TartanHomeValues.ARMED);
                } else {
                    tartanHome.setAlarmArmed(TartanHomeValues.DISARMED);
                }
            } else if (key.equals(IoTValues.ALARM_ACTIVE)) {
                Boolean alarmActiveState = (Boolean) state.get(key);
                if (alarmActiveState) {
                    tartanHome.setAlarmActive(TartanHomeValues.ACTIVE);
                } else {
                    tartanHome.setAlarmActive(TartanHomeValues.INACTIVE);
                }

            } else if (key.equals(IoTValues.HVAC_MODE)) {
                if (state.get(key).equals("Heater")) {
                    tartanHome.setHvacMode(TartanHomeValues.HEAT);
                } else if (state.get(key).equals("Chiller")) {
                    tartanHome.setHvacMode(TartanHomeValues.COOL);
                }

                // If either heat or chill is on then the hvac is on
                String heaterState = String.valueOf(state.get(IoTValues.HEATER_STATE));
                String chillerState = String.valueOf(state.get(IoTValues.CHILLER_STATE));

                if (heaterState.equals("true") || chillerState.equals("true")) {
                    tartanHome.setHvacState(TartanHomeValues.ON);

                } else {
                    tartanHome.setHvacState(TartanHomeValues.OFF);
                }
            }
        }

        return tartanHome;
    }

    /**
     * Convert the state to a format suitable for the hardware
     * 
     * @param tartanHome the state
     * @return a map of settings appropriate for the hardware
     */
    @SuppressWarnings("DoubleBraceInitialization")
    private Map<String, Object> toIotState(TartanHome tartanHome) {
        Map<String, Object> state = new Hashtable<>();

        if (tartanHome.getProximity() != null) {
            state.put(IoTValues.PROXIMITY_STATE, toIoTProximityState(tartanHome));
        }

        if (tartanHome.getBrokenWindow() != null) {
            state.put(IoTValues.BROKEN_WINDOW, toIoTWindowState(tartanHome));
        }

        if (tartanHome.getIntruder() != null) {
            state.put(IoTValues.INTRUDER_STATE, toIoTIntruderState(tartanHome));
        }

        if (tartanHome.getGeofenceState() != null) {
            state.put(IoTValues.GEOFENCE_STATE, toIoTGeofenceState(tartanHome));
        }

        if (tartanHome.getAllClear() != null) {
            state.put(IoTValues.ALL_CLEAR, toIoTAllClearState(tartanHome));
        }

        if (tartanHome.getDoor() != null) {
            state.put(IoTValues.DOOR_STATE, toIoTDoorState(tartanHome));
        }
        if (tartanHome.getSmartLock() != null) {
            state.put(IoTValues.SMART_LOCK_STATE, toIoTSmartLockState(tartanHome));
        }
        if (tartanHome.getLight() != null) {
            state.put(IoTValues.LIGHT_STATE, toIoTLightState(tartanHome));
        }
        if (tartanHome.getHumidifier() != null) {
            state.put(IoTValues.HUMIDIFIER_STATE, toIoTHumdifierState(tartanHome));
        }
        if (tartanHome.getAlarmActive() != null) {
            state.put(IoTValues.ALARM_ACTIVE, toIoTAlarmActiveState(tartanHome));
        }
        // entering a passcode also disables the alarm
        if (tartanHome.getAlarmPasscode() != null) {
            state.put(IoTValues.GIVEN_PASSCODE, toIoTPasscode(tartanHome));
            tartanHome.setAlarmArmed(TartanHomeValues.DISARMED);
            state.put(IoTValues.ALARM_STATE, toIoTAlarmArmedState(tartanHome));
        } else {
            if (tartanHome.getAlarmArmed() != null) {
                state.put(IoTValues.ALARM_STATE, toIoTAlarmArmedState(tartanHome));
            }
        }
        if (tartanHome.getExperimentGroup() != null) {
            state.put(IoTValues.EXPERIMENT_GROUP, toIoTExperimentGroup(tartanHome));
        }
        if (tartanHome.getSmartLockPasscode() != null) {
            state.put(IoTValues.GIVEN_SMART_LOCK_PASSWORD, toIoTSmartLockPasscode(tartanHome));
        }
        if (tartanHome.getAlarmDelay() != null) {
            this.alarmDelay = tartanHome.getAlarmDelay();

            Hashtable<String, Object> ht = new Hashtable<String, Object>() {
                {
                    put(IoTValues.ALARM_DELAY, Integer.parseInt(TartanHomeService.this.alarmDelay));
                }
            };
            controller.updateSettings(ht);
        }

        if (tartanHome.getNightStartTime() != null) {
            this.nightStartTime = tartanHome.getNightStartTime();
            Hashtable<String, Object> ht = new Hashtable<String, Object>() {
                {
                    put(IoTValues.NIGHT_START, Integer.parseInt(TartanHomeService.this.nightStartTime));
                }
            };
            controller.updateSettings(ht);
        }

        if (tartanHome.getNightEndTime() != null) {
            this.nightEndTime = tartanHome.getNightEndTime();
            Hashtable<String, Object> ht = new Hashtable<String, Object>() {
                {
                    put(IoTValues.NIGHT_END, Integer.parseInt(TartanHomeService.this.nightEndTime));
                }
            };
            controller.updateSettings(ht);
        }

        if (tartanHome.getHvacMode() != null) {
            if (tartanHome.getHvacMode().equals(TartanHomeValues.HEAT)) {
                state.put(IoTValues.HVAC_MODE, "Heater");
                if (tartanHome.getHvacState() != null) {
                    state.put(IoTValues.HEATER_STATE, toIoTHeaterState(tartanHome));
                }
            }
            if (tartanHome.getHvacMode().equals(TartanHomeValues.COOL)) {
                state.put(IoTValues.HVAC_MODE, "Chiller");
                if (tartanHome.getHvacState() != null) {
                    if (tartanHome.getHvacState().equals(TartanHomeValues.ON)) {
                        state.put(IoTValues.CHILLER_ON, toIoTChillerState(tartanHome));
                    }
                }
            }
        }

        for (Map.Entry<String, Object> e : state.entrySet()) {
            LOGGER.info("State: " + e.getKey() + "=" + e.getValue());
        }

        return state;
    }

    /**
     * Connect to the house
     * 
     * @throws TartanHomeConnectException exception passed when connect fails
     */
    public void connect() throws TartanHomeConnectException {
        if (controller.isConnected() == false) {
            if (!controller.connectToHouse(this.address, this.port, this.user, this.password)) {
                throw new TartanHomeConnectException();
            }
        }
    }
}