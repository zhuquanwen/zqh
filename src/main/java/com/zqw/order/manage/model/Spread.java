package com.zqw.order.manage.model;

import com.zqw.order.manage.domain.p.BasePage;

import java.math.BigInteger;

/**
 * @author: zhuquanwen
 * @date: 2017/11/14 9:17
 * @desc:
 */
public class Spread extends BasePage {
    private BigInteger goodsId;
    private String goodsName;
    private String url;
    private String userName;

    public BigInteger getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(BigInteger goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
