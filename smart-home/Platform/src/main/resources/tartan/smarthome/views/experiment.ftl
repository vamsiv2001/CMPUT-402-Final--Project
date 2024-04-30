<#-- @ftlvariable name="" type="tartan.smarthome.views.ExperimentView" -->
<html lang="us">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <title>Report Variation Experiment</title>
    <style>
        .bar-container {
            display: inline-block;
            margin-right: 10px;
            text-align: center;
        }
        .bar-label {
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="root">
        <h3>#Times door is closed</h3>
        <div class="graph">
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType1DoorCloseCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 1</p>
            </div>
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType2DoorCloseCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 2</p>
            </div>
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType3DoorCloseCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 3</p>
            </div>
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType4DoorCloseCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 4</p>
            </div>
        </div>
        <h3>#Intruder incidences</h3>
        <div class="graph">
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType1IntruderIncidenceCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 1</p>
            </div>
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType2IntruderIncidenceCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 2</p>
            </div>
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType3IntruderIncidenceCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 3</p>
            </div>
            <div class="bar-container">
                <div class="bar" style="width: 45px; height: ${reportType4IntruderIncidenceCount}px; background-color: blue"></div>
                <p class="bar-label">Report type 4</p>
            </div>
        </div>
    </div>
</body>
</html>