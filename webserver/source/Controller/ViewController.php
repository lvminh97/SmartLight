<?php
require_once "Controller.php";
class ViewController extends Controller{

    public function __construct(){
        parent::__construct();
    }

    public function getIndex(){
        if(isset($_SESSION["smlgt_user"]))
            getView('home');
        else
            getView('login');
    }

    public function getLoginPage(){
        if(isset($_SESSION["smlgt_user"]))
            nextpage("/");
        else
            getView('login');
    }
    public function getRegisterPage(){
        if(isset($_SESSION["smlgt_user"]))
            nextpage("/");
        else
            getView('register');
    }

    public function getLightDesignPage($data) {
        if(!isset($_SESSION["smlgt_user"]))
            nextpage("/");
        else{
            $types = $this->typeObj->getList();
            $room_list = $this->roomObj->getList($_SESSION["smlgt_user"]["id"]);
            $room = $room_list[0];
            if(isset($data["room"])) {
                foreach($room_list as $item) {
                    if($item["id"] == $data["room"]) {
                        $room = $item;
                        break;
                    }
                }
            }
            //
            $device_list = $this->deviceObj->getList($room["id"]);
            $device = $device_list[0];
            if(isset($data["device"])) {
                foreach($device_list as $item) {
                    $item["name"] = $item["id"]." - ".$types[$item["type"] - 1];
                    if($item["id"] == $data["device"]) {
                        $device = $item;
                    }
                }
            }
            $light_data = $this->deviceObj->getLight($device["id"]);
            $power_data = $this->deviceObj->getPower($device["id"]);
            $control = $this->deviceObj->getControl($device["api_key"]);
            //
            getView('light_design', [
                "rooms" => $room_list,
                "devices" => $device_list,
                "room" => $room,
                "device" => $device,
                "lights" => $light_data,
                "powers" => $power_data,
                "control" => $control
            ]);
        }
    }
}
?>