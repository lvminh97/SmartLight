<?php
require_once "Controller.php";
class ActionController extends Controller{

    public function __construct(){
        parent::__construct();
    }
    
    public function signupAction($data) {
        $resp = $this->userObj->signup($data);
        $uid = $resp["id"];
        // create room and device
        $roomResp = $this->roomObj->create(["name" => "Meeting Room", "user_id" => $uid]);
        $this->deviceObj->create(["room_id" => $roomResp["id"], "type" => "1"]);
        $roomResp = $this->roomObj->create(["name" => "Classroom", "user_id" => $uid]);
        $this->deviceObj->create(["room_id" => $roomResp["id"], "type" => "1"]);
        $roomResp = $this->roomObj->create(["name" => "Conference Room", "user_id" => $uid]);
        $this->deviceObj->create(["room_id" => $roomResp["id"], "type" => "1"]);
        $roomResp = $this->roomObj->create(["name" => "Customize", "user_id" => $uid]);
        $this->deviceObj->create(["room_id" => $roomResp["id"], "type" => "1"]);
        echo json_encode($resp);
    }

    public function loginAction($data){
        $resp = $this->userObj->login($data);
        if($resp["response"] == "OK"){
            $roomList = $this->roomObj->getList($resp["user"]["id"]);
            $resp["roomList"] = $roomList;
        }
        echo json_encode($resp);
    }

    public function setParamAction($data) {
        $this->deviceObj->setControl($data["apikey"], $data["param"], $data["value"]);
        echo json_encode(["response" => "OK"]);
    }

    public function getDevices($data) {
        $deviceList = $this->deviceObj->getList($data["room_id"]);
        echo json_encode($deviceList);
    }

    public function getLightData($data){
        $lightData = $this->deviceObj->getLight($data["id"]);
        echo json_encode($lightData);
    }

    public function getPowerData($data){
        $powerData = $this->deviceObj->getPower($data["id"]);
        echo json_encode($powerData);
    }

    public function setDataAction($data) {
        $this->deviceObj->setData($data);
        echo json_encode(["response" => "OK"]);
    }

    public function generateData($data){
        $start = strtotime($data["start"]);
        $end = $start + 4 * 86400;
        while($start < $end) {
            $this->deviceObj->setData([
                "id" => $data["id"],
                "time" => date("Y-m-d H:i:s", $start),
                "light" => rand($data["min_light"], $data["max_light"]),
                "power" => rand($data["min_power"], $data["max_power"])
            ]);
            $start += 30;
        }
        echo json_encode(["response" => "OK"]);
    }

    public function getTypes(){
        echo json_encode($this->typeObj->getList(), JSON_UNESCAPED_UNICODE);
    }

    public function addDevice($data) {
        $this->deviceObj->create($data);
        $device = $this->deviceObj->getLastDevice($data["room_id"]);
        echo json_encode(["response" => "OK", "device" => $device]);
    }
}
?>