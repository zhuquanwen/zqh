package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:02
 * @desc:
 */
@Entity
@Table(name = "t_qrcode")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Qrcode extends BasePage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //自增id
    @Column
    private Long goodsId;
    @Column(length = 50)
    private String topPath; //二维码最上面的图片
    @Column(length = 50)
    private String logPath; //二维码中间logo图片
    @Column(length = 150)
    private String word1; //文字描述
    @Column(length = 150)
    private String word2; //文字描述
    @Column(length = 150)
    private String word3; //文字描述
    @Column(length = 150)
    private String word4; //文字描述
    @Column(length = 150)
    private String word5; //文字描述

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopPath() {
        return topPath;
    }

    public void setTopPath(String topPath) {
        this.topPath = topPath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }

    public String getWord4() {
        return word4;
    }

    public void setWord4(String word4) {
        this.word4 = word4;
    }

    public String getWord5() {
        return word5;
    }

    public void setWord5(String word5) {
        this.word5 = word5;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
