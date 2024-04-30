package tartan.smarthome.core;

import javax.persistence.*;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a database table for home status
 */
@Entity
@Table(name = "Home")
public class TartanHomeData {

    // Primary key for the table. Not meant to be used
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // the creation time
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    private Date createTimeStamp;

    @Column(name = "home_name", nullable = false)
    private String homeName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "experiment_group", nullable = false)
    private String experimentGroup;

    // The desired temperature
    @Column(name = "target_temp")
    private String targetTemp;

    // the current temperature
    @Column(name = "temperature")
    private String temperature;

    // the current humidity
    @Column(name = "humidity")
    private String humidity;

    // the state of the door (true if open, false if closed)
    @Column(name = "door_state")
    private String door;

    // the state of the smartLock (true if open, false if closed)
    @Column(name = "smart_lock_state")
    private String smartLock;

    // the state of the light (true if on, false if off)
    @Column(name = "light_state")
    private String light;

    // the humidifier state (true if on, false if off)
    @Column(name = "humidifier_state")
    private String humidifier;

    // the state of the proximity sensor (true if house occupied, false if vacant)
    @Column(name = "proximity_state")
    private String proximity;

    // the state of the window sensor (true of a window is broken, false if all windows are intact)
    @Column(name = "broken_window")
    private String brokenWindow;

    // the state of the intruder defense system (true if the system detects an intruder, false if no intruder detected)
    @Column(name = "intruder_state")
    private String intruder;

    // the heater state (true if on, false if off)
    @Column(name = "hvac_mode")
    
    private String hvacMode;
    // The state of the HVAC system
    @Column(name = "hvac_state")
    private String hvacState;

    // the alarm active state (true if alarm sounding, false if alarm not sounding)
    @Column(name = "alarm_active_state")
    private String alarmActive;

    // the alarm delay timeout
    @Column(name = "alarm_delay")
    private String alarmDelay;

    // the alarm enabled state
    @Column(name = "alarm_enabled_state")
    private String alarmArmed;

    // the night state
    @Column(name = "night_state")
    private String nightState;

    // the night start time
    @Column(name = "night_start_time")
    private String nightStartTime;

    // the night end time 
    @Column(name = "night_end_time")
    private String nightEndTime;

    // the geofence state
    @Column(name = "geofence_state")
    private String geofenceState;

    // the detected cellphones
    @Column(name = "detected_cellphones")
    private String detectedCellphones;

    // the known cellphones
    @Column(name = "known_cellphones")
    private String knownCellphones;

    // the all clear state
    @Column(name = "all_clear_state")
    private String allClear;

    /**
     * Create a mew data set from a TartanHome model
     * @param h the home model
     */
    public TartanHomeData(TartanHome h) {
        this.homeName = h.getName();
        this.address = h.getAddress();
        this.experimentGroup = h.getExperimentGroup();
        this.targetTemp = h.getTargetTemp();
        this.temperature = h.getTemperature();
        this.humidity = h.getHumidity();
        this.door = h.getDoor();
        this.smartLock = h.getSmartLock();
        this.light = h.getLight();
        this.humidifier = h.getHumidifier();
        this.proximity = h.getProximity();
        this.brokenWindow = h.getBrokenWindow();
        this.intruder = h.getIntruder();
        this.hvacMode = h.getHvacMode();
        this.hvacState = h.getHvacState();
        this.alarmActive = h.getAlarmActive();
        this.alarmDelay = h.getAlarmDelay();
        this.alarmArmed = h.getAlarmArmed();
        this.nightState = h.getNightState();
        this.nightStartTime = h.getNightStartTime();
        this.nightEndTime = h.getNightEndTime();
        this.geofenceState = h.getGeofenceState();
        this.detectedCellphones = h.getDetectedCellphones();
        this.knownCellphones = h.getKnownCellphones();
        this.allClear = h.getAllClear();

        // Remember when this record is created
        this.createTimeStamp = new Date();
    }

    /**
     * Get the name
     * @return the name
     */
    public String getHomeName() {
        return homeName;
    }

    /**
     * Set the name
     * @param homeName the new name
     */
    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    /**
     * Get the address
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the experiment group
     * @return the experiment group
     */
    public String getExperimentGroup() {
        return experimentGroup;
    }

    /**
     * Set the experiment group
     * @param experimentGroup
     */
    public void setExperimentGroup(String experimentGroup) {
        this.experimentGroup = experimentGroup;
    }

    /**
     * Get the target temperature
     * @return the target temperature
     */
    public String getTargetTemp() {
        return targetTemp;
    }

    /**
     * Set the target temperature
     * @param targetTemp the new target temperature
     */
    public void setTargetTemp(String targetTemp) { this.targetTemp = targetTemp; }

    /**
     * Get the current temperature
     * @return the temperature
     */
    public String getTemperature() {
        return this.temperature;
    }

    /**
     * Set the temperature
     * @param temperature the new temperature
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /**
     * Get the humidity
     * @return Current humidity
     */
    public String getHumidity() {
        return this.humidity;
    }

    /**
     * Set the humidity
     * @param humidity the new humidity
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Get the door state
     * @return the door state
     */
    public String getDoor() {
        return this.door;
    }

    /**
     * Set the door state
     * @param door the new door state
     */
    public void setDoor(String door) {
        this.door = door;
    }

    /**
     * Get the smartLock state
     * @return the smartLock state
     */
    public String getSmartLock() {
        return this.smartLock;
    }

    /**
     * Set the smartLock state
     * @param smartLock the new smartLock state
     */
    public void setSmartLock(String smartLock) {
        this.smartLock = smartLock;
    }

    /**
     * Get the light state
     * @return the light state
     */
    public String getLight() {
        return this.light;
    }

    /**
     * Set the light state
     * @param light the new light state
     */
    public void setLight(String light) {
        this.light = light;
    }

    /**
     * Get the dehumidifier state
     * @return the dehumidifier state
     */
    public String getHumidifier() {
        return humidifier;
    }

    /**
     * Set the dehumidifier state
     * @param humidifier the new state
     */
    public void setHumidifier(String humidifier) {
        this.humidifier = humidifier;
    }

    /**
     * Get the motion sensor state
     * @return the motion sensor state
     */
    public String getProximity() {
        return proximity;
    }

    /**
     * Set the motion sensor state
     * @param proximity the new state
     */
    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    /**
     * Get the brokenWindow state
     * @return the brokenWindow state
     */
    public String getBrokenWindow() {
        return this.brokenWindow;
    }

    /**
     * Set the brokenWindow state
     * @param brokenWindow the new brokenWindow state
     */
    public void setBrokenWindow(String brokenWindow) {
        this.brokenWindow = brokenWindow;
    }

    /**
     * Get the intruder state
     * @return the intruder state
     */
    public String getIntruder() {
        return this.intruder;
    }

    /**
     * Set the intruder state
     * @param intruder the new intruder state
     */
    public void setIntruder(String intruder) {
        this.intruder = intruder;
    }

    /**
     * Get the alarm armed state
     * @return the status of the alarm
     */
    public String getAlarmArmed() {
        return alarmArmed;
    }

    /**
     * Arm/Disarm the alarm
     * @param alarmArmed the new state
     */
    public void setAlarmArmed(String alarmArmed) {
        this.alarmArmed = alarmArmed;
    }

    /**
     * Get the HVAC mode
     * @return the HVAC mode
     */
    public String getHvacMode() {
        return hvacMode;
    }

    /**
     * Set the HVAC mode
     * @param hvacMode the new mode
     */
    public void setHvacMode(String hvacMode) {
        this.hvacMode = hvacMode;
    }

    /**
     * Get the alarm active state
     * @return the current state
     */
    public String getAlarmActive() {
        return alarmActive;
    }

    /**
     * Set the alarm active state
     * @param alarmActive the new state
     */
    public void setAlarmActive(String alarmActive) {
        this.alarmActive = alarmActive;
    }

    /**
     * Get the alarm delay
     * @return the current delay
     */
    public String getAlarmDelay() {
        return alarmDelay;
    }

    /**
     * Set the alarm delay
     * @param alarmDelay the new delay
     */
    public void setAlarmDelay(String alarmDelay) {
        this.alarmDelay = alarmDelay;
    }

    /**
     * Get the HVAC state
     * @return the current state
     */
    public String getHvacState() {
        return hvacState;
    }

    /**
     * Set the HVAC state
     * @param hvacState the new state
     */
    public void setHvacState(String hvacState) {
        this.hvacState = hvacState;
    }

    /**
     * Get the night state
     * @return the current state
     */
    public String getNightState() {
        return nightState;
    }
    /**
     * Set the night state
     * @param nightState the new state
     */
    public void setNightState(String nightState) {
        this.nightState = nightState;
    }

    /**
     * Get the night start time
     * @return the start time
     */
    public String getNightStartTime() {
        return nightStartTime;
    }

    /**
     * Set the night start time
     * @param nightStartTime the new start time
     */
    public void setNightStartTime(String nightStartTime) {
        this.nightStartTime = nightStartTime;
    }

    /**
     * Get the geofence state
     */
    public String getGeofenceState() {
        return geofenceState;
    }  

    /**
     * Set the geofence state
     * @param geofenceState the new state
     */
    public void setGeofenceState(String geofenceState) {
        this.geofenceState = geofenceState;
    }

    /**
     * Get the detected cellphones
     */
    public String getDetectedCellphones() {
        return detectedCellphones;
    }

    /**
     * Set the detected cellphones
     */
    public void setDetectedCellphones(String detectedCellphones) {
        this.detectedCellphones = detectedCellphones;
    }

    /**
     * Get the known cellphones
     */
    public String getKnownCellphones() {
        return knownCellphones;
    }

    /**
     * Set the known cellphones
     */
    public void setKnownCellphones(String knownCellphones) {
        this.knownCellphones = knownCellphones;
    }

    /**
     * Get the all clear state
     */
    public String getAllClear() {
        return allClear;
    }

    /**
     * Set the all clear state
     */
    public void setAllClear(String allClear) {
        this.allClear = allClear;
    }

    /**
     * Get the ID
     * @return the ID
     */
    public long getId() {
        return id;
    }

    /**
     * Set the ID
     * @param id the new ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the creation time for this record
     * @return the creation time
     */
    public Date getCreateTimeStamp() { return createTimeStamp; }

    /**
     * Set the creation time
     * @param createTimeStamp the new timestamp
     */
    public void setCreateTimeStamp(Date createTimeStamp) { this.createTimeStamp = createTimeStamp; }

    @Override
    public int hashCode() {
        return Objects.hash(id, homeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TartanHomeData)) {
            return false;
        }
        final TartanHomeData that = (TartanHomeData) o;
        return Objects.equals(this.homeName, that.homeName);
    }

}
