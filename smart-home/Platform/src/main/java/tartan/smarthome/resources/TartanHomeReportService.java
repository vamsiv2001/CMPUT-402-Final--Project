package tartan.smarthome.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tartan.smarthome.resources.iotcontroller.IoTValues;

public class TartanHomeReportService {
    // Store the state changes over period of 7 days
    private List<Map<String, Object>> stateRecord;
    private String experimentGroup;

    public TartanHomeReportService(String experimentGroup) {
        this.stateRecord = new ArrayList<>();
        this.experimentGroup = experimentGroup;
    }

    /**
     * Records a state update for future report generation.
     *
     * @param stateUpdate The state update to record.
     */
    public void recordStateUpdate(Map<String, Object> stateUpdate) {
        stateRecord.add(stateUpdate);
    }

    public void recordUserBehaviour() {
        // records the user behaviour after reports has been generated
        if (experimentGroup.equals("1")) {
            ReportVariationExperiment.persistStateRecord1.addAll(stateRecord);
        } else if (experimentGroup.equals("2")) {
            ReportVariationExperiment.persistStateRecord2.addAll(stateRecord);
        } else if (experimentGroup.equals("3")) {
            ReportVariationExperiment.persistStateRecord3.addAll(stateRecord);
        } else if (experimentGroup.equals("4")) {
            ReportVariationExperiment.persistStateRecord4.addAll(stateRecord);
        }
    }

    /**
     * Generates and sends a report based on the recorded state changes.
     */
    public String generateReport() {
        String report = buildReport();
        stateRecord.clear();

        return report;
    }

    /**
     * Builds the report from the recorded state changes.
     *
     * @return A string representing the report.
     */
    private String buildReport() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Weekly Smart Home Report\n");

        switch (experimentGroup) {
            case "1":
                reportBuilder.append(type1Report());
                break;
            case "2":
                reportBuilder.append(type2Report());
                break;
            case "3":
                reportBuilder.append(type1Report());
                break;
            case "4":
                reportBuilder.append(type2Report());
                break;
            default:
                reportBuilder.append(type1Report());

        }

        return reportBuilder.toString();
    }

    /**
     * Builds a report of type 1 for experiment group 1 houses
     *
     * @return A string representing the formatted report.
     */
    private String type1Report() {
        // House security report
        StringBuilder reportBuilder = new StringBuilder();

        int brokenWindowsCount = 0; 
        int alarmActivatedCount = 0;
        int intruderStateCount = 0;
        int smartLockedCount = 0;
        int smartUnlockedCount = 0;

        for (Map<String, Object> state : stateRecord) {
            Boolean brokenWindow = null;
            Boolean alarmActiveState = null;
            Boolean intruderState = null;
            Boolean smartLockState = null;

            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.BROKEN_WINDOW)) {
                    brokenWindow = (Boolean) state.get(key);

                    if (brokenWindow) {
                        brokenWindowsCount++;
                    } 
                }
                if (key.equals(IoTValues.ALARM_ACTIVE)) {
                    alarmActiveState = (Boolean) state.get(key);

                    if (alarmActiveState) {
                        alarmActivatedCount++;
                    }
                }
                if (key.equals(IoTValues.INTRUDER_STATE)) {
                    intruderState = (Boolean) state.get(key);
                    
                    if (intruderState) {
                        intruderStateCount++;
                    }
                }
                if (key.equals(IoTValues.SMART_LOCK_STATE)) {
                    smartLockState = (Boolean) state.get(key);

                    if (smartLockState) {
                        smartLockedCount++;
                    } else {
                        smartUnlockedCount++;
                    }
                }
            }
        }

        reportBuilder.append("Weekly Security Report:\n");
        reportBuilder.append(" A total of: ").append(brokenWindowsCount).append(" were broken").append("\n");
        reportBuilder.append(" Door remained locked a total of: ").append(smartLockedCount).append(" times").append("\n");
        reportBuilder.append(" Door remained unlocked a total of: ").append(smartUnlockedCount).append(" times").append("\n");
        reportBuilder.append(" Intruder state was activate a total of: ").append(intruderStateCount).append(" times").append("\n");
        reportBuilder.append(" The alarm sounded a total of: ").append(alarmActivatedCount).append(" times").append("\n");

        return reportBuilder.toString();
    }

    /**
     * Builds a report of type 2 for experiment group 2 houses
     *
     * @return A string representing the formatted report.
     */
    private String type2Report() {
        // House occupancy report
        StringBuilder reportBuilder = new StringBuilder();

        int lightOnCount = 0;
        int lightOffCount = 0;
        int occupiedCount = 0;
        int vacantCount = 0;
        int doorOpenedCount = 0;
        int doorClosedCount = 0;

        for (Map<String, Object> state : stateRecord) {
            Boolean lightState = null;
            Boolean proximityState = null;
            Boolean doorState = null;

            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.LIGHT_STATE)) {
                    lightState = (Boolean) state.get(key);

                    if (lightState) {
                        lightOnCount++;
                    } else {
                        lightOffCount++;
                    }
                }
                if (key.equals(IoTValues.PROXIMITY_STATE)) {
                    proximityState = (Boolean) state.get(key);

                    if (proximityState) {
                        occupiedCount++;
                    } else {
                        vacantCount++;
                    }
                }
                if (key.equals(IoTValues.DOOR_STATE)) {
                    doorState = (Boolean) state.get(key);

                    if (doorState) {
                        doorOpenedCount++;
                    } else {
                        doorClosedCount++;
                    }
                }
            }
        }

        reportBuilder.append("Weekly Lights Usage Report:\n");
        reportBuilder.append(" Door remained opened a total of: ").append(doorOpenedCount).append(" times").append("\n");
        reportBuilder.append(" Lights were on a total of: ").append(lightOnCount).append(" times").append("\n");
        reportBuilder.append(" House was occupied a total of: ").append(occupiedCount).append(" times").append("\n");
        reportBuilder.append(" Door remained closed a total of: ").append(doorClosedCount).append(" times").append("\n");
        reportBuilder.append(" Lights were off a total of: ").append(lightOffCount).append(" times").append("\n");
        reportBuilder.append(" House was vacant a total of: ").append(vacantCount).append(" times").append("\n");

        return reportBuilder.toString();
    }
}
