package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/10 17:08
 * @desc:
 */
@Entity
@Table(name = "t_cloth_size")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class ClothSize extends BasePage implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;

    @OneToMany(targetEntity = Stock.class, mappedBy = "clothSize", fetch= FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Stock> stockList;
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

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}
