<!-- This Apache Freemarker script is similar to a Java Server Page. It renders Dynamic server-side contnent.
See -->
<#-- @ftlvariable name="" type="tartan.smarthome.views.SmartHomeView" -->
<html lang="us">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
    <title>Tartan House Control Panel</title>
    <script type="text/javascript">
        $(document).ready(function() {

             $("#refresh_button").click(function() {
                window.location.reload();
             });

            function updateState() {
                var door = $('#door').val();
                var light = $('#light').val();
                var alarmDelay = $('#alarmDelay').val();
                var targetTemp = $('#targetTemp').val();
                var nightStartTime = $('#nightStartTime').val();
                var nightEndTime = $('#nightEndTime').val();
                var humidifier = $('#humidifier').val();
                var armAlarm = $('#armAlarm').val();
                var passcode = $('#alarmPasscode').val();
                var experimentGroup = $('#experimentGroup').val();
                var hvacMode = $('#hvacMode').val();
                var smartLock = $('#smartLock').val();
                var smartLockPasscode = $('#smartLockPasscode').val();

                return JSON.stringify({
                    "door":door,
                    "light":light,
                    "targetTemp":targetTemp,
                    "nightStartTime":nightStartTime,
                    "nightEndTime":nightEndTime,
                    "humidifier":humidifier,
                    "alarmArmed":armAlarm,
                    "alarmDelay":alarmDelay,
                    "alarmPasscode":passcode,
                    "experimentGroup":experimentGroup,
                    "smartLock":smartLock,
                    "smartLockPasscode":smartLockPasscode
                    });
            }

            // Auto scroll
            $('#log').scrollTop($('#log')[0].scrollHeight);

            setInterval(function() {
                $.ajax({
                    type: 'POST',
                    contentType: 'application/json',
                    url:  '/smarthome/update/${tartanHome.name}',
                    data: updateState(),
                    success: function(data) {
                        location.reload(true);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not update ${tartanHome.name}" + errorThrown);
                    },
                });
            }, 5 * 60 * 1000); // 5 minutes in milliseconds


            $("#update_button").click(function(){
                  $.ajax({
                    type: 'POST',
                    contentType: 'application/json',
                    url:  '/smarthome/update/${tartanHome.name}',
                    data: updateState(),
                    success: function(data) {
                        location.reload(true);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not update ${tartanHome.name}" + errorThrown);
                    },
                });

            });

            $("#alarm_button").click(function(){

                  $.ajax({
                    type: 'POST',
                    contentType: 'application/json',
                    url:  '/smarthome/update/${tartanHome.name}',
                    data: updateState(),
                    success: function(data) {
                        location.reload(true);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("Could not deactivate alarm for ${tartanHome.name}");
                    },
                });
            });
        });
</script>
</head>

<style>
font-family: "Times New Roman", Times, serif;
input[type=text], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}

input[type=submit] {
    width: 100%;
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

div {
    border-radius: 5px;
    background-color: #f2f2f2;
    padding: 20px;
}
</style>

<div id="${tartanHome.name}_div">
<fieldset id="${tartanHome.name}_control">
    <h1>This is Monday Group 7's Tartan House Control Panel</h1>
    <legend><h2>House: ${tartanHome.name}@${tartanHome.address}</h2></legend>
    <legend><h2>Experiment group: ${tartanHome.experimentGroup}</h2></legend>
    <h3>HVAC</h3>
    <p>
        <strong>Temperature: <font color="blue">${tartanHome.temperature} F </font></strong>
    </p>
    <strong>
        <label for="targetTemp">Set Temperature: </label>
        <input id="targetTemp" type="number" value="${tartanHome.targetTemp}" min="50" max="85" /> degrees F
    </strong>
    <p>
        <strong>Humidity: <font color="blue">${tartanHome.humidity}% </font></strong>
    </p>
    <p>
        <strong><label for="humidifier">Humidifier:</label></strong>
        <select name="slider-flip-m" id="humidifier" data-role="slider" data-mini="true">
            <option value="off">off</option>
            <option value="on" <#if tartanHome.humidifier=="on">selected="true"</#if> >on</option>
        </select>
    </p>
    <p>
        <strong>Mode:
        <#if tartanHome.hvacMode == "heat">
            <font color="red">Heat</font>
        <#else>
            <font color="blue">Cool</font>
        </#if>
        </strong>
    </p>
    <P>
        <strong>HVAC is ${tartanHome.hvacState}</strong>
    </P>
    <hr>

    <h3>Proximity</h3>
    <p>
        <strong>House is <font color="blue"> ${tartanHome.proximity}</font></strong>
    </p>

    <h3>Windows</h3>
    <p>
        <strong>Windows state: <font color="red"> ${tartanHome.brokenWindow}</font></strong>
    </p>

    <h3>Intruder</h3>
    <p>
        <strong>Intruder state: <font color="red"> ${tartanHome.intruder}</font></strong>
    </p>

    <p>
        <strong>Door state:</strong>
        <select name="slider-flip-m" id="door" data-role="slider" data-mini="true">
        <#if tartanHome.door == "closed">
            <option value="closed" selected="true">closed</option>
            <option value="open">open</option>
        <#else>
            <option value="closed" >closed</option>
            <option value="open" selected="true">open</option>
        </#if>
        </select>
    </p>
    <p>
        <strong>Light state:</strong>
        <select name="slider-flip-m" id="light" data-role="slider" data-mini="true">
        <#if tartanHome.light == "on">
            <option value="on" selected="true">on</option>
            <option value="off">off</option>
        <#else>
            <option value="on">on</option>
            <option value="off" selected="true">off</option>
        </#if>
        </select>
    </p>
    <hr>
    <h3>Alarm System</h3>
    <p>
        <strong>Alarm status:</strong>
        <select name="slider-flip-m" id="armAlarm" data-role="slider" data-mini="true">
            <#if tartanHome.alarmArmed=="armed">
                <option value="armed" selected="true">armed</option>
                <option value="disarmed">disarmed</option>
            <#else>
                <option value="armed">armed</option>
                <option value="disarmed" selected="true">disarmed</option>
            </#if>
        </select>
    </p>
    <p>
        <#if tartanHome.alarmActive != "active">
        <strong><font color="green">Alarm Off</font></strong>
        <#else>
        <strong><font color="red">Alarm Active!</font></strong>
        <label for="alarmPasscode">Alarm passcode: </label><input id="alarmPasscode" type="text" />
        <button id="alarm_button">Stop alarm</button>
    </#if>
    </p>
    <p>
        <strong>
            Alarm delay: <input id="alarmDelay" type="number" value="${tartanHome.alarmDelay}" /> seconds
        </strong>
    </p>
    <hr>
    <h3>Smart Lock</h3>
    <p>
        <strong>Lock Status:</strong>
        <select name="slider-flip-m" id="smartLock" data-role="slider" data-mini="true">
            <option value="locked">Locked</option>
            <option value="unlocked">Unlocked</option>
        </select>
    </p>
    <p>
        <strong>Enter Password to Operate Lock:</strong>
        <input id="smartLockPasscode" type="password" />
    </p>
    <hr>
    <h3>Night Lock</h3>
    <p>
    <strong>
        <label for="nightStartTime">Set Start Time (24 HR): </label>
        <input id="nightStartTime" type="number" value="${tartanHome.nightStartTime}" min="0" max="23" />
    </strong>
    </p>
    <p>
    <strong>
        <label for="nightEndTime">Set End Time (24 HR): </label>
        <input id="nightEndTime" type="number" value="${tartanHome.nightEndTime}" min="0" max="23" />
    </strong>
    </p>
    <hr>
    <h3> Event log</h3>
    <textarea id="log" rows="15" cols="150">
    <#list tartanHome.eventLog as i>
    ${i}
    </#list>
    </textarea>
    <p>
        <button id="update_button">Update house state</button> <button id="refresh_button">Refresh house state</button>
    </p>
</fieldset>
</div>
</body>
</html>
