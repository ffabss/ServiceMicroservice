package com.asdf;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/")
public class RESTRedirectController {
    @RequestMapping(value = "*", method = RequestMethod.GET)
    public ModelAndView redirect() {
        String projectUrl = "https://service-manager.web.app/";
        return new ModelAndView("redirect:" + projectUrl);
    }
}
