package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: zhuquanwen
 * @date: 2017/10/30 17:17
 * @desc:
 */

@Entity
@Table(name = "t_order")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Order extends BasePage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,length = 32)
    private String courierNum;
    @Column(length = 11)
    private String phoneNum;
    @Column(length = 32)
    private String name;
    @Column()
    private String orderDate;
    @Column
    private String recordDate;

    @Column(length = 100)
    private String address;
    @Column(length = 32)
    private String cardType = "1"; //1 移动;2 联通；3电信
    @Column(length = 32)
    private String style; //款式
    @Column(length = 32)
    private String clothSize; //尺码
    @Column(length = 32)
    private String spreadUserName;
    @Transient
    private String userINfo;
    @Column
    private String goodsName;

    @Transient
    private String startDate;
    @Transient
    private String endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourierNum() {
        return courierNum;
    }

    public void setCourierNum(String courierNum) {
        this.courierNum = "".equals(courierNum) ? null :courierNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = "".equals(phoneNum) ? null :phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = "".equals(name) ? null :name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = "".equals(orderDate) ? null :orderDate;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = "".equals(recordDate) ? null :recordDate;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = "".equals(startDate) ? null : startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = "".equals(endDate) ? null : endDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getClothSize() {
        return clothSize;
    }

    public void setClothSize(String clothSize) {
        this.clothSize = clothSize;
    }

    public String getSpreadUserName() {
        return spreadUserName;
    }
    public void setSpreadUserName(String spreadUserName) {
        this.spreadUserName = spreadUserName;
    }

    public String getUserINfo() {
        return userINfo;
    }

    public void setUserINfo(String userINfo) {
        this.userINfo = userINfo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}