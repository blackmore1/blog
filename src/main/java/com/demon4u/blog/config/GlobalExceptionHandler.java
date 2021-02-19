package com.demon4u.blog.config;

import com.demon4u.blog.controller.ExceptionController;
import com.demon4u.blog.service.BlogService;
import com.demon4u.blog.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LogUtil.getErrorLog();
    @Autowired
    private ExceptionController exceptionController;

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        LOGGER.error(request.getRequestURI(), e);
        return exceptionController.error(request, response, e);
    }
}
