<?php
require_once "functions.php";
require_once "DB.php";

class Device extends DB{

    public function __construct(){
        parent::__construct();
    }

    public function create($data) {
        $this->insert("device", [
            "room_id" => $data["room_id"],
            "type" => $data["type"]
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

    public function setControl($id, $param, $value) {
        $this->update("device_control", [
            $param => $value
        ],
        "id='$id'");
    }
}
?>