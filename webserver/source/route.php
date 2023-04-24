<?php
    require_once "Controller/Route.php";
    $route = new Route("ViewController@getIndex");
    
    $route->get("site", "home", "ViewController@getIndex");

    $route->post("action", "signup", "ActionController@signupAction");
    $route->get("site", "register", "ViewController@getRegisterPage");
    $route->post("action", "login", "ActionController@loginAction");
    $route->get("site", "login", "ViewController@getLoginPage");
    $route->get("action", "logout", "ActionController@logoutAction");
    $route->post("action", "update_info", "ActionController@updateInfoAction");
    $route->post("action", "change_pass", "ActionController@changePassAction");

    $route->post("action", "setparam", "ActionController@setParamAction");
    $route->post("action", "set_app_control", "ActionController@setAppControl");

    $route->get("action", "get_devices", "ActionController@getDevices");
    $route->get("action", "get_light", "ActionController@getLightData");
    $route->get("action", "get_power", "ActionController@getPowerData");

    $route->post("action", "set_data", "ActionController@setDataAction");
    $route->post("action", "generate", "ActionController@generateData");
    $route->get("action", "get_control", "ActionController@getControlAction");

    $route->get("action", "get_types", "ActionController@getTypes");
    $route->post("action", "add_device", "ActionController@addDevice");


    // web view
    $route->get("site", "light_design", "ViewController@getLightDesignPage");

    $route->process();
?>  