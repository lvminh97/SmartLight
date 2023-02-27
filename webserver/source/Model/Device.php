<?php
require_once "functions.php";
require_once "DB.php";

class Device extends DB{

    public function __construct(){
        parent::__construct();
    }

    private function generateApiKey() {
        $tmp = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        $res = "";
        while(true){
            $res = "";
            for($i = 0; $i < 20; $i++) {
                $id = rand(0, 61);
                $res .= $tmp[$id];
            }
            $check = $this->select("device", "*", "api_key='$res'");
            if(count($check) == 0){
                break;
            }
        }
        return $res;
    }

    public function create($data) {
        $this->insert("device", [
            "room_id" => $data["room_id"],
            "type" => $data["type"],
            "api_key" => $this->generateApiKey()
        ]);
        $check = $this->select("device", "*", "1", "id DESC");
        $this->insert("device_control", [
            "id" => $check[0]["id"],
            "temp" => 0,
            "light" => 0,
            "power" => 0
        ]);
    }

    public function getList($room_id) {
        $deviceList = $this->execute("SELECT * from device JOIN device_control ON device.id=device_control.id WHERE device.room_id='$room_id' ORDER BY device.id");
        return $deviceList;
    }

    public function getLastDevice($room_id) {
        $deviceList = $this->execute("SELECT * from device JOIN device_control ON device.id=device_control.id WHERE device.room_id='$room_id' ORDER BY device.id DESC");
        if(count($deviceList) > 0) {
            return $deviceList[0];
        }
        else {
            return null;
        }
    }

    public function setControl($apikey, $param, $value) {
        $check = $this->select("device", "*", "api_key='$apikey'");
        if(count($check) == 1){
            $id = $check[0]["id"];
            $this->update("device_control", [
                $param => $value
            ],
            "id='$id'");
        }
    }

    public function getLight($id){
        $data = $this->execute("SELECT time,light FROM device_data WHERE id='$id' ORDER BY time DESC LIMIT 50");
        $data = array_reverse($data);
        return $data;
    }

    public function getPower($id){
        $last_day = $this->execute("SELECT time from device_data ORDER BY time DESC LIMIT 1");
        if(count($last_day) > 0)
            $last_day = strtotime($last_day[0]["time"]);
        else 
            $last_day = strtotime(date("Y-m-d"));
        $start_day = date("Y-m-d", $last_day - 4 * 86400);
        $data = [];
        $buffer = $this->select("device_data", "time,power", "id='$id' AND time >= '$start_day'");
        foreach($buffer as $row){
            $cur_day = explode(" ", $row["time"])[0];
            $cur_day = date("d/m/Y", strtotime($cur_day));
            if(!isset($data[$cur_day]))
                $data[$cur_day] = 0;
            $data[$cur_day] += $row["power"] * 30;
        }
        foreach(array_keys($data) as $key){
            $data[$key] /= 3600000;
        }
        return $data;
    }

    public function setData($data) {
        $check = $this->select("device", "*", "api_key='{$data['apikey']}'");
        if(count($check) == 1) {
            $id = $check[0]["id"];
            if(!isset($data["time"]))
                $data["time"] = date("Y-m-d H:i:s");
            if(!isset($data["light"]))
                $data["light"] = 0;
            if(!isset($data["power"]))
                $data["power"] = 0;

            $this->insert("device_data", [
                "id" => $data["id"],
                "time" => $data["time"],
                "light" => $data["light"],
                "power" => $data["power"]
            ]);
        }
    }

    public function getControl($data){
        $check = $this->select("device", "*", "api_key='{$data['apikey']}'");
        if(count($check) == 1) {
            $id = $check[0]["id"];
            $control = $this->select("device_control", "*", "id='$id'")[0];
            return $control;
        }
        else{
            return null;
        }
    }
}
?>