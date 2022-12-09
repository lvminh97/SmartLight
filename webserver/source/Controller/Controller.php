<?php
if(!defined('__CONTROLLER__')) define('__CONTROLLER__', 'true');
require_once "Model/User.php";

class Controller{
    protected $userObj;

    public function __construct(){
        $this->userObj = new User;
        //
        sessionInit();
        setTimeZone();
    }
}
?>