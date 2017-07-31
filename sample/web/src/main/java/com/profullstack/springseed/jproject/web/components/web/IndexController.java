package com.profullstack.springseed.jproject.web.components.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by christianxiao on 4/10/16.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
	@ResponseBody
    public String index(){
        return "home page";
    }

}
