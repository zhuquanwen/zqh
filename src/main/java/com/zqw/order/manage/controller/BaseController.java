package com.zqw.order.manage.controller;

import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.util.EncodeUtils;

import javax.servlet.http.HttpSession;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:26
 * @desc:
 */
public class BaseController {
//    public String validateSession(HttpSession session) throws PageException{
////        try{
////            String desUsername = (String) session.getAttribute(USER_SESSION);
////            if(desUsername == null){
////                return "redirect:login";
////            }
////            String username = EncodeUtils.aesDecrypt(desUsername, encodeKey);
////            if(username == null){
////                return "redirect:login";
////            }
////        }catch (Exception e){
////            e.printStackTrace();
////            throw new PageException();
////        }
//    }
}
