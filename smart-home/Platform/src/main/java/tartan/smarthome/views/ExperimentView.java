package tartan.smarthome.views;

import io.dropwizard.views.View;

public class ExperimentView extends View {
    private String reportType1DoorCloseCount;

    private String reportType2DoorCloseCount;

    private String reportType3DoorCloseCount;

    private String reportType4DoorCloseCount;

    private String reportType1IntruderIncidenceCount;

    private String reportType2IntruderIncidenceCount;

    private String reportType3IntruderIncidenceCount;

    private String reportType4IntruderIncidenceCount;

    /**
     * Create a new view
     * @param tartanHome the home to view
     */
    public ExperimentView(
        String reportType1DoorCloseCount,
        String reportType2DoorCloseCount,
        String reportType3DoorCloseCount,
        String reportType4DoorCloseCount,
        String reportType1IntruderIncidenceCount,
        String reportType2IntruderIncidenceCount,
        String reportType3IntruderIncidenceCount,
        String reportType4IntruderIncidenceCount
    ) {
        super("experiment.ftl");
        this.reportType1DoorCloseCount = reportType1DoorCloseCount;
        this.reportType2DoorCloseCount = reportType2DoorCloseCount;
        this.reportType3DoorCloseCount = reportType3DoorCloseCount;
        this.reportType4DoorCloseCount = reportType4DoorCloseCount;
        this.reportType1IntruderIncidenceCount = reportType1IntruderIncidenceCount;
        this.reportType2IntruderIncidenceCount = reportType2IntruderIncidenceCount;
        this.reportType3IntruderIncidenceCount = reportType3IntruderIncidenceCount;
        this.reportType4IntruderIncidenceCount = reportType4IntruderIncidenceCount;
    }

    public String getReportType1DoorCloseCount() {
        return reportType1DoorCloseCount;
    }

    public String getReportType2DoorCloseCount() {
        return reportType2DoorCloseCount;
    }

    public String getReportType3DoorCloseCount() {
        return reportType3DoorCloseCount;
    }

    public String getReportType4DoorCloseCount() {
        return reportType4DoorCloseCount;
    }

    public String getReportType1IntruderIncidenceCount() {
        return reportType1IntruderIncidenceCount;
    }

    public String getReportType2IntruderIncidenceCount() {
        return reportType2IntruderIncidenceCount;
    }

    public String getReportType3IntruderIncidenceCount() {
        return reportType3IntruderIncidenceCount;
    }

    public String getReportType4IntruderIncidenceCount() {
        return reportType4IntruderIncidenceCount;
    }
}
