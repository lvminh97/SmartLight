<?php
require_once "Controller.php";
class ActionController extends Controller{

    public function __construct(){
        parent::__construct();
    }
    
    public function loginAction($data){
        echo json_encode($data);
    }

}
?>