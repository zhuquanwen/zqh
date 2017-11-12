package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.ClothSize;
import com.zqw.order.manage.domain.p.Style;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.service.api.ClothSizeService;
import com.zqw.order.manage.service.api.StyleService;
import com.zqw.order.manage.util.EncodeUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:26
 * @desc:
 */
@SuppressWarnings("Duplicates")
public class BaseController {

    protected String TARGET_URL = "TARGET_URL";
    protected  String encodeKey = "zhuquanhong";
    protected  String USER_SESSION = "USER_SESSION";
    protected  String LOGIN_MESSAGE = "LOGIN_MESSAGE";
    protected  String HIDDEN_FLAG = "HIDDEN_FLAG";
    private  String DEFAULT_RECORD = "DEFAULT_RECORD";
    private String ADMIN_FLAG = "ADMIN_FLAG";
    protected String validateSession(HttpServletRequest request, HttpSession session, String page, ClothSizeService clothSizeService,
                                     StyleService styleService) throws PageException{

        try{
            String url = request.getRequestURL().toString();
            //验证一下application中有没有款式和尺码默认信息，如果没有就插入，
            ServletContext sc = session.getServletContext();
            String defaultRecord = (String) sc.getAttribute(DEFAULT_RECORD);
            if(defaultRecord == null){
                //查询下数据库中有没有
                Style style = styleService.findByName("默认");
                if(style == null){
                    style = new Style();
                    style.setName("默认");
                    styleService.save(style);
                }
                ClothSize clothSize = clothSizeService.findByName("默认");
                if(clothSize == null){
                    clothSize = new ClothSize();
                    clothSize.setName("默认");
                    clothSizeService.save(clothSize);
                }
                sc.setAttribute(DEFAULT_RECORD,"YES");
            }
            String desUsername = (String) session.getAttribute(USER_SESSION);
            if(desUsername == null){
                session.setAttribute(TARGET_URL, url);
                return "redirect:login";
            }
            String username = EncodeUtils.aesDecrypt(desUsername, encodeKey);
            if(username == null){
                session.setAttribute(TARGET_URL, url);
                return "redirect:login";
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new PageException();
        }
        session.setAttribute(ADMIN_FLAG,"yes");
        return page;
    }
}
