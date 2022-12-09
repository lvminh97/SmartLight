<?php
require_once "functions.php";
require_once "DB.php";

class User extends DB{

    public function __construct(){
        parent::__construct();
    }

    public function signup($data){
        $data["password"] = sha1(sha1($data["password"]));
        $this->insert("user", $data);
        return ["response" => "OK"];
    }

    public function login($data) {
        $resp = [];
        $data["password"] = sha1(sha1($data["password"]));
        $check = $this->select("user", "*", "username='{$data['username']}' AND password='{$data['password']}'");
        if(count($check) == 1)
            $resp["response"] = "OK";
        else 
            $resp["response"] = "Failed";
        
        return $resp;
    }
}
?>