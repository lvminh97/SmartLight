<?php
require_once "functions.php";
require_once "DB.php";

class BaseModel extends DB{

	public function __construct(){
		parent::__construct();
	}
	
}
?>