<?php
require_once "Controller.php";
class ViewController extends Controller{

    public function __construct(){
        parent::__construct();
    }

    public function getIndex(){
        getView("home", array('title' => "Home",
                                'user' => array('fullname' => "User", 'role' => "Role_Host")
                            ));
        return null;
    }
}
?>