package com.gsd.image;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AnOtherMsg {
	
	@RequestMapping(value = "/home")
	public String  index() {
		return "index";
	}

}
