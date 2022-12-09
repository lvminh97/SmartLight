<?php
require_once "functions.php";
require_once "DB.php";

class Room extends DB{

    public function __construct(){
        parent::__construct();
    }

    public function create($data){
        $this->insert("room", [
            "name" => $data["name"],
            "user_id" => $data["user_id"]
        ]);
        $check = $this->select("room", "*", "1", "id DESC");
        return ["response" => "OK", "id" => $check[0]["id"]];
    }

    public function getList($uid) {
        $roomList = $this->select("room", "*", "user_id='$uid'", "id");
        return $roomList;
    }
}
?>