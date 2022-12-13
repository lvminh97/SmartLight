<?php
class Route{
    // $store = array(array(method => , key => , value => , action => ))
    private $store;

    public function __construct($function = ""){
        $this->store = array(array('method' => 'GET',
                                    'key' => 'DEFAULT',
                                    'value' => '',
                                    'function' => $function));
    }

    public function get($key, $value, $function){
        $routeItem = array('method' => 'GET',
                            'key' => $key,
                            'value' => $value,
                            'function' => $function);
        $this->store[] = $routeItem;
    }

    public function post($key, $value, $function, $data = null){
        $routeItem = array('method' => 'POST',
                            'key' => $key,
                            'value' => $value,
                            'function' => $function);
        $this->store[] = $routeItem;
    }

    private function implementFunction($func, $data1 = null, $data2 = null){
        $_func = explode("@", $func);
        $controller = $_func[0];
        $function = $_func[1];
        require_once $controller.".php";
        $ctrlObj = new $controller;
        $resp = $ctrlObj->$function($data1, $data2);
        if($resp !== null) echo json_encode($resp);
    }

    public function process(){
        $implemented = false;
        foreach($this->store as $routeItem){
            if($routeItem['key'] == 'DEFAULT') continue;
            if(isset($_GET[$routeItem['key']]) && $_GET[$routeItem['key']] == $routeItem['value']){
                if($routeItem['method'] == 'GET'){
                    $implemented = true;
                    $this->implementFunction($routeItem['function'], $_GET);
                    break;
                }
                if($routeItem['method'] == 'POST'){
                    $implemented = true;
                    $this->implementFunction($routeItem['function'], $_POST, $_FILES);
                    break;
                }
            }
        }
        if($implemented == false){
            $this->implementFunction($this->store[0]['function']);
        }
    }
}
?>