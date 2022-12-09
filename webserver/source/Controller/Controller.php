<?php
if(!defined('__CONTROLLER__')) define('__CONTROLLER__', 'true');
require_once "Model/BaseModel.php";

class Controller{
    protected $baseModelObj;

    public function __construct(){
        $this->baseModelObj = new BaseModel;
        //
        sessionInit();
        setTimeZone();
    }
}
?>