<?php
    require_once "Controller/Route.php";
    $route = new Route("ViewController@getIndex");
    
    $route->get("site", "home", "ViewController@getIndex");

    $route->post("action", "signup", "ActionController@signupAction");
    $route->post("action", "login", "ActionController@loginAction");

    $route->process();
?>