package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:13
 * @desc:
 */
@Entity
@Table(name = "t_stock")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Stock extends BasePage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name = "cloth_size_id")
    private ClothSize clothSize;

    @ManyToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name = "style_id")
    private Style style;

    @Column
    private Long sum; //库存

    @Transient
    private String goodsName;
    @Transient
    private String clothSizeName;
    @Transient
    private String styleName;

    @Transient
    private Long goodsId;
    @Transient
    private Long clothSizeId;
    @Transient
    private Long styleId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public ClothSize getClothSize() {
        return clothSize;
    }

    public void setClothSize(ClothSize clothSize) {
        this.clothSize = clothSize;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getClothSizeName() {
        return clothSizeName;
    }

    public void setClothSizeName(String clothSizeName) {
        this.clothSizeName = clothSizeName;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getClothSizeId() {
        return clothSizeId;
    }

    public void setClothSizeId(Long clothSizeId) {
        this.clothSizeId = clothSizeId;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }
}
