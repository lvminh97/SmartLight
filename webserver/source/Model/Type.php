<?php
require_once "functions.php";
require_once "DB.php";

class Type extends DB{

    public function __construct(){
        parent::__construct();
    }

    public function getList() {
        $types = $this->select("device_type", "*", "1", "id");
        return $types;
    }
}
?>