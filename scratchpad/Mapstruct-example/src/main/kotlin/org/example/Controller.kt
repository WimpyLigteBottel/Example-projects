package org.example

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {


    @GetMapping
    fun getHello():String{
        return "hello world";
    }
}