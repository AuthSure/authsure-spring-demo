package com.authsure.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Demo controller to handle requests for resources based on user authorization.
 *
 * @author Erik R. Jensen
 */
@Controller
public class DemoController {

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @RequestMapping("/protected")
  public String protectedPage() {
    return "protected";
  }

  @RequestMapping("/another_protected")
  public String anotherProtectedPage() {
    return "another_protected";
  }

  @RequestMapping("/logout_success")
  public String logoutSuccessPage() {
    return "logout_success";
  }

}
