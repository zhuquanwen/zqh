package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 9:45
 * @desc:
 */
@Entity
@Table(name="t_url_path")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class UrlPath extends BasePage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80)
    private String path;

    @Column(length = 1)
    private String useFlag = "0";

    @ManyToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @OneToMany(targetEntity = Shopping.class, mappedBy = "urlPath", fetch= FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Shopping> shoppingList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    public List<Shopping> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Shopping> shoppingList) {
        this.shoppingList = shoppingList;
    }
}
