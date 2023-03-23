<?php
    require_once "Controller/Route.php";
    $route = new Route("ViewController@getIndex");
    
    $route->get("site", "home", "ViewController@getIndex");

    $route->post("action", "signup", "ActionController@signupAction");
    $route->post("action", "login", "ActionController@loginAction");
    $route->post("action", "update_info", "ActionController@updateInfoAction");
    $route->post("action", "change_pass", "ActionController@changePassAction");

    $route->post("action", "setparam", "ActionController@setParamAction");

    $route->get("action", "get_devices", "ActionController@getDevices");
    $route->get("action", "get_light", "ActionController@getLightData");
    $route->get("action", "get_power", "ActionController@getPowerData");

    $route->post("action", "set_data", "ActionController@setDataAction");
    $route->post("action", "generate", "ActionController@generateData");
    $route->get("action", "get_control", "ActionController@getControlAction");

    $route->get("action", "get_types", "ActionController@getTypes");
    $route->post("action", "add_device", "ActionController@addDevice");

    $route->process();
?>  