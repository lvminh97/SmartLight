<?php
require_once "Controller.php";
class ActionController extends Controller{

    public function __construct(){
        parent::__construct();
    }
    
    public function signupAction($data) {
        $resp = $this->userObj->signup($data);
        echo json_encode($resp);
    }

    public function loginAction($data){
        $resp = $this->userObj->login($data);
        echo json_encode($resp);
    }

}
?>