package com.demon4u.blog.controller;


import com.demon4u.blog.service.BlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ExceptionController implements ErrorController {


    @Override
    public String getErrorPath() {
        return "/error";
    }

    @Autowired
    private BlogService blogService;

    @RequestMapping("/error")
    public  ModelAndView  error(HttpServletRequest request, HttpServletResponse response, Exception e){
        String accept = request.getHeader("accept");
        Integer status = response.getStatus();
        if (StringUtils.isNotEmpty(accept) && accept.contains("text/html")) {
            ModelAndView mv = new ModelAndView("frame");
            blogService.frameData(mv);
            mv.addObject("action", "error");
            return mv;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "别瞎整");
            return new ModelAndView(new MappingJackson2JsonView(), map);
        }
    }


}
