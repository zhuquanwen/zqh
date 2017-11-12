package com.zqw.order.manage.domain.p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author: zhuquanwen
 * @date: 2017/11/9 9:37
 * @desc:
 */
@Entity
@Table(name = "t_employee")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Employee extends BasePage implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 32)
    private String name;

    @Column(length = 32)
    private String realName;

    @Transient
    private String urlPath;


    @OneToMany(targetEntity = UrlPath.class, mappedBy = "employee", fetch= FetchType.LAZY,cascade={CascadeType.ALL})
    private List<UrlPath> urlPathList;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<UrlPath> getUrlPathList() {
        return urlPathList;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public void setUrlPathList(List<UrlPath> urlPathList) {
        this.urlPathList = urlPathList;

    }
}
