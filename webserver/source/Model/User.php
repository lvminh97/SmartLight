<?php
require_once "functions.php";
require_once "DB.php";

class User extends DB{

    public function __construct(){
        parent::__construct();
    }

    public function signup($data){
        $data["password"] = _hash($data["password"]);
        $this->insert("user", $data);
        $check = $this->select("user", "*", "email='{$data["email"]}'");
        return ["response" => "OK", "id" => $check[0]["id"]];
    }

    public function login($data) {
        $resp = [];
        $data["password"] = _hash($data["password"]);
        $check = $this->select("user", "*", "email='{$data['username']}' AND password='{$data['password']}'");
        if(count($check) == 1){
            $resp["response"] = "OK";
            $resp["user"] = [
                "id" => $check[0]["id"],
                "fullname" => $check[0]["fullname"],
                "email" => $check[0]["email"],
                "mobile" => $check[0]["mobile"],
                "app_control" => $check[0]["app_control"]
            ];
        }
        else 
            $resp["response"] = "Failed";
        
        return $resp;
    }

    public function updateInfo($data){
        $resp = [];
        $check = $this->select("user", "*", "email='{$data['email']}' AND id='{$data['uid']}'");
        if(count($check) == 1){
            $this->update("user", [
                "fullname" => $data["fullname"],
                "mobile" => $data["mobile"]
            ], "id='{$data['uid']}'");
            $resp["response"] = "OK";
        }
        else{
            $resp["response"] = "Fail";
        }
        return $resp;
    }

    public function changePass($data){
        $resp = [];
        $oldPass = _hash($data["pass1"]);
        $check = $this->select("user", "*", "id='{$data['id']}' AND password='$oldPass'");
        if(count($check) == 1){
            if(strlen($data["pass2"]) < 8)
                $resp["response"] = "ShortPass";
            else if($data["pass2"] != $data["pass3"])
                $resp["response"] = "Mismatch";
            else{
                $newPass = _hash($data["pass2"]);
                $this->update("user", [
                    "password" => $newPass
                ], "id='{$data['id']}'");
                $resp["response"] = "OK";
            }
        }
        else {
            $resp["response"] = "WrongPass";
        }
        return $resp;
    }

    public function setAppControl($data){
        $resp = [];
        $check = $this->select("user", "*", "email='{$data['email']}' AND id='{$data['uid']}'");
        if(count($check) == 1){
            $this->update("user", [
                "app_control" => $data["app_control"]
            ], "id='{$data['uid']}'");
            $resp["response"] = "OK";
        }
        else{
            $resp["response"] = "Fail";
        }
        return $resp;
    }

    public function getAppControl($data){
        $resp = [];

        if(isset($data["uid"]))
            $check = $this->select("user", "*", "id='{$data['uid']}'");
        elseif(isset($data["id"]))
            $check = $this->execute("SELECT * FROM device JOIN room JOIN user WHERE device.id='{$data['id']}' AND device.room_id=room.id AND room.user_id=user.id");
        else
            $resp["response"] = "Fail";

        if(!isset($resp["response"])){
            if(count($check) == 1){
                $resp["response"] = "OK";
                $resp["app_control"] = $check[0]["app_control"];
            }
            else{
                $resp["response"] = "Fail";
            }
        }
        return $resp;
    }
}
?>