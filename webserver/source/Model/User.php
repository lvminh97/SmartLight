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
        $check = $this->select("user", "*", "email='{$data["email"]}'");
        return ["response" => "OK", "id" => $check[0]["id"]];
    }

    public function login($data) {
        $resp = [];
        $data["password"] = sha1(sha1($data["password"]));
        $check = $this->select("user", "*", "email='{$data['username']}' AND password='{$data['password']}'");
        if(count($check) == 1){
            $resp["response"] = "OK";
            $resp["user"] = [
                "id" => $check[0]["id"],
                "fullname" => $check[0]["fullname"],
                "email" => $check[0]["email"],
                "mobile" => $check[0]["mobile"]
            ];
        }
        else 
            $resp["response"] = "Failed";
        
        return $resp;
    }
}
?>