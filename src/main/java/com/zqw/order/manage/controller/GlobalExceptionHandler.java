package com.zqw.order.manage.controller;

import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author: zhuquanwen
 * @date: 2017/11/3 10:26
 * @desc:
 */
@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @ExceptionHandler(value= PageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle(Exception exception) {
//        return new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数有误");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("500");
        return mav;
//        return "500";
    }

    @ExceptionHandler(value= AjaxException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public com.zqw.order.manage.entity.ResponseEntity handle(AjaxException exception) {
//        return new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数有误");
        ResponseEntity re = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务器内部错误");
        return re;
//        return "500";
    }

//    @ExceptionHandler(value=MissingServletRequestParameterException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity handle(MissingServletRequestParameterException exception) {
//
//        return new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数有误");
//    }
}
