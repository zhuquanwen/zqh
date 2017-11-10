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
}
