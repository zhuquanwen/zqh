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
}