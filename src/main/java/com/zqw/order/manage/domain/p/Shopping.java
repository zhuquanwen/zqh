package com.zqw.order.manage.domain.p;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 11:04
 * @desc:
 */
@Entity
@Table(name="t_shopping")
public class Shopping extends BasePage implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 32)
    private String name;
    @Column(length = 32)
    private String address;
    @Column(length = 32)
    private String phoneNum;
    @Column(length = 32)
    private String cardType = "1"; //1 移动;2 联通；3电信
    @Column(length = 32)
    private String style; //款式
    @Column(length = 32)
    private String clothSize; //尺码
    @Column(length = 32)
    private String ownPostage = "1" ; //是否邮费自理 1 是，2否

    @Column
    private Date shoppingTime;

    @ManyToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name = "url_path_id")
    private UrlPath urlPath;

    @Column(length = 32)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getOwnPostage() {
        return ownPostage;
    }

    public void setOwnPostage(String ownPostage) {
        this.ownPostage = ownPostage;
    }

    public Date getShoppingTime() {
        return shoppingTime;
    }

    public void setShoppingTime(Date shoppingTime) {
        this.shoppingTime = shoppingTime;
    }

    public UrlPath getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(UrlPath urlPath) {
        this.urlPath = urlPath;
    }
}
