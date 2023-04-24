<!DOCTYPE html>
<html lang="en">
<head>
   <link rel="icon" href="View/image/logo_icon.png" type="image/x-icon">
   <title>Light Design</title>
   <!-- custom css file link  -->
   <link rel="stylesheet" href="View/Css/style.css">
</head>
<body>
    <div class="home_page" style="height: 170vh!important;"> 
        <div class="navbar">
            <div class="row">
                <div class="icon">
                    <img class="logo-icon" src="View/image/logo_icon.png"><br>
                    <h2 class="logo"> SMART LIGHT </h2>
                </div>
                <div class="hello-user">
                    Xin chào <b><?php echo $_SESSION["smlgt_user"]["name"] ?></b><a href="/?action=logout" style="margin-left: 20px"> Đăng xuất</a>
                </div>
            </div>
        
            <div class="menu">
                <ul>
                    <li><a href="./.">TRANG CHỦ</a></li>
                    <li><a href="/?site=about">VỀ CHÚNG TÔI</a></li>
                    <li><a href="/?site=service">DỊCH VỤ</a></li>
                    <li><a href="/?site=light_design">HỆ THỐNG ĐÈN THÔNG MINH</a></li>
                    <li><a href="/?site=contact">LIÊN HỆ</a></li>
                </ul>
            </div>
        </div>
        <div class="devide"></div>
        <div class="content">
            <div class="row" style="width: 100%; margin-top: 50px;">
                <div style="flex: 0 0 28%; max-width: 28%;">
                    Chọn phòng
                    <select id="room_select" style="margin-left: 20px; padding: 10px 5px 10px 5px; font-size: 14px; font-weight: bold;" onchange="roomSelect()">
                    <?php 
                    foreach($viewParams["rooms"] as $room) { ?>
                        <option value="<?php echo $room["id"] ?>" <?php echo ($room["id"] == $viewParams["room"]["id"] ? "selected" : "")?> ><?php echo $room["name"] ?></option>
                    <?php 
                    } ?>
                    </select>
                </div>
                <div style="flex: 0 0 30%; max-width: 30%">
                    Chọn thiết bị
                    <select id="device_select" style="margin-left: 20px; padding: 10px 5px 10px 5px; font-size: 14px; font-weight: bold;" onchange="deviceSelect()">
                    <?php 
                    foreach($viewParams["devices"] as $device) { ?>
                        <option value="<?php echo $device["id"] ?>" <?php echo ($device["id"] == $viewParams["device"]["id"] ? "selected" : "")?> ><?php echo $device["name"] ?></option>
                    <?php 
                    } ?>
                    </select>
                </div>
                <div style="flex: 0 0 30%; max-width: 30%">
                    Chọn hiển thị
                    <select id="display_select" style="margin-left: 20px; padding: 10px 5px 10px 5px; font-size: 14px; font-weight: bold;" onchange="tabSelect()">
                        <option value="0" selected>Thông số điều khiển</option>
                        <option value="1">Biểu đồ</option>
                    </select>
                </div>
            </div>
            <div style="display:none">
            <?php echo json_encode($viewParams["control"]) ?>
            </div>
            <div id="control_tab">
                <div class="row" style="width: 100%; margin-top: 100px;">
                    <div class="panel">
                        <div class="card">
                            <img src="https://i.ibb.co/8c8tBqT/temp-icon.png">
                            <div class="text">
                                <div class="title">Nhiệt độ</div>
                                <div class="value"><?php echo $viewParams["control"]["temp"] ?> °C</div>
                            </div>
                        </div>
                    </div>
                    <div class="panel">
                        <div class="card">
                            <img src="https://i.ibb.co/XCNPtRy/lamp.png">
                            <div class="text">
                                <div class="title">Cường độ sáng</div>
                                <div class="value"><?php echo $viewParams["control"]["light"] ?>%</div>
                            </div>
                        </div>
                    </div>
                    <div class="panel">
                        <div class="card">
                            <img src="https://i.ibb.co/Wptz60q/power.png">
                            <div class="text">
                                <div class="title">Công suất</div>
                                <div class="value"><?php echo $viewParams["control"]["power"] ?>W</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="chart_tab" style="display: none;">
                <div id="light_chart" style="height: 54vh; width: 100%; margin-top: 50px"></div> 
                <div id="power_chart" style="height: 54vh; width: 100%; margin-top: 50px"></div> 
            </div>
            <script>
                var lightChart, powerChart;

                function roomSelect() {
                    window.location.href = "./?site=light_design&room=" + document.getElementById("room_select").value;
                }
                function deviceSelect() {
                    window.location.href = "./?site=light_design&room=" + document.getElementById("room_select").value + "&device=" + document.getElementById("device_select").value;
                }
                function tabSelect() {
                    if(document.getElementById("display_select").value == 0) {
                        document.getElementById("control_tab").style.display = "block";
                        document.getElementById("chart_tab").style.display = "none";
                    }
                    else {
                        document.getElementById("control_tab").style.display = "none";
                        document.getElementById("chart_tab").style.display = "block";
                        lightChart.render();
                        powerChart.render();
                    }
                }

                window.onload = function() {
                    var lightDps = [];
                    var powerDps = [];
                    var buf = [];
                    <?php
                    foreach($viewParams["lights"] as $light) {
                        echo "buff = ['{$light['time']}', '{$light['light']}'];\n"; ?>
                        lightDps.push({
                            x: new Date(buff[0]),
                            y: parseFloat(buff[1])
                        });
                    <?php    
                    } ?>
                    <?php
                    foreach(array_keys($viewParams["powers"]) as $key) {
                        echo "buff = ['$key', '{$viewParams["powers"][$key]}'];\n"; ?>
                        powerDps.push({
                            label: buff[0].substr(0, 2) + "/" + buff[0].substr(3, 2),
                            y: Math.round(buff[1] * 3000)
                        });
                    <?php    
                    } ?>
                    lightChart = new CanvasJS.Chart("light_chart", {
                        title :{
                            text: "Light chart"
                        },
                        zoomEnabled: true,
                        axisX: {
                            title: "Time",
                            valueFormatString: "HH:mm:ss"
                        },
                        axisY: {
                            title: "Light (%)",
                            minimum: 0,
                            maximum: 100,
                            suffix: "%",
                            includeZero: false
                        },     
                        data: [
                        {
                            type: "spline",
                            xValueFormatString: "DD MMM YY - HH:mm:ss",
                            color: 'red',
                            dataPoints: lightDps
                        }]
                    });
                    lightChart.render();

                    var maxPowerCost = 0;
                    for(var i = 0; i < powerDps.length; i++) {
                        if(maxPowerCost < powerDps[i].y){
                            maxPowerCost = powerDps[i].y;
                        }
                    }
                    powerChart = new CanvasJS.Chart(("power_chart"), {
                        title: {
                            text: "Power Chart"
                        },
                        zoomEnabled: true,
                        axisX: {
                            title: "Day"
                        },
                        axisY: {
                            title: "Money (VND)",
                            minimum: 0,
                            maximum: parseInt(maxPowerCost) * 1.2,
                            includeZero: false
                        },  
                        data: [
                        {
                            type: "column",
                            xValueFormatString: "DD/MM",
                            indexLabel: "{y}đ",
                            color: 'blue',
                            dataPoints: powerDps
                        }]
                    });
                    powerChart.render();
                }
            </script>
        </div>
    </div>
    <script src="https://unpkg.com/ionicons@5.4.0/dist/ionicons.js"></script>
    <script src="View/js/canvasjs.js"></script>

</body>
</html>