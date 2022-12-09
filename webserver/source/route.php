<?php
    require_once "Controller/Route.php";
    $route = new Route("ViewController@getIndex");
    
    $route->get("site", "home", "ViewController@getIndex");

    $route->post("action", "signup", "ActionController@signupAction");
    $route->post("action", "login", "ActionController@loginAction");

    $route->post("action", "setparam", "ActionController@setParamAction");

    $route->get("action", "get_devices", "ActionController@getDevices");

    $route->process();
?>