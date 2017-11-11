package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:02
 * @desc:
 */
@Entity
@Table(name = "t_goods")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Goods extends BasePage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //自增id

    @Column(length = 50,unique = true)
    private String name; //商品名称

    @Column(length = 80)
    private String descriptor; //商品描述

    @OneToMany(targetEntity = Stock.class, mappedBy = "goods", fetch= FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Stock> stockList; //商品库存

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

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}
