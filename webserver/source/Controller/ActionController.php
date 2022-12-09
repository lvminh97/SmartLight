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
        $this->deviceObj->setControl($data["id"], $data["param"], $data["value"]);
        echo json_encode(["response" => "OK"]);
    }

    public function getDevices($data) {
        $deviceList = $this->deviceObj->getList($data["room_id"]);
        echo json_encode($deviceList);
    }
}
?>