package com.authsure.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
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

}
