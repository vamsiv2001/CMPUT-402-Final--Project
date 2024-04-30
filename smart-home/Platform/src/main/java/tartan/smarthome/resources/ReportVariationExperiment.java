package tartan.smarthome.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tartan.smarthome.resources.iotcontroller.IoTValues;

abstract class ReportVariationExperiment {
    public static List<Map<String, Object>> persistStateRecord1 = new ArrayList<>();
    public static List<Map<String, Object>> persistStateRecord2 = new ArrayList<>();
    public static List<Map<String, Object>> persistStateRecord3 = new ArrayList<>();
    public static List<Map<String, Object>> persistStateRecord4 = new ArrayList<>();
    private static Map<String, Integer> doorCloseAnalytics = new HashMap<>();
    private static Map<String, Integer> intruderIncidenceAnalytics = new HashMap<>();

    public static Map<String, Integer> getDoorCloseAnalytics() {
        doorCloseAnalytics.clear();

        int doorClosedCount1 = 0;
        for (Map<String, Object> state : persistStateRecord1) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.DOOR_STATE)) {
                    Boolean doorState = (Boolean) state.get(key);
                    if (!doorState) {
                        doorClosedCount1++;
                    }
                }
            }
        }
        doorCloseAnalytics.put("reportType1", doorClosedCount1);

        int doorClosedCount2 = 0;
        for (Map<String, Object> state : persistStateRecord2) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.DOOR_STATE)) {
                    Boolean doorState = (Boolean) state.get(key);
                    if (!doorState) {
                        doorClosedCount2++;
                    }
                }
            }
        }
        doorCloseAnalytics.put("reportType2", doorClosedCount2);

        int doorClosedCount3 = 0;
        for (Map<String, Object> state : persistStateRecord3) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.DOOR_STATE)) {
                    Boolean doorState = (Boolean) state.get(key);
                    if (!doorState) {
                        doorClosedCount3++;
                    }
                }
            }
        }
        doorCloseAnalytics.put("reportType3", doorClosedCount3);

        int doorClosedCount4 = 0;
        for (Map<String, Object> state : persistStateRecord4) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.DOOR_STATE)) {
                    Boolean doorState = (Boolean) state.get(key);
                    if (!doorState) {
                        doorClosedCount4++;
                    }
                }
            }
        }
        doorCloseAnalytics.put("reportType4", doorClosedCount4);

        return doorCloseAnalytics;
    }

    public static Map<String, Integer> getIntruderIncidenceAnalytics() {
        intruderIncidenceAnalytics.clear();

        int intruderIncidenceCount1 = 0;
        for (Map<String, Object> state : persistStateRecord1) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.INTRUDER_STATE)) {
                    Boolean intruderState = (Boolean) state.get(key);
                    if (intruderState) {
                        intruderIncidenceCount1++;
                    }
                }
            }
        }
        intruderIncidenceAnalytics.put("reportType1", intruderIncidenceCount1);

        int intruderIncidenceCount2 = 0;
        for (Map<String, Object> state : persistStateRecord2) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.INTRUDER_STATE)) {
                    Boolean intruderState = (Boolean) state.get(key);
                    if (intruderState) {
                        intruderIncidenceCount2++;
                    }
                }
            }
        }
        intruderIncidenceAnalytics.put("reportType2", intruderIncidenceCount2);

        int intruderIncidenceCount3 = 0;
        for (Map<String, Object> state : persistStateRecord3) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.INTRUDER_STATE)) {
                    Boolean intruderState = (Boolean) state.get(key);
                    if (intruderState) {
                        intruderIncidenceCount3++;
                    }
                }
            }
        }
        intruderIncidenceAnalytics.put("reportType3", intruderIncidenceCount3);

        int intruderIncidenceCount4 = 0;
        for (Map<String, Object> state : persistStateRecord4) {
            Set<String> keys = state.keySet();
            for (String key : keys) {
                if (key.equals(IoTValues.INTRUDER_STATE)) {
                    Boolean intruderState = (Boolean) state.get(key);
                    if (intruderState) {
                        intruderIncidenceCount4++;
                    }
                }
            }
        }
        intruderIncidenceAnalytics.put("reportType4", intruderIncidenceCount4);
        
        return intruderIncidenceAnalytics;
    }
}
