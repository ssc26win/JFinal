package com.shangsc.platform.vo;

import java.io.Serializable;

/**
 * @Author ssc
 * @Date 2018/8/14 14:38
 * @Desc 用途：
 */
public class SysUserVo implements Serializable {

    private static final long serialVersionUID = -5744477715478286940L;

    private Integer id;

    private String name;

    private String companyName;

    public SysUserVo() {
    }

    public SysUserVo(Integer id, String name, String companyName) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
