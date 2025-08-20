package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketDemoController {

  @GetMapping("/websocket-demo")
  public String websocketDemo() {
    return "websocket-demo.html";
  }
}
