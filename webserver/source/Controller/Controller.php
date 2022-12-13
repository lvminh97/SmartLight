<?php
if(!defined('__CONTROLLER__')) define('__CONTROLLER__', 'true');
require_once "Model/User.php";
require_once "Model/Device.php";
require_once "Model/Room.php";

class Controller{
    protected $userObj;
    protected $deviceObj;
    protected $roomObj;

    public function __construct(){
        $this->userObj = new User;
        $this->deviceObj = new Device;
        $this->roomObj = new Room;
        //
        sessionInit();
        setTimeZone();
    }
}
?>