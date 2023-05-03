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
        $deviceList = $this->execute("SELECT device.*,device_control.*,device_type.name from device JOIN device_control JOIN device_type ON device.id=device_control.id AND device.type=device_type.id WHERE device.room_id='$room_id' ORDER BY device.id");
        for($i = 0; $i < count($deviceList); $i++) {
            $deviceList[$i]["name"] = $deviceList[$i]["id"]." - ".$deviceList[$i]["name"];
        }
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
        $data = $this->execute("SELECT time,light FROM device_data WHERE id='$id' ORDER BY time DESC LIMIT 400");
        $data = array_reverse($data);
        return $data;
    }

    public function getPower($id, $type){
        $data = [];
        if($type == "monthly") {
            $data['type'] = "monthly";
            $cur_year = date("Y");
            $query = $this->select("device_data", "*", "id='$id' AND time LIKE '$cur_year-%'", "time");
            $data['data'] = [];
            for($i = 1; $i <= 12; $i++) {
                $m = ($i < 10) ? "0$i" : $i;
                $data['data']["$m/".date("Y")] = 0;
            }
            foreach($query as $row) {
                $cur_month = date("m/Y", strtotime($row['time']));
                $data['data'][$cur_month] += $row['power'] * 30;
            }
        }
        else if($type == "yearly") {
            $data['type'] = "yearly";
            $cur_year = date("Y") - 4;
            $query = $this->select("device_data", "*", "id='$id' AND time >= '$cur_year-01-01'", "time");
            $data['data'] = [];
            for($i = $cur_year; $i <= date("Y"); $i++) {
                $data['data'][$i] = 0;
            }
            foreach($query as $row) {
                $cur_year = date("Y", strtotime($row['time']));
                $data['data'][$cur_year] += $row['power'] * 30;
            }
        }
        else {
            $data['type'] = "daily";
            $cur_month = date("Y-m");
            $query = $this->select("device_data", "*", "id='$id' AND time LIKE '$cur_month-%'", "time");
            $data['data'] = [];
            for($i = 1; $i <= date("t"); $i++) {
                $d = ($i < 10) ? "0$i" : $i;
                $data['data']["$d/".date("m/Y")] = 0;
            }
            foreach($query as $row) {
                $cur_day = date("d/m/Y", strtotime($row['time']));
                $data['data'][$cur_day] += $row['power'] * 30;
            }
        }
        //
        foreach(array_keys($data['data']) as $key){
            $data['data'][$key] /= 3600000;
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
                "id" => $id,
                "time" => $data["time"],
                "light" => $data["light"],
                "power" => $data["power"]
            ]);
            return ["response" => "OK"];
        }
        else {
            return ["response" => "Fail"];
        }
    }

    public function getControl($apikey){
        $check = $this->select("device", "*", "api_key='$apikey'");
        if(count($check) == 1) {
            $id = $check[0]["id"];
            $control = $this->select("device_control", "*", "id='$id'")[0];
            $user_check = $this->execute("SELECT * FROM device JOIN room JOIN user WHERE device.id='$id' AND device.room_id=room.id AND room.user_id=user.id");
            $control["app_control"] = $user_check[0]["app_control"];
            return $control;
        }
        else{
            return null;
        }
    }
}
?>