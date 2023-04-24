<?php
require_once "Controller.php";
class ActionController extends Controller{

    public function __construct(){
        parent::__construct();
    }
    
    public function signupAction($data) {
        $platform = "app";
        if(isset($data['platform']) && $data['platform'] == "web"){     // web
            $platform = "web";
            if(strlen($data["password"]) < 8) 
                $resp["response"] = "shortpassword";
            else if($data["password"] != $data["cpassword"])
                $resp["response"] = "mismatch";
            
            unset($data["platform"]);
            unset($data["cpassword"]);
            unset($data["submit"]);
        }
        if(!isset($resp)) {
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
        }
        if($platform == "web") {
            if($resp["response"] == "OK"){
                notice_and_nextpage("Đăng ký thành công. Mời bạn đăng nhập", "/");
            }
            else{
                $errors = [];
                if($resp["response"] == "shortpassword")
                    $errors[] = "Mật khẩu quá ngắn. Độ dài tối thiểu 8 ký tự";
                if($resp["response"] == "mismatch")
                    $errors[] = "Mật khẩu nhập lại không khớp";

                getView("register", [
                    "error" => $errors
                ]);
            }
        }
        else {
            echo json_encode($resp);
        }
    }

    public function loginAction($data){
        if(isset($data['platform']) && $data['platform'] == "web"){     // web
            $resp = $this->userObj->login($data);
            if($resp["response"] == "OK"){
                $_SESSION["smlgt_user"] = [
                    "email" => $resp["user"]["email"],
                    "name" => $resp["user"]["fullname"],
                    "id" => $resp["user"]["id"]
                ];
                nextpage("/");
            }
            else{
                getView("login", [
                    "error" => ['Incorrect email or password!'],
                    "username" => $data["username"]
                ]);
            }
        }
        else{   // app
            $resp = $this->userObj->login($data);
            if($resp["response"] == "OK"){
                $roomList = $this->roomObj->getList($resp["user"]["id"]);
                $resp["roomList"] = $roomList;
            }
            echo json_encode($resp);
        }
    }

    public function logoutAction(){
        if(isset($_SESSION["smlgt_user"])){
            unset($_SESSION["smlgt_user"]);
        }
        nextpage("/");
    }

    public function updateInfoAction($data){
        $resp = $this->userObj->updateInfo($data);
        echo json_encode($resp);
    }

    public function changePassAction($data){
        $resp = $this->userObj->changePass($data);
        echo json_encode($resp);
    }

    public function setAppControl($data){
        $resp = $this->userObj->setAppControl($data);
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
        $resp = $this->deviceObj->setData($data);
        echo json_encode($resp);
    }

    public function getControlAction($data) {
        $control = $this->deviceObj->getControl($data["apikey"]);
        echo json_encode($control);
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